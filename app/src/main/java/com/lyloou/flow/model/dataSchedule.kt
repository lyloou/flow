package com.lyloou.flow.model

import android.app.Application
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName


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
    private fun fromJson(string: String): Schedule {
        val fromJson = gson.fromJson(string, Schedule::class.java)
        return fromJson ?: Schedule()
    }


    fun getSchedule(application: Application): Schedule {
        return fromJson(getPreferences(application).getString(Key.SCHEDULE.name, "") ?: "")
    }

    fun saveSchedule(application: Application, value: Schedule) {
        getPreferences(application).edit().putString(Key.SCHEDULE.name, value.toJson()).apply()
    }

    private fun getPreferences(application: Application) =
        application.getSharedPreferences(SpName.SCHEDULE.name, Application.MODE_PRIVATE)
}