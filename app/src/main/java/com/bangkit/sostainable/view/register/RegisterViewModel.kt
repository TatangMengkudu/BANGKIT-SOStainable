package com.bangkit.sostainable.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.json.RegisterJson
import com.bangkit.sostainable.data.remote.response.auth.AuthResponse
import com.bangkit.sostainable.data.repository.auth.AuthRepository
import com.bangkit.sostainable.data.utils.Result

class RegisterViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    suspend fun register(user: RegisterJson) : LiveData<Result<AuthResponse>> {
        return authRepository.register(user)
    }
}