package com.bangkit.sostainable.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.sostainable.data.injection.InjectionHistoryDonate
import com.bangkit.sostainable.data.repository.event.historyDonate.HistoryDonateRepository
import com.bangkit.sostainable.view.main.donate.historyDonate.HistoryDonateViewModel

class HistoryDonateFactory(private val historyDonateRepository: HistoryDonateRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryDonateViewModel::class.java)) {
            return HistoryDonateViewModel(historyDonateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        fun getInstance(context: Context) = HistoryDonateFactory(InjectionHistoryDonate.provideRepository(context))
    }
}