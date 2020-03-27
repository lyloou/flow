package com.lyloou.flow

import com.lyloou.flow.model.gson
import com.lyloou.flow.util.Udata
import org.junit.Test
import java.util.*

class ScheduleTest {

    @Test
    fun testDate() {
        println(gson.toJson(Date()))
    }

    @Test
    fun testUUID() {
        for (i in 1..10) {
            val message = Udata.uuid()
            println(message)
        }
    }


    @Test
    fun testList() {
        val remoteList = listOf(1, 2, 3, 4, 5, 10, 11, 12)
        val localList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val onlyLocal = localList.subtract(remoteList)
        println("onlyLocal:$onlyLocal")

        Date().time
        val onlyRemote = remoteList.subtract(localList)
        println("onlyRemote:$onlyRemote")
        onlyRemote.map { }
    }
}