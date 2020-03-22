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
            this.tvName.text = schedule.date

            val context = holder.itemView.context
            renderWithMarkdown(context, tvA, schedule.a.content)
            renderWithMarkdown(context, tvB, schedule.b.content)
            renderWithMarkdown(context, tvC, schedule.c.content)
            renderWithMarkdown(context, tvD, schedule.d.content)
        }
    }

    private fun renderWithMarkdown(context: Context, tv: TextView, text: String) {
        Markwon.builder(context)
            .usePlugin(TaskListPlugin.create(context))
            .build().setMarkdown(tv, text)
    }


    class TodoListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvA: TextView = view.findViewById(R.id.tvA)
        val tvB: TextView = view.findViewById(R.id.tvB)
        val tvC: TextView = view.findViewById(R.id.tvC)
        val tvD: TextView = view.findViewById(R.id.tvD)
    }

}