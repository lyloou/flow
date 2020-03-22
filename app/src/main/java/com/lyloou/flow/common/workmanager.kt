package com.lyloou.flow.common

import android.content.Context
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

inline fun <reified T : ListenableWorker> enqueueWork(
    context: Context,
    data: Data
) {
    val workRequest =
        OneTimeWorkRequestBuilder<T>()
            .setInputData(data)
            .build()
    WorkManager.getInstance(context).enqueue(workRequest)
}
