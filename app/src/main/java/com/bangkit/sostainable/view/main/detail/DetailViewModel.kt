package com.bangkit.sostainable.view.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.sostainable.data.json.JoinJson
import com.bangkit.sostainable.data.remote.response.event.EventMessageResponse
import com.bangkit.sostainable.data.remote.response.event.detail.DetailResponse
import com.bangkit.sostainable.data.repository.event.EventRepository
import com.bangkit.sostainable.data.utils.Result
import kotlinx.coroutines.launch

class DetailViewModel(
    private var eventRepository: EventRepository
):ViewModel() {
    suspend fun getDetailEvent(id: String): LiveData<Result<DetailResponse>> {
        return eventRepository.getDetailEvent(id)
    }

    suspend fun joinEvent(joinJson: JoinJson): LiveData<Result<EventMessageResponse>> {
        return eventRepository.joinEvent(joinJson)
    }
}