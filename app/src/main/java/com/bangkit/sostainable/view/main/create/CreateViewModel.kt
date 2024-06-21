package com.bangkit.sostainable.view.main.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.remote.response.event.EventMessageResponse
import com.bangkit.sostainable.data.repository.event.Event
import com.bangkit.sostainable.data.repository.event.EventRepository
import com.bangkit.sostainable.data.utils.Result

class CreateViewModel(private val eventRepository: EventRepository): ViewModel() {
    suspend fun createEvent(event: Event): LiveData<Result<EventMessageResponse>> {
        return eventRepository.createEvent(event)
    }
}