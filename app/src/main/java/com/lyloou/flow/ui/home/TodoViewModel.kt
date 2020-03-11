package com.lyloou.flow.ui.home

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.Order
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.model.ScheduleHelper
import com.lyloou.flow.model.ScheduleHelper.key
import com.lyloou.flow.model.toJson


class TodoViewModel(application: Application) : AndroidViewModel(application) {

    val schedule: Schedule = ScheduleHelper.getSchedule(application)
    val preferences: SharedPreferences = ScheduleHelper.getPreferences(application)

    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val content: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun refreshContent() {
        content.value = when (name.value) {
            Order.A.name -> schedule.a.content
            Order.B.name -> schedule.b.content
            Order.C.name -> schedule.c.content
            Order.D.name -> schedule.d.content
            else -> ""
        }
    }

    fun save() {
        when (name.value) {
            Order.A.name -> schedule.a.content = content.value ?: ""
            Order.B.name -> schedule.b.content = content.value ?: ""
            Order.C.name -> schedule.c.content = content.value ?: ""
            Order.D.name -> schedule.d.content = content.value ?: ""
        }
        preferences.edit().putString(key, schedule.toJson()).apply()
    }
}