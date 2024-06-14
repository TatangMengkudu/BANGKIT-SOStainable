package com.bangkit.sostainable.view.main.profile

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.ProfileModelFactory
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileUser()
        }
    }

    private suspend fun profileUser(){
        profileViewModel.profileUser().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    result.data.data?.let { profile ->
                        binding.apply {
                            edtName.text = profile.nama as Editable?
                            edtUsername.text = profile.username as Editable?
                            edtTglLahir.text = profile.tanggalLahir as Editable?
                            edtNoRekening.text = profile.noRekening as Editable?
                            edtNamaBank.text = profile.namaBank as Editable?
                            edtPassword.text = profile.password as Editable?
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

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}