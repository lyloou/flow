package com.lyloou.flow.module.dblist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyloou.flow.R
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.util.Utime
import com.lyloou.flow.widget.TimeLineItemDecoration
import kotlinx.android.synthetic.main.fragment_dbdetail.*

/**
 * A simple [Fragment] subclass.
 */
class DbdetailFragment : Fragment() {
    private lateinit var viewModel: DbflowViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dbdetail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DbflowViewModel::class.java)


        with(arguments) {
            if (this != null) {
                viewModel.day.value = arguments!!.getString("day", Utime.today())
            } else {
                if (viewModel.day.value == null) {
                    viewModel.day.value = Utime.today()
                }
            }
        }

        Log.i("TTAG", "day=${viewModel.day.value}")
        val flowDay = viewModel.dbFlowDay
        flowDay.observe(viewLifecycleOwner, Observer {
            // 没有数据的时候，添加默认的
            flowDay.value ?: viewModel.insertDbFlowDay()
            viewModel.flowItems.value = FlowItemHelper.fromJsonArray(flowDay.value?.items)
            Log.i("TTAG", "viewModel.flowItems.value=${viewModel.flowItems.value}")
        })


        val adapter = DbflowItemAdapter(viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(TimeLineItemDecoration())
        viewModel.flowItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.notifyDataSetChanged()
            }
        })

    }
}
