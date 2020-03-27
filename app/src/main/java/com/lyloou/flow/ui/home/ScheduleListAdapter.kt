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
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tasklist.TaskListPlugin
import kotlinx.android.synthetic.main.cell_schedule.view.*


interface OnItemClickListener {
    fun onItemClick(schedule: DbSchedule)
    fun onItemLongClick(schedule: DbSchedule)
}

class ScheduleListAdapter(
    val clickListener: OnItemClickListener? = null
) :
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
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_schedule, parent, false)
        return TodoListHolder(inflate)
    }

    override fun onBindViewHolder(holder: TodoListHolder, position: Int) {
        getItem(position)?.let { data ->
            with(holder) {
                val context = holder.itemView.context
                holder.itemView.setOnClickListener { clickListener?.onItemClick(data) }
                holder.itemView.setOnLongClickListener { clickListener?.onItemLongClick(data);true }
                itemView.tvName.text = data.title
                renderWithMarkdown(context, itemView.tvA, data.a)
                renderWithMarkdown(context, itemView.tvB, data.b)
                renderWithMarkdown(context, itemView.tvC, data.c)
                renderWithMarkdown(context, itemView.tvD, data.d)
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