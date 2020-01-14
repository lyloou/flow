package com.lyloou.flow.module.dblist

import com.lyloou.flow.model.FlowItem

interface OnItemListener {
    fun onLongClickItem(item: FlowItem, position: Int)
    fun onClickTimeStart(item: FlowItem, position: Int)
    fun onClickTimeEnd(item: FlowItem, position: Int)
    fun onLongClickTimeStart(item: FlowItem, position: Int)
    fun onLongClickTimeEnd(item: FlowItem, position: Int)
    fun onTextChanged(item: FlowItem, s: CharSequence, position: Int)
    fun onEditTextFocused(hasFocus: Boolean, item: FlowItem, position: Int)
}