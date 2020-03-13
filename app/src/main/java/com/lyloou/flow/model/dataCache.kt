package com.lyloou.flow.model

import android.app.Application
import com.lyloou.flow.App
import com.lyloou.flow.common.SpName

object Cache {

    fun clear() {
        val preferences = App.instance
            .getSharedPreferences(SpName.USER.name, Application.MODE_PRIVATE)
        preferences.edit().clear().apply()
    }
}