package com.bangkit.sostainable.data.repository.event.historyDonate

import androidx.lifecycle.LiveData
import com.bangkit.sostainable.data.local.room.dao.HistoryDonateDao
import com.bangkit.sostainable.data.local.room.dao.JoinEventDao
import com.bangkit.sostainable.data.local.room.entities.HistoryDonateEvent
import com.bangkit.sostainable.data.local.room.entities.JoinEvent
import com.bangkit.sostainable.data.utils.AppExecutors

class HistoryDonateRepository(
    private val historyDonateDao: HistoryDonateDao,
    private val appExecutors: AppExecutors) {
    fun insertHistoryDonate(event: HistoryDonateEvent) {
        appExecutors.diskIO.execute {
            historyDonateDao.insertDonate(event)
        }
    }
    fun getAllHistoryDonate(): LiveData<List<HistoryDonateEvent>> {
        return historyDonateDao.getAllHistoryDonate()
    }

}