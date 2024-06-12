package com.bangkit.sostainable.view.register

import androidx.lifecycle.LiveData
import com.bangkit.sostainable.data.remote.response.auth.AuthResponse
import com.bangkit.sostainable.data.repository.auth.AuthRepository
import com.bangkit.sostainable.data.repository.auth.User
import com.bangkit.sostainable.data.utils.Result

class RegisterViewModel(
    private val authRepository: AuthRepository
) {
    suspend fun register(user: User) : LiveData<Result<AuthResponse>> {
        return authRepository.register(user)
    }
}