package com.lyloou.flow.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.model.Schedule
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tasklist.TaskListPlugin
import kotlinx.android.synthetic.main.cell_todo.view.*


class TodoListAdapter(val list: List<Schedule>) :
    RecyclerView.Adapter<TodoListAdapter.TodoListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.cell_todo, parent, false)
        return TodoListHolder(inflate)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodoListHolder, position: Int) {
        val schedule = list[position]
        with(holder) {
            itemView.tvName.text = schedule.title

            val context = holder.itemView.context
            renderWithMarkdown(context, itemView.tvA, schedule.a)
            renderWithMarkdown(context, itemView.tvB, schedule.b)
            renderWithMarkdown(context, itemView.tvC, schedule.c)
            renderWithMarkdown(context, itemView.tvD, schedule.d)
        }
    }

    private fun renderWithMarkdown(context: Context, tv: TextView, text: String) {
        Markwon.builder(context)
            .usePlugin(TaskListPlugin.create(context))
            .build().setMarkdown(tv, text)
    }


    class TodoListHolder(view: View) : RecyclerView.ViewHolder(view)

}