package com.lyloou.flow.ui.kalendar

import android.app.Application
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
                    flow.value = it.data?.toDbFlow() ?: DbFlow(0, -1, day, "[]", "", "")

                    detail.value = StringBuilder().apply {
                        val value = flow.value
                        if (value != null) {
                            append("【${value.day}】")
                            append("\n天气:\t${value.weather.replace("高温", "").replace("低温", "")}")
                            append("\n备忘:\t${value.memo}")
                            append("\n\n")
                            append(FlowItemHelper.toPrettyText(value.items))
                        }
                    }.toString()
                }
            }
    }

}