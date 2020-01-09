package com.lyloou.flow.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.work.*
import kotlinx.coroutines.coroutineScope

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

    fun updateDbFlowItems(day: String, items: String) {
        val workRequest = OneTimeWorkRequestBuilder<UpdateFlowItemsWork>().setInputData(
            Data.Builder()
                .putString(Keys.DAY, day)
                .putString(Keys.ITEMS, items)
                .build()
        ).build()
        WorkManager.getInstance(context).enqueue(workRequest)
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
                .setEnablePlaceholders(true)
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

    object Keys {
        const val DAY = "day"
        const val ITEMS = "ITEMS"
    }

    class UpdateFlowItemsWork(
        private val context: Context,
        private val workerParameters: WorkerParameters
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result = coroutineScope {
            val day = workerParameters.inputData.getString(Keys.DAY)
            val items = workerParameters.inputData.getString(Keys.ITEMS)
            val flowDao = FlowDatabase.getInstance(context).flowDao()
            if (flowDao.updateDbFlowDayWithItems(day!!, items!!) >= 0) {
                Result.success()
            } else {
                Result.failure()
            }
        }
    }
}

