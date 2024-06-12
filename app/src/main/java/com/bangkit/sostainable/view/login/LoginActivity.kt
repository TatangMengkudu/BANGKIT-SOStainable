package com.bangkit.sostainable.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.bangkit.sostainable.data.local.datastore.model.LoginSession
import com.bangkit.sostainable.data.repository.auth.User
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

    private fun setupAction() {
        // MOVE TO REGISTER PAGE
        moveRegister()

        // LOGIN
        binding.loginButton.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
        }
    }

    private fun moveHomepage() {
        binding.loginButton.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
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
            val user = User(username = username, password = password)
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
}
