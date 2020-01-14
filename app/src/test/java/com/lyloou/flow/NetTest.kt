package com.lyloou.flow

import android.util.Log
import com.lyloou.flow.common.Url
import com.lyloou.flow.net.KingsoftwareAPI
import com.lyloou.flow.net.Network
import io.reactivex.schedulers.Schedulers
import org.junit.Test


class NetTest {
    @Test
    fun testKingSoftware() {
        Network.get(Url.Kingsoftware.url, KingsoftwareAPI::class.java)
            .getDaily("2020-01-14")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                Log.i("TTAG", "yyyyyy: $it");

            }, {

                Log.i("TTAG", "zzzzzzz: ");
                it.printStackTrace()
            })
    }
}