package com.lyloou.flow.module.dblist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.lyloou.flow.repository.DbFlowDay
import com.lyloou.flow.repository.FlowDatabase

class DbflowViewModel(application: Application) : AndroidViewModel(application) {
    private val flowDao = FlowDatabase.getInstance(application).flowDao()
    val dbFlowDays: LiveData<PagedList<DbFlowDay>> by lazy {
        LivePagedListBuilder(
            flowDao.getAllDbFlowDays(),
            PagedList.Config.Builder()
                .setPageSize(5)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .build()
        ).build()
    }

    fun getDbFlowDay(day: String): LiveData<DbFlowDay> {
        Thread {
            val day1 = flowDao.getDbFlowDayNormal(day)
            Log.i("TTAG", "day1=$day1")
        }.start()

        return flowDao.getDbFlowDay(day)
    }
}