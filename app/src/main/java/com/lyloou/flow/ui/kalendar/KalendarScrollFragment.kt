package com.lyloou.flow.ui.kalendar


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.lyloou.flow.R
import com.lyloou.flow.databinding.FragmentKalendarScrollBinding
import kotlinx.android.synthetic.main.fragment_kalendar_scroll.*


/**
 * A simple [Fragment] subclass.
 */
class KalendarScrollFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        kalendarViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(KalendarViewModel::class.java)
        // Inflate the layout for this fragment
        val binding = FragmentKalendarScrollBinding.inflate(inflater)
        binding.data = kalendarViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        showBackButton()
        initView()
    }

    private fun setTitle(title: String) {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.let {
                it.title = title
            }
        }
    }

    private fun showBackButton() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private lateinit var kalendarViewModel: KalendarViewModel

    private fun initView() {
        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                calendar?.let {
                    kalendarViewModel.loadFromNet(it.toString())
                }
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {

            }
        })


        kalendarViewModel.flow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            setTitle(it.day)
            scrollView.smoothScrollTo(0, 0)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.flow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = Navigation.findNavController(view!!)
        when (item.itemId) {
            R.id.about -> {
                controller.navigate(R.id.aboutFragment)
            }
            android.R.id.home -> {
                requireActivity().finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
