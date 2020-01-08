package com.lyloou.flow.temp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TempViewModel : ViewModel() {
    val stringData: MutableLiveData<Int> by lazy {
        MutableLiveData(0)
    }

    fun add(num: Int) {
        stringData.value = stringData.value?.plus(num)
    }
}