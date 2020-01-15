package com.lyloou.flow.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.model.ScheduleHelper
import com.lyloou.flow.model.toJson

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val key = "schedule_key"
    private val preferences =
        application.getSharedPreferences("SP_SCHEDULE", Application.MODE_PRIVATE)

    val schedule: Schedule = ScheduleHelper.fromJson(preferences.getString(key, "") ?: "")

    val a: MutableLiveData<String> by lazy {
        MutableLiveData<String>(schedule.a.content)
    }
    val b: MutableLiveData<String> by lazy {
        MutableLiveData<String>(schedule.b.content)
    }
    val c: MutableLiveData<String> by lazy {
        MutableLiveData<String>(schedule.c.content)
    }
    val d: MutableLiveData<String> by lazy {
        MutableLiveData<String>(schedule.d.content)
    }

    fun save() {
        schedule.a.content = a.value ?: ""
        schedule.b.content = b.value ?: ""
        schedule.c.content = c.value ?: ""
        schedule.d.content = d.value ?: ""
        preferences.edit().putString(key, schedule.toJson()).apply()
    }
}