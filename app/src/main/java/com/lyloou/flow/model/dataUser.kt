package com.lyloou.flow.model

import android.app.Application
import com.google.gson.annotations.SerializedName
import com.lyloou.flow.App
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import java.util.*

data class UserResult(var err_code: Int, var err_msg: String, var data: User?)

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val phone: Long,
    val avatar: String,
    @SerializedName("personal_signature")
    val personalSignature: String,
    @SerializedName("gmt_create")
    val gmtCreate: Date
)

data class UserPassword(
    val userId: Long,
    val name: String,
    val password: String
)

fun UserPassword.toJson(): String {
    return gson.toJson(this)
}

fun User.toJson(): String {
    return gson.toJson(this)
}

val DEFAULT_USER = User(0, "登录", "", 0, "", "天天做好事", Date())

object UserHelper {
    private val preferences = App.instance
        .getSharedPreferences(SpName.USER.name, Application.MODE_PRIVATE)

    private fun fromJson(str: String): User {
        return gson.fromJson(str, User::class.java) ?: DEFAULT_USER
    }

    fun getUser(): User {
        return fromJson(preferences.getString(Key.USER.name, "") ?: "")
    }

    fun saveUser(value: User) {
        var toJson = value.toJson()
        preferences.edit().putString(Key.USER.name, toJson).apply()
    }

    fun clearUser() {
        preferences.edit().remove(Key.USER.name).apply()
    }

}

object UserPasswordHelper {
    private val preferences = App.instance
        .getSharedPreferences(SpName.NET_AUTHORIZATION.name, Application.MODE_PRIVATE)

    private fun fromJson(str: String?): UserPassword? {
        return gson.fromJson(str, UserPassword::class.java)
    }

    fun getUserPassword(): UserPassword? {
        return fromJson(preferences.getString(Key.NET_AUTHORIZATION.name, null))
    }

    fun saveUserPassword(value: UserPassword) {
        preferences.edit()
            .putString(Key.NET_AUTHORIZATION.name, value.toJson())
            .apply()
    }

    fun clearUserPassword() {
        preferences.edit().remove(Key.NET_AUTHORIZATION.name).apply()
    }

}