package com.lyloou.flow.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import com.lyloou.flow.common.Key
import com.lyloou.flow.databinding.ActivityScheduleDetailBinding
import com.lyloou.flow.model.Order
import kotlinx.android.synthetic.main.activity_schedule_detail.*

class ScheduleDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ScheduleDetailViewModel
    private lateinit var binding: ActivityScheduleDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_detail)
        viewModel =
            ViewModelProviders.of(this).get(ScheduleDetailViewModel::class.java)

        binding.data = viewModel
        binding.lifecycleOwner = this

        viewModel.name.observe(this, Observer {
            viewModel.refreshContent()
        })
        val key = intent.getStringExtra(Key.SCHEDULE.name)
        viewModel.name.value = key
        viewModel.content.observe(this, Observer {
            viewModel.save()
        })
        initEditText(key)
        ivClose.setOnClickListener { onBackPressed() }
    }

    private fun initEditText(key: String?) {
        editText.setBackgroundResource(
            when (key) {
                Order.A.name -> R.drawable.item_schedule_a_bg
                Order.B.name -> R.drawable.item_schedule_b_bg
                Order.C.name -> R.drawable.item_schedule_c_bg
                Order.D.name -> R.drawable.item_schedule_d_bg
                else -> 0
            }
        )

    }
}
