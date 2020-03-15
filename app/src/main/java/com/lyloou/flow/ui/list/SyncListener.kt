package com.lyloou.flow.ui.list

interface SyncListener {
    fun handle(result: SyncResult)
    fun progress(successNum: Int, failedNum: Int, all: Int)
}

data class SyncResult(
    val all: Int,
    val successNum: Int,
    val failNum: Int,
    val failMemo: String
)