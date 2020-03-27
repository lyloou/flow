package com.lyloou.flow.model

import com.lyloou.flow.common.SPreference
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.clear

enum class Order {
    A, B, C, D
}


object ScheduleHelper {
    fun clearSchedule() {
        SPreference(SpName.SCHEDULE_ITEM.name, "", "").clear()
    }
}