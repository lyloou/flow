package com.lyloou.flow.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.SpPreference
import com.lyloou.flow.model.Order


class ScheduleDetailViewModel(application: Application) : AndroidViewModel(application) {

    var a: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_A.name, "")
    var b: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_B.name, "")
    var c: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_C.name, "")
    var d: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_D.name, "")

    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val content: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun refreshContent() {
        content.value = when (name.value) {
            Order.A.name -> a
            Order.B.name -> b
            Order.C.name -> c
            Order.D.name -> d
            else -> ""
        }
    }

    fun save() {
        when (name.value) {
            Order.A.name -> a = content.value ?: ""
            Order.B.name -> b = content.value ?: ""
            Order.C.name -> c = content.value ?: ""
            Order.D.name -> d = content.value ?: ""
        }
    }
}