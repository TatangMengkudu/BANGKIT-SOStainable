package com.bangkit.sostainable.data.repository.event.joinEvent

import androidx.lifecycle.LiveData
import com.bangkit.sostainable.data.local.room.dao.JoinEventDao
import com.bangkit.sostainable.data.local.room.entities.JoinEvent
import com.bangkit.sostainable.data.utils.AppExecutors

class JoinEventRepository(private val joinEventDao: JoinEventDao,private val appExecutors: AppExecutors) {
    fun insertJoin(event: JoinEvent) {
        appExecutors.diskIO.execute {
            joinEventDao.insertJoin(event)
        }
    }
    fun deleteJoin(event: JoinEvent) {
        appExecutors.diskIO.execute {
            joinEventDao.delete(event)
        }
    }

    fun getJoinEventById(idEvent: String): LiveData<JoinEvent> {
        return joinEventDao.getJoinEventById(idEvent)
    }

    fun getAllJoinEvent(): LiveData<List<JoinEvent>> {
        return joinEventDao.getAllJoinEvent()
    }

}