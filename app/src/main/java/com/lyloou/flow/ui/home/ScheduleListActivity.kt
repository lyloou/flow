package com.lyloou.flow.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lyloou.flow.R
import com.lyloou.flow.databinding.ActivityScheduleListBinding
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.repository.schedule.ScheduleNetWork
import com.lyloou.flow.util.Uscreen
import com.lyloou.flow.widget.ItemOffsetDecoration
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_schedule_list.*
import kotlinx.android.synthetic.main.item_toolbar.*

class ScheduleListActivity : AppCompatActivity(), ToolbarManager {
    private lateinit var binding: ActivityScheduleListBinding
    private lateinit var viewModel: ScheduleListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScheduleListViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_list)
        binding.data = viewModel

        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        toolbarTitle = resources.getString(R.string.schedule_list)
        toolbar.setTitleTextColor(Color.WHITE)
        enableHomeAsUp { onBackPressed() }
        attachToScroll(rvList)

        rvList.layoutManager = LinearLayoutManager(this)
        rvList.addItemDecoration(ItemOffsetDecoration(Uscreen.dp2Px(this, 16f)))

        val scheduleListAdapter = ScheduleListAdapter()
        rvList.adapter = scheduleListAdapter
        viewModel.dbScheduleList.observe(this, Observer {
            it?.let {
                scheduleListAdapter.submitList(it)
            }
        })
    }


    override val toolbar: Toolbar by lazy { myToolbar }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.schedule_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_schedule_sync -> {
                if (UserHelper.isNotLogin(this)) return true

                WorkManager.getInstance(this)
                    .enqueue(OneTimeWorkRequestBuilder<ScheduleNetWork>().build())

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
