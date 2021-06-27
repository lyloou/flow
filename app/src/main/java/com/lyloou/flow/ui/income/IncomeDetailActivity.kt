package com.lyloou.flow.ui.income

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.databinding.ActivityIncomeDetailBinding
import kotlinx.android.synthetic.main.activity_income_detail.*
import java.util.*


class IncomeDetailActivity : BaseCompatActivity(), OnChartValueSelectedListener {
    companion object {
        const val CODE = "code"
    }

    private lateinit var binding: ActivityIncomeDetailBinding
    val chartUtils = ChartUtils()

    lateinit var viewModel: IncomeDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_income_detail)
        viewModel = ViewModelProvider(this).get(IncomeDetailViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        initData()
        initView()

    }

    private fun initData() {
        viewModel.code.observe(this, Observer {
            viewModel.getIncomeValueByCode(it)
            chartUtils.initStateChart(chart)
            chartUtils.addPairList(viewModel.getChartData())
        })
        viewModel.code.value = intent.getStringExtra(CODE)
        viewModel.priceOfEndForInput.observe(this, Observer {
            viewModel.refreshInput()
        })
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        chart.setOnChartValueSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private val TAG = "IncomeDetailActivity"
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.d(TAG, "onValueSelected: x=${e?.x}, y=${e?.y}")
        chart.highlightValue(h)
        viewModel.refreshChartData(e?.x, e?.y)
    }

    override fun onNothingSelected() {

    }
}