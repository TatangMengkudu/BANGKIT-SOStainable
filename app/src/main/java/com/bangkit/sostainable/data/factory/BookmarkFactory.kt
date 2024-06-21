package com.bangkit.sostainable.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.sostainable.data.injection.InjectionBookmark
import com.bangkit.sostainable.data.repository.bookmark.BookmarkRepository
import com.bangkit.sostainable.view.main.bookmark.BookmarkViewModel

class BookmarkFactory private constructor(
    private val bookmarkRepository: BookmarkRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(bookmarkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        fun getInstance(context: Context) = BookmarkFactory(InjectionBookmark.provideRepository(context))
    }
}