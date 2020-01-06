package com.lyloou.flow.model

import com.lyloou.flow.util.Utime
import java.io.Serializable

class FlowItem : Serializable {
    var timeStart: String? = null
    var timeEnd: String? = null
    var timeSep = "~"
    var content: String? = null

    val spend: String
        get() = Utime.getInterval(timeStart, timeEnd) ?: "--:--"

    companion object {
        private const val serialVersionUID = 1L
    }
}