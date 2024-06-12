package com.bangkit.sostainable.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.sostainable.data.injection.InjectionAuth
import com.bangkit.sostainable.data.repository.auth.AuthRepository
import com.bangkit.sostainable.view.login.LoginViewModel
import com.bangkit.sostainable.view.register.RegisterViewModel

class AuthModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = AuthModelFactory(InjectionAuth.provideRepository(context))
    }
}