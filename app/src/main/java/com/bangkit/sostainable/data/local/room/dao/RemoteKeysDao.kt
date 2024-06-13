package com.bangkit.sostainable.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.sostainable.data.local.room.entities.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)
    @Query("SELECT * FROM Remote_Keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys?
    @Query("DELETE FROM Remote_Keys")
    suspend fun deleteRemoteKeys()
}