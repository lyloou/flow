package com.lyloou.flow.ui.city

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import com.lyloou.flow.databinding.ActivityCitySelectorBinding
import com.lyloou.flow.model.City
import kotlinx.android.synthetic.main.activity_city_selector.*

class CitySelectorActivity : AppCompatActivity(), OnClickItemListener {

    private lateinit var viewModel: CitySelectorViewModel
    private lateinit var binding: ActivityCitySelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_selector)
        viewModel = ViewModelProviders.of(this).get(CitySelectorViewModel::class.java)
        binding.data = viewModel

        val city = mutableListOf<City>()
        rvList.adapter = CitySelectorAdapter(city).apply { listener = this@CitySelectorActivity }
        rvList.layoutManager = LinearLayoutManager(baseContext)
        viewModel.list.observe(this, Observer {
            city.clear()
            city.addAll(it)
            rvList.adapter!!.notifyDataSetChanged()
        })

        viewModel.filter("")
        viewModel.key.observe(this, Observer {
            viewModel.filter(it)
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(city: City) {
        viewModel.saveCity(city)
        toast("已选择${city.cityName}")
        onBackPressed()
    }


}
