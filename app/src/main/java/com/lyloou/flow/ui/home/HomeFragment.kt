package com.lyloou.flow.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lyloou.flow.R
import com.lyloou.flow.common.Key
import com.lyloou.flow.model.ScheduleHelper
import com.lyloou.flow.model.ScheduleItem
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tasklist.TaskListPlugin
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
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
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.title = resources.getString(R.string.event);
    }

    override fun onStart() {
        super.onStart()
        val schedule = ScheduleHelper.getSchedule()
        val editTexts = arrayOf(editTextA, editTextB, editTextC, editTextD)
        val datas = arrayOf(
            schedule.a,
            schedule.b,
            schedule.c,
            schedule.d
        )

        editTexts.forEachIndexed { index, editText ->
            Markwon.builder(context!!)
                .usePlugin(TaskListPlugin.create(context!!))
                .build().setMarkdown(editText, datas[index].content);
        }

        val onClickListener = View.OnClickListener {
            when (it.id) {
                R.id.textViewA -> enterMode(schedule.a)
                R.id.textViewB -> enterMode(schedule.b)
                R.id.textViewC -> enterMode(schedule.c)
                R.id.textViewD -> enterMode(schedule.d)
            }
        }
        textViewA.setOnClickListener(onClickListener)
        textViewB.setOnClickListener(onClickListener)
        textViewC.setOnClickListener(onClickListener)
        textViewD.setOnClickListener(onClickListener)

        toolbar.title = "HOME"
        toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun enterMode(data: ScheduleItem) {
        val intent = Intent(context, TodoActivity::class.java)
        intent.putExtra(Key.TODO.name, data.name)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.flow_home, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

}