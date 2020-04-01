package com.lyloou.flow.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.common.toast
import com.lyloou.flow.model.CityHelper
import com.lyloou.flow.model.ScheduleHelper
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.model.UserPasswordHelper
import com.lyloou.flow.repository.FlowDataCleaner
import com.lyloou.flow.ui.city.CitySelectorActivity

class SettingsActivity : BaseCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingsFragment()
            )
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.setting_preferences, rootKey)
            initPreference()
        }

        private fun initPreference() {
            findPreference<Preference>("city")?.onPreferenceClickListener = this
            findPreference<Preference>("clearUser")?.onPreferenceClickListener = this
            findPreference<Preference>("clearSchedule")?.onPreferenceClickListener = this
            findPreference<Preference>("clearFlow")?.onPreferenceClickListener = this
            findPreference<Preference>("clearAll")?.onPreferenceClickListener = this
        }

        override fun onStart() {
            reLoadCity()
            super.onStart()
        }

        private fun reLoadCity() {
            findPreference<Preference>("city")?.summary = CityHelper.getCity()?.cityName ?: "深圳"
        }

        override fun onPreferenceClick(preference: Preference): Boolean {
            when (preference.key) {
                "city" -> startActivity(Intent(context, CitySelectorActivity::class.java))
                "clearUser" -> UserHelper.clearUser()
                "clearSchedule" -> ScheduleHelper.clearSchedule()
                "clearFlow" -> FlowDataCleaner.execute()
                "clearAll" -> clearAll()
            }

            if (preference.key != "city") {
                toast("已清除")
            }
            return true
        }

        private fun clearAll() {
            UserHelper.clearUser()
            CityHelper.clearCity()
            UserPasswordHelper.clearUserPassword()
            ScheduleHelper.clearSchedule()
            FlowDataCleaner.execute()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}