package com.lyloou.flow.ui.income

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.Income
import com.lyloou.flow.model.incomeComparator
import com.lyloou.flow.repository.income.IncomeRepository

class IncomeViewModel(application: Application) : AndroidViewModel(application) {
    val data: MutableLiveData<MutableList<Income>> = MutableLiveData(
        IncomeRepository.getIncomeList()
            .sortedWith(incomeComparator())
            .toMutableList()
    )

    fun reload() {
        data.value = IncomeRepository.getIncomeList()
            .sortedWith(incomeComparator())
            .toMutableList()
    }

    fun addIncome(income: Income) {
        data.value?.let {
            it.add(0, income)
            IncomeRepository.saveIncomeList(it)
        }
    }

    fun deleteIncome(income: Income) {
        data.value?.let {
            it.remove(income)
            IncomeRepository.saveIncomeList(it)
        }
    }

    fun updateIncome() {
        data.value?.let {
            IncomeRepository.saveIncomeList(it)
        }
    }

    fun recoverIncome() {
        IncomeRepository.recoverDefault()
    }
}