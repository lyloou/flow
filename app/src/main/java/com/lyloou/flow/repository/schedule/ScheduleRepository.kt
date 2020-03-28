package com.lyloou.flow.repository.schedule

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.lyloou.flow.common.Consumer
import com.lyloou.flow.model.SyncStatus
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.scheduleApi
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.CountDownLatch

class ScheduleRepository(private val context: Context) {
    private val scheduleDao: ScheduleDao

    init {
        val database = ScheduleDatabase.getInstance(context)
        scheduleDao = database.scheduleDao()
    }

    companion object {
        @Volatile
        private var instance: ScheduleRepository? = null

        fun getInstance(context: Context): ScheduleRepository {
            return instance ?: synchronized(this) {
                instance ?: ScheduleRepository(context).also {
                    instance = it
                }
            }

        }
    }

    fun insertDbSchedule(vararg dbSchedules: DbSchedule) {
        InsertAsyncTask(scheduleDao).execute(*dbSchedules)
    }

    fun deleteDbSchedule(vararg dbSchedules: DbSchedule) {
        DeleteAsyncTask(scheduleDao).execute(*dbSchedules)
    }

    fun updateDbSchedule(vararg dbSchedules: DbSchedule) {
        UpdateAsyncTask(scheduleDao).execute(*dbSchedules)
    }

    class InsertAsyncTask(private val scheduleDao: ScheduleDao) :
        AsyncTask<DbSchedule, Unit, Unit>() {

        override fun doInBackground(vararg schedules: DbSchedule) {
            if (schedules.size < 0) {
                return
            }
            scheduleDao.insertDbSchedule(*schedules)
        }

    }

    class DeleteAsyncTask(private val scheduleDao: ScheduleDao) :
        AsyncTask<DbSchedule, Unit, Unit>() {

        override fun doInBackground(vararg schedules: DbSchedule) {
            if (schedules.size < 0) {
                return
            }
            scheduleDao.deleteDbSchedule(*schedules)
        }
    }

    class UpdateAsyncTask(private val scheduleDao: ScheduleDao) :
        AsyncTask<DbSchedule, Unit, Unit>() {

        override fun doInBackground(vararg schedules: DbSchedule) {
            if (schedules.size < 0) {
                return
            }
            scheduleDao.updateDbSchedule(*schedules)
        }
    }

    class GetAsyncTask(
        private val scheduleDao: ScheduleDao,
        private val consumer: Consumer<Map<SyncStatus, List<DbSchedule>>>
    ) :
        AsyncTask<Unit, Unit, Map<SyncStatus, List<DbSchedule>>>() {
        override fun doInBackground(vararg p0: Unit?): Map<SyncStatus, List<DbSchedule>> {
            val map: MutableMap<SyncStatus, MutableList<DbSchedule>> = mutableMapOf()

            val latch = CountDownLatch(1)
            val localList = scheduleDao.getAllDbSchedule().toList()
            val remoteList = mutableListOf<DbSchedule>()
            Network.scheduleApi()
                .list(9999, 0)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.i("TTAG", "=====>: 0, $it");
                    if (it.err_code == 0) {
                        it.data?.let { schedules ->
                            remoteList.addAll(schedules)
                        }
                    }
                    latch.countDown()
                }, {
                    Log.i("TTAG", "=====>: 1");
                    latch.countDown()
                })
            latch.await()
            Log.i("TTAG", "=====>: 2");
            splitList(map, localList, remoteList)
            return map
        }

        private fun splitList(
            map: MutableMap<SyncStatus, MutableList<DbSchedule>>,
            localList: List<DbSchedule>,
            remoteList: MutableList<DbSchedule>
        ) {
            initMap(map)
            val remoteMap = remoteList.groupBy { dbSchedule: DbSchedule -> dbSchedule.uuid }
            for (local in localList) {
                if (local.uuid == "F0F65E42C121454C8F8782CEE384BD26") {
                    local.rsyncTime = 1285356685416
                    local.title = "12342412412"
                }
                // 1. 无变化的
                if (local.syncTime == local.rsyncTime) {
                    continue
                }

                // 2. 本地新增的
                if (local.rsyncTime == 0L) {
                    map[SyncStatus.LOCAL_ADD]!!.add(local)
                    continue
                }
                val remote = remoteMap[local.uuid]?.firstOrNull()
                if (remote != null) {
                    // 4. 本地修改的
                    if ((local.rsyncTime == remote.syncTime) && (local.syncTime > local.rsyncTime)) {
                        map[SyncStatus.LOCAL_CHANGE]!!.add(local)
                        continue
                    }

                    // 5. 远程修改的
                    if ((local.syncTime > local.rsyncTime) && (remote.syncTime > local.rsyncTime)) {
                        map[SyncStatus.REMOTE_CHANGE]!!.add(remote)
                    }
                }
            }

            // 3. 远程新增的
            val remoteAdd = remoteList.subtract(localList)
            map[SyncStatus.REMOTE_ADD]!!.addAll(remoteAdd)
            Log.i("TTAG", "=====>: 3, $map");
        }

        private fun initMap(map: MutableMap<SyncStatus, MutableList<DbSchedule>>) {
            for (value in SyncStatus.values()) {
                map[value] = mutableListOf()
            }
        }

        override fun onPostExecute(result: Map<SyncStatus, List<DbSchedule>>) {
            Log.i("TTAG", "----5: ")
            consumer.accept(result)
        }
    }

    fun getDbSchedule(id: Long): LiveData<DbSchedule> {
        return scheduleDao.getDbSchedule(id)
    }


    fun getAllDbScheduleList(consumer: Consumer<Map<SyncStatus, List<DbSchedule>>>) {
        GetAsyncTask(scheduleDao, consumer).execute()
    }

    fun getEnabledPagedList(): LiveData<PagedList<DbSchedule>> {
        return LivePagedListBuilder(
            scheduleDao.getEnabledDbSchedule(),
            PagedList.Config.Builder()
                .setPageSize(5)
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .build()
        ).build()
    }
}

