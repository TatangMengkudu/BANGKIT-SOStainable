package com.bangkit.sostainable.view.main.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.local.room.entities.Bookmark
import com.bangkit.sostainable.data.repository.bookmark.BookmarkRepository

class BookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    fun getListBookmark(): LiveData<List<Bookmark>> {
        return bookmarkRepository.getListBookmark()
    }

    fun getBookmarkById(event: String): LiveData<Bookmark> {
        return bookmarkRepository.getBookmarkById(event)
    }

    fun insertBookmark(event: Bookmark) {
        return bookmarkRepository.insertBookmark(event)
    }

    fun deleteBookmark(event: Bookmark) {
        return bookmarkRepository.deleteBookmark(event)
    }
}