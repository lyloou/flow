package com.lyloou.flow.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lyloou.flow.model.Schedule

class TodoListViewModel : ViewModel() {

    val data: MutableLiveData<List<Schedule>> by lazy {
        MutableLiveData<List<Schedule>>()
    }

    fun add() {
        data.value = listOf(
            Schedule(
                "20200301",
                "A- [ ] A content\n - [ ] this is your \n- [x] 多么美好的太阳",
                "BB content",
                "CC content",
                "DD content"
            ),
            Schedule(
                "20200302",
                "AA content",
                "BB content",
                "CC content",
                "DD content"
            ),
            Schedule(
                "20200303",
                "AA content",
                "BB content",
                "CC content",
                "DD content"
            ),
            Schedule(
                "20200304",
                "AA content",
                "BB content",
                "CC content",
                "DD content"
            )
        )
    }
}