package com.bangkit.sostainable.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.sostainable.data.injection.InjectionJoinEvent
import com.bangkit.sostainable.data.repository.event.joinEvent.JoinEventRepository
import com.bangkit.sostainable.view.main.joinVolunter.JoinEventViewModel

class JoinEventFactory(private val joinEventRepository: JoinEventRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JoinEventViewModel::class.java)) {
            return JoinEventViewModel(joinEventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        fun getInstance(context: Context) = JoinEventFactory(InjectionJoinEvent.provideRepository(context))
    }
}