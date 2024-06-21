package com.bangkit.sostainable.view.main.donate.historyDonate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.local.room.entities.HistoryDonateEvent
import com.bangkit.sostainable.data.repository.event.historyDonate.HistoryDonateRepository

class HistoryDonateViewModel(private val repository: HistoryDonateRepository): ViewModel() {
    fun getAllHistoryDonate(): LiveData<List<HistoryDonateEvent>> {
        return repository.getAllHistoryDonate()
    }

    fun insertHistoryDonate(event: HistoryDonateEvent) {
        return repository.insertHistoryDonate(event)
    }
}