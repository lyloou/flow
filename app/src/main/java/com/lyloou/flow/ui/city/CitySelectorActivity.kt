package com.lyloou.flow.ui.city

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import kotlinx.android.synthetic.main.activity_city_selector.*

class CitySelectorActivity : AppCompatActivity() {

    lateinit var viewModel: CitySelectorViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_selector)
        viewModel = ViewModelProviders.of(this).get(CitySelectorViewModel::class.java)
        viewModel.list.observe(this, Observer {
            rvList.adapter
        })
    }
}
