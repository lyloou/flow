package com.lyloou.flow.ui.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.Bookmark
import com.lyloou.flow.model.bookmarkComparator
import com.lyloou.flow.repository.bookmark.BookmarkRepository

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val data: MutableLiveData<MutableList<Bookmark>> = MutableLiveData(
        BookmarkRepository.getBookmarkList()
            .sortedWith(bookmarkComparator())
            .toMutableList()
    )

    fun reload() {
        data.value = BookmarkRepository.getBookmarkList()
            .sortedWith(bookmarkComparator())
            .toMutableList()
    }

    fun addBookmark(bookmark: Bookmark) {
        data.value?.let {
            it.add(0, bookmark)
            BookmarkRepository.saveBookmarkList(it)
        }
    }

    fun deleteBookmark(bookmark: Bookmark) {
        data.value?.let {
            it.remove(bookmark)
            BookmarkRepository.saveBookmarkList(it)
        }
    }

    fun updateBookmark() {
        data.value?.let {
            BookmarkRepository.saveBookmarkList(it)
        }
    }

    fun recoverBookmark() {
        BookmarkRepository.recoverDefault()
    }
}