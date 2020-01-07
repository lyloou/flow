package com.lyloou.flow.model

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

data class FlowDay(
    var day: String,
    val items: MutableList<FlowItem>,
    val isArchived: Boolean = false,
    val isDisabled: Boolean = false
)

fun FlowRep.toFlowDay(): FlowDay {
    return FlowDay(
        day,
        FlowItemHelper.fromJsonArray(item),
        isArchived,
        isDisabled
    )
}