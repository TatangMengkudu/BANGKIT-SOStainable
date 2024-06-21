package com.bangkit.sostainable.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.sostainable.data.local.room.entities.JoinEvent

@Dao
interface JoinEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertJoin(event: JoinEvent)

    @Delete
    fun delete(event: JoinEvent)

    @Query("SELECT * FROM JoinEvent WHERE id_event = :idEvent LIMIT 1")
    fun getJoinEventById(idEvent: String): LiveData<JoinEvent>

    @Query("SELECT * FROM JoinEvent")
    fun getAllJoinEvent(): LiveData<List<JoinEvent>>
}