package com.lyloou.flow.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.SPreference
import com.lyloou.flow.common.SpName

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    var enableGrayMode: Boolean by SPreference(SpName.DEFAULT.name, Key.GRAY_MODE.name, false)
}