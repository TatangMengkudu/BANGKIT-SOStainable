package com.bangkit.sostainable.view.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.sostainable.data.remote.response.event.DataItem
import com.bangkit.sostainable.data.repository.event.EventRepository

class HomeViewModel(
    private val eventRepository: EventRepository
): ViewModel() {
    val event: LiveData<PagingData<DataItem>> =
        eventRepository.getEvents().cachedIn(viewModelScope)

}