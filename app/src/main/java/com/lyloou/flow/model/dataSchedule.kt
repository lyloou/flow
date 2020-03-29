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

enum class SyncStatus(var desc: String) {
    LOCAL_ADD("本地新增"),
    REMOTE_ADD("远程新增"),
    LOCAL_CHANGE("本地修改"),
    REMOTE_CHANGE("远程修改"),
    ALL_CHANGE("匀有修改"),
    LOCAL_DELETE("本地删除"),
    ;
}