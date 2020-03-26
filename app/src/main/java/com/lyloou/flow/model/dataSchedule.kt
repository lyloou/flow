package com.lyloou.flow.model

import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.SpPreference

enum class Order {
    A, B, C, D
}


object ScheduleHelper {
    var a: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_A.name, "")
    var b: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_B.name, "")
    var c: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_C.name, "")
    var d: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_D.name, "")


    fun clearSchedule() {
        a = ""
        b = ""
        c = ""
        d = ""
    }
}