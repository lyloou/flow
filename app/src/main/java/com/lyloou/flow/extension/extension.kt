package com.lyloou.flow.extension

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken
import com.lyloou.flow.model.gson
import java.text.DateFormat
import java.util.*


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

fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}

fun Date.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}

fun View.slideExit() {
    if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.slideEnter() {
    if (translationY < 0f) animate().translationY(0f)
}

fun <T> String?.toTypedList(): List<T> {
    if (this == null) {
        return emptyList()
    }
    val type = object : TypeToken<List<T>>() {}.type
    return gson.fromJson(this, type)
}