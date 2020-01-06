package com.lyloou.flow.extension

import android.content.Context


fun Context.d2p(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (scale * dpValue + 0.5f).toInt()
}