package com.lyloou.flow.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

const val TABLE_FLOW = "flow"
const val COL_FLOW_ID = "id"
const val COL_FLOW_DAY = "day"
const val COL_FLOW_ITEMS = "items"
const val COL_FLOW_IS_ARCHIVED = "is_archived"
const val COL_FLOW_IS_SYNCED = "is_synced"
const val COL_FLOW_IS_DISABLED = "is_disabled"

@Entity(tableName = TABLE_FLOW, indices = [Index("day")])
data class DbFlowDay(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_FLOW_ID)
    val id: Int,
    @ColumnInfo(name = COL_FLOW_DAY)
    val day: String,
    @ColumnInfo(name = COL_FLOW_ITEMS)
    val items: String,
    @ColumnInfo(name = COL_FLOW_IS_ARCHIVED)
    val isArchived: Boolean = false,
    @ColumnInfo(name = COL_FLOW_IS_SYNCED)
    val isSynced: Boolean = false,
    @ColumnInfo(name = COL_FLOW_IS_DISABLED)
    val isDisabled: Boolean = false
)