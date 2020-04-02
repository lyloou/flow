package com.lyloou.flow.ui.about

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.lyloou.flow.BuildConfig
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getView())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getView(): View {
        simulateDayNight(3)

        return AboutPage(this)
            .isRTL(false)
            .setImage(R.mipmap.ic_launcher)
            .addItem(Element().setTitle("Version V${BuildConfig.VERSION_NAME}"))
            .addItem(Element().setTitle("Advertise with us"))
            .addGroup("Connect with us")
            .addEmail("lyloou6@gmail.com")
            .addWebsite("http://lyloou.github.io/")
            .addPlayStore("com.lyloou.flow")
            .addGitHub("lyloou")
            .addItem(getCopyRightsElement())
            .create()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun simulateDayNight(currentSetting: Int) {
        val day = 0
        val night = 1
        val flowSystem = 3
        val currentNightMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)
        if (currentSetting == day && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else if (currentSetting == night && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (currentSetting == flowSystem) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun getCopyRightsElement(): Element? {
        val copyRightsElement = Element()
        val copyrights =
            String.format(getString(R.string.copy_right), Calendar.getInstance()[Calendar.YEAR])
        copyRightsElement.title = copyrights
        copyRightsElement.iconDrawable = R.drawable.ic_very_satisfied
        copyRightsElement.iconTint = R.color.colorPrimary
        copyRightsElement.iconNightTint = android.R.color.white
        copyRightsElement.gravity = Gravity.CENTER
        copyRightsElement.onClickListener = View.OnClickListener { toast(copyrights) }
        return copyRightsElement
    }

}