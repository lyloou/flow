package com.lyloou.flow.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import com.lyloou.flow.databinding.ActivityScheduleListBinding
import com.lyloou.flow.model.Order
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.model.toJsonString
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.ScheduleNetWork
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Uscreen
import com.lyloou.flow.util.Usystem
import com.lyloou.flow.widget.ItemOffsetDecoration
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_schedule_list.*
import kotlinx.android.synthetic.main.dialog_schedule_detail.view.*
import kotlinx.android.synthetic.main.item_toolbar.*

class ScheduleListActivity : AppCompatActivity(), ToolbarManager, OnItemClickListener {
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

        val scheduleListAdapter = ScheduleListAdapter(this)
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

    override fun onItemClick(schedule: DbSchedule, name: String, position: Int) {
        showEditScheduleDialog(schedule, name, position)
    }

    private fun showEditScheduleDialog(
        schedule: DbSchedule,
        name: String,
        position: Int
    ) {
        val dialog = BottomSheetDialog(this)
        @SuppressLint("InflateParams")
        val view = layoutInflater.inflate(R.layout.dialog_schedule_detail, null, false)
        dialog.setContentView(view)
        dialog.show()

        setBackground(view.editText, name)
        view.editText.setText(getContent(name, schedule))
        view.tvOk.setOnClickListener {
            saveContent(name, view.editText.text.toString(), schedule)
            rvList.adapter?.notifyItemChanged(position)
            dialog.dismiss()
        }
        view.tvCancel.setOnClickListener { dialog.dismiss() }
    }

    private fun saveContent(name: String, content: String, schedule: DbSchedule) {
        when (name) {
            Order.A.name -> schedule.a = content
            Order.B.name -> schedule.b = content
            Order.C.name -> schedule.c = content
            Order.D.name -> schedule.d = content
        }
        viewModel.updateSchedule(schedule)
    }

    private fun setBackground(editText: EditText, key: String) {
        editText.setBackgroundResource(
            when (key) {
                Order.A.name -> R.drawable.item_schedule_a_bg
                Order.B.name -> R.drawable.item_schedule_b_bg
                Order.C.name -> R.drawable.item_schedule_c_bg
                Order.D.name -> R.drawable.item_schedule_d_bg
                else -> 0
            }
        )
    }

    private fun getContent(name: String, schedule: DbSchedule): String {
        return when (name) {
            Order.A.name -> schedule.a ?: ""
            Order.B.name -> schedule.b ?: ""
            Order.C.name -> schedule.c ?: ""
            Order.D.name -> schedule.d ?: ""
            else -> throw IllegalArgumentException("无效的名称")
        }
    }

    override fun onItemLongClick(schedule: DbSchedule) {
        Udialog.AlertMultiItem.builder(this)
            .add("复制内容") { Usystem.copyString(this, schedule.toJsonString()) }
            .add("删除此项") {
                schedule.isDisabled = true
                viewModel.updateSchedule(schedule)
                toast("已删除")
            }
            .show()
    }
}
