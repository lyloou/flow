package com.lyloou.flow.ui.about

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.lyloou.flow.BuildConfig
import com.lyloou.flow.R
import com.lyloou.flow.ui.web.NormalWebViewActivity
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
            .setImage(R.mipmap.flow)
            .addItem(Element().setTitle("Version V${BuildConfig.VERSION_NAME}"))
            .setDescription(getString(R.string.about_page_desc))
            .addGroup("功能特性")
            .addItem(Element().setTitle("- 清单，计划还是要有的，万一实现了呢"))
            .addItem(Element().setTitle("- 时间流，原来时间都花在这了呢"))
            .addGroup("联系方式")
            .addEmail("lyloou6@gmail.com")
            .addItem(github("lyloou"))
            .addItem(
                website(
                    getString(mehdi.sakout.aboutpage.R.string.about_website),
                    "http://lyloou.github.io/"
                )
            )
            .addPlayStore("com.lyloou.flow")
            .addGroup("Open Source Licence")
            .addItem(website("calendarview", "https://github.com/huanghaibin-dev/CalendarView"))
            .addItem(website("glide", "https://github.com/bumptech/glide"))
            .addItem(website("commons-codec", "http://commons.apache.org/proper/commons-codec/"))
            .addItem(website("markwon", "https://github.com/noties/Markwon"))
            .addItem(website("agentweb", "https://github.com/Justson/AgentWeb"))
            .addItem(website("android-about-page", "https://github.com/noties/Markwon"))
            .addItem(getCopyRightsElement())
            .create()
    }


    private fun github(id: String): Element {
        val url = String.format("https://github.com/%s", id)
        return Element()
            .setTitle(getString(mehdi.sakout.aboutpage.R.string.about_github))
            .setIconDrawable(mehdi.sakout.aboutpage.R.drawable.about_icon_github)
            .setIconTint(mehdi.sakout.aboutpage.R.color.about_github_color)
            .setValue(id)
            .setIntent(NormalWebViewActivity.getWebIntent(this, url))
    }

    private fun website(title: String, url: String): Element {
        return Element()
            .setTitle(title)
            .setIconDrawable(mehdi.sakout.aboutpage.R.drawable.about_icon_link)
            .setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color)
            .setValue(url)
            .setIntent(NormalWebViewActivity.getWebIntent(this, url))
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

    private fun getCopyRightsElement(): Element? = Element().apply {
        val year = Calendar.getInstance()[Calendar.YEAR]
        title = String.format(getString(R.string.copy_right), year)
        iconDrawable = R.drawable.ic_very_satisfied
        iconTint = R.color.colorPrimary
        iconNightTint = android.R.color.white
        gravity = Gravity.CENTER
    }

}