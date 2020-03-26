package com.lyloou.flow.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.ScheduleRepository

class ScheduleListViewModel(application: Application) : AndroidViewModel(application) {

    val dbScheduleList: LiveData<PagedList<DbSchedule>> by lazy {
        ScheduleRepository.getInstance(application).getAllPagedList()
    }
}