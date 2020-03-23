package com.lyloou.flow.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.ScheduleRepository

class TodoListViewModel(application: Application) : AndroidViewModel(application) {

    val data: MutableLiveData<List<Schedule>> by lazy {
        MutableLiveData<List<Schedule>>()
    }

    val dbScheduleList: LiveData<PagedList<DbSchedule>> by lazy {
        ScheduleRepository.getInstance(application).getAllPagedList()
    }
}