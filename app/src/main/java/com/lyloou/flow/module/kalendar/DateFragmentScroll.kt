package com.lyloou.flow.module.kalendar


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.lyloou.flow.R
import com.lyloou.flow.databinding.FragmentDateScrollBinding
import com.lyloou.flow.util.Utime
import kotlinx.android.synthetic.main.fragment_date_scroll.*


/**
 * A simple [Fragment] subclass.
 */
class DateFragmentScroll : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initData()
        // Inflate the layout for this fragment
        val binding = FragmentDateScrollBinding.inflate(inflater)
        binding.data = myViewModel
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        initView()
    }

    private lateinit var myViewModel: MyViewModel
    private fun initData() {
        myViewModel = ViewModelProviders.of(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(MyViewModel::class.java)
    }

    private fun initView() {
        val day = myViewModel.flowDay.value?.day
        day?.let {
            myViewModel.loadFromNet(it)

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
                calendar?.let { myViewModel.loadFromNet(it.toString()) }
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {

            }

        })


        myViewModel.flowDay.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            scrollView.smoothScrollTo(0, 0)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.flow_menu, menu);
        menu.findItem(R.id.view_mode).title = "Recycler Mode"
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
                controller.navigateUp()
            }
            R.id.about -> {
                controller.navigate(R.id.aboutFragment)
            }
            R.id.list -> {
                controller.navigate(R.id.listFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
