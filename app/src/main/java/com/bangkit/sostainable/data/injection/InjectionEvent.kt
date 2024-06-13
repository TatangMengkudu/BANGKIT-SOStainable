package com.bangkit.sostainable.data.injection

import android.content.Context
import com.bangkit.sostainable.BuildConfig
import com.bangkit.sostainable.data.local.datastore.UserPreference
import com.bangkit.sostainable.data.local.datastore.dataStore
import com.bangkit.sostainable.data.local.room.database.EventDatabase
import com.bangkit.sostainable.data.remote.api.config.ApiConfig
import com.bangkit.sostainable.data.repository.event.EventRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object InjectionEvent {
    fun provideRepository(context: Context): EventRepository {
        val database = EventDatabase.getDatabase(context)
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(BuildConfig.BASE_URL, user.token!!)
        return EventRepository(apiService, database)
    }
}