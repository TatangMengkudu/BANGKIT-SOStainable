package com.bangkit.sostainable.data.injection

import android.content.Context
import com.bangkit.sostainable.data.local.room.database.EventDatabase
import com.bangkit.sostainable.data.repository.bookmark.BookmarkRepository
import com.bangkit.sostainable.data.utils.AppExecutors

object InjectionBookmark {
    fun provideRepository(context: Context): BookmarkRepository {
        val database = EventDatabase.getDatabase(context)
        val dao = database.bookmarkDao()
        val appExecutors = AppExecutors()
        return BookmarkRepository(dao, appExecutors)
    }
}