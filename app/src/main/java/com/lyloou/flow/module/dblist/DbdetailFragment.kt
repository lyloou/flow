package com.lyloou.flow.module.dblist


import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyloou.flow.R
import com.lyloou.flow.extension.notifyObserver
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.repository.DbFlowDay
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Usystem
import com.lyloou.flow.util.Utime
import com.lyloou.flow.util.Utransfer
import com.lyloou.flow.widget.TimeLineItemDecoration
import kotlinx.android.synthetic.main.fragment_dbdetail.*

/**
 * A simple [Fragment] subclass.
 */
class DbdetailFragment : Fragment() {
    private lateinit var viewModel: DbflowViewModel
    private lateinit var dbFlowDay: LiveData<DbFlowDay>
    private var handler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dbdetail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(DbflowViewModel::class.java)

        val adapter = DbflowItemAdapter(viewModel)
        adapter.itemListener = getItemListener()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(TimeLineItemDecoration())

        viewModel.flowItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.notifyDataSetChanged()
            }
        })

        with(arguments) {
            if (this != null) {
                viewModel.day.value = arguments!!.getString("day", Utime.today())
            } else {
                if (viewModel.day.value == null) {
                    viewModel.day.value = Utime.today()
                }
            }
        }


        dbFlowDay = viewModel.getDbFlowDay(viewModel.day.value.toString())
        dbFlowDay.observe(viewLifecycleOwner, Observer {
            // 没有数据的时候，添加默认的
            dbFlowDay.value ?: viewModel.insertDbFlowDay()
            viewModel.flowItems.value =
                FlowItemHelper.fromJsonArray(dbFlowDay.value?.items)
        })
    }

    fun updateDb(vararg now: Boolean) {
        if (now.isNotEmpty() && now[0]) {
            updateDbTask.run()
            return
        }
        delayUpdate()
    }

    private val updateDbTask = Runnable {
        dbFlowDay.value?.let {
            it.items = FlowItemHelper.toJsonArray(viewModel.flowItems.value)
            Log.i("TTAG", "items: ${it.items}");
            viewModel.updateDbFlowData(it)
        }

    }


    private fun delayUpdate() {
        handler.removeCallbacks(updateDbTask)
        handler.postDelayed(updateDbTask, 800)
    }

    private fun updateUiAndDb() {
        viewModel.flowItems.notifyObserver()
        delayUpdate()
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
                    val value = viewModel.flowItems.value
                    value?.remove(item)
                }
                .show()
        }

        override fun onClickTimeStart(item: FlowItem) {
            // 原文链接：https://blog.csdn.net/qq_17009881/article/details/75371406
            val listener = OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                item.timeStart = Utime.getTimeString(hourOfDay, minute)
                Utransfer.sortItems(viewModel.flowItems.value)
                updateUiAndDb()
            }
            Udialog.showTimePicker(requireContext(), listener, Utime.getValidTime(item.timeStart))
        }

        override fun onClickTimeEnd(item: FlowItem) {
            val listener = OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                item.timeEnd = Utime.getTimeString(hourOfDay, minute)
                Utransfer.sortItems(viewModel.flowItems.value)
                updateUiAndDb()
            }
            Udialog.showTimePicker(requireContext(), listener, Utime.getValidTime(item.timeEnd))
        }

        override fun onLongClickTimeStart(item: FlowItem) {
            Udialog.AlertOneItem.builder(requireContext())
                .consumer {
                    if (it) {
                        item.timeStart = null
                        Utransfer.sortItems(viewModel.flowItems.value)
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
                        Utransfer.sortItems(viewModel.flowItems.value)
                        updateUiAndDb()
                    }
                }
                .message("清空结束时间")
                .show();
        }

        override fun onTextChanged(item: FlowItem, s: CharSequence) {
            item.content = s.toString()
            // 这个时候文字已经显示到屏幕了，不需要再通知 liveData 刷新屏幕了
            // 只需要更新数据即可
            updateDb()

        }

        override fun onEditTextFocused(hasFocus: Boolean, item: FlowItem) {

        }

    }

}
