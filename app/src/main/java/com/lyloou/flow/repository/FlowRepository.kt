package com.lyloou.flow.repository

import android.content.Context
import androidx.lifecycle.LiveData

class FlowRepository(private val context: Context) {
    private val flowDao: FlowDao

    init {
        val database = FlowDatabase.getInstance(context)
        flowDao = database.flowDao()
    }

    fun getDbFlowDay(day: String): LiveData<DbFlowDay> {
        return flowDao.getDbFlowDay(day)
    }
}