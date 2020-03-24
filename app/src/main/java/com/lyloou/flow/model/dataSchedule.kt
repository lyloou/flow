package com.lyloou.flow.model

import com.lyloou.flow.App
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.extension.Preference
import com.lyloou.flow.extension.clear
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
    private var preference =
        Preference(App.instance, Key.SCHEDULE.name, "", SpName.SCHEDULE.name)
    private var data: String by preference

    fun fromJson(string: String?): Schedule {
        val fromJson = gson.fromJson(string, Schedule::class.java)
        return fromJson ?: Schedule(Utime.today())
    }

    fun getSchedule(): Schedule {
        return fromJson(data)
    }

    fun saveSchedule(value: Schedule) {
        data = value.toJson()
    }

    fun clearSchedule() {
        preference.clear()
    }
}