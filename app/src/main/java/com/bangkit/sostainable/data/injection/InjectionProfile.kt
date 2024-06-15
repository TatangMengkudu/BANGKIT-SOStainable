package com.bangkit.sostainable.data.injection

import android.content.Context
import com.bangkit.sostainable.BuildConfig
import com.bangkit.sostainable.data.local.datastore.UserPreference
import com.bangkit.sostainable.data.local.datastore.dataStore
import com.bangkit.sostainable.data.remote.api.config.ApiConfig
import com.bangkit.sostainable.data.repository.profile.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object InjectionProfile {
    fun provideRepository(context: Context): ProfileRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(BuildConfig.BASE_URL, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJuYW1lIjoic2l3YSJ9LCJpYXQiOjE3MTg0MTEyODUsImV4cCI6MTcxODQxNDg4NX0.DTgLDCkYY1TJKhqURBnMUIzvJjhfH0GGuc4ZeCUlpYY")
        return ProfileRepository(apiService)
    }
}