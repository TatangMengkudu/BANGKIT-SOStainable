package com.bangkit.sostainable.data.repository.bookmark

import androidx.lifecycle.LiveData
import com.bangkit.sostainable.data.local.room.dao.BookmarkDao
import com.bangkit.sostainable.data.local.room.entities.Bookmark
import com.bangkit.sostainable.data.utils.AppExecutors

class BookmarkRepository(
    private val bookmarkDao: BookmarkDao,
    private val appExecutors: AppExecutors
) {

    fun getListBookmark(): LiveData<List<Bookmark>> {
        return bookmarkDao.getListBookmark()
    }
    fun getBookmarkById(event: String): LiveData<Bookmark> {
        return bookmarkDao.getBookmarkById(event)
    }
    fun insertBookmark(event: Bookmark) {
        appExecutors.diskIO.execute {
            bookmarkDao.insertBookmark(event)
        }
    }
    fun deleteBookmark(event: Bookmark) {
        appExecutors.diskIO.execute {
            bookmarkDao.deleteBookmark(event.idEvent)
        }
    }
    companion object {
        @Volatile
        private var instance: BookmarkRepository? = null
        fun getInstance(
            bookmarkDao: BookmarkDao,
            appExecutors: AppExecutors
        ): BookmarkRepository =
            instance ?: synchronized(this) {
                instance ?: BookmarkRepository(bookmarkDao, appExecutors)
            }.also { instance = it }
    }
}