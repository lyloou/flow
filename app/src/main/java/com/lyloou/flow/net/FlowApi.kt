package com.lyloou.flow.net

import android.app.Application
import com.lyloou.flow.App
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.Url
import com.lyloou.flow.model.CommonResult
import com.lyloou.flow.model.FlowListResult
import com.lyloou.flow.model.FlowReq
import com.lyloou.flow.model.FlowResult
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FlowApi {
    @GET("get")
    fun get(@Query("user_id") userId: Long, @Query("day") day: String): Observable<FlowResult>

    @GET("list")
    fun list(@Query("user_id") userId: Long, @Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): Observable<FlowListResult>

    @POST("sync")
    fun sync(@Query("user_id") userId: Long, @Body flowReq: FlowReq): Observable<CommonResult>

    @POST("batch_sync")
    fun batchSync(@Query("user_id") userId: Long, @Body flowReqs: List<FlowReq>): Observable<CommonResult>
}

fun Network.flowApi(): FlowApi {
    return withHeader { auth() }
        .get(Url.FlowApi.url, FlowApi::class.java)
}

// [Retrofit — Add Custom Request Header](https://futurestud.io/tutorials/retrofit-add-custom-request-header)
private fun auth(): List<Pair<String, String>> {
    val preferences = App.instance
        .getSharedPreferences(SpName.NET_AUTHORIZATION.name, Application.MODE_PRIVATE)
//    val auth = preferences.getString(Key.NET_AUTHORIZATION.name, null)
    val auth = preferences.getString(
        Key.NET_AUTHORIZATION.name,
        "86f7e437faa5a7fce15d1ddcb9eaeaea377667b8"
    )

    auth?.let { authorization ->
        return listOf("Authorization" to authorization)
    }
    return listOf()
}