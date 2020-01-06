package com.lyloou.flow.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.common.Url
import com.lyloou.flow.net.FlowApi
import com.lyloou.flow.net.Network
import com.lyloou.flow.util.Utime
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyViewModel(application: Application) : AndroidViewModel(application) {
    val flowDay: MutableLiveData<FlowDay> by lazy {
        MutableLiveData<FlowDay>().also {
            it.value = FlowDay(Utime.getDayWithFormatTwo(), arrayListOf())
        }
    }

    val detail: MutableLiveData<String> = MutableLiveData()

    fun loadFromNet(day: String) {
        Network.get(Url.FlowApi.url, FlowApi::class.java)
            .get(day)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fun(it: FlowResult) {
                if (it.err_code == 0) {
                    flowDay.value = it.data?.toFlowDay()
                    flowDay.value = flowDay.value ?: FlowDay(day, arrayListOf())
                    Log.e("TTAG", "flowDay.value=${flowDay.value}")

                    detail.value = StringBuilder().apply {
                        val value = flowDay.value
                        if (value != null) {
                            append("【${value.day}】")
                            append("\n(\tA: ${value.isArchived}")
                            append("\t\tD: ${value.isDisabled}")
                            append(")\n\n")
                            append(FlowItemHelper.toPrettyText(value.items))
                        }
                    }.toString()
                    Log.e("TTAG", "error_code=${it.err_code}")

                }
            }, { throwable ->
                Log.e("TTAG", "error occured.", throwable)
            })
    }

}