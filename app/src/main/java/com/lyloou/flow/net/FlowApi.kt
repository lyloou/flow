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
    fun login(@Query("name") name: String, @Query("password") password: String): Observable<CResult<User?>>

    @GET("get")
    fun get(@Query("day") day: String): Observable<CResult<FlowRep?>>

    @GET("list")
    fun list(@Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): Observable<CResult<List<FlowRep>?>>

    @POST("batch_sync")
    fun batchSync(@Body flowReqs: List<FlowReq>): Observable<CResult<String?>>
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





