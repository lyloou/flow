package com.lyloou.flow

import com.google.gson.Gson
import com.lyloou.flow.model.Schedule
import org.junit.Test

class ScheduleTest {

    @Test
    fun testGson() {
        println(Gson().toJson(Schedule("he", "aa", "bb", "cc", "dd")))
    }
}