package com.lyloou.flow.repository.schedule

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

class ScheduleRepository(private val context: Context) {
    private val scheduleDao: ScheduleDao

    init {
        val database = ScheduleDatabase.getInstance(context)
        scheduleDao = database.scheduleDao()
    }

    companion object {
        @Volatile
        private var instance: ScheduleRepository? = null

        fun getInstance(context: Context): ScheduleRepository {
            return instance ?: synchronized(this) {
                instance ?: ScheduleRepository(context).also {
                    instance = it
                }
            }

        }
    }

    fun insertDbSchedule(vararg dbSchedules: DbSchedule) {
        InsertAsyncTask(scheduleDao).execute(*dbSchedules)
    }

    fun deleteDbSchedule(vararg dbSchedules: DbSchedule) {
        DeleteAsyncTask(scheduleDao).execute(*dbSchedules)
    }

    fun updateDbSchedule(vararg dbSchedules: DbSchedule) {
        UpdateAsyncTask(scheduleDao).execute(*dbSchedules)
    }

    class InsertAsyncTask(private val scheduleDao: ScheduleDao) :
        AsyncTask<DbSchedule, Unit, Unit>() {

        override fun doInBackground(vararg schedules: DbSchedule) {
            if (schedules.size < 0) {
                return
            }
            scheduleDao.insertDbSchedule(*schedules)
        }

    }

    class DeleteAsyncTask(private val scheduleDao: ScheduleDao) :
        AsyncTask<DbSchedule, Unit, Unit>() {

        override fun doInBackground(vararg schedules: DbSchedule) {
            if (schedules.size < 0) {
                return
            }
            scheduleDao.deleteDbSchedule(*schedules)
        }
    }

    class UpdateAsyncTask(private val scheduleDao: ScheduleDao) :
        AsyncTask<DbSchedule, Unit, Unit>() {

        override fun doInBackground(vararg schedules: DbSchedule) {
            if (schedules.size < 0) {
                return
            }
            scheduleDao.updateDbSchedule(*schedules)
        }
    }


    fun getDbSchedule(id: Long): LiveData<DbSchedule> {
        return scheduleDao.getDbSchedule(id)
    }


    fun getAllPagedList(): LiveData<PagedList<DbSchedule>> {
        return LivePagedListBuilder(
            scheduleDao.getAllDbSchedule(),
            PagedList.Config.Builder()
                .setPageSize(5)
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .build()
        ).build()
    }

    fun getEnabledPagedList(): LiveData<PagedList<DbSchedule>> {
        return LivePagedListBuilder(
            scheduleDao.getEnabledDbSchedule(),
            PagedList.Config.Builder()
                .setPageSize(5)
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .build()
        ).build()
    }
}

