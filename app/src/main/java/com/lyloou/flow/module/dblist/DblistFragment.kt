package com.lyloou.flow.module.dblist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyloou.flow.R
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_dblist.*

/**
 * A simple [Fragment] subclass.
 */
class DblistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dblist, container, false)
    }

    private lateinit var viewModel: DbflowViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(DbflowViewModel::class.java)

        val adapter = DbflowAdapter()
        recyclerView.apply {
            startPostponedEnterTransition()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.addItemDecoration(ItemOffsetDecoration(requireContext().dp2px(16f)))
            viewModel.dbFlowDayList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        }
    }
}
