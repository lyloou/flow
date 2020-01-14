package com.lyloou.flow.repository

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lyloou.flow.common.Url
import com.lyloou.flow.model.toDbFlow
import com.lyloou.flow.net.FlowApi
import com.lyloou.flow.net.Network
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.CountDownLatch

class FlowNetWork(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result =
        coroutineScope {
            val latch = CountDownLatch(1)
            var isOk = false
            Network.get(Url.FlowApi.url, FlowApi::class.java)
                .list(9999, 0)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({
                    if (it.err_code == 0) {
                        isOk = true
                        Log.e("TTAG", "error occured.${it.data}")

                        val database = FlowDatabase.getInstance(applicationContext)
                        val flowDao = database.flowDao()
                        flowDao.insertDbFlows(
                            *(it.data?.map { it.toDbFlow() } ?: emptyList()).toTypedArray()
                        )
                        latch.countDown()
                    }
                }, { throwable ->
                    Log.e("TTAG", "error occured.", throwable)
                    latch.countDown()
                })
            if (isOk) {
                Result.success()
            } else {
                Result.failure()

            }
        }
}