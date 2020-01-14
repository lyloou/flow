package com.lyloou.flow.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

@Database(entities = [DbFlow::class], version = 1, exportSchema = false)
abstract class FlowDatabase : RoomDatabase() {

    abstract fun flowDao(): FlowDao

    companion object {
        @Volatile
        private var instance: FlowDatabase? = null

        fun getInstance(context: Context): FlowDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
                    .also {
                        instance = it
                    }
            }

        }

        private fun buildDatabase(context: Context): FlowDatabase {
            return Room
                .databaseBuilder(
                    context.applicationContext,
                    FlowDatabase::class.java,
                    "flow_database"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // 第一从网络获取数据
                        val workRequest = OneTimeWorkRequestBuilder<FlowNetWork>().build()
                        WorkManager.getInstance(context).enqueue(workRequest)
                    }
                })
                .build()
        }
    }
}