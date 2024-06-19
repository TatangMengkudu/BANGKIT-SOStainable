package com.bangkit.sostainable.view.register

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.AuthModelFactory
import com.bangkit.sostainable.data.json.RegisterJson
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.databinding.ActivityRegisterBinding
import com.bangkit.sostainable.view.login.LoginActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        AuthModelFactory.getInstance(this)
    }
    private val calender = Calendar.getInstance()
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

        // SELECT Date of Birth
        binding.edtTglLahir.apply {
            isFocusable = false
            isFocusableInTouchMode = false
            inputType = android.text.InputType.TYPE_NULL

            setOnClickListener {
                selectDate()
            }
        }

        // SELECT BANK
        binding.edtNamaBank.apply {
            isFocusable = false
            isFocusableInTouchMode = false
            inputType = android.text.InputType.TYPE_NULL

            setOnClickListener {
                selectBank()
            }
        }

        // REGISTER
        binding.registerButton.setOnClickListener {
            lifecycleScope.launch {
                register()
            }
        }
    }

    private fun moveLogin() {
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

        if (usernameValid && emailValid && passwordValid == confirmPassValid) {
            val user = RegisterJson(
                username = username,
                email = email,
                tanggal_lahir = tanggalLahir,
                no_rekening = nomorRekening,
                nama_bank = namaBank,
                password = password
            )
            registerViewModel.register(user).observe(this, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        showMessage(result.data.message!!)
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
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

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun selectDate() {
        val datePicker = DatePickerDialog(this, { _, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.edtTglLahir.text = formattedDate.toEditable()
        },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun selectBank() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        val options = arrayOf(
            "BCA",
            "BRI",
            "BNI",
            "BTN",
            "Mandiri",
            "BSI Syariah")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Bank")

        builder.setItems(options) { dialog, which ->
            binding.edtNamaBank.text = options[which].toEditable()
        }

        builder.show()
    }
}