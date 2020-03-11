package com.lyloou.flow.common

import java.util.*

var TRUE = 1
var FALSE = 0

enum class Url(val url: String) : Str {
    Kingsoftware("http://open.iciba.com/"),
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

enum class SpName : Str {
    NET_AUTHORIZATION,

    ;

    override fun str(): String {
        return getSimpleStr(javaClass, name)
    }
}

enum class Key : Str {
    DAY,
    NAVIGATION_ID,
    TODO,
    NET_AUTHORIZATION,
    ;

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

enum class IMG(val url: String) : Str {
    TOP_BG_01("https://ws1.sinaimg.cn/large/610dc034gy1fhupzs0awwj20u00u0tcf.jpg"),
    ;

    override fun str(): String {
        return getSimpleStr(javaClass, name)
    }

}