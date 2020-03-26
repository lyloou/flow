package com.lyloou.flow.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.lyloou.flow.App
import com.lyloou.flow.common.toast
import com.lyloou.flow.model.UserPassword
import com.lyloou.flow.model.gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
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

fun <T> Observable<T>.defaultSubscribe(
    onNext: (T) -> Unit
): Disposable {
    return this.defaultScheduler().subscribe(onNext, { doError(it) })
}

private fun doError(it: Throwable) {
    it.printStackTrace()
    if (!isNetworkAvailable(App.instance)) {
        toast("网络不可用，请检查网络")
    } else {
        it.message?.let {
            toast(it)
        }
    }
}

fun <T> Observable<T>.defaultScheduler(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

