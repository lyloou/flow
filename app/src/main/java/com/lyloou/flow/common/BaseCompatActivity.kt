package com.lyloou.flow.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.lyloou.flow.R
import com.lyloou.flow.ui.setting.SettingsViewModel
import com.lyloou.flow.widget.GrayFrameLayout


@SuppressLint("Registered")
open class BaseCompatActivity : AppCompatActivity() {
    lateinit var settingViewModel: SettingsViewModel
    protected lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        settingViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    // https://mp.weixin.qq.com/s/8fTWLYaPhi0to47EUmFd7A
    // 网页端，加入css： html {filter:progid:DXImageTransform.Microsoft.BasicImage(grayscale=1);-webkit-filter: grayscale(100%);}
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        // 启用灰色模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (settingViewModel.enableGrayMode) {
                // https://stackoverflow.com/questions/22192291/how-to-change-the-status-bar-color-in-android
                window.statusBarColor = ContextCompat.getColor(this, R.color.gray)
            }
        }

        if (settingViewModel.enableGrayMode && "FrameLayout" == name) {
            val count: Int = attrs.attributeCount
            for (i in 0 until count) {
                val attributeName: String = attrs.getAttributeName(i)
                val attributeValue: String = attrs.getAttributeValue(i)
                if (attributeName == "id") {
                    val id = attributeValue.substring(1).toInt()
                    val idVal = resources.getResourceName(id)
                    if ("android:id/content" == idVal) {
                        return GrayFrameLayout(context, attrs)
                    }
                }
            }
        }
        return super.onCreateView(name, context, attrs)
    }
}