package com.lyloou.flow.module.dblist

import com.lyloou.flow.model.FlowItem

interface OnItemListener {
    fun onLongClickItem(item: FlowItem)
    fun onClickTimeStart(item: FlowItem)
    fun onClickTimeEnd(item: FlowItem)
    fun onLongClickTimeStart(item: FlowItem)
    fun onLongClickTimeEnd(item: FlowItem)
    fun onTextChanged(item: FlowItem, s: CharSequence)
    fun onEditTextFocused(hasFocus: Boolean, item: FlowItem)
}