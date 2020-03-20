package com.lyloou.flow.ui.kalendar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.model.toDbFlow
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.defaultSubscribe
import com.lyloou.flow.net.flowApi
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.util.Utime

class KalendarViewModel(application: Application) : AndroidViewModel(application) {
    val flow: MutableLiveData<DbFlow> by lazy {
        MutableLiveData<DbFlow>().also {
            loadFromNet(Utime.getDayWithFormatTwo())
        }
    }

    val detail: MutableLiveData<String> = MutableLiveData()

    fun loadFromNet(day: String) {
        Network.flowApi()
            .get(day)
            .defaultSubscribe {
                if (it.err_code == 0) {
                    flow.value = it.data?.toDbFlow()
                    flow.value = flow.value ?: DbFlow(
                        0,
                        -1,
                        day,
                        "[]",
                        "",
                        ""
                    )
                    Log.e("TTAG", "flow.value=${flow.value}")

                    detail.value = StringBuilder().apply {
                        val value = flow.value
                        if (value != null) {
                            append("【${value.day}】")
                            append("\n(\tA: ${value.isArchived}")
                            append("\t\tD: ${value.isDisabled}")
                            append(")\n\n")
                            append(FlowItemHelper.toPrettyText(FlowItemHelper.fromJsonArray(value.items)))
                        }
                    }.toString()
                    Log.e("TTAG", "error_code=${it.err_code}")

                }
            }
    }

}