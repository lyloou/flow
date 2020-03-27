package com.lyloou.flow.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.ScheduleRepository
import com.lyloou.flow.util.Utime

class ScheduleListViewModel(application: Application) : AndroidViewModel(application) {

    val repository: ScheduleRepository by lazy {
        ScheduleRepository.getInstance(application)
    }

    val dbScheduleList: LiveData<PagedList<DbSchedule>> by lazy {
        repository.getEnabledPagedList()
    }

    fun deleteSchedule(vararg dbSchedules: DbSchedule) {
        repository.deleteDbSchedule(*dbSchedules)
    }

    fun updateSchedule(vararg dbSchedules: DbSchedule) {
        for (schedule in dbSchedules) {
            schedule.syncTime = Utime.now()
        }
        repository.updateDbSchedule(*dbSchedules)
    }
}