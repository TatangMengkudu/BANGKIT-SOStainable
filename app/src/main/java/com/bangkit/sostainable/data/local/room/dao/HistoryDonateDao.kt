package com.bangkit.sostainable.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.sostainable.data.local.room.entities.HistoryDonateEvent

@Dao
interface HistoryDonateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDonate(event: HistoryDonateEvent)

    @Query("SELECT * FROM HistoryDonateEvent")
    fun getAllHistoryDonate(): LiveData<List<HistoryDonateEvent>>
}