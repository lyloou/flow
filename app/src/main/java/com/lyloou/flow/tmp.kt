package com.lyloou.flow

import android.graphics.Color

class tmp {
    fun invertColor(myColorString: String): Int {
        val color = myColorString.toLong(16).toInt()
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color shr 0 and 0xFF
        val invertedRed = 255 - r
        val invertedGreen = 255 - g
        val invertedBlue = 255 - b
        return Color.rgb(invertedRed, invertedGreen, invertedBlue)
    }
}