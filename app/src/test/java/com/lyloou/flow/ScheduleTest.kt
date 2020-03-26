package com.lyloou.flow

import com.google.gson.Gson
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.model.gson
import org.junit.Test
import java.util.*

class ScheduleTest {

    @Test
    fun testGson() {
        println(Gson().toJson(Schedule("he", "aa", "bb", "cc", "dd")))
    }

    @Test
    fun testDate() {
        println(gson.toJson(Date()))
    }
}