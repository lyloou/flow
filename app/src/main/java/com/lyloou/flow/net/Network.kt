package com.lyloou.flow.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private var headerPairs: (() -> List<Pair<String, String>>)? = null
    fun withHeader(pairList: (() -> List<Pair<String, String>>)): Network {
        this.headerPairs = pairList
        return this
    }

    private val map = HashMap<String, Any>()

    fun <T> get(baseUrl: String, clazz: Class<T>): T {
        val key = clazz.simpleName.plus(baseUrl)
        if (map.containsKey(key)) {
            @Suppress("UNCHECKED_CAST")
            return map[key] as T
        }
        val builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.baseUrl(baseUrl)
        val okHttpBuilder = OkHttpClient.Builder()
        headerPairs?.invoke()?.let {
            okHttpBuilder.addInterceptor(interceptor(it))
        }

        builder.client(okHttpBuilder.build())

        val t: T = builder.build().create(clazz)
        map.put(key, t!!)
        return t
    }

}

fun interceptor(headers: List<Pair<String, String>>): (Interceptor.Chain) -> Response {
    return {
        val newBuilder = it.request().newBuilder()
        headers.forEach { header ->
            newBuilder.addHeader(header.first, header.second)
        }
        it.proceed(newBuilder.build())
    }
}
