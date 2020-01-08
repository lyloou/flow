package com.lyloou.flow.module.dblist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.util.Utime
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

        arguments?.let {
            val day = it.getString("day", Utime.today())
            Log.i("TTAG", "day=${day}")
            val flowDay = viewModel.getDbFlowDay(day)
            flowDay.observe(viewLifecycleOwner, Observer {
                Log.i("TTAG", "flowday=${flowDay.value?.day}")
                val prettyText =
                    FlowItemHelper.toPrettyText(FlowItemHelper.fromJsonArray(flowDay.value?.items))
                textView.text = "${flowDay.value?.day} \n $prettyText"
            })
        }
    }
}
