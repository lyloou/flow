package com.lyloou.flow.ui.web

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.Bookmark
import com.lyloou.flow.repository.bookmark.BookmarkRepository

class NormalWebViewModel(application: Application) : AndroidViewModel(application) {
    val data: MutableLiveData<MutableList<Bookmark>> by lazy {
        MutableLiveData(BookmarkRepository.getBookmarkList().toMutableList())
    }

    fun addBookmark(title: String, url: String) {
        data.value?.let {
            it.add(0, Bookmark(title, url))
            BookmarkRepository.saveBookmarkList(it)
        }
    }

    fun deleteBookmark(url: String) {
        data.value?.let {
            val iterator = it.iterator()
            while (iterator.hasNext()) {
                val bookmark = iterator.next()
                if (bookmark.url == url) {
                    iterator.remove()
                }
            }

            BookmarkRepository.saveBookmarkList(it)
        }
    }
}