package com.lyloou.flow.module.dblist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.repository.DbFlowDay
import com.lyloou.flow.repository.FlowRepository


class DbflowViewModel(application: Application) : AndroidViewModel(application) {
    private val flowRepository = FlowRepository.getInstance(application)
    val dbFlowDayList: LiveData<PagedList<DbFlowDay>> by lazy {
        flowRepository.getPagedList()
    }

    fun getDbFlowDay(day: String): LiveData<DbFlowDay> {
        return flowRepository.getDbFlowDay(day)
    }

    fun insertDbFlowDay(day: String) {
        val init = DbFlowDay(0, day, "[]")
        flowRepository.insertDbFlowDay(init)
    }

    fun updateDbFlowItems(day: String, itemList: List<FlowItem>) {
        val items = FlowItemHelper.toJsonArray(itemList)
        flowRepository.updateDbFlowItems(day, items)
    }
}