package com.lyloou.flow.util

import java.util.*

object Udata {

    fun uuid(): String = toUpperString(UUID.randomUUID())

    private fun toUpperString(randomUUID: UUID) =
        randomUUID.toString().replace("-", "").toUpperCase()
}