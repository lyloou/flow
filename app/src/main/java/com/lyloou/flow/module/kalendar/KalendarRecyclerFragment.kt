package com.lyloou.flow.module.kalendar


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.lyloou.flow.R
import com.lyloou.flow.databinding.FragmentKalendarRecycleBinding
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.module.list.ListActivity
import com.lyloou.flow.util.Utime
import com.lyloou.flow.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_kalendar_recycle.*
import kotlinx.android.synthetic.main.fragment_kalendar_scroll.calendarView


/**
 * A simple [Fragment] subclass.
 */
class KalendarRecyclerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initData()
        // Inflate the layout for this fragment
        val binding = FragmentKalendarRecycleBinding.inflate(inflater)
        binding.data = kalendarViewModel
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        initView()
    }

    private lateinit var kalendarViewModel: KalendarViewModel
    private fun initData() {
        kalendarViewModel = ViewModelProviders.of(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(KalendarViewModel::class.java)
    }

    private fun initView() {
        val day = kalendarViewModel.flow.value?.day
        day?.let {
            val date = Utime.transferTwo(day)
            val calendar = java.util.Calendar.getInstance()
            calendar.time = date
            calendarView.scrollToCalendar(
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH) + 1,
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            )
        }

        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                if (isClick) {
                    calendar?.let { kalendarViewModel.loadFromNet(it.toString()) }
                }
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {

            }

        })


        val context = requireContext()
        val flowItemAdapter =
            KalendarAdapter(kalendarViewModel)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(ItemOffsetDecoration(context.dp2px(16f)))
        recyclerView.adapter = flowItemAdapter

        kalendarViewModel.flow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            flowItemAdapter.notifyHeaderItem()
            flowItemAdapter.submitList(kalendarViewModel.flow.value?.items)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.flow_menu, menu);
        val searchView: SearchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = Navigation.findNavController(view!!)
        when (item.itemId) {
            R.id.view_mode -> {
                controller.navigate(R.id.action_dateFragmentRecycler_to_dateFragmentScroll)
            }
            R.id.about -> {
                controller.navigate(R.id.action_dateFragment_to_aboutFragment)
            }
            R.id.add -> {
            }
            R.id.local_list -> {
                startActivity(Intent(context, ListActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
