package com.bangkit.sostainable.data.repository.event

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.sostainable.data.local.room.database.EventDatabase
import com.bangkit.sostainable.data.paging.EventRemoteMediator
import com.bangkit.sostainable.data.remote.api.service.ApiService
import com.bangkit.sostainable.data.remote.response.event.DataItem

class EventRepository(
    private val apiService: ApiService,
    private val eventDatabase: EventDatabase
) {
    // TODO: Get All Event
    fun getEvents(): LiveData<PagingData<DataItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = EventRemoteMediator(apiService, eventDatabase),
            pagingSourceFactory = {
                eventDatabase.eventDao().getAllEvent()
            }
        ).liveData
    }

    // TODO: Get Detail Event

    // TODO: Create Event

    // TODO: Join Event

    // TODO: Donate Event
}