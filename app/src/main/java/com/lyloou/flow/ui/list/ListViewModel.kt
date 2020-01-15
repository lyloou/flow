package com.lyloou.flow.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.repository.FlowRepository

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val flowRepository = FlowRepository.getInstance(application)
    val dbFlowList: LiveData<PagedList<DbFlow>> by lazy {
        flowRepository.getPagedList()
    }

    fun getDbFlow(day: String): LiveData<DbFlow> {
        return flowRepository.getDbFlow(day)
    }

    fun insertDbFlow(dbFlow: DbFlow) {
        flowRepository.insertDbFlow(dbFlow)
    }

    fun updateDbFlowItems(day: String, itemList: List<FlowItem>) {
        val items = FlowItemHelper.toJsonArray(itemList)
        flowRepository.updateDbFlowItems(day, items)
    }
}