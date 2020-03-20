package com.lyloou.flow.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [DbFlow::class], version = 2, exportSchema = false)
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
                    }
                })
                .addMigrations(migration_1_2)
                .build()
        }

        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
        // https://developer.android.com/training/data-storage/room/migrating-db-versions
        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $TABLE_FLOW ADD COLUMN $COL_FLOW_WEATHER TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE $TABLE_FLOW ADD COLUMN $COL_FLOW_MEMO TEXT NOT NULL DEFAULT ''")
            }

        }
    }
}