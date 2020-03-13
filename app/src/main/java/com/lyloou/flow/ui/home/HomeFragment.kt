package com.lyloou.flow.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                R.id.editTextA -> enterMode(schedule.a)
                R.id.editTextB -> enterMode(schedule.b)
                R.id.editTextC -> enterMode(schedule.c)
                R.id.editTextD -> enterMode(schedule.d)
            }
        }
        editTextA.setOnClickListener(onClickListener)
        editTextB.setOnClickListener(onClickListener)
        editTextC.setOnClickListener(onClickListener)
        editTextD.setOnClickListener(onClickListener)
    }


    fun enterMode(data: ScheduleItem) {
        val intent = Intent(context, TodoActivity::class.java)
        intent.putExtra(Key.TODO.name, data.name)
        startActivity(intent)
    }
}