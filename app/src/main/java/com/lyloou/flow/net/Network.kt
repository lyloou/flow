package com.lyloou.flow.net

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private val map = HashMap<String, Any>()
    fun <T> get(baseUrl: String, clazz: Class<T>): T {
        val key = clazz.simpleName.plus(baseUrl)
        if (map.containsKey(key)) {
            @Suppress("UNCHECKED_CAST")
            return map[key] as T
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val t: T = retrofit.create(clazz)
        map.put(key, t!!)
        return t
    }
}