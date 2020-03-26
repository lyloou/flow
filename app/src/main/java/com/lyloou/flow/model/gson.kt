package com.lyloou.flow.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder


val gson: Gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .setLenient()
    .create()

fun Any.toJsonString(): String {
    return gson.toJson(this)
}