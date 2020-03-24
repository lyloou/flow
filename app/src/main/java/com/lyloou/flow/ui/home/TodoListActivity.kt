package com.lyloou.flow.ui.home

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lyloou.flow.R
import com.lyloou.flow.databinding.ActivityTodoListBinding
import com.lyloou.flow.repository.schedule.ScheduleNetWork
import com.lyloou.flow.util.Uscreen
import com.lyloou.flow.widget.ItemOffsetDecoration
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_todo_list.*
import kotlinx.android.synthetic.main.item_toolbar.*

class TodoListActivity : AppCompatActivity(), ToolbarManager {
    private lateinit var binding: ActivityTodoListBinding
    private lateinit var viewModel: TodoListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TodoListViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_list)
        binding.data = viewModel

        initView()
    }

    private fun initView() {
        toolbarTitle = resources.getString(R.string.todo)
        toolbar.setTitleTextColor(Color.WHITE)
        enableHomeAsUp { onBackPressed() }
        attachToScroll(rvTodoList)

        rvTodoList.layoutManager = LinearLayoutManager(this)
        rvTodoList.addItemDecoration(ItemOffsetDecoration(Uscreen.dp2Px(this, 16f)))

        val todoListAdapter = TodoListAdapter()
        rvTodoList.adapter = todoListAdapter
        viewModel.dbScheduleList.observe(this, Observer {
            it?.let {
                todoListAdapter.submitList(it)
            }
        })
        //
        WorkManager.getInstance(this).enqueue(OneTimeWorkRequestBuilder<ScheduleNetWork>().build())
    }


    override val toolbar: Toolbar by lazy { myToolbar }
}
