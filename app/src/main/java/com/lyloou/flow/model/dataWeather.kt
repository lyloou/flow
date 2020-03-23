package com.lyloou.flow.model

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.lyloou.flow.App
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SpName
import com.lyloou.flow.extension.Preference
import com.lyloou.flow.extension.clear

class CityInfoBean {
    /**
     * city : 天津市
     * citykey : 101030100
     * parent : 天津
     * updateTime : 16:47
     */
    var city: String? = null
    var citykey: String? = null
    var parent: String? = null
    var updateTime: String? = null
}

class WeatherResult {
    /**
     * message : success感谢又拍云(upyun.com)提供CDN赞助
     * status : 200
     * date : 20191015
     * time : 2019-10-15 17:07:27
     * cityInfo : {"city":"天津市","citykey":"101030100","parent":"天津","updateTime":"16:47"}
     * data : {"shidu":"28%","pm25":31,"pm10":34,"quality":"优","wendu":"16","ganmao":"各类人群可自由活动","forecast":[{"date":"15","high":"高温 18℃","low":"低温 8℃","ymd":"2019-10-15","week":"星期二","sunrise":"06:19","sunset":"17:35","aqi":61,"fx":"西风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"16","high":"高温 17℃","low":"低温 10℃","ymd":"2019-10-16","week":"星期三","sunrise":"06:20","sunset":"17:33","aqi":79,"fx":"西南风","fl":"<3级","type":"阴","notice":"不要被阴云遮挡住好心情"},{"date":"17","high":"高温 15℃","low":"低温 11℃","ymd":"2019-10-17","week":"星期四","sunrise":"06:21","sunset":"17:32","aqi":89,"fx":"西南风","fl":"<3级","type":"阴","notice":"不要被阴云遮挡住好心情"},{"date":"18","high":"高温 19℃","low":"低温 11℃","ymd":"2019-10-18","week":"星期五","sunrise":"06:22","sunset":"17:30","aqi":120,"fx":"东南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"19","high":"高温 22℃","low":"低温 13℃","ymd":"2019-10-19","week":"星期六","sunrise":"06:23","sunset":"17:29","aqi":135,"fx":"西南风","fl":"3-4级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"20","high":"高温 20℃","low":"低温 12℃","ymd":"2019-10-20","week":"星期日","sunrise":"06:24","sunset":"17:28","aqi":76,"fx":"北风","fl":"4-5级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"21","high":"高温 20℃","low":"低温 12℃","ymd":"2019-10-21","week":"星期一","sunrise":"06:25","sunset":"17:26","fx":"西风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"22","high":"高温 22℃","low":"低温 12℃","ymd":"2019-10-22","week":"星期二","sunrise":"06:26","sunset":"17:25","fx":"西南风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"23","high":"高温 19℃","low":"低温 11℃","ymd":"2019-10-23","week":"星期三","sunrise":"06:27","sunset":"17:23","fx":"东风","fl":"3-4级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"24","high":"高温 20℃","low":"低温 13℃","ymd":"2019-10-24","week":"星期四","sunrise":"06:28","sunset":"17:22","fx":"西南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"25","high":"高温 20℃","low":"低温 11℃","ymd":"2019-10-25","week":"星期五","sunrise":"06:29","sunset":"17:21","fx":"西北风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"26","high":"高温 16℃","low":"低温 10℃","ymd":"2019-10-26","week":"星期六","sunrise":"06:30","sunset":"17:19","fx":"东北风","fl":"<3级","type":"小雨","notice":"雨虽小，注意保暖别感冒"},{"date":"27","high":"高温 17℃","low":"低温 10℃","ymd":"2019-10-27","week":"星期日","sunrise":"06:31","sunset":"17:18","fx":"西风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"28","high":"高温 18℃","low":"低温 8℃","ymd":"2019-10-28","week":"星期一","sunrise":"06:32","sunset":"17:17","fx":"北风","fl":"4-5级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"29","high":"高温 15℃","low":"低温 8℃","ymd":"2019-10-29","week":"星期二","sunrise":"06:33","sunset":"17:16","fx":"东南风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}],"yesterday":{"date":"14","high":"高温 15℃","low":"低温 5℃","ymd":"2019-10-14","week":"星期一","sunrise":"06:18","sunset":"17:36","aqi":55,"fx":"北风","fl":"3-4级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}}
     */
    var message: String? = null
    var status = 0
    var date: String? = null
    var time: String? = null
    var cityInfo: CityInfoBean? = null
    var data: DataBean? = null
}

class DataBean {
    /**
     * shidu : 28%
     * pm25 : 31.0
     * pm10 : 34.0
     * quality : 优
     * wendu : 16
     * ganmao : 各类人群可自由活动
     * forecast : [{"date":"15","high":"高温 18℃","low":"低温 8℃","ymd":"2019-10-15","week":"星期二","sunrise":"06:19","sunset":"17:35","aqi":61,"fx":"西风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"16","high":"高温 17℃","low":"低温 10℃","ymd":"2019-10-16","week":"星期三","sunrise":"06:20","sunset":"17:33","aqi":79,"fx":"西南风","fl":"<3级","type":"阴","notice":"不要被阴云遮挡住好心情"},{"date":"17","high":"高温 15℃","low":"低温 11℃","ymd":"2019-10-17","week":"星期四","sunrise":"06:21","sunset":"17:32","aqi":89,"fx":"西南风","fl":"<3级","type":"阴","notice":"不要被阴云遮挡住好心情"},{"date":"18","high":"高温 19℃","low":"低温 11℃","ymd":"2019-10-18","week":"星期五","sunrise":"06:22","sunset":"17:30","aqi":120,"fx":"东南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"19","high":"高温 22℃","low":"低温 13℃","ymd":"2019-10-19","week":"星期六","sunrise":"06:23","sunset":"17:29","aqi":135,"fx":"西南风","fl":"3-4级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"20","high":"高温 20℃","low":"低温 12℃","ymd":"2019-10-20","week":"星期日","sunrise":"06:24","sunset":"17:28","aqi":76,"fx":"北风","fl":"4-5级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"21","high":"高温 20℃","low":"低温 12℃","ymd":"2019-10-21","week":"星期一","sunrise":"06:25","sunset":"17:26","fx":"西风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"22","high":"高温 22℃","low":"低温 12℃","ymd":"2019-10-22","week":"星期二","sunrise":"06:26","sunset":"17:25","fx":"西南风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"23","high":"高温 19℃","low":"低温 11℃","ymd":"2019-10-23","week":"星期三","sunrise":"06:27","sunset":"17:23","fx":"东风","fl":"3-4级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"24","high":"高温 20℃","low":"低温 13℃","ymd":"2019-10-24","week":"星期四","sunrise":"06:28","sunset":"17:22","fx":"西南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"25","high":"高温 20℃","low":"低温 11℃","ymd":"2019-10-25","week":"星期五","sunrise":"06:29","sunset":"17:21","fx":"西北风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"26","high":"高温 16℃","low":"低温 10℃","ymd":"2019-10-26","week":"星期六","sunrise":"06:30","sunset":"17:19","fx":"东北风","fl":"<3级","type":"小雨","notice":"雨虽小，注意保暖别感冒"},{"date":"27","high":"高温 17℃","low":"低温 10℃","ymd":"2019-10-27","week":"星期日","sunrise":"06:31","sunset":"17:18","fx":"西风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"28","high":"高温 18℃","low":"低温 8℃","ymd":"2019-10-28","week":"星期一","sunrise":"06:32","sunset":"17:17","fx":"北风","fl":"4-5级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"29","high":"高温 15℃","low":"低温 8℃","ymd":"2019-10-29","week":"星期二","sunrise":"06:33","sunset":"17:16","fx":"东南风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}]
     * yesterday : {"date":"14","high":"高温 15℃","low":"低温 5℃","ymd":"2019-10-14","week":"星期一","sunrise":"06:18","sunset":"17:36","aqi":55,"fx":"北风","fl":"3-4级","type":"晴","notice":"愿你拥有比阳光明媚的心情"}
     */
    var shidu: String? = null
    var pm25 = 0.0
    var pm10 = 0.0
    var quality: String? = null
    var wendu: String? = null
    var ganmao: String? = null
    var yesterday: YesterdayBean? = null
    var forecast: List<ForecastBean>? = null
}

class YesterdayBean {
    /**
     * date : 14
     * high : 高温 15℃
     * low : 低温 5℃
     * ymd : 2019-10-14
     * week : 星期一
     * sunrise : 06:18
     * sunset : 17:36
     * aqi : 55
     * fx : 北风
     * fl : 3-4级
     * type : 晴
     * notice : 愿你拥有比阳光明媚的心情
     */
    var date: String? = null
    var high: String? = null
    var low: String? = null
    var ymd: String? = null
    var week: String? = null
    var sunrise: String? = null
    var sunset: String? = null
    var aqi = 0
    var fx: String? = null
    var fl: String? = null
    var type: String? = null
    var notice: String? = null

}

class ForecastBean {
    /**
     * date : 15
     * high : 高温 18℃
     * low : 低温 8℃
     * ymd : 2019-10-15
     * week : 星期二
     * sunrise : 06:19
     * sunset : 17:35
     * aqi : 61
     * fx : 西风
     * fl : <3级
     * type : 晴
     * notice : 愿你拥有比阳光明媚的心情
     */
    var date: String? = null
    var high: String? = null
    var low: String? = null
    var ymd: String? = null
    var week: String? = null
    var sunrise: String? = null
    var sunset: String? = null
    var aqi = 0
    var fx: String? = null
    var fl: String? = null
    var type: String? = null
    var notice: String? = null

}


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

fun City.toJson(): String {
    return gson.toJson(this)
}

object CityHelper {
    private fun fromJson(str: String): City? {
        return gson.fromJson(str, City::class.java)
    }

    private var preference =
        Preference(App.instance, Key.WEATHER_CITY.name, "", SpName.WEATHER_CITY.name)
    private var data: String by preference


    fun saveCity(city: City?) {
        city?.let {
            data = it.toJson()
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