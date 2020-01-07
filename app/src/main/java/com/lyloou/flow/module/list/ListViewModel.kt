package com.lyloou.flow.module.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.common.Consumer
import com.lyloou.flow.common.Url
import com.lyloou.flow.model.FlowDay
import com.lyloou.flow.model.FlowListResult
import com.lyloou.flow.model.toFlowDay
import com.lyloou.flow.net.FlowApi
import com.lyloou.flow.net.Network
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val _flowDayList: MutableLiveData<List<FlowDay>> by lazy {
        MutableLiveData<List<FlowDay>>()
    }

    val page: MutableLiveData<Int> by lazy {
        MutableLiveData(0)
    }
    val currentPage: MutableLiveData<Int> by lazy {
        MutableLiveData(1)
    }

    val isNoData: MutableLiveData<Boolean> = MutableLiveData(false)

    val flowDayList: LiveData<List<FlowDay>>
        get() = _flowDayList


    fun loadFromNet(clear: Boolean, limit: Int, offset: Int, consumer: Consumer<Int>? = null) {
        Network.get(Url.FlowApi.url, FlowApi::class.java)
            .list(limit, offset)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fun(it: FlowListResult) {
                if (it.err_code == 0) {
                    Log.e("TTAG", "data:${it.data}")
                    val originList: List<FlowDay>
                    if (clear) {
                        originList = mutableListOf()
                        page.value = 0
                    } else {
                        originList = _flowDayList.value ?: mutableListOf()
                    }
                    _flowDayList.value = originList.plus(
                        it.data?.map { it.toFlowDay() }.orEmpty()
                    )
                    consumer?.accept(it.data?.size ?: 0)
                }
            }, { throwable ->
                Log.e("TTAG", "error occured.", throwable)
                consumer?.accept(0)
            })
    }
}