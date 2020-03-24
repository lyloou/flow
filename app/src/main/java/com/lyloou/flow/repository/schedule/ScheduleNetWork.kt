package com.lyloou.flow.repository.schedule

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.scheduleApi
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.CountDownLatch

class ScheduleNetWork(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun doWork(): Result =
        coroutineScope {
            val latch = CountDownLatch(1)
            var isOk = false
            Network
                .scheduleApi()
                .list(9999, 0)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({
                    if (it.err_code == 0) {
                        isOk = true
                        Log.i("TTAG", "get data111:${it.data}")

                        val database = ScheduleDatabase.getInstance(applicationContext)
                        val scheduleDao = database.scheduleDao()
                        val listOrigin = (it.data ?: emptyList())

                        database.clearAllTables()
                        scheduleDao.insertDbSchedule(*listOrigin.toTypedArray())
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
}