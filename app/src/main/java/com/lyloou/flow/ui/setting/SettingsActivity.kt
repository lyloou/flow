package com.lyloou.flow.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.google.gson.reflect.TypeToken
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.model.City
import com.lyloou.flow.model.gson
import java.nio.charset.Charset

class SettingsActivity : BaseCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        Log.i("TTAG", "lp: 2222222222");
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingsFragment()
            )
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val lp: ListPreference? = findPreference("reply")
            Log.i("TTAG", "lp: ${lp}");
            val cityFile = requireActivity().assets.open("city.json")
            Log.i("TTAG", "cityFile: $cityFile");
            val size = cityFile.available()
            Log.i("TTAG", "size: $cityFile");
            val buffer = ByteArray(size)
            cityFile.read(buffer)
            cityFile.close()
            val cityStr = String(buffer, Charset.forName("UTF-8"))

            val type = object : TypeToken<List<City>>() {}.type
            val list: List<City> = gson.fromJson(cityStr, type)
            Log.i("TTAG", "list: $list");
            var toTypedArray = list.map { it.cityName }.toTypedArray()
            lp?.entries = toTypedArray
            lp?.entryValues = list.map { it.cityCode }.toTypedArray()
            lp?.setValueIndex(2)
            lp?.setOnPreferenceChangeListener { preference, newValue ->
                Log.i(
                    "TTAG",
                    "newvalue: $newValue"
                )
                true
            }
        }
    }
}