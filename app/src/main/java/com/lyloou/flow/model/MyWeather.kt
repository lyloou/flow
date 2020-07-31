package com.lyloou.flow.model

// http://wthrcdn.etouch.cn/weather_mini?citykey=101010100
data class MyWeather(
    val data: Data,
    val status: Int = 0,
    val desc: String = ""
)


data class Yesterday(
    val date: String = "",
    val high: String = "",
    val fx: String = "",
    val low: String = "",
    val fl: String = "",
    val type: String = ""
)


data class ForecastItem(
    val date: String = "",
    val high: String = "",
    val fengli: String = "",
    val low: String = "",
    val fengxiang: String = "",
    val type: String = ""
)


data class Data(
    val yesterday: Yesterday,
    val city: String = "",
    val forecast: List<ForecastItem>?,
    val ganmao: String = "",
    val wendu: String = ""
)


