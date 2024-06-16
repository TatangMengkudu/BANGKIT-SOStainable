package com.bangkit.sostainable.view.main.joinVolunter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.local.room.entities.JoinEvent
import com.bangkit.sostainable.data.repository.event.joinEvent.JoinEventRepository

class JoinEventViewModel(private val joinEventRepository: JoinEventRepository): ViewModel() {
    fun insertJoin(event: JoinEvent) {
        return joinEventRepository.insertJoin(event)
    }
    fun deleteJoin(event: JoinEvent) {
        return joinEventRepository.deleteJoin(event)
    }
    fun getJoinEventById(idEvent: String): LiveData<JoinEvent> {
        return joinEventRepository.getJoinEventById(idEvent)
    }
    fun getAllJoinEvent(): LiveData<List<JoinEvent>> {
        return joinEventRepository.getAllJoinEvent()
    }
}