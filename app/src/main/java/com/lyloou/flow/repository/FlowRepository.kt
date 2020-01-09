package com.lyloou.flow.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

class FlowRepository(private val context: Context) {
    private val flowDao: FlowDao

    init {
        val database = FlowDatabase.getInstance(context)
        flowDao = database.flowDao()
    }

    companion object {
        @Volatile
        private var instance: FlowRepository? = null

        fun getInstance(context: Context): FlowRepository {
            return instance ?: synchronized(this) {
                instance ?: FlowRepository(context).also {
                    instance = it
                }
            }

        }
    }

    fun updateDbFlowDay(vararg dbFlowDays: DbFlowDay) {
        UpdateAsyncTask(flowDao).execute(*dbFlowDays)
    }

    fun insertDbFlowDay(vararg dbFlowDays: DbFlowDay) {
        InsertAsyncTask(flowDao).execute(*dbFlowDays)
    }

    fun getDbFlowDay(day: String): LiveData<DbFlowDay> {
        return flowDao.getDbFlowDay(day)
    }

    fun getPagedList(): LiveData<PagedList<DbFlowDay>> {
        return LivePagedListBuilder(
            flowDao.getAllDbFlowDays(),
            PagedList.Config.Builder()
                .setPageSize(5)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .build()
        ).build()
    }

    class InsertAsyncTask(private val flowDao: FlowDao) : AsyncTask<DbFlowDay, Unit, Unit>() {

        override fun doInBackground(vararg flowDays: DbFlowDay) {
            if (flowDays.size < 0) {
                return
            }
            flowDao.insertDbFlowDay(*flowDays)
        }

    }

    class UpdateAsyncTask(private val flowDao: FlowDao) : AsyncTask<DbFlowDay, Unit, Unit>() {

        override fun doInBackground(vararg flowDays: DbFlowDay) {
            if (flowDays.size < 0) {
                return
            }
            flowDao.updateDbFlowDay(*flowDays)
        }

    }
}