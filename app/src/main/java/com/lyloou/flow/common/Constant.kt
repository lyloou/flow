package com.lyloou.flow.common

import java.util.*

var TRUE = 1
var FALSE = 0

enum class Url(val url: String) : Str {
    Kingsoftware("http://open.iciba.com/"),
    WeatherApi("http://wthrcdn.etouch.cn/"),
    FlowApi("http://flow.lyloou.com/api/v1/flow/"),
    UserApi("http://flow.lyloou.com/api/v1/user/"),
    ScheduleApi("http://flow.lyloou.com/api/v1/schedule/"),
    ;

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
    DEFAULT,
    NET_AUTHORIZATION,
    SCHEDULE_ITEM,
    USER,
    WEATHER_CITY,

    ;

    override fun str(): String {
        return getSimpleStr(javaClass, name)
    }
}

enum class Key : Str {
    DAY,
    NET_AUTHORIZATION,
    SCHEDULE,
    SCHEDULE_ITEM_A,
    SCHEDULE_ITEM_B,
    SCHEDULE_ITEM_C,
    SCHEDULE_ITEM_D,
    USER,
    WEATHER_CITY,
    GRAY_MODE,

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