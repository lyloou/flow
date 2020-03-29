package com.lyloou.flow.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.lyloou.flow.R
import com.lyloou.flow.common.Consumer
import com.lyloou.flow.common.toast
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

        loadData()
    }

    private fun loadData(consumer: Consumer<String> = Consumer { }) {
        val progressBar = LouProgressBar.builder(this).tips("Loading...")
        progressBar.show()
        viewModel.getAllSchedule(adapterMap, Consumer {
            progressBar.hide()
            // 给标题加上角标
            val statusArr = SyncStatus.values()
            for (i in statusArr.indices) {
                val syncStatus = statusArr[i]
                updateBadge(syncStatus, it[syncStatus])
            }
        })
    }

    private fun updateBadge(syncStatus: SyncStatus, count: Int?) {
        val tab = tabLayout.getTabAt(syncStatus.ordinal)
        val countText = if (count != null && count > 0) "(+$count)" else ""
        tab?.text = syncStatus.desc + countText
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.flow_sync, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sync -> syncCurrent()
            R.id.sync_all -> {
                viewModel.count.observe(this, Observer {
                    if (it == 0) {
                        loadData(Consumer { toast("Done") })
                    }
                })
                viewModel.syncAll(adapterMap)

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun syncCurrent() {
        val syncStatus = SyncStatus.values()[vpSchedule.currentItem]
        val consumer = Consumer<SyncResult> {
            toast(it.msg)
            updateBadge(syncStatus, it.remain)
        }

        when (SyncStatus.values()[vpSchedule.currentItem]) {
            SyncStatus.LOCAL_ADD -> {
                viewModel.doLocalAdd(adapterMap, consumer)
            }
            SyncStatus.REMOTE_ADD -> viewModel.doRemoteAdd(adapterMap, consumer)
            SyncStatus.LOCAL_CHANGE -> viewModel.doLocalChange(adapterMap, consumer)
            SyncStatus.REMOTE_CHANGE -> viewModel.doRemoteChange(adapterMap, consumer)
            SyncStatus.LOCAL_DELETE -> viewModel.doLocalDelete(adapterMap, consumer)
            SyncStatus.ALL_CHANGE -> viewModel.doAllChange(adapterMap, consumer)
        }
    }
}
