package com.lyloou.flow.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.model.CityHelper
import com.lyloou.flow.ui.city.CitySelectorActivity
import com.lyloou.flow.util.Uapp
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
        lateinit var viewModel: SettingsViewModel

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
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
            reloadPreferenceValue()
            super.onStart()
        }

        private fun reloadPreferenceValue() {
            findPreference<Preference>(getString(R.string.setting_city))?.summary =
                CityHelper.getCity()?.cityName ?: "深圳"
            findPreference<Preference>(getString(R.string.setting_cache_size))?.summary =
                Ufile.getTotalCacheSize(requireContext())

            val enableGrayMode =
                findPreference<SwitchPreference>(getString(R.string.setting_gray_mode))
            enableGrayMode?.setDefaultValue(viewModel.enableGrayMode)
            enableGrayMode?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    handleGrayModeChangListener(newValue)
                    true
                }

        }

        private fun handleGrayModeChangListener(newValue: Any?) {
            viewModel.enableGrayMode = newValue as? Boolean ?: false
            Udialog.AlertOneItem.builder(context)
                .message("部分页面需重启后生效")
                .positiveTips("立即重启")
                .negativeTips("稍后重启")
                .consumer {
                    if (it) {
                        Uapp.restartApp(context)
                    }
                }
                .show()
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