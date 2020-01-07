package com.lyloou.flow.module.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.lyloou.flow.common.Consumer
import com.lyloou.flow.databinding.FragmentListBinding
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
    companion object {
        const val LIMIT = 10
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentListBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(requireActivity()).get(ListViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private var isLoading: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = requireContext()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(ItemOffsetDecoration(context.dp2px(16f)))
        recyclerView.adapter = FlowDayAdapter(viewModel)
        recyclerView.addOnScrollListener(onScrollListener())
        viewModel.flowDayList.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter?.notifyDataSetChanged()
        })

        // 上拉加载更多
        swipeRefreshLayout.setOnRefreshListener {
            loadData(true, 0)
        }

        // 有数据的时候就不用加载了
        if (viewModel.flowDayList.value?.size ?: 0 == 0) {
            swipeRefreshLayout.isRefreshing = true
            loadData(true, 0)
        }
    }

    private fun onScrollListener(): OnScrollListener {
        return object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                viewModel.currentPage.value = lastVisibleItem / LIMIT + 1
                val totalItemCount = layoutManager.itemCount
                val nearBottom = totalItemCount - 4
                if (viewModel.isNoData.value != true && lastVisibleItem >= nearBottom) {
                    if (!isLoading) {
                        viewModel.listPage.value = 1 + (viewModel.listPage.value ?: 0)
                        loadData(viewModel.listPage.value!!)
                    }
                }
            }
        }
    }

    private fun loadData(page: Int) {
        loadData(false, LIMIT * page)
    }

    private fun loadData(clear: Boolean, offset: Int) {
        isLoading = true
        viewModel.loadFromNet(clear, LIMIT, offset, Consumer { count ->
            swipeRefreshLayout.isRefreshing = false
            viewModel.isNoData.value = count == 0
            isLoading = false
        })
    }
}
