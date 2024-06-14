package com.bangkit.sostainable.view.main.profile

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.AuthModelFactory
import com.bangkit.sostainable.data.factory.ProfileModelFactory
import com.bangkit.sostainable.data.local.datastore.model.LoginSession
import com.bangkit.sostainable.data.repository.auth.User
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.databinding.FragmentProfileBinding
import com.bangkit.sostainable.view.login.LoginViewModel
import com.bangkit.sostainable.view.utils.dateFormat
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileModelFactory.getInstance(requireContext())
    }
    private val loginViewModel: LoginViewModel by viewModels {
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

        binding.buttonUpdateProfile.setOnClickListener {
            lifecycleScope.launch {
                updateProfile()
            }
        }
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
    private suspend fun updateProfile() {
        val name = binding.edtName.text.toString().trim()
        val username = binding.edtUsername.text.toString().trim()
        val tanggalLahir = binding.edtTglLahir.text.toString().trim()
        val nomorRekening = binding.edtNoRekening.text.toString().trim()
        val namaBank = binding.edtNamaBank.text.toString().trim()
        val nomorTelp = binding.edtNoTelp.text.toString().trim()
        val alamat = binding.edtAlamat.text.toString().trim()

        val user = User(
            name = name,
            username = username,
            tanggalLahir = tanggalLahir,
            nomorRekening = nomorRekening,
            namaBank = namaBank,
            nomorTelp = nomorTelp,
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

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}