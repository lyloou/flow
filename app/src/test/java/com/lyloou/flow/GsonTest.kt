package com.lyloou.flow

import com.lyloou.flow.model.jsonStringToList
import com.lyloou.flow.model.jsonStringToObject
import com.lyloou.flow.model.toJsonString
import org.junit.Test

class GsonTest {
    @Test
    fun testArray() {
        val ps = listOf<Person>(Person("name1", 1), Person("name2", 2))
        val s = ps.toJsonString()
        println(s)
        val jsonArrayToList = s.jsonStringToList<Person>()
        println(jsonArrayToList)
    }

    data class Person(val name: String, val age: Int)

    @Test
    fun testObject() {
        val p = Person("name1", 12)
        val toJsonString = p.toJsonString()
        println(toJsonString)
        val p2 = toJsonString.jsonStringToObject<Person>()
        println(p2)
    }
}