package com.lyloou.flow.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lyloou.flow.model.Daily
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.flowApi
import com.lyloou.flow.net.kingSoftwareApi
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.repository.FlowRepository
import com.lyloou.flow.repository.toFlowRq
import com.lyloou.flow.util.Ulist
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val flowRepository = FlowRepository.getInstance(application)
    val activeDbFlowList: LiveData<PagedList<DbFlow>> by lazy {
        flowRepository.getActivePagedList()
    }
    val archivedDbFlowList: LiveData<PagedList<DbFlow>> by lazy {
        flowRepository.getArchivedPagedList()
    }

    fun getDbFlow(day: String): LiveData<DbFlow> {
        return flowRepository.getDbFlow(day)
    }

    fun insertDbFlow(dbFlow: DbFlow) {
        flowRepository.insertDbFlow(dbFlow)
    }

    fun updateDbFlowItems(day: String, itemList: List<FlowItem>) {
        val items = FlowItemHelper.toJsonArray(itemList)
        flowRepository.updateDbFlowItems(day, items)
        flowRepository.updateDbFlowsSyncStatus(arrayOf(day), false)
    }

    fun syncFlows(data: MutableList<DbFlow>, syncListener: SyncListener) {
        val maxSyncNumber = 5
        val partition = Ulist.partition(data, maxSyncNumber)
        var successNum = 0;
        var failNum = 0;
        var failMemo = ""
        for (list in partition) {
            val flowReqs = list.map { it.toFlowRq() }.toList()
            Network.flowApi()
                .batchSync(flowReqs)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rep ->
                    if (rep.err_code == 0) {
                        val days = flowReqs.map { it.day }.toTypedArray()
                        flowRepository.updateDbFlowsSyncStatus(days, true)
                        successNum += flowReqs.size
                    } else {
                        failNum += flowReqs.size
                        failMemo = "错误码:${rep.err_code}"
                    }
                    syncListener.progress(data.size, successNum, failNum)

                    if ((successNum + failNum) == data.size) {
                        syncListener.handle(SyncResult(data.size, successNum, failNum, failMemo))
                    }
                }, { t ->
                    failMemo = "错误信息：${t.message}"
                    failNum += flowReqs.size
                    syncListener.progress(data.size, successNum, failNum)

                    if ((successNum + failNum) == data.size) {
                        syncListener.handle(SyncResult(data.size, successNum, failNum, failMemo))
                    }
                })
        }
    }

    fun getDbFlowsBySyncStatus(status: Boolean): LiveData<MutableList<DbFlow>> {
        return flowRepository.getDbFlowsBySyncStatus(status)
    }

    fun updateByKingSoftware(day: String, doFunc: (Daily) -> Unit) {
        Network.kingSoftwareApi()
            .getDaily(day)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(doFunc, Throwable::printStackTrace)
    }
}