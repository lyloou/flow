package com.lyloou.flow.net

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
    fun get(@Query("day") day: String): Observable<FlowResult>

    @GET("list")
    fun list(@Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): Observable<FlowListResult>

    @POST("sync")
    fun sync(@Body flowReq: FlowReq): Observable<CommonResult>

    @POST("batch_sync")
    fun batchSync(@Body flowReqs: List<FlowReq>): Observable<CommonResult>
}