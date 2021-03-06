package com.lyloou.flow.net

import com.lyloou.flow.common.Url
import com.lyloou.flow.model.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("login")
    suspend fun login(@Query("name") name: String, @Query("password") password: String): CResult<User?>

    @POST("update")
    suspend fun update(@Body user: User): CResult<String?>

    @POST("reset_password")
    suspend fun resetPassword(
        @Query("old_password") oldPassword: String,
        @Query("new_password") newPassword: String
    ): CResult<String?>

    @POST("register")
    suspend fun register(@Body userRegister: UserRegister): CResult<User?>
}

fun Network.userApi(): UserApi {
    return get(Url.UserApi.url, UserApi::class.java)
}

fun Network.userWithAuthApi(userPassword: UserPassword? = UserPasswordHelper.getUserPassword()): UserApi {
    return withHeader { auth(userPassword) }
        .get(Url.UserApi.url, UserApi::class.java)
}
