package com.lyloou.flow.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.lyloou.flow.R
import com.lyloou.flow.model.SyncStatus
import com.lyloou.flow.util.LouProgressBar
import com.lyloou.flow.widget.TitleViewPagerAdapter
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_schedule_sync.*
import kotlinx.android.synthetic.main.item_toolbar.*

class ScheduleSyncActivity : AppCompatActivity(), ToolbarManager {

    private lateinit var viewModel: ScheduleListViewModel
    private lateinit var adapterMap: MutableMap<SyncStatus, ScheduleSyncAdapter>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_sync)

        viewModel = ViewModelProviders.of(this).get(ScheduleListViewModel::class.java)

        setSupportActionBar(toolbar)
        whiteToolbarText()
        enableHomeAsUp { onBackPressed() }

        val pagerAdapter = TitleViewPagerAdapter()
        initAdapter(pagerAdapter)
        vpSchedule.adapter = pagerAdapter

        tabLayout.setupWithViewPager(vpSchedule)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val progressBar = LouProgressBar.builder(this).tips("Loading...")
        progressBar.show()
        viewModel.getAllSchedule(adapterMap, Runnable {
            progressBar.hide()
        })
    }

    private fun initAdapter(pagerAdapter: TitleViewPagerAdapter) {
        adapterMap = mutableMapOf()
        for (value in SyncStatus.values()) {
            val part = SchedulePart(this, value.desc)
            pagerAdapter.addView(part.title, part.view)
            adapterMap[value] = part.adapter
        }
    }

    override val toolbar: Toolbar
        get() = myToolbar
}
