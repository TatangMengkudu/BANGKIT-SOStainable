package com.bangkit.sostainable.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.sostainable.data.json.LoginJson
import com.bangkit.sostainable.data.local.datastore.model.LoginSession
import com.bangkit.sostainable.data.remote.response.auth.AuthResponse
import com.bangkit.sostainable.data.repository.auth.AuthRepository
import com.bangkit.sostainable.data.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    suspend fun login(loginJson: LoginJson) : LiveData<Result<AuthResponse>> {
        return authRepository.login(loginJson)
    }

    fun saveSession(token: LoginSession) {
        viewModelScope.launch {
            authRepository.saveSession(token)
        }
    }
}