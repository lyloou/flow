package com.lyloou.flow.model

import java.util.*

data class UserResult(var err_code: Int, var err_msg: String, var data: User?)

data class User(
    val name: String,
    val email: String,
    val phone: Long,
    val avatar: String,
    val personalSignature: String,
    val gmtCreate: Date
)

data class UserPassword(
    val name: String,
    val password: String
)
