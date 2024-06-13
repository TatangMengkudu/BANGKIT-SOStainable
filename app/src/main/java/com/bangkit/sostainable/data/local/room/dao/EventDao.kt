package com.bangkit.sostainable.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.sostainable.data.remote.response.event.DataItem

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: List<DataItem>)

    @Query("SELECT * FROM events")
    fun getAllEvent(): PagingSource<Int, DataItem>

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}