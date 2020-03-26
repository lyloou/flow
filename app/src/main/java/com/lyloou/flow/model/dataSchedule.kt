package com.lyloou.flow.model

import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.SpPreference
import com.lyloou.flow.common.clear

enum class Order {
    A, B, C, D
}


object ScheduleHelper {
    fun clearSchedule() {
        SpPreference(SpName.SCHEDULE_ITEM.name, "", "").clear()
    }
}