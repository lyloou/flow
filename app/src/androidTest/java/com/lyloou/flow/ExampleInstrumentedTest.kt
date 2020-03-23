package com.lyloou.flow

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.lyloou.flow.model.UserPassword
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.flowApi
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.lyloou.flow", appContext.packageName)
    }

    @Test
    fun net_sync() {
        Network.flowApi(UserPassword(1, "lyloou", "122"))
            .get("20200322")
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                Log.e("TTAG", "success:${it}")
            }, { t ->
                Log.e("TTAG", "failed:${t.message}")
            })
        Thread.sleep(6000)

    }
}
