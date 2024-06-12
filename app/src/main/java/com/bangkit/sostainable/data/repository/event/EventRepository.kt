package com.bangkit.sostainable.data.repository.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.sostainable.data.remote.api.service.ApiService
import com.bangkit.sostainable.data.remote.response.event.EventMessageResponse
import com.bangkit.sostainable.data.remote.response.event.EventResponse
import com.bangkit.sostainable.data.utils.Result
import com.google.gson.Gson
import retrofit2.HttpException

class EventRepository(
    private val apiService: ApiService
) {
    // TODO: Get All Event
    suspend fun getEvents(): LiveData<Result<EventResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val data = apiService.getEvents()
                emit(Result.Success(data))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, EventMessageResponse::class.java)
                emit(Result.Error(errorResponse.message!!))
            }
        }
    }

    // TODO: Get Detail Event

    // TODO: Create Event

    // TODO: Join Event

    // TODO: Donate Event
}