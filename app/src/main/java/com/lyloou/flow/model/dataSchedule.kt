package com.lyloou.flow.model

import android.app.Application
import com.lyloou.flow.App
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.util.Utime


enum class Order {
    A, B, C, D
}

data class Schedule(
    val title: String,
    var a: String = "",
    var b: String = "",
    var c: String = "",
    var d: String = ""
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