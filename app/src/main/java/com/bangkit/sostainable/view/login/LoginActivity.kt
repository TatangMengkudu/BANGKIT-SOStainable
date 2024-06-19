package com.bangkit.sostainable.view.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.AuthModelFactory
import com.bangkit.sostainable.data.json.LoginJson
import com.bangkit.sostainable.data.local.datastore.model.LoginSession
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.databinding.ActivityLoginBinding
import com.bangkit.sostainable.view.main.MainActivity
import com.bangkit.sostainable.view.register.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        AuthModelFactory.getInstance(this)
    }
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupAction() {
        // MOVE TO REGISTER PAGE
        moveRegister()

        // SHOW/HIDE PASSWORD
        binding.edtPassword.setOnTouchListener { _, event ->
            val DRAWABLE_END = 2
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.edtPassword.right - binding.edtPassword.compoundDrawables[DRAWABLE_END].bounds.width())) {
                    showHidePass()
                    return@setOnTouchListener true
                }
            }
            false
        }

        // LOGIN
        binding.loginButton.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
            moveHomepage()
        }
    }

    private fun moveHomepage() {
        intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun moveRegister() {
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private suspend fun login() {
        val username = binding.edtUsername.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        val usernameValid = when {
            username.isEmpty() -> false
            else -> true
        }

        val passwordValid = when {
            password.isEmpty() -> false
            else -> true
        }

        if (usernameValid && passwordValid) {
            val user = LoginJson(username = username, password = password)
            loginViewModel.login(user).observe(this, Observer{ result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        val loginSession = LoginSession(
                            token = result.data.token
                        )
                        loginViewModel.saveSession(loginSession)
                        moveHomepage()
                        showMessage(result.data.message!!)
                        binding.progressBar.visibility = View.GONE
                    }
                    is Result.Error -> {
                        showMessage(result.error)
                        binding.progressBar.visibility = View.GONE
                    }
                }
            })
        } else {
            showMessage(getString(R.string.validation_message))
        }
    }
    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showHidePass() {
        // TODO: Buatkan function ketika user menekan icon drawable eye akan muncul password
        if (isPasswordVisible) {
            // Hide Password
            binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_icon, 0)
        } else {
            // Show Password
            binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_off_icon, 0)
        }
        // Move cursor to end
        binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
        isPasswordVisible = !isPasswordVisible
    }
}
