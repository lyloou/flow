package com.lyloou.flow.model

import android.app.Activity
import com.google.gson.annotations.SerializedName
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SPreference
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.clear
import com.lyloou.flow.extension.simpleStartActivity
import com.lyloou.flow.extension.snackbar
import com.lyloou.flow.ui.user.LoginActivity
import java.util.*

data class User(
    val id: Long,
    var name: String,
    var nickname: String,
    var email: String,
    var phone: Long,
    var avatar: String,
    @SerializedName("personal_signature")
    var personalSignature: String,
    @SerializedName("gmt_create")
    var gmtCreate: Date
)

data class UserPassword(
    val userId: Long,
    val name: String,
    val password: String
)

data class UserRegister(
    val name: String,
    val password: String,
    val email: String? = null,
    val avatar: String? = null,
    val phone: Long? = null
)

val DEFAULT_USER = User(0, "用户名", "昵称", "", 0, "", "天天做好事", Date())

object UserHelper {
    private var preference = SPreference(
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
        data = value.toJsonString()
    }

    fun clearUser() {
        preference.clear()
    }

    fun isNotLogin(context: Activity): Boolean {
        if (getUser().id == 0L) {
            context.snackbar("还没登录哦")
                .setAction("去登录") {
                    context.simpleStartActivity<LoginActivity>()
                }
                .show()
            return true
        }
        return false
    }
}

object UserPasswordHelper {
    private var preference =
        SPreference(
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
        data = value.toJsonString()
    }

    fun clearUserPassword() {
        preference.clear()
    }

}