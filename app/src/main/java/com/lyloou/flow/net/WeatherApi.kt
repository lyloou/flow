package com.lyloou.flow.net

import com.lyloou.flow.common.Url
import com.lyloou.flow.model.WeatherResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {
    @GET("/api/weather/city/{city_code}")
    fun getWeather(@Path("city_code") cityCode: String?): Observable<WeatherResult>
}

fun Network.weatherApi(): WeatherApi {
    return get(Url.WeatherApi.url, WeatherApi::class.java)
}