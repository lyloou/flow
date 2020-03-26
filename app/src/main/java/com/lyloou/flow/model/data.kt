package com.lyloou.flow.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.util.Utime
import kotlinx.android.parcel.Parcelize

data class FlowReq(
    val userId: Long,
    val day: String,
    val item: String? = null,
    val weather: String? = null,
    val memo: String? = null,
    val isArchived: Boolean = false,
    val isDisabled: Boolean = false
)

data class FlowRep(
    val userId: Long,
    val day: String,
    val item: String? = null,
    val weather: String? = null,
    val memo: String? = null,
    @SerializedName("is_archived")
    val isArchived: Boolean = false,
    @SerializedName("is_disabled")
    val isDisabled: Boolean = false
)

@Parcelize
data class FlowItem(
    var timeStart: String? = null,
    var timeEnd: String? = null,
    var timeSep: String = "~",
    var content: String? = null,
    var spend: String = Utime.getInterval(timeStart, timeEnd) ?: "--:--"
) : Parcelable


fun FlowRep.toDbFlow(): DbFlow {
    return DbFlow(
        0,
        userId,
        day,
        item ?: "[]",
        weather ?: "",
        memo ?: "",
        isArchived,
        true,
        isDisabled
    )
}


