package com.lyloou.flow

import com.lyloou.flow.net.Network
import com.lyloou.flow.net.flowApi
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun net_sync() {
        Network.flowApi()
            .get("20200105")
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe {
                println(it.err_code)
                println(it.err_msg)
                println(it.data)
            }
        Thread.sleep(3000)

    }

    // [Convert List Or Array To Vararg (Kotlin) | Lua Software Code](https://code.luasoftware.com/tutorials/kotlin/convert-list-and-array-to-vararg/)
    @Test
    fun testVararg() {
        test(*arrayOf("a", "b", "c"))
        test(*listOf("a", "b", "c").toTypedArray())
    }

    fun test(vararg strings: String) {
        for (string in strings) {
            print(string)
        }
    }
}
