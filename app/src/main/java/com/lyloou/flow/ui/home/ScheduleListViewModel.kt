package com.lyloou.flow.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.lyloou.flow.common.Consumer
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.ScheduleRepository
import com.lyloou.flow.util.Utime

class ScheduleListViewModel(application: Application) : AndroidViewModel(application) {

    val repository: ScheduleRepository by lazy {
        ScheduleRepository.getInstance(application)
    }

    val enabledScheduleList: LiveData<PagedList<DbSchedule>> by lazy {
        repository.getEnabledPagedList()
    }

    fun deleteSchedule(vararg dbSchedules: DbSchedule) {
        repository.deleteDbSchedule(*dbSchedules)
    }

    var allScheduleList: MutableLiveData<List<DbSchedule>> = MutableLiveData()

    fun getAllSchedule() {
        repository.getAllDbScheduleList(Consumer {
            allScheduleList.value = it
        })
    }

    fun updateSchedule(vararg dbSchedules: DbSchedule) {
        for (schedule in dbSchedules) {
            schedule.syncTime = Utime.now()
        }
        repository.updateDbSchedule(*dbSchedules)
    }

    fun cleanSchedules(vararg dbSchedules: DbSchedule) {
        val canBeClean = mutableListOf<DbSchedule>()
        for (schedule in dbSchedules) {
            // 若是删除，且远程没有此项，则直接删除了
            if (schedule.rsyncTime == 0L && schedule.isDisabled) {
                canBeClean.add(schedule)
                continue
            }
        }

        deleteSchedule(*canBeClean.toTypedArray())
    }
}