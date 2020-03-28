package com.lyloou.flow.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import com.lyloou.flow.databinding.FragmentScheduleBinding
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tasklist.TaskListPlugin
import kotlinx.android.synthetic.main.fragment_schedule.*

class ScheduleFragment : Fragment() {
    lateinit var viewModel: ScheduleViewModel
    lateinit var binding: FragmentScheduleBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(requireActivity()).get(ScheduleViewModel::class.java)
        binding = FragmentScheduleBinding.inflate(inflater)
        binding.data = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        binding.toolbar.setTitleTextColor(Color.WHITE)
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(binding.toolbar)
        appCompatActivity.supportActionBar?.let {
            it.title = resources.getString(R.string.schedule)
        }
        refreshView()
    }

    private fun refreshView() {

        val editTexts = arrayOf(editTextA, editTextB, editTextC, editTextD)
        val list = arrayOf(viewModel.a, viewModel.b, viewModel.c, viewModel.d)

        editTexts.forEachIndexed { index, editText ->
            list[index].let {
                Markwon.builder(requireContext())
                    .usePlugin(TaskListPlugin.create(requireContext()))
                    .build().setMarkdown(editText, it)
            }
        }
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
                if (viewModel.isEmpty()) {
                    toast("还没添加内容哦")
                    return true
                }
                viewModel.startNewSchedule()
                refreshView()
            }
            R.id.menu_schedule_clear -> {
                viewModel.clearSchedule()
                refreshView()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}