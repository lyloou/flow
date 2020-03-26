package com.lyloou.flow.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.repository.schedule.toSchedule
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tasklist.TaskListPlugin
import kotlinx.android.synthetic.main.cell_todo.view.*


class ScheduleListAdapter :
    PagedListAdapter<DbSchedule, ScheduleListAdapter.TodoListHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DbSchedule>() {
            override fun areItemsTheSame(oldItem: DbSchedule, newItem: DbSchedule): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DbSchedule, newItem: DbSchedule): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.cell_todo, parent, false)
        return TodoListHolder(inflate)
    }

    override fun onBindViewHolder(holder: TodoListHolder, position: Int) {
        getItem(position)?.let {
            with(holder) {
                itemView.tvName.text = it.title
                val schedule = it.toSchedule()
                val context = holder.itemView.context
                renderWithMarkdown(context, itemView.tvA, schedule.a)
                renderWithMarkdown(context, itemView.tvB, schedule.b)
                renderWithMarkdown(context, itemView.tvC, schedule.c)
                renderWithMarkdown(context, itemView.tvD, schedule.d)
            }
        }
    }

    private fun renderWithMarkdown(context: Context, tv: TextView, text: String?) {
        text?.let {
            Markwon.builder(context)
                .usePlugin(TaskListPlugin.create(context))
                .build().setMarkdown(tv, it)
        }
    }


    class TodoListHolder(view: View) : RecyclerView.ViewHolder(view)

}