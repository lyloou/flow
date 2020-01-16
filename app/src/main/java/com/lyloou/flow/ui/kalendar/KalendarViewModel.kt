package com.lyloou.flow.ui.kalendar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.Flow
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.model.FlowResult
import com.lyloou.flow.model.toFlow
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.flowApi
import com.lyloou.flow.util.Utime
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class KalendarViewModel(application: Application) : AndroidViewModel(application) {
    val flow: MutableLiveData<Flow> by lazy {
        MutableLiveData<Flow>().also {
            loadFromNet(Utime.getDayWithFormatTwo())
        }
    }

    val detail: MutableLiveData<String> = MutableLiveData()

    fun loadFromNet(day: String) {
        Network.flowApi()
            .get(day)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fun(it: FlowResult) {
                if (it.err_code == 0) {
                    flow.value = it.data?.toFlow()
                    flow.value = flow.value ?: Flow(
                        day,
                        arrayListOf()
                    )
                    Log.e("TTAG", "flow.value=${flow.value}")

                    detail.value = StringBuilder().apply {
                        val value = flow.value
                        if (value != null) {
                            append("【${value.day}】")
                            append("\n(\tA: ${value.isArchived}")
                            append("\t\tD: ${value.isDisabled}")
                            append(")\n\n")
                            append(
                                FlowItemHelper.toPrettyText(
                                    value.items
                                )
                            )
                        }
                    }.toString()
                    Log.e("TTAG", "error_code=${it.err_code}")

                }
            }, { throwable ->
                Log.e("TTAG", "error occured.", throwable)
            })
    }

}