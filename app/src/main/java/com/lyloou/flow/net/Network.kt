package com.lyloou.flow.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private var headerPairs: (() -> List<Pair<String, String>>)? = null
    private val builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    fun withHeader(pairList: (() -> List<Pair<String, String>>)): Network {
        this.headerPairs = pairList
        return this
    }

    fun <T> get(baseUrl: String, clazz: Class<T>): T {
        val okHttpBuilder = OkHttpClient.Builder()
        headerPairs?.invoke()?.let {
            okHttpBuilder.addInterceptor(interceptor(it))
        }
        return builder.baseUrl(baseUrl)
            .client(okHttpBuilder.build())
            .build().create(clazz)
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
