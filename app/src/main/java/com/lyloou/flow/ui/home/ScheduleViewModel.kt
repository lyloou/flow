package com.lyloou.flow.ui.home

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SPreference
import com.lyloou.flow.common.SpName
import com.lyloou.flow.model.ScheduleHelper
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.ScheduleRepository
import com.lyloou.flow.util.Udata
import com.lyloou.flow.util.Utime


class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    var a: String by SPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_A.name, "")
    var b: String by SPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_B.name, "")
    var c: String by SPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_C.name, "")
    var d: String by SPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_D.name, "")

    fun isEmpty(): Boolean {
        return TextUtils.isEmpty(a + b + c + d)
    }

    fun clearSchedule() {
        ScheduleHelper.clearSchedule()
    }

    fun saveSchedule(dbSchedule: DbSchedule) {
        a = dbSchedule.a ?: ""
        b = dbSchedule.b ?: ""
        c = dbSchedule.c ?: ""
        d = dbSchedule.d ?: ""
    }

    fun startNewSchedule() {
        if (isEmpty()) {
            return
        }

        // 保存现有的到数据库
        ScheduleRepository.getInstance(getApplication()).insertDbSchedule(
            DbSchedule(
                0,
                Udata.uuid(),
                0,
                Utime.today(),
                "",
                a, b, c, d,
                Utime.now()
            )
        )

        // 清空 schedule
        clearSchedule()
    }

    fun enterMode(name: String) {
        val application = getApplication() as Application
        val intent = Intent(application, ScheduleDetailActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Key.SCHEDULE.name, name)
        application.startActivity(intent)
    }
}