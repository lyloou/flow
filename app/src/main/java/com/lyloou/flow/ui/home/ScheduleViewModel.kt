package com.lyloou.flow.ui.home

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.lifecycle.AndroidViewModel
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.SpPreference
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.model.toJsonString
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.ScheduleRepository
import com.lyloou.flow.util.Utime


class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    var a: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_A.name, "")
    var b: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_B.name, "")
    var c: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_C.name, "")
    var d: String by SpPreference(SpName.SCHEDULE_ITEM.name, Key.SCHEDULE_ITEM_D.name, "")


    fun startNewSchedule() {
        // 保存现有的到数据库
        val repository = ScheduleRepository.getInstance(getApplication())
        repository.insertDbSchedule(
            DbSchedule(
                0,
                0,
                Utime.today(),
                Schedule(Utime.today(), a, b, c, d).toJsonString(),
                "2020-03-26"
            )
        )

        // 清空 schedule
        a = ""
        b = ""
        c = ""
        d = ""

    }

    fun enterMode(name: String) {
        val application = getApplication() as Application
        val intent = Intent(application, ScheduleDetailActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Key.SCHEDULE.name, name)
        application.startActivity(intent)
    }
}