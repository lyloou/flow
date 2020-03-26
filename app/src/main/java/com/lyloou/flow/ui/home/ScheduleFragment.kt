package com.lyloou.flow.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import com.lyloou.flow.common.Key
import com.lyloou.flow.model.Order
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.model.ScheduleHelper
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tasklist.TaskListPlugin
import kotlinx.android.synthetic.main.fragment_schedule.*

class ScheduleFragment : Fragment(), View.OnClickListener {
    lateinit var viewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(requireActivity()).get(ScheduleViewModel::class.java)
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
    }

    private fun initToolbar() {
        toolbar.setTitleTextColor(Color.WHITE)
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.let {
            it.title = resources.getString(R.string.schedule)
        }
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private lateinit var schedule: Schedule
    private fun initView() {
        schedule = ScheduleHelper.getSchedule()
        val editTexts = arrayOf(editTextA, editTextB, editTextC, editTextD)
        val list = arrayOf(schedule.a, schedule.b, schedule.c, schedule.d)

        editTexts.forEachIndexed { index, editText ->
            list[index]?.let {
                Markwon.builder(requireContext())
                    .usePlugin(TaskListPlugin.create(requireContext()))
                    .build().setMarkdown(editText, it)
            }
        }

        textViewA.setOnClickListener(this)
        textViewB.setOnClickListener(this)
        textViewC.setOnClickListener(this)
        textViewD.setOnClickListener(this)
    }

    private fun enterMode(name: String) {
        val intent = Intent(context, ScheduleActivity::class.java)
        intent.putExtra(Key.SCHEDULE.name, name)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.flow_home, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_schedule_list -> {
                startActivity(Intent(requireContext(), ScheduleListActivity::class.java))
            }
            R.id.menu_schedule_new -> {
                viewModel.startNewSchedule()
                initView()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(it: View?) {
        when (it?.id) {
            R.id.textViewA -> enterMode(Order.A.name)
            R.id.textViewB -> enterMode(Order.B.name)
            R.id.textViewC -> enterMode(Order.C.name)
            R.id.textViewD -> enterMode(Order.D.name)
        }
    }

}