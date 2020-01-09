package com.lyloou.flow.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface FlowDao {
    @Insert
    fun insertDbFlowDay(vararg dbFlowDays: DbFlowDay)

    @Update
    fun updateDbFlowDay(vararg dbFlowDays: DbFlowDay)

    @Delete
    fun deleteDbFlowDay(vararg dbFlowDays: DbFlowDay)

    @Query("DELETE FROM $TABLE_FLOW")
    fun deleteAllDbFlowDay()

//    @Query("SELECT * FROM $TABLE_FLOW ORDER　BY $COL_FLOW_DAY DESC LIMIT :offset,:limit ")
//    fun listDbFlowDays(limit: Int = 10, offset: Int = 0): LiveData<List<DbFlowDay>>

    @Query("SELECT * FROM $TABLE_FLOW ORDER BY $COL_FLOW_DAY DESC")
    fun getAllDbFlowDays(): DataSource.Factory<Int, DbFlowDay>;

    @Query("SELECT * FROM $TABLE_FLOW WHERE $COL_FLOW_DAY = :day")
    fun getDbFlowDay(day: String): LiveData<DbFlowDay>

    @Query("UPDATE  $TABLE_FLOW SET $COL_FLOW_ITEMS=:items WHERE $COL_FLOW_DAY = :day")
    fun updateDbFlowDayWithItems(day: String, items: String): Int
}