package com.bangkit.sostainable.data.injection

import android.content.Context
import com.bangkit.sostainable.data.local.room.database.EventDatabase
import com.bangkit.sostainable.data.repository.event.joinEvent.JoinEventRepository
import com.bangkit.sostainable.data.utils.AppExecutors

object InjectionJoinEvent {
    fun provideRepository(context: Context): JoinEventRepository {
        val database = EventDatabase.getDatabase(context)
        val dao = database.joinEventDao()
        val appExecutors = AppExecutors()
        return JoinEventRepository(dao, appExecutors)
    }
}