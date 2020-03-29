package com.lyloou.flow.model

// https://medium.com/@stustirling/responses-errors-with-retrofit-2-rxjava2-6d55eafecf5a
data class CResult<T>(var err_code: Int, var err_msg: String, var data: T?) {
    fun isSuccess() = err_code == 0
}