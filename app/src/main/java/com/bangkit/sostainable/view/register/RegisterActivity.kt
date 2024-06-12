package com.bangkit.sostainable.view.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
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
import com.bangkit.sostainable.data.repository.auth.User
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.databinding.ActivityRegisterBinding
import com.bangkit.sostainable.view.login.LoginActivity
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        AuthModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        // MOVE TO LOGIN PAGE
        moveLogin()

        // REGISTER
        binding.registerButton.setOnClickListener {
            lifecycleScope.launch {
                register()
            }
        }
    }

    private fun moveLogin() {
        // WITH BUTTON
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // WITH TEXTVIEW
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private suspend fun register() {
        val username = binding.edtUsername.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val tanggalLahir = binding.edtTglLahir.text.toString().trim()
        val nomorRekening = binding.edtNoRekening.text.toString().trim()
        val namaBank = binding.edtNamaBank.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        // Validasi
        val namePattern = getString(R.string.validasi_name)
        val emailPattern = Patterns.EMAIL_ADDRESS

        val usernameValid = when {
            username.isEmpty() -> false
            !username.matches(namePattern.toRegex()) -> false
            else -> true
        }

        val emailValid = when {
            email.isEmpty() -> false
            !email.matches(emailPattern.toRegex()) -> false
            else -> true
        }

        val passwordValid = when {
            password.isEmpty() -> false
            password.length < 8 -> false
            else -> true
        }

        val confirmPassValid = when {
            confirmPassword.isEmpty() -> false
            confirmPassword.length < 8 -> false
            else -> true
        }

        if (usernameValid && emailValid && passwordValid && confirmPassValid) {
            val user = User(
                username = username,
                email = email,
                tanggalLahir = tanggalLahir,
                nomorRekening = nomorRekening,
                namaBank = namaBank,
                password = password
            )
            registerViewModel.register(user).observe(this, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        showMessage(result.data.message!!)
                        moveLogin()
                        binding.progressBar.visibility = View.GONE
                    }
                    is Result.Error -> {
                        showMessage(getString(R.string.account_registered))
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