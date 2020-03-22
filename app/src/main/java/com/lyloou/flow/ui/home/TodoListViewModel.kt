package com.lyloou.flow.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lyloou.flow.model.Schedule
import com.lyloou.flow.model.ScheduleItem

class TodoListViewModel : ViewModel() {

    val data: MutableLiveData<List<Schedule>> by lazy {
        MutableLiveData<List<Schedule>>()
    }

    fun add() {
        data.value = listOf(
            Schedule(
                "20200301",
                ScheduleItem("A", "- [ ] A content\n - [ ] this is your \n- [x] 多么美好的太阳"),
                ScheduleItem("B", "B content"),
                ScheduleItem("C", "C content"),
                ScheduleItem("D", "D content")
            ),
            Schedule(
                "20200302",
                ScheduleItem("A", "A content"),
                ScheduleItem("B", "B content"),
                ScheduleItem("C", "C content"),
                ScheduleItem("D", "D content")
            ),
            Schedule(
                "20200303",
                ScheduleItem("A", "A content"),
                ScheduleItem("B", "B content"),
                ScheduleItem("C", "C content"),
                ScheduleItem("D", "D content")
            ),
            Schedule(
                "20200304",
                ScheduleItem("A", "A content"),
                ScheduleItem("B", "B content"),
                ScheduleItem("C", "C content"),
                ScheduleItem("D", "D content")
            )
        )
    }
}