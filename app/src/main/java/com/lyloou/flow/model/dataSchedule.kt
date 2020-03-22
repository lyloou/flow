package com.lyloou.flow.model

import android.app.Application
import com.lyloou.flow.App
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.util.Utime


data class ScheduleItem(val name: String, var content: String)
enum class Order {
    A, B, C, D
}

data class Schedule(
    val date: String,
    val a: ScheduleItem = ScheduleItem(Order.A.name, ""),
    val b: ScheduleItem = ScheduleItem(Order.B.name, ""),
    val c: ScheduleItem = ScheduleItem(Order.C.name, ""),
    val d: ScheduleItem = ScheduleItem(Order.D.name, "")
)

fun Schedule.toJson(): String {
    return gson.toJson(this)
}

object ScheduleHelper {
    private val preferences = App.instance
        .getSharedPreferences(SpName.SCHEDULE.name, Application.MODE_PRIVATE)

    private fun fromJson(string: String): Schedule {
        val fromJson = gson.fromJson(string, Schedule::class.java)
        return fromJson ?: Schedule(Utime.today())
    }

    fun getSchedule(): Schedule {
        return fromJson(preferences.getString(Key.SCHEDULE.name, "") ?: "")
    }

    fun saveSchedule(value: Schedule) {
        preferences.edit().putString(Key.SCHEDULE.name, value.toJson()).apply()
    }

    fun clearSchedule() {
        preferences.edit().remove(Key.SCHEDULE.name).apply()
    }
}