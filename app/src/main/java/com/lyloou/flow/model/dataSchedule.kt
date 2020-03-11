package com.lyloou.flow.model

import android.app.Application
import android.util.Log
import com.google.gson.GsonBuilder

private val gson = GsonBuilder()
    .setLenient()
    .create()

data class ScheduleItem(val name: String, var content: String)
enum class Order {
    A, B, C, D
}

data class Schedule(
    val a: ScheduleItem = ScheduleItem(Order.A.name, ""),
    val b: ScheduleItem = ScheduleItem(Order.B.name, ""),
    val c: ScheduleItem = ScheduleItem(Order.C.name, ""),
    val d: ScheduleItem = ScheduleItem(Order.D.name, "")
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

    const val key = "schedule_key"
    fun getSchedule(application: Application): Schedule {
        val preferences = getPreferences(application)
        val schedule: Schedule = ScheduleHelper.fromJson(preferences.getString(key, "") ?: "")
        return schedule
    }

    fun getPreferences(application: Application) =
        application.getSharedPreferences("SP_SCHEDULE", Application.MODE_PRIVATE)
}