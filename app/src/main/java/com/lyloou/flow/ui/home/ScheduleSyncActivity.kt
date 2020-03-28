package com.lyloou.flow.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.lyloou.flow.R
import com.lyloou.flow.widget.TitleViewPagerAdapter
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_schedule_sync.*
import kotlinx.android.synthetic.main.fragment_list.tabLayout
import kotlinx.android.synthetic.main.item_toolbar.*

class ScheduleSyncActivity : AppCompatActivity(), ToolbarManager {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_sync)

        setSupportActionBar(toolbar)
        whiteToolbarText()
        enableHomeAsUp { onBackPressed() }
        val pagerAdapter = TitleViewPagerAdapter()
        initAdapter(pagerAdapter)
        vpSchedule.adapter = pagerAdapter

        tabLayout.setupWithViewPager(vpSchedule)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

    }

    private fun initAdapter(pagerAdapter: TitleViewPagerAdapter) {
        for (value in SchedulePart.Type.values()) {
            val part = SchedulePart(this, value.desc)
            pagerAdapter.addView(part.title, part.view)
        }
    }

    override val toolbar: Toolbar
        get() = myToolbar
}
