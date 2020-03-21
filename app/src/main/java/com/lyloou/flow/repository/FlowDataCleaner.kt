package com.lyloou.flow.repository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.lyloou.flow.App
import kotlinx.coroutines.coroutineScope

class FlowDataCleaner(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun doWork(): Result =
        coroutineScope {
            val database = FlowDatabase.getInstance(applicationContext)
            database.clearAllTables()
            Result.success()
        }

    companion object {
        fun execute() {
            val workRequest = OneTimeWorkRequestBuilder<FlowDataCleaner>().build()
            WorkManager.getInstance(App.instance).enqueue(workRequest)
        }
    }
}

