package com.lyloou.flow.net

import com.lyloou.flow.common.Url
import com.lyloou.flow.model.MyWeather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

// [分享免费天气API - 蓄谋已久 - 博客园](https://www.cnblogs.com/liujiuzhou/p/11547156.html)
// http://wthrcdn.etouch.cn/weather_mini?citykey=101280601
interface WeatherApi {
    @GET("/weather_mini")
    fun getWeather(@Query("citykey") cityCode: String): Observable<MyWeather>
}

fun Network.weatherApi(): WeatherApi {
    return get(Url.WeatherApi.url, WeatherApi::class.java)
}