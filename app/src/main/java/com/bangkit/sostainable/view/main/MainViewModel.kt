package com.bangkit.sostainable.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.sostainable.data.local.datastore.model.LoginSession
import com.bangkit.sostainable.data.repository.auth.AuthRepository

class MainViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    fun getSession(): LiveData<LoginSession> {
        return authRepository.getSession().asLiveData()
    }
}