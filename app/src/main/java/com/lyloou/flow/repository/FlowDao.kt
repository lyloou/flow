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

    @Query("SELECT * FROM $TABLE_FLOW ORDER BY $COL_FLOW_DAY DESC")
    fun getAllDbFlows(): DataSource.Factory<Int, DbFlow>;

    @Query("SELECT * FROM $TABLE_FLOW WHERE $COL_FLOW_DAY = :day")
    fun getDbFlow(day: String): LiveData<DbFlow>

    @Query("UPDATE  $TABLE_FLOW SET $COL_FLOW_ITEMS=:items WHERE $COL_FLOW_DAY = :day")
    fun updateDbFlowWithItems(day: String, items: String): Int
}