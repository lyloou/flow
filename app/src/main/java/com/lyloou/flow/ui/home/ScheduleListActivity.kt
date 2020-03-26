package com.lyloou.flow.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
        toolbarTitle = resources.getString(R.string.schedule)
        toolbar.setTitleTextColor(Color.WHITE)
        enableHomeAsUp { onBackPressed() }
        attachToScroll(rvList)

        rvList.layoutManager = LinearLayoutManager(this)
        rvList.addItemDecoration(ItemOffsetDecoration(Uscreen.dp2Px(this, 16f)))

        val scheduleListAdapter = ScheduleListAdapter()
        rvList.adapter = scheduleListAdapter
        viewModel.dbScheduleList.observe(this, Observer {
            it?.let {
                Log.i("TTAG", "todoList: ${it}");
                scheduleListAdapter.submitList(it)
            }
        })
        //
        WorkManager.getInstance(this).enqueue(OneTimeWorkRequestBuilder<ScheduleNetWork>().build())
    }


    override val toolbar: Toolbar by lazy { myToolbar }
}
