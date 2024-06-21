package com.bangkit.sostainable.data.injection

import android.content.Context
import com.bangkit.sostainable.data.local.room.database.EventDatabase
import com.bangkit.sostainable.data.repository.event.historyDonate.HistoryDonateRepository
import com.bangkit.sostainable.data.utils.AppExecutors

object InjectionHistoryDonate {
    fun provideRepository(context: Context): HistoryDonateRepository {
        val database = EventDatabase.getDatabase(context)
        val dao = database.historyDonateDao()
        val appExecutors = AppExecutors()
        return HistoryDonateRepository(dao, appExecutors)
    }
}