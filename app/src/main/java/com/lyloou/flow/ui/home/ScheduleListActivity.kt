package com.lyloou.flow.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.common.toast
import com.lyloou.flow.databinding.ActivityScheduleListBinding
import com.lyloou.flow.model.Order
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.model.toPrettyJsonString
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Uscreen
import com.lyloou.flow.util.Usystem
import com.lyloou.flow.widget.ItemOffsetDecoration
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_schedule_list.*
import kotlinx.android.synthetic.main.dialog_schedule_detail.view.*
import kotlinx.android.synthetic.main.item_toolbar.*

class ScheduleListActivity : BaseCompatActivity(), ToolbarManager, OnItemClickListener {
    private lateinit var binding: ActivityScheduleListBinding
    private lateinit var viewModel: ScheduleListViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScheduleListViewModel::class.java)

        scheduleViewModel =
            ViewModelProvider(this).get(ScheduleViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_list)
        binding.data = viewModel

        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        whiteToolbarText()
        enableHomeAsUp { onBackPressed() }
        attachToScroll(rvList)

        rvList.layoutManager = LinearLayoutManager(this)
        rvList.addItemDecoration(ItemOffsetDecoration(Uscreen.dp2Px(this, 16f)))

        val scheduleListAdapter = ScheduleListAdapter(this)
        rvList.adapter = scheduleListAdapter
        viewModel.enabledScheduleList.observe(this, Observer {
            it?.let {
                scheduleListAdapter.submitList(it)
                scheduleListAdapter.notifyDataSetChanged()
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
                startActivity(Intent(this, ScheduleSyncActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(schedule: DbSchedule, name: String) {
        showEditScheduleDialog(schedule, name)
    }

    private fun showEditScheduleDialog(
        schedule: DbSchedule,
        name: String
    ) {
        Log.i("TTAG", "before showEditScheduleDialog: $schedule")

        val dialog = BottomSheetDialog(this)
        @SuppressLint("InflateParams")
        val view = layoutInflater.inflate(R.layout.dialog_schedule_detail, null, false)
        dialog.setContentView(view)
        dialog.show()

        setBackground(view.editText, name)
        view.editText.setText(getContent(name, schedule))
        view.tvOk.setOnClickListener {
            saveContent(name, view.editText.text.toString(), schedule)
            dialog.dismiss()
        }
        view.tvCancel.setOnClickListener { dialog.dismiss() }
        view.tvClear.setOnClickListener {
            view.editText.setText("")
        }
        view.tvCopy.setOnClickListener {
            Usystem.copyString(this, view.editText.text.toString())
            toast("已复制")
        }

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

    override fun onItemMoreClick(schedule: DbSchedule) {
        Udialog.AlertMultiItem.builder(this)
            .add("复制内容") {
                Usystem.copyString(this, schedule.toPrettyJsonString())
                toast("已复制")
            }
            .add("应用到首页") {
                applyToHome(schedule)
            }
            .add("删除此项") {
                schedule.isDisabled = true
                viewModel.updateSchedule(schedule)
                toast("已删除")
            }
            .show()
    }

    override fun onItemTitleClick(schedule: DbSchedule) {
        Udialog.AlertInputDialog.builder(this)
            .title("修改标题")
            .hint("请输入名称")
            .defaultValue(schedule.title)
            .cancelable(false)
            .consumer {
                schedule.title = it
                viewModel.updateSchedule(schedule)
            }
            .show()
    }

    private fun applyToHome(schedule: DbSchedule) {
        if (scheduleViewModel.isEmpty()) {
            applyAndTips(schedule)
            return
        }

        Udialog.AlertOneItem.builder(this)
            .message("首页存在数据，是否需要保存？")
            .positiveTips("我要保存")
            .negativeTips("不用了")
            .consumer {
                if (it) {
                    scheduleViewModel.startNewSchedule()
                }
                applyAndTips(schedule)
            }
            .show()

    }

    private fun applyAndTips(schedule: DbSchedule) {
        scheduleViewModel.saveSchedule(schedule)
        onBackPressed()
        toast("已应用到首页")
    }
}
