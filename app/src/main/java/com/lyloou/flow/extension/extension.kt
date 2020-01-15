package com.lyloou.flow.extension

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (scale * dpValue + 0.5f).toInt()
}

fun Fragment.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (scale * dpValue + 0.5f).toInt()
}
// https://stackoverflow.com/a/52075248
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}