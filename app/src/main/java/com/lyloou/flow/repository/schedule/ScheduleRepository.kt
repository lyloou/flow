package com.lyloou.flow.repository.schedule

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.lyloou.flow.common.Consumer

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

    class GetAsyncTask(
        private val scheduleDao: ScheduleDao,
        private val consumer: Consumer<List<DbSchedule>>
    ) :
        AsyncTask<Unit, Unit, List<DbSchedule>>() {
        override fun doInBackground(vararg p0: Unit?): List<DbSchedule> {
            Log.i("TTAG", "----4: ");
            return scheduleDao.getAllDbSchedule().toList()
        }

        override fun onPostExecute(result: List<DbSchedule>) {
            Log.i("TTAG", "----5: ");
            consumer.accept(result)
        }
    }

    fun getDbSchedule(id: Long): LiveData<DbSchedule> {
        return scheduleDao.getDbSchedule(id)
    }


    fun getAllDbScheduleList(consumer: Consumer<List<DbSchedule>>) {
        GetAsyncTask(scheduleDao, consumer).execute()
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

