package com.lyloou.flow.ui.income

import android.app.Application
import android.widget.SeekBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.Income
import com.lyloou.flow.repository.income.IncomeRepository
import com.lyloou.flow.util.Ustr

class IncomeDetailViewModel(application: Application) : AndroidViewModel(application) {
    val code: MutableLiveData<String> = MutableLiveData()
    val incomeData: MutableLiveData<Income> = MutableLiveData()

    val priceOfEnd: MutableLiveData<String> = MutableLiveData()
    val rateOfEnd: MutableLiveData<String> = MutableLiveData()
    val rateOfActual: MutableLiveData<String> = MutableLiveData()

    val priceOfEndForInput: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val rateOfEndForInput: MutableLiveData<String> = MutableLiveData()

    fun onSeekBarChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        incomeData.value?.let {
            // 获取
            refreshByData(it, progress, seekBar.max)
        }
    }

    private fun refreshByData(
        income: Income,
        currentProgress: Int,
        maxProgress: Int
    ) {
        val priceOfEndIntervalStart = income.priceOfEndIntervalStart
        val priceOfEndIntervalEnd = income.priceOfEndIntervalEnd

        // 每一格的大小
        var per = (priceOfEndIntervalEnd - priceOfEndIntervalStart) / maxProgress
        if (per < 0) {
            per = 0.0
        }
        val priceOfEndValue = income.priceOfBegin + currentProgress * per
        priceOfEnd.value = "$priceOfEndValue"

        val rateOfEndValue = getRateOfEndValue(priceOfEndValue, income)
        rateOfEnd.value = Ustr.toPercentStr(rateOfEndValue)
        rateOfActual.value = Ustr.toPercentStr(rateOfEndValue + 0.001)
    }

    private fun getRateOfEndValue(priceOfEndValue: Double, it: Income): Double {
        if (priceOfEndValue > it.getPriceOfTapOut()) {
            return it.rateOfTapOut
        }

        val rateOfEndValue = 1.2 * (priceOfEndValue - it.priceOfBegin) / it.priceOfBegin
        if (rateOfEndValue < 0) {
            return 0.0
        }
        return rateOfEndValue
    }

    fun getIncomeValueByCode(code: String): Income? {
        val incomeValue = IncomeRepository.getIncomeList().firstOrNull { it.code == code }
        incomeData.value = incomeValue
        incomeValue?.let {
            refreshByData(it, 0, 100)
        }
        return incomeValue
    }

    fun getChartData(): List<Pair<String, Float>> {
        incomeData.value?.let {
            val list = ArrayList<Pair<String, Float>>(100)
            // 获取
            val priceOfEndIntervalStart = it.priceOfEndIntervalStart
            val priceOfEndIntervalEnd = it.priceOfEndIntervalEnd

            // 每一格的大小
            var per = (priceOfEndIntervalEnd - priceOfEndIntervalStart) / 100
            if (per < 0) {
                per = 0.0
            }

            for (i in 1 until 100) {
                val priceOfEndValue = it.priceOfBegin + i * per
                priceOfEnd.value = "$priceOfEndValue"

                val rateOfEndValue = getRateOfEndValue(priceOfEndValue, it)
                val pair = Ustr.to2Decimal(priceOfEndValue) to rateOfEndValue.toFloat()
                list.add(pair)
            }
            return list
        }
        return emptyList()
    }

    fun refreshInput() {
        if (incomeData.value == null) {
            return
        }
        priceOfEndForInput.value?.let {
            if (it.isEmpty()) {
                return@let
            }
            rateOfEndForInput.value =
                Ustr.toPercentStr(getRateOfEndValue(it.toDouble(), incomeData.value!!))
        }
    }
}