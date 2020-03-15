package com.lyloou.flow.ui.list

interface SyncListener {
    fun handle(result: SyncResult)
    fun progress(all: Int, successNum: Int, failedNum: Int)
}

data class SyncResult(
    val all: Int,
    val successNum: Int,
    val failNum: Int,
    val failMemo: String
)