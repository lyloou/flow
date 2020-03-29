package com.lyloou.flow.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lyloou.flow.common.Consumer
import com.lyloou.flow.model.SyncStatus
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.defaultSubscribe
import com.lyloou.flow.net.scheduleApi
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.ScheduleRepository
import com.lyloou.flow.util.Utime

class ScheduleListViewModel(application: Application) : AndroidViewModel(application) {

    val repository: ScheduleRepository by lazy {
        ScheduleRepository.getInstance(application)
    }

    val enabledScheduleList: LiveData<PagedList<DbSchedule>> by lazy {
        repository.getEnabledPagedList()
    }

    fun deleteSchedule(vararg dbSchedules: DbSchedule) {
        repository.deleteDbSchedule(*dbSchedules)
    }

    fun getAllSchedule(
        adapterMap: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<Map<SyncStatus, Int>>
    ) {
        repository.getAllDbScheduleList(Consumer {
            val mapBadge: MutableMap<SyncStatus, Int> = mutableMapOf()
            for (status in SyncStatus.values()) {
                it[status]?.let { list ->
                    adapterMap[status]?.let { adapter ->
                        adapter.addData(list)
                        mapBadge[status] = list.size
                    }
                }
            }
            consumer.accept(mapBadge)
        })
    }

    fun updateSchedule(vararg dbSchedules: DbSchedule) {
        for (schedule in dbSchedules) {
            schedule.syncTime = Utime.now()
        }
        repository.updateDbSchedule(*dbSchedules)
    }

    fun cleanSchedules(vararg dbSchedules: DbSchedule) {
        val canBeClean = mutableListOf<DbSchedule>()
        for (schedule in dbSchedules) {
            // 若是删除，且远程没有此项，则直接删除了
            if (schedule.rsyncTime == 0L && schedule.isDisabled) {
                canBeClean.add(schedule)
                continue
            }
        }

        deleteSchedule(*canBeClean.toTypedArray())
    }


    fun doLocalAdd(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<String> = Consumer {}
    ) {
        val adapter = map[SyncStatus.LOCAL_ADD]
        val list = adapter?.getData()
        if (list.isNullOrEmpty()) {
            return
        }
        Network.scheduleApi()
            .batchSync(list)
            .defaultSubscribe {
                if (it.isSuccess()) {
                    // 同步时间
                    list.forEach { data ->
                        data.rsyncTime = data.syncTime
                    }
                    repository.updateDbSchedule(*list.toTypedArray())
                    adapter.clear()
                    consumer.accept("Done")
                } else {
                    Log.i("TTAG", "failed: $it");
                    consumer.accept("Failed")
                }
            }


    }

    fun doRemoteAdd(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<String> = Consumer {}
    ) {
        val adapter = map[SyncStatus.REMOTE_ADD]
        val list = adapter?.getData()
        if (list.isNullOrEmpty()) {
            return
        }
        // 同步时间
        list.forEach {
            it.syncTime = it.rsyncTime
        }

        repository.insertDbSchedule(*list.toTypedArray())
        adapter.clear()
        consumer.accept("Done")
    }

    fun doLocalChange(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<String> = Consumer {}
    ) {

    }

    fun doRemoteChange(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<String> = Consumer {}
    ) {

    }

    fun doLocalDelete(
        adapterMap: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<String> = Consumer {}
    ) {

    }
}