package com.lyloou.flow.ui


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.lyloou.flow.R
import com.lyloou.flow.databinding.FragmentDateBinding
import com.lyloou.flow.model.MyViewModel
import kotlinx.android.synthetic.main.fragment_date.*


/**
 * A simple [Fragment] subclass.
 */
class DateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initData()
        // Inflate the layout for this fragment
        var binding = FragmentDateBinding.inflate(inflater)
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
//        calendarView.scrollToCalendar(2018,11,11)
        Log.i("TTAG", "${calendarView.curYear}, ${calendarView.curMonth}, ${calendarView.curDay}")
        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                Log.i("TTAG", calendar.toString())
                calendar?.let { myViewModel.loadFromNet(it.toString()) }
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        day?.let { myViewModel.loadFromNet(it) }

        myViewModel.flowDay.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            scrollView.smoothScrollTo(0, 0)
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

}
