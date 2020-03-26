package com.lyloou.flow.net

import com.lyloou.flow.common.Url
import com.lyloou.flow.model.CResult
import com.lyloou.flow.model.UserPassword
import com.lyloou.flow.model.UserPasswordHelper
import com.lyloou.flow.repository.schedule.DbSchedule
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ScheduleApi {

    @GET("list")
    fun list(@Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): Observable<CResult<List<DbSchedule>?>>

    @POST("batch_sync")
    fun batchSync(@Body dbSchedules: List<DbSchedule>): Observable<CResult<String?>>

    @POST("batch_delete")
    fun batchDelete(@Body ids: List<Long>): Observable<CResult<String?>>
}

fun Network.scheduleApi(userPassword: UserPassword? = UserPasswordHelper.getUserPassword()): ScheduleApi {
    return withHeader { auth(userPassword) }
        .get(Url.ScheduleApi.url, ScheduleApi::class.java)
}