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
            schedule.localTime = Utime.now()
        }
        repository.updateDbSchedule(*dbSchedules)
    }

    fun doLocalAdd(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<SyncResult> = Consumer {}
    ) {
        val adapter = map[SyncStatus.LOCAL_ADD]
        val list = adapter?.getData()
        if (list.isNullOrEmpty()) {
            return
        }

        // 同步时间
        list.map {
            it.snapTime = it.localTime
            it.syncTime = it.localTime
        }
        Network.scheduleApi()
            .batchSync(list)
            .defaultSubscribe {
                if (it.isSuccess()) {
                    repository.updateDbSchedule(*list.toTypedArray())
                    adapter.clear()
                    consumer.accept(SyncResult(true, "Done", 0))
                } else {
                    Log.e("TTAG", "doLocalAdd failed: $it");
                    consumer.accept(SyncResult(false, "Failed", list.size))
                }
            }
    }

    fun doRemoteAdd(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<SyncResult> = Consumer {}
    ) {
        val adapter = map[SyncStatus.REMOTE_ADD]
        val list = adapter?.getData()
        if (list.isNullOrEmpty()) {
            consumer.accept(SyncResult(true, "无需同步", 0))
            return
        }

        // 同步时间
        list.map {
            it.snapTime = it.syncTime
            it.localTime = it.syncTime
        }

        repository.insertDbSchedule(*list.toTypedArray())
        adapter.clear()
        consumer.accept(SyncResult(true, "Done", 0))
    }

    fun doLocalChange(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<SyncResult> = Consumer {}
    ) {
        val adapter = map[SyncStatus.LOCAL_CHANGE]
        val list = adapter?.getData()
        if (list.isNullOrEmpty()) {
            return
        }
        Network.scheduleApi()
            .batchSync(list)
            .defaultSubscribe {
                if (it.isSuccess()) {
                    // 同步时间
                    list.map { data ->
                        data.syncTime = data.localTime
                        data.snapTime = data.localTime
                    }

                    repository.updateDbSchedule(*list.toTypedArray())
                    adapter.clear()
                    consumer.accept(SyncResult(true, "Done", 0))
                } else {
                    Log.e("TTAG", "doLocalChange failed: $it");
                    consumer.accept(SyncResult(false, "Failed", list.size))
                }
            }
    }

    fun doRemoteChange(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<SyncResult> = Consumer {}
    ) {
        val adapter = map[SyncStatus.REMOTE_CHANGE]
        val list = adapter?.getData()
        if (list.isNullOrEmpty()) {
            consumer.accept(SyncResult(true, "无需同步", 0))
            return
        }
        // 同步时间
        list.map {
            it.snapTime = it.syncTime
            it.localTime = it.syncTime
        }

        repository.updateDbSchedule(*list.toTypedArray())
        adapter.clear()
        consumer.accept(SyncResult(true, "Done", 0))
    }

    fun doLocalDelete(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<SyncResult> = Consumer {}
    ) {
        val adapter = map[SyncStatus.LOCAL_DELETE]
        val list = adapter?.getData()
        if (list.isNullOrEmpty()) {
            consumer.accept(SyncResult(true, "无需同步", 0))
            return
        }
        repository.deleteDbSchedule(*list.toTypedArray())
        adapter.clear()
        consumer.accept(SyncResult(true, "Done", 0))
    }

    fun doAllChange(
        map: MutableMap<SyncStatus, ScheduleSyncAdapter>,
        consumer: Consumer<SyncResult>
    ) {
        val adapter = map[SyncStatus.ALL_CHANGE]
        val list = adapter?.getData()
        if (list.isNullOrEmpty()) {
            consumer.accept(SyncResult(true, "无需同步", 0))
            return
        }
        consumer.accept(SyncResult(true, "冲突情况，请手动修改", list.size))

    }
}