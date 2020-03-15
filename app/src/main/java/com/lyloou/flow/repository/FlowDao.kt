package com.lyloou.flow.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface FlowDao {
    @Insert
    fun insertDbFlows(vararg dbFlows: DbFlow)

    @Update
    fun updateDbFlows(vararg dbFlows: DbFlow)

    @Delete
    fun deleteDbFlows(vararg dbFlows: DbFlow)

    @Query("DELETE FROM $TABLE_FLOW")
    fun deleteAllDbFlows()

    @Query("SELECT * FROM $TABLE_FLOW WHERE $COL_FLOW_IS_ARCHIVED=0 ORDER BY $COL_FLOW_DAY DESC")
    fun getActiveDbFlows(): DataSource.Factory<Int, DbFlow>

    @Query("SELECT * FROM $TABLE_FLOW WHERE $COL_FLOW_IS_ARCHIVED=1 ORDER BY $COL_FLOW_DAY DESC")
    fun getArchivedDbFlows(): DataSource.Factory<Int, DbFlow>

    @Query("SELECT * FROM $TABLE_FLOW WHERE $COL_FLOW_DAY = :day")
    fun getDbFlow(day: String): LiveData<DbFlow>

    @Query("SELECT * FROM $TABLE_FLOW WHERE $COL_FLOW_IS_SYNCED = :status")
    fun getAllDbFlowBySyncStatus(status: Boolean): LiveData<MutableList<DbFlow>>

    @Query("UPDATE  $TABLE_FLOW SET $COL_FLOW_ITEMS=:items WHERE $COL_FLOW_DAY = :day")
    fun updateDbFlowItems(day: String, items: String): Int

    @Query("UPDATE  $TABLE_FLOW SET $COL_FLOW_IS_SYNCED=:status WHERE $COL_FLOW_DAY in (:days)")
    fun updateDbFlowSyncStatus(days: List<String>, status: Boolean): Int
}