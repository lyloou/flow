package com.lyloou.flow.module.dblist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.repository.DbFlowDay
import com.lyloou.flow.repository.FlowRepository
import com.lyloou.flow.util.Utime

class DbflowViewModel(application: Application) : AndroidViewModel(application) {
    private val flowRepository = FlowRepository.getInstance(application)
    val dbFlowDays: LiveData<PagedList<DbFlowDay>> by lazy {
        flowRepository.getPagedList()
    }

    var flowItems: MutableLiveData<List<FlowItem>> = MutableLiveData()

    var day: MutableLiveData<String> = MutableLiveData()

    val dbFlowDay: LiveData<DbFlowDay> by lazy {
        flowRepository.getDbFlowDay(day.value ?: Utime.today())
    }

    fun updateDbFlowDay() {

    }

    fun insertDbFlowDay() {
        val tmp = DbFlowDay(0, day.value ?: Utime.today(), "[]")
        val dbFlowDay = flowRepository.insertDbFlowDay(tmp)
        Log.i("TTAG", "dbFlowDay=$dbFlowDay")
    }
}