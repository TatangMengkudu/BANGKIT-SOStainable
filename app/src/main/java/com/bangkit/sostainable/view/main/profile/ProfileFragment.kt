package com.bangkit.sostainable.view.main.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.AuthModelFactory
import com.bangkit.sostainable.data.factory.ProfileModelFactory
import com.bangkit.sostainable.data.json.ProfileJson
import com.bangkit.sostainable.data.local.datastore.model.LoginSession
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.databinding.FragmentProfileBinding
import com.bangkit.sostainable.view.login.LoginActivity
import com.bangkit.sostainable.view.login.LoginViewModel
import com.bangkit.sostainable.view.main.MainViewModel
import com.bangkit.sostainable.view.utils.dateFormat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val calender = Calendar.getInstance()
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileModelFactory.getInstance(requireContext())
    }
    private val loginViewModel: LoginViewModel by viewModels {
        AuthModelFactory.getInstance(requireContext())
    }
    private val userSession: MainViewModel by viewModels {
        AuthModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileUser()
        }

        setProfile()

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
        logout()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun profileUser(){
        profileViewModel.profileUser().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    result.data.data?.let { profile ->
                        binding.apply {
                            edtName.text = profile.nama?.toEditable() ?: "".toEditable()
                            edtUsername.text = profile.username?.toEditable()
                            edtTglLahir.text = profile.tanggalLahir?.let { dateFormat(it).toEditable() }
                            edtNoRekening.text = profile.noRekening?.toEditable()
                            edtNamaBank.text = profile.namaBank?.toEditable()
                            edtNoTelp.text = profile.noTelepon?.toEditable()
                            edtAlamat.text = profile.alamat?.toEditable()
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    showMessage(getString(R.string.fail_get_account))
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setProfile() {
        binding.buttonUpdateProfile.setOnClickListener {
            lifecycleScope.launch {
                updateProfile()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateProfile() {
        val nama = binding.edtName.text.toString().trim()
        val username = binding.edtUsername.text.toString().trim()
        val tanggalLahir = binding.edtTglLahir.text.toString().trim()
        val nomorRekening = binding.edtNoRekening.text.toString().trim()
        val namaBank = binding.edtNamaBank.text.toString().trim()
        val nomorTelp = binding.edtNoTelp.text.toString().trim()
        val alamat = binding.edtAlamat.text.toString().trim()

        val user = ProfileJson(
            nama = nama,
            username = username,
            tanggal_lahir = tanggalLahir,
            no_rekening = nomorRekening,
            nama_bank = namaBank,
            no_telepon = nomorTelp,
            alamat = alamat
        )
        profileViewModel.updateProfile(user).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val loginSession = LoginSession(
                        token = result.data.token
                    )
                    loginViewModel.saveSession(loginSession)
                    showMessage(result.data.message!!)
                    binding.progressBar.visibility = View.GONE
                    lifecycleScope.launch {
                        profileUser()
                    }
                }
                is Result.Error -> {
                    showMessage(getString(R.string.fail_get_account))
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun logout() {
        binding.buttonLogout.setOnClickListener {
            userSession.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun selectBank() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        val options = arrayOf(
            "BCA",
            "BRI",
            "BNI",
            "BTN",
            "Mandiri",
            "BSI Syariah")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Bank")

        builder.setItems(options) { dialog, which ->
            binding.edtNamaBank.text = options[which].toEditable()
        }

        builder.show()
    }

    private fun selectDate() {
        val datePicker = DatePickerDialog(requireContext(), { _, year: Int, month: Int, dayOfMonth: Int ->
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
}