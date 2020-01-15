package com.lyloou.flow.ui.setting

enum class KalendarMode(private val code: Int) {

    RECYCLE(0), SCROLL(1);

    val codeValue: Int
        get() = this.code

    companion object {
        fun of(code: Int): KalendarMode {
            return values()[code]
        }
    }
}