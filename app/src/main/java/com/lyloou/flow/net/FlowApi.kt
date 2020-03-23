package com.lyloou.flow.net

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

    @POST("batch_sync")
    fun batchSync(@Body flowReqs: List<FlowReq>): Observable<CommonResult>
}

fun Network.flowApiWithoutAuth(): FlowApi {
    return get(Url.FlowApi.url, FlowApi::class.java)
}

/**
 * 提供默认这种方式，如果测试，也可以用
 */
fun Network.flowApi(userPassword: UserPassword? = UserPasswordHelper.getUserPassword()): FlowApi {
    return withHeader { auth(userPassword) }
        .get(Url.FlowApi.url, FlowApi::class.java)
}

// [Retrofit — Add Custom Request Header](https://futurestud.io/tutorials/retrofit-add-custom-request-header)
private fun auth(userPassword: UserPassword?): List<Pair<String, String>> {
    userPassword?.let {
        return listOf(
            "Content-Type" to "application/json",
            "Authorization" to userPassword.password,
            "UserId" to userPassword.userId.toString()
        )
    }
    return emptyList()
}



