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
        val apiService = ApiConfig.getApiService(BuildConfig.BASE_URL, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJuYW1lIjoic2l3YSJ9LCJpYXQiOjE3MTgzNzE2OTIsImV4cCI6MTcxODM3NTI5Mn0.Y9Q_D2Os0wFtPuHTa-Q6ZmGaSvdIiCYBRNDfT449U8E")
        return ProfileRepository(apiService)
    }
}