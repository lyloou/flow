package com.lyloou.flow.net

import com.lyloou.flow.App
import com.lyloou.flow.common.toast
import com.lyloou.flow.util.Unet
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


fun <T> Observable<T>.defaultSubscribe(
    onNext: (T) -> Unit
): Disposable {
    return this.defaultScheduler().subscribe(onNext, { doError(it) })
}

private fun doError(it: Throwable) {
    it.printStackTrace()
    if (!Unet.isNetworkAvailable(App.instance)) {
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

