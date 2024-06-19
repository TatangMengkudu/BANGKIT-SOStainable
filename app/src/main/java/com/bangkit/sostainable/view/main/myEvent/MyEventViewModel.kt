package com.bangkit.sostainable.view.main.myEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.remote.response.event.MyEventResponse
import com.bangkit.sostainable.data.repository.event.EventRepository
import com.bangkit.sostainable.data.utils.Result

class MyEventViewModel(private val eventRepository: EventRepository):ViewModel() {
    suspend fun getMyEvent(): LiveData<Result<MyEventResponse>> = eventRepository.getMyEvent()
}