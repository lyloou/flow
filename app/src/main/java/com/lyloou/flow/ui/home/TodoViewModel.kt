package com.lyloou.flow.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.Order
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.model.ScheduleHelper


class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val schedule: Schedule = ScheduleHelper.getSchedule()

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
        ScheduleHelper.saveSchedule(schedule)
    }
}