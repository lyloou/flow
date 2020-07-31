package com.lyloou.flow.model

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SPreference
import com.lyloou.flow.common.SpName
import com.lyloou.flow.common.clear


data class City(
    val id: Long,
    val pid: Long,
    @SerializedName("city_code")
    val cityCode: String,
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("post_code")
    val postCode: String,
    @SerializedName("area_code")
    val areaCode: String,
    val ctime: String
)


object CityHelper {
    private fun fromJson(str: String): City? {
        return gson.fromJson(str, City::class.java)
    }

    private var preference =
        SPreference(
            SpName.WEATHER_CITY.name,
            Key.WEATHER_CITY.name,
            ""
        )
    private var data: String by preference


    fun saveCity(city: City?) {
        city?.let {
            data = it.toJsonString()
        }
    }

    fun getCity(): City? {
        return fromJson(data)
    }

    fun clearCity() {
        preference.clear()
    }
}

private val type = object : TypeToken<List<City?>?>() {}.type