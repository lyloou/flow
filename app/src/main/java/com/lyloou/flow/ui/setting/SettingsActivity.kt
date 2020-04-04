package com.lyloou.flow.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.model.CityHelper
import com.lyloou.flow.ui.city.CitySelectorActivity
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Ufile
import com.lyloou.flow.util.Usystem

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
            findPreference<Preference>(getString(R.string.setting_city))?.onPreferenceClickListener =
                this
            findPreference<Preference>(getString(R.string.setting_cache_size))?.onPreferenceClickListener =
                this
        }

        override fun onStart() {
            reloadSummary()
            super.onStart()
        }

        private fun reloadSummary() {
            findPreference<Preference>(getString(R.string.setting_city))?.summary =
                CityHelper.getCity()?.cityName ?: "深圳"
            findPreference<Preference>(getString(R.string.setting_cache_size))?.summary =
                Ufile.getTotalCacheSize(requireContext())
        }

        override fun onPreferenceClick(preference: Preference): Boolean {
            when (preference.key) {
                getString(R.string.setting_city) ->
                    startActivity(Intent(context, CitySelectorActivity::class.java))
                getString(R.string.setting_cache_size) -> clearCache()
            }
            return true
        }

        private fun clearCache() {
            Udialog.AlertOneItem.builder(requireContext())
                .message("要清数据吗")
                .positiveTips("是的")
                .negativeTips("不用了")
                .consumer {
                    if (!it) {
                        return@consumer
                    }
                    showTwiceWithTips()
                }
                .show()
        }

        private fun showTwiceWithTips() {
            Udialog.AlertOneItem.builder(requireContext())
                .message("清数据前，要先同步数据哦")
                .positiveTips("已同步")
                .negativeTips("再想想")
                .consumer {
                    if (!it) {
                        return@consumer
                    }
                    Usystem.toAppSetting(requireContext())
                }
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}