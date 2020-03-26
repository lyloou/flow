package com.lyloou.flow.net

import com.lyloou.flow.model.UserPassword
import com.lyloou.flow.model.gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private var headerPairs: (() -> List<Pair<String, String>>)? = null
    private val builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
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

    // 把用户信息和授权信息通过 header 的方式发到服务器
    // [Retrofit — Add Custom Request Header](https://futurestud.io/tutorials/retrofit-add-custom-request-header)
    fun auth(userPassword: UserPassword?): List<Pair<String, String>> {
        userPassword?.let {
            return listOf(
                "Content-Type" to "application/json",
                "Authorization" to it.password,
                "UserId" to it.userId.toString()
            )
        }
        return emptyList()
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
