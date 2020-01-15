package com.lyloou.flow.model

import android.util.Log
import com.google.gson.GsonBuilder

private val gson = GsonBuilder()
    .setLenient()
    .create()

data class ScheduleItem(val name: String, var content: String)

data class Schedule(
    val a: ScheduleItem = ScheduleItem("A", ""),
    val b: ScheduleItem = ScheduleItem("B", ""),
    val c: ScheduleItem = ScheduleItem("C", ""),
    val d: ScheduleItem = ScheduleItem("D", "")
)

fun Schedule.toJson(): String {
    return gson.toJson(this)
}

object ScheduleHelper {
    fun fromJson(string: String): Schedule {
        val fromJson = gson.fromJson(string, Schedule::class.java)
        Log.i("TTAG", "string: $string");
        Log.i("TTAG", "data: $fromJson");
        return fromJson ?: Schedule()
    }
}