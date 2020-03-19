package com.lyloou.flow.ui.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.reflect.TypeToken
import com.lyloou.flow.App
import com.lyloou.flow.model.City
import com.lyloou.flow.model.CityHelper
import com.lyloou.flow.model.gson
import java.nio.charset.Charset

class CitySelectorViewModel : ViewModel() {

    val key: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val data: LiveData<List<City>> by lazy {
        getData()
    }

    val list: MutableLiveData<List<City>> by lazy {
        MutableLiveData<List<City>>()
    }

    private fun getData(): MutableLiveData<List<City>> {
        val cityFile = App.instance.assets.open("city.json")
        val size = cityFile.available()
        val buffer = ByteArray(size)
        cityFile.read(buffer)
        cityFile.close()
        val cityStr = String(buffer, Charset.forName("UTF-8"))
        val type = object : TypeToken<List<City>>() {}.type
        val list: List<City> = gson.fromJson(cityStr, type)
        return MutableLiveData(list)
    }

    fun filter(filter: String) {
        list.value = data.value?.filter { it.cityName.contains(filter) && it.cityCode.isNotEmpty() }
    }

    fun saveCity(city: City) {
        CityHelper.saveCity(city)
    }
}