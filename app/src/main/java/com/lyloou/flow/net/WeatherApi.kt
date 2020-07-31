package com.lyloou.flow.net

import com.lyloou.flow.common.Url
import com.lyloou.flow.model.MyWeather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/weather_mini")
    fun getWeather(@Query("citykey") cityCode: String): Observable<MyWeather>
}

fun Network.weatherApi(): WeatherApi {
    return get(Url.WeatherApi.url, WeatherApi::class.java)
}