package com.bangkit.sostainable.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.sostainable.data.local.room.entities.Bookmark

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM Bookmark")
    fun getListBookmark() : LiveData<List<Bookmark>>

    @Query("SELECT * FROM Bookmark WHERE id_event = :idEvent")
    fun getBookmarkById(idEvent: String): LiveData<Bookmark>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBookmark(event: Bookmark)

    @Query("DELETE FROM Bookmark WHERE id_event = :idEvent")
    fun deleteBookmark(idEvent: String)
}