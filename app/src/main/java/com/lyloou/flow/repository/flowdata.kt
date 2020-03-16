package com.lyloou.flow.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lyloou.flow.model.FlowReq

const val TABLE_FLOW = "flow"
const val COL_FLOW_ID = "id"
const val COL_FLOW_DAY = "day"
const val COL_FLOW_USER_ID = "user_id"
const val COL_FLOW_ITEMS = "items"
const val COL_FLOW_IS_ARCHIVED = "is_archived"
const val COL_FLOW_IS_SYNCED = "is_synced"
const val COL_FLOW_IS_DISABLED = "is_disabled"

@Entity(tableName = TABLE_FLOW, indices = [Index("day", unique = true)])
data class DbFlow(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_FLOW_ID)
    val id: Int,
    @ColumnInfo(name = COL_FLOW_USER_ID)
    val userId: Long,
    @ColumnInfo(name = COL_FLOW_DAY)
    val day: String,
    @ColumnInfo(name = COL_FLOW_ITEMS)
    var items: String,
    @ColumnInfo(name = COL_FLOW_IS_ARCHIVED)
    var isArchived: Boolean = false,
    @ColumnInfo(name = COL_FLOW_IS_SYNCED)
    var isSynced: Boolean = false,
    @ColumnInfo(name = COL_FLOW_IS_DISABLED)
    val isDisabled: Boolean = false
)

fun DbFlow.toFlowRq(): FlowReq = FlowReq(
    userId,
    day,
    items,
    isArchived,
    isDisabled
)