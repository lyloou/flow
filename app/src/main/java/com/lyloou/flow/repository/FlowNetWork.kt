package com.lyloou.flow.repository

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lyloou.flow.model.toDbFlow
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.flowApi
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.CountDownLatch

class FlowNetWork(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun doWork(): Result =
        coroutineScope {
            val latch = CountDownLatch(1)
            var isOk = false
            Network
                .flowApi()
                .list(9999, 0)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({
                    if (it.err_code == 0) {
                        isOk = true
                        Log.i("TTAG", "get data:${it.data}")

                        val database = FlowDatabase.getInstance(applicationContext)
                        val flowDao = database.flowDao()

                        val listOrigin = (it.data?.map { data -> data.toDbFlow() } ?: emptyList())
                        val listLocal = flowDao.getAllDbFlow()

                        val newList = getNewList(listLocal.toList(), listOrigin)

                        flowDao.insertDbFlows(*newList.toTypedArray())
                        latch.countDown()
                    }
                }, { throwable ->
                    Log.e("TTAG", "error occur:", throwable)
                    latch.countDown()
                })
            latch.await()
            if (isOk) {
                Result.success()
            } else {
                Result.failure()
            }
        }

    /**
     *先简单处理，本地有的以本地为主，本地没有的，就插入进来
     */
    private fun getNewList(listLocal: List<DbFlow>, listOrigin: List<DbFlow>): List<DbFlow> {
        val conflictList = listOrigin.subtract(listLocal)
        return conflictList.toList()
    }

}