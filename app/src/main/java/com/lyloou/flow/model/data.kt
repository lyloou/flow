package com.lyloou.flow.model

import android.os.Parcelable
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.util.Utime
import kotlinx.android.parcel.Parcelize

data class CommonResult(var err_code: Int, var err_msg: String, var data: Any?)

data class FlowResult(var err_code: Int, var err_msg: String, var data: FlowRep?)
data class FlowListResult(var err_code: Int, var err_msg: String, var data: List<FlowRep>?)

data class FlowReq(
    val userId: Long,
    val day: String,
    val item: String? = null,
    val isArchived: Boolean = false,
    val isDisabled: Boolean = false
)

data class FlowRep(
    val userId: Long,
    val day: String,
    val item: String? = null,
    val isArchived: Boolean = false,
    val isDisabled: Boolean = false
)

@Parcelize
data class Flow(
    val userId: Long,
    var day: String,
    val items: MutableList<FlowItem> = mutableListOf(),
    val isArchived: Boolean = false,
    val isDisabled: Boolean = false
) : Parcelable

@Parcelize
data class FlowItem(
    var timeStart: String? = null,
    var timeEnd: String? = null,
    var timeSep: String = "~",
    var content: String? = null,
    var spend: String = Utime.getInterval(timeStart, timeEnd) ?: "--:--"
) : Parcelable

fun FlowRep.toFlow(): Flow {
    return Flow(
        userId,
        day,
        FlowItemHelper.fromJsonArray(item),
        isArchived,
        isDisabled
    )
}

fun FlowRep.toDbFlow(): DbFlow {
    return DbFlow(
        0,
        userId,
        day,
        item ?: "[]",
        isArchived,
        isDisabled,
        true
    )
}


fun Flow.toDbFlow(): DbFlow {
    return DbFlow(
        0,
        userId,
        day,
        FlowItemHelper.toJsonArray(items),
        isArchived,
        isDisabled,
        true
    )
}

