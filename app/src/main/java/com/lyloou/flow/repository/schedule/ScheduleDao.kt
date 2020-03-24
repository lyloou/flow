package com.lyloou.flow.repository.schedule

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface ScheduleDao {
    @Insert
    fun insertDbSchedule(vararg dbSchedules: DbSchedule)

    @Update
    fun updateDbSchedule(vararg dbSchedules: DbSchedule)

    @Delete
    fun deleteDbSchedule(vararg dbSchedules: DbSchedule)

    @Query("DELETE FROM $TABLE_SCHEDULE")
    fun deleteAllDbSchedule()

    @Query("SELECT * FROM $TABLE_SCHEDULE WHERE $COL_SCHEDULE_ID = :id")
    fun getDbSchedule(id: Long): LiveData<DbSchedule>

    @Query("SELECT * FROM $TABLE_SCHEDULE ORDER BY $COL_SCHEDULE_ID DESC")
    fun getAllDbSchedule(): DataSource.Factory<Int, DbSchedule>
}