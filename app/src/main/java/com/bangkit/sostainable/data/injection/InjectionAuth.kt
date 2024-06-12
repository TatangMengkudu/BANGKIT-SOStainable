package com.bangkit.sostainable.data.injection

import android.content.Context
import com.bangkit.sostainable.BuildConfig
import com.bangkit.sostainable.data.local.datastore.UserPreference
import com.bangkit.sostainable.data.local.datastore.dataStore
import com.bangkit.sostainable.data.remote.api.config.ApiConfig
import com.bangkit.sostainable.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object InjectionAuth {
    fun provideRepository(context: Context): AuthRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(BuildConfig.BASE_URL, user.token!!)
        return AuthRepository(apiService, userPreference)
    }
}