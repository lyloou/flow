package com.lyloou.flow.model

import com.lyloou.flow.repository.DbFlow

data class CommonResult(var err_code: Int, var err_msg: String, var data: Any?)

data class FlowResult(var err_code: Int, var err_msg: String, var data: FlowRep?)
data class FlowListResult(var err_code: Int, var err_msg: String, var data: List<FlowRep>?)

data class FlowReq(
    val day: String,
    val item: String? = null,
    val isArchived: Boolean = false,
    val isDisabled: Boolean = false
)

data class FlowRep(
    val day: String,
    val item: String? = null,
    val isArchived: Boolean = false,
    val isDisabled: Boolean = false
)

data class Flow(
    var day: String,
    val items: MutableList<FlowItem>,
    val isArchived: Boolean = false,
    val isDisabled: Boolean = false
)

fun FlowRep.toFlow(): Flow {
    return Flow(
        day,
        FlowItemHelper.fromJsonArray(item),
        isArchived,
        isDisabled
    )
}

fun FlowRep.toDbFlow(): DbFlow {
    return DbFlow(
        0,
        day,
        item ?: "[]",
        isArchived,
        isDisabled,
        true
    )
}