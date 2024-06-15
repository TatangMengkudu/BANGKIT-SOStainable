package com.bangkit.sostainable.view.main.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.json.DonateJson
import com.bangkit.sostainable.data.remote.response.event.EventMessageResponse
import com.bangkit.sostainable.data.repository.event.EventRepository
import com.bangkit.sostainable.data.utils.Result

class DonateViewModel(private val eventRepository: EventRepository):ViewModel() {
    suspend fun donateEvent(donateJson: DonateJson): LiveData<Result<EventMessageResponse>> =
        eventRepository.donateEvent(donateJson)
}