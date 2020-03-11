package com.lyloou.flow.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import com.lyloou.flow.common.Key
import com.lyloou.flow.databinding.ActivityTodoBinding
import com.lyloou.flow.model.Order
import kotlinx.android.synthetic.main.activity_todo.*

class TodoActivity : AppCompatActivity() {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var binding: ActivityTodoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo)
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)

        binding.data = todoViewModel
        binding.lifecycleOwner = this

        todoViewModel.name.observe(this, Observer {
            todoViewModel.refreshContent()
        })
        val key = intent.getStringExtra(Key.TODO.name)
        todoViewModel.name.value = key
        todoViewModel.content.observe(this, Observer {
            todoViewModel.save()
        })
        initEditText(key)
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
