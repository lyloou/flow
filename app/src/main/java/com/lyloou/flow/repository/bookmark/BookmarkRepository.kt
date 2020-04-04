package com.lyloou.flow.repository.bookmark

import com.google.gson.reflect.TypeToken
import com.lyloou.flow.App
import com.lyloou.flow.model.Bookmark
import com.lyloou.flow.model.gson
import com.lyloou.flow.model.toJsonString
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object BookmarkRepository {
    private const val FILE_NAME = "bookmark.json"
    private val file = File(App.instance.filesDir, FILE_NAME)
    private val defaultBookmarkList = listOf(
        Bookmark("每日一文", "https://meiriyiwen.com/random/iphone"),
        Bookmark("陈 皓", "https://coolshell.cn/"),
        Bookmark("刘未鹏", "http://mindhacks.cn/"),
        Bookmark("木子楼", "http://lyloou.com", order = 2)
    )

    fun getBookmarkList(): List<Bookmark> {
        val type = object : TypeToken<List<Bookmark>>() {}.type
        if (file.exists()) {
            FileReader(file).use {
                return gson.fromJson(it, type) ?: defaultBookmarkList
            }
        }
        return defaultBookmarkList
    }

    fun recoverDefault() {
        saveBookmarkList(defaultBookmarkList)
    }

    fun saveBookmarkList(list: List<Bookmark>) {
        if (!file.exists()) {
            file.createNewFile()
        }

        FileWriter(file).use {
            it.write(list.toJsonString())
            it.flush()
        }
    }
}