package com.lyloou.flow.common

import java.util.*

var TRUE = 1
var FALSE = 0

enum class Url(val url: String) : Str {
    FlowApi("http://114.67.95.131:8888/api/v1/flow/");

    override fun str(): String {
        return getSimpleStr(javaClass, name)
    }

}

enum class Action : Str {
    ;

    override fun str(): String {
        return getSimpleStr(javaClass, name)
    }
}

enum class Key : Str {
    BACKGROUND_SERVER;

    override fun str(): String {
        return getSimpleStr(javaClass, name)
    }
}

fun getSimpleStr(clazz: Class<*>, name: String): String {
    return clazz.simpleName.toUpperCase(Locale.getDefault()) + "_" + name
}

interface Str {
    fun str(): String?
}