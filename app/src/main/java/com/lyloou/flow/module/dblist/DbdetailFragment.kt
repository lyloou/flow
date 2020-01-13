package com.lyloou.flow.module.dblist


import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.*
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.lyloou.flow.R
import com.lyloou.flow.extension.notifyObserver
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.util.*
import com.lyloou.flow.widget.TimeLineItemDecoration
import kotlinx.android.synthetic.main.fragment_dbdetail.*

/**
 * A simple [Fragment] subclass.
 */
class DbdetailFragment : Fragment() {
    private lateinit var viewModel: DbflowViewModel
    private lateinit var day: String
    private lateinit var adapter: DbflowItemAdapter
    private var handler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initData()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dbdetail, container, false)
    }

    private fun initData() {
        viewModel = ViewModelProviders.of(requireActivity()).get(DbflowViewModel::class.java)

        day = arguments?.getString("day", Utime.today()) ?: Utime.today()
        val dbFlowDay = viewModel.getDbFlowDay(day)
        // 没有数据的时候，添加默认的
        dbFlowDay.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                viewModel.insertDbFlowDay(day)
                return@Observer
            }
            viewModel.flowItemList.value = FlowItemHelper.fromJsonArray(it.items)
        })
        viewModel.flowItemList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.default_transition).apply { duration = 300 }
            enterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.no_transition)
            val transitionName = arguments?.getString("transitionName")
            iv_header.transitionName = transitionName
        }
        Glide.with(requireContext())
            .load(ImageHelper.getBigImage(day))
            .into(iv_header)

        adapter = DbflowItemAdapter(viewModel.flowItemList)
        adapter.itemListener = getItemListener()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(TimeLineItemDecoration())
    }

    fun updateDb(vararg noDelay: Boolean) {
        if (noDelay.isNotEmpty() && noDelay[0]) {
            updateDbTask.run()
            return
        }
        delayUpdateDb()
    }

    private val updateDbTask = Runnable {
        viewModel.flowItemList.value?.let {
            viewModel.updateDbFlowItems(day, it)
        }
    }


    private fun delayUpdateDb() {
        handler.removeCallbacks(updateDbTask)
        handler.postDelayed(updateDbTask, 800)
    }

    private fun updateUiAndDb() {
        viewModel.flowItemList.notifyObserver()
        delayUpdateDb()
    }

    private fun addNewItem() {
        viewModel.flowItemList.value?.let {
            val newItem = FlowItem()
            val startArr = Utime.getValidTime(null)
            var currentTime = Utime.getTimeString(startArr[0], startArr[1])

            if (it.size > 0) {
                val item = it[0]
                // 当前时间已经存在，则不在新建
                if (currentTime == item.timeStart) {
                    showTips("该时间点已经有了一个哦")
                    return
                }
                if (!TextUtils.isEmpty(item.timeEnd)) {
                    currentTime = item.timeEnd
                }
            }
            newItem.timeStart = currentTime
            it.add(0, newItem)
            updateUiAndDb()
        }
    }

    private fun showTips(text: String) {
        Snackbar.make(recyclerView, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun getItemListener() = object : OnItemListener {
        override fun onLongClickItem(item: FlowItem) {
            Udialog.AlertMultiItem.builder(requireContext())
                .add("复制内容") { Usystem.copyString(requireContext(), item.content) }
                .add("复制全部") {
                    Usystem.copyString(
                        requireContext(), Utime.getFormatTime(item.timeStart)
                            .plus(item.timeSep)
                            .plus(Utime.getFormatTime(item.timeEnd))
                            .plus("\n")
                            .plus(item.content)
                    )
                }
                .add("删除此项") {
                    viewModel.flowItemList.value?.let {
                        it.remove(item)
                        updateUiAndDb()
                    }
                }
                .show()
        }

        override fun onClickTimeStart(item: FlowItem) {
            // 原文链接：https://blog.csdn.net/qq_17009881/article/details/75371406
            val listener = OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                item.timeStart = Utime.getTimeString(hourOfDay, minute)
                Utransfer.sortItems(viewModel.flowItemList.value)
                updateUiAndDb()
            }
            Udialog.showTimePicker(requireContext(), listener, Utime.getValidTime(item.timeStart))
        }

        override fun onClickTimeEnd(item: FlowItem) {
            val listener = OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                item.timeEnd = Utime.getTimeString(hourOfDay, minute)
                Utransfer.sortItems(viewModel.flowItemList.value)
                updateUiAndDb()
            }
            Udialog.showTimePicker(requireContext(), listener, Utime.getValidTime(item.timeEnd))
        }

        override fun onLongClickTimeStart(item: FlowItem) {
            Udialog.AlertOneItem.builder(requireContext())
                .consumer {
                    if (it) {
                        item.timeStart = null
                        Utransfer.sortItems(viewModel.flowItemList.value)
                        updateUiAndDb()
                    }
                }
                .message("清空开始时间")
                .show();
        }

        override fun onLongClickTimeEnd(item: FlowItem) {
            Udialog.AlertOneItem.builder(requireContext())
                .consumer {
                    if (it) {
                        item.timeEnd = null
                        Utransfer.sortItems(viewModel.flowItemList.value)
                        updateUiAndDb()
                    }
                }
                .message("清空结束时间")
                .show();
        }

        override fun onTextChanged(item: FlowItem, s: CharSequence) {
            item.content = s.toString()
            // 只需要更新数据即可
            updateDb()
        }

        override fun onEditTextFocused(hasFocus: Boolean, item: FlowItem) {

        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dbflow_menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = Navigation.findNavController(view!!)
        when (item.itemId) {
            R.id.view_mode -> {
                controller.navigate(R.id.action_dateFragmentRecycler_to_dateFragmentScroll)
            }

            R.id.add -> {
                addNewItem()
            }
            R.id.local_list -> {
                startActivity(Intent(context, DblistActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
