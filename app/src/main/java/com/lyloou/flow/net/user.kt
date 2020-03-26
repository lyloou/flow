package com.lyloou.flow.net

import com.lyloou.flow.common.Url
import com.lyloou.flow.model.CResult
import com.lyloou.flow.model.User
import com.lyloou.flow.model.UserPassword
import com.lyloou.flow.model.UserPasswordHelper
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("login")
    fun login(@Query("name") name: String, @Query("password") password: String): Observable<CResult<User?>>

    @POST("update")
    fun update(@Body user: User): Observable<CResult<String?>>
}

fun Network.userApi(): UserApi {
    return get(Url.UserApi.url, UserApi::class.java)
}

fun Network.userWithAuthApi(userPassword: UserPassword? = UserPasswordHelper.getUserPassword()): UserApi {
    return withHeader { auth(userPassword) }
        .get(Url.UserApi.url, UserApi::class.java)
}
