package com.lyloou.flow.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.lyloou.flow.common.enqueueWork
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
        val data = Data.Builder()
            .putString(Keys.DAY, day)
            .putString(Keys.ITEMS, items)
            .build()
        enqueueWork<UpdateFlowItemsWork>(context, data)
    }

    fun updateDbFlowsWeather(day: String, weather: String) {
        val data = Data.Builder()
            .putString(Keys.DAY, day)
            .putString(Keys.WEATHER, weather)
            .build()
        enqueueWork<UpdateDbFlowWeatherWork>(context, data)
    }

    fun updateDbFlowsMemo(day: String, memo: String) {
        val data = Data.Builder()
            .putString(Keys.DAY, day)
            .putString(Keys.MEMO, memo)
            .build()
        enqueueWork<UpdateDbFlowMemoWork>(context, data)
    }

    fun updateDbFlowsSyncStatus(days: Array<String>, status: Boolean) {
        val data = Data.Builder()
            .putStringArray(Keys.DAYS, days)
            .putBoolean(Keys.STATUS, status)
            .build()
        enqueueWork<UpdateDbFlowSyncStatusWork>(context, data)
    }

    fun updateDbFlowsArchivedStatus(days: Array<String>, status: Boolean) {
        val data = Data.Builder()
            .putStringArray(Keys.DAYS, days)
            .putBoolean(Keys.STATUS, status)
            .build()
        enqueueWork<UpdateDbFlowArchivedStatusWork>(context, data)
    }

    fun insertDbFlow(vararg dbFlows: DbFlow) {
        InsertAsyncTask(flowDao).execute(*dbFlows)
    }

    fun deleteDbFlow(vararg dbFlows: DbFlow) {
        DeleteAsyncTask(flowDao).execute(*dbFlows)
    }

    fun getDbFlow(day: String): LiveData<DbFlow> {
        return flowDao.getDbFlow(day)
    }

    fun getDbFlowsBySyncStatus(status: Boolean): LiveData<MutableList<DbFlow>> {
        return flowDao.getAllDbFlowBySyncStatus(status)
    }

    fun getActivePagedList(): LiveData<PagedList<DbFlow>> {
        return LivePagedListBuilder(
            flowDao.getActiveDbFlows(),
            PagedList.Config.Builder()
                .setPageSize(5)
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .build()
        ).build()
    }

    fun getArchivedPagedList(): LiveData<PagedList<DbFlow>> {
        return LivePagedListBuilder(
            flowDao.getArchivedDbFlows(),
            PagedList.Config.Builder()
                .setPageSize(5)
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .build()
        ).build()
    }

    class InsertAsyncTask(private val flowDao: FlowDao) : AsyncTask<DbFlow, Unit, Unit>() {

        override fun doInBackground(vararg flows: DbFlow) {
            if (flows.size < 0) {
                return
            }
            flowDao.insertDbFlows(*flows)
        }

    }

    class DeleteAsyncTask(private val flowDao: FlowDao) : AsyncTask<DbFlow, Unit, Unit>() {

        override fun doInBackground(vararg flows: DbFlow) {
            if (flows.size < 0) {
                return
            }
            flowDao.deleteDbFlows(*flows)
        }

    }

    object Keys {
        const val DAY = "DAY"
        const val ITEMS = "ITEMS"
        const val WEATHER = "WEATHER"
        const val MEMO = "MEMO"

        const val DAYS = "DAYS"
        const val STATUS = "STATUS"
    }

    class UpdateFlowItemsWork(
        private val context: Context,
        private val workerParameters: WorkerParameters
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result = coroutineScope {
            val day = workerParameters.inputData.getString(Keys.DAY)
            val items = workerParameters.inputData.getString(Keys.ITEMS)
            val flowDao = FlowDatabase.getInstance(context).flowDao()
            if (flowDao.updateDbFlowItems(day!!, items!!) >= 0) {
                Result.success()
            } else {
                Result.failure()
            }
        }

    }


    class UpdateDbFlowSyncStatusWork(
        private val context: Context,
        private val workerParameters: WorkerParameters
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result = coroutineScope {
            val days = workerParameters.inputData.getStringArray(Keys.DAYS)
            if (days == null || days.isEmpty()) {
                Result.failure()
            }
            val status = workerParameters.inputData.getBoolean(Keys.STATUS, false)
            val flowDao = FlowDatabase.getInstance(context).flowDao()
            val nums = flowDao.updateDbFlowSyncStatus(days!!.toList(), status)
            if (nums >= 0) {
                Result.success()
            } else {
                Result.failure()
            }
        }
    }

    class UpdateDbFlowArchivedStatusWork(
        private val context: Context,
        private val workerParameters: WorkerParameters
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result = coroutineScope {
            val days = workerParameters.inputData.getStringArray(Keys.DAYS)
            if (days == null || days.isEmpty()) {
                Result.failure()
            }
            val status = workerParameters.inputData.getBoolean(Keys.STATUS, false)
            val flowDao = FlowDatabase.getInstance(context).flowDao()
            val nums = flowDao.updateDbFlowArchivedStatus(days!!.toList(), status)
            if (nums >= 0) {
                Result.success()
            } else {
                Result.failure()
            }
        }
    }

    class UpdateDbFlowWeatherWork(
        private val context: Context,
        private val workerParameters: WorkerParameters
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result = coroutineScope {
            val day = workerParameters.inputData.getString(Keys.DAY)
            val weather = workerParameters.inputData.getString(Keys.WEATHER)

            val flowDao = FlowDatabase.getInstance(context).flowDao()
            val nums = flowDao.updateDbFlowWeather(day!!, weather ?: "")
            if (nums >= 0) {
                Result.success()
            } else {
                Result.failure()
            }
        }
    }

    class UpdateDbFlowMemoWork(
        private val context: Context,
        private val workerParameters: WorkerParameters
    ) : CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result = coroutineScope {
            val day = workerParameters.inputData.getString(Keys.DAY)
            val memo = workerParameters.inputData.getString(Keys.MEMO)

            val flowDao = FlowDatabase.getInstance(context).flowDao()
            val nums = flowDao.updateDbFlowMemo(day!!, memo ?: "")
            if (nums >= 0) {
                Result.success()
            } else {
                Result.failure()
            }
        }
    }
}

