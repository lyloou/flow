package com.lyloou.flow.net

import android.app.Application
import com.lyloou.flow.App
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.Url
import com.lyloou.flow.model.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FlowApi {
    @POST("login")
    fun login(@Query("name") name: String, @Query("password") password: String): Observable<UserResult>

    @GET("get")
    fun get(@Query("day") day: String): Observable<FlowResult>

    @GET("list")
    fun list(@Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): Observable<FlowListResult>

    @POST("sync")
    fun sync(@Body flowReq: FlowReq): Observable<CommonResult>

    @POST("batch_sync")
    fun batchSync(@Body flowReqs: List<FlowReq>): Observable<CommonResult>
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
//    val userId = preferences.getLong(Key.NET_USER_ID.name, 0L)

    val auth = "86f7e437faa5a7fce15d1ddcb9eaeaea377667b8"
    val userId: Long = 1
    auth?.let { authorization ->
        return listOf(
            "Content-Type" to "application/json",
            "Authorization" to authorization,
            "UserId" to userId.toString()
        )
    }
    return listOf()
}