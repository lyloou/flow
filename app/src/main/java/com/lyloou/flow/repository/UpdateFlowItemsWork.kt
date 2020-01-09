package com.lyloou.flow.repository

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope

class UpdateFlowItemsWork(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result =
        coroutineScope {
            val day = workerParameters.inputData.getString("day")
            var items = workerParameters.inputData.getString("items")
            Log.i("TTAG", "day=$day: items=$items");
            if (day == null) {
                return@coroutineScope Result.failure()
            }
            if (items == null) {
                items = "[]"
            }
            FlowDatabase.getInstance(context).flowDao().updateDbFlowDayWithItems(day, items)
            Result.success()
        }
}