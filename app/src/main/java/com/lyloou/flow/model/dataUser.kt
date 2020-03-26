package com.lyloou.flow.model

import com.google.gson.annotations.SerializedName
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.SpPreference
import com.lyloou.flow.common.clear
import java.util.*

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
    private var preference = SpPreference(
        SpName.USER.name,
        Key.USER.name,
        ""
    )
    private var data: String by preference

    private fun fromJson(str: String): User {
        return gson.fromJson(str, User::class.java) ?: DEFAULT_USER
    }

    fun getUser(): User {
        return fromJson(data)
    }

    fun saveUser(value: User) {
        data = value.toJson()
    }

    fun clearUser() {
        preference.clear()
    }

}

object UserPasswordHelper {
    private var preference =
        SpPreference(
            SpName.NET_AUTHORIZATION.name,
            Key.NET_AUTHORIZATION.name,
            ""
        )
    private var data: String by preference


    private fun fromJson(str: String?): UserPassword? {
        return gson.fromJson(str, UserPassword::class.java)
    }

    fun getUserPassword(): UserPassword? {
        return fromJson(data)
    }

    fun saveUserPassword(value: UserPassword) {
        data = value.toJson()
    }

    fun clearUserPassword() {
        preference.clear()
    }

}