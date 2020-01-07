package com.lyloou.flow.module.setting

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

class SettingViewModel(application: Application, handle: SavedStateHandle) :
    AndroidViewModel(application) {
    private val handle: SavedStateHandle

    init {
        if (!handle.contains(KEY_KALENDAR_MODE)) {
            handle.set(
                KEY_KALENDAR_MODE,
                KalendarMode.of(sp().getInt(SAVE_SP_LIST_NAME, 0))
            )
        }
        this.handle = handle

    }

    companion object {
        const val KEY_KALENDAR_MODE = "key_kalendar"
        const val SAVE_SP_LIST_NAME = "SAVE_SP_LIST_NAME"
    }

    fun getMode(): MutableLiveData<KalendarMode> {
        return handle.getLiveData(KEY_KALENDAR_MODE)
    }

    fun saveMode(mode: KalendarMode) {
        sp().edit().putInt(KEY_KALENDAR_MODE, mode.codeValue).apply()
    }

    private fun sp(): SharedPreferences {
        return getApplication<Application>().getSharedPreferences(
            SAVE_SP_LIST_NAME,
            Context.MODE_PRIVATE
        )
    }
}