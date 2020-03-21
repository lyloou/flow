package com.lyloou.flow.extension

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar


fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (scale * dpValue + 0.5f).toInt()
}

fun Activity.snackbar(str: String, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
    return Snackbar.make(findViewById(android.R.id.content), str, duration)
}

fun Fragment.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (scale * dpValue + 0.5f).toInt()
}

fun Fragment.snackbar(str: String, duration: Int = Snackbar.LENGTH_SHORT): Snackbar {
    return Snackbar.make(requireView(), str, duration)
}

// https://stackoverflow.com/a/52075248
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

