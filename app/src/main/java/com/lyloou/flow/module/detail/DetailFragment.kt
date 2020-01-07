package com.lyloou.flow.module.detail


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lyloou.flow.R
import com.lyloou.flow.databinding.FragmentDetailBinding
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.model.FlowDay
import com.lyloou.flow.module.list.ListViewModel
import com.lyloou.flow.util.ImageHelper
import com.lyloou.flow.util.Utime
import com.lyloou.flow.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel

    private lateinit var flowDay: FlowDay
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listViewModel = ViewModelProviders.of(requireActivity()).get(ListViewModel::class.java)

        val position =
            arguments?.getInt(resources.getString(R.string.arg_key_list_to_detail), -1) ?: -1
        if (position == -1) {
            flowDay = FlowDay(
                Utime.today(),
                mutableListOf()
            )
        } else {
            flowDay = listViewModel.flowDayList.value?.get(position) ?: FlowDay(
                Utime.today(),
                mutableListOf()
            )
        }
        // Inflate the layout for this fragment
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.data = listViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = requireContext()
        val flowItemAdapter = FlowAdapter(requireContext())
        flowItemAdapter.setList(flowDay.items)
        Log.i("TTAG", "flowDay=${flowDay.items}")
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(ItemOffsetDecoration(context.dp2px(16f)))
        recyclerView.adapter = flowItemAdapter
        flowItemAdapter.notifyDataSetChanged()
        Glide.with(context).load(ImageHelper.getBigImage(flowDay.day)).into(iv_header)
    }
}
