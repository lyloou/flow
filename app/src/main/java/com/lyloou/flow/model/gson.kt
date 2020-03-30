package com.lyloou.flow.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


val gson: Gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .setLenient()
    .create()

fun Any.toJsonString(): String {
    return gson.toJson(this)
}

fun Any.toPrettyJsonString(): String {
    return GsonBuilder().setPrettyPrinting().create().toJson(this)
}

fun <T> String.jsonStringToList(): List<T> {
    val type = object : TypeToken<List<T>>() {}.type
    return gson.fromJson(this, type)
}

inline fun <reified T> String.jsonStringToObject(): T {
    return gson.fromJson(this, T::class.java)
}