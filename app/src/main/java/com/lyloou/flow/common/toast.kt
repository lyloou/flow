package com.lyloou.flow.common

import android.widget.Toast
import com.lyloou.flow.App

fun toast(msg: String) {
    Toast.makeText(App.instance, msg, Toast.LENGTH_SHORT).show()
}