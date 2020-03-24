package com.lyloou.flow.repository.schedule

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [DbSchedule::class], version = 1, exportSchema = false)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var instance: ScheduleDatabase? = null

        fun getInstance(context: Context): ScheduleDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
                    .also {
                        instance = it
                    }
            }

        }

        private fun buildDatabase(context: Context): ScheduleDatabase {

            return Room
                .databaseBuilder(
                    context.applicationContext,
                    ScheduleDatabase::class.java,
                    "schedule_database"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })
                .build()
        }
    }
}