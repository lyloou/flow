package com.lyloou.flow.extension

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(
    val context: Context,
    val name: String,
    private val default: T,
    private val spName: String = "default"
) : ReadWriteProperty<Any?, T> {
    val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun <T> findPreference(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default) ?: default
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        @Suppress("UNCHECKED_CAST")
        res as T
    }

    private fun <U> putPreference(name: String, value: U) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }
}

fun <T> Preference<T>.remove(key: String) {
    prefs.edit().remove(key).apply()
}

fun <T> Preference<T>.clear() {
    prefs.edit().clear().apply()
}