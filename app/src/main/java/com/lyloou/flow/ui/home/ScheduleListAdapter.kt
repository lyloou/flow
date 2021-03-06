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
import com.lyloou.flow.model.Order
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.util.Utime
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tasklist.TaskListPlugin
import kotlinx.android.synthetic.main.cell_schedule.view.*


interface OnItemClickListener {
    fun onItemClick(schedule: DbSchedule, name: String)
    fun onItemMoreClick(schedule: DbSchedule)
    fun onItemTitleClick(schedule: DbSchedule)
}

class ScheduleListAdapter(
    val listener: OnItemClickListener? = null
) :
    PagedListAdapter<DbSchedule, ScheduleListAdapter.ScheduleListHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DbSchedule>() {
            override fun areItemsTheSame(oldItem: DbSchedule, newItem: DbSchedule): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: DbSchedule, newItem: DbSchedule): Boolean {
                return oldItem.uuid == newItem.uuid
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_schedule, parent, false)
        return ScheduleListHolder(inflate)
    }

    override fun onBindViewHolder(holder: ScheduleListHolder, position: Int) {
        getItem(position)?.let { s ->
            with(holder) {
                val context = holder.itemView.context
                itemView.tvA.setOnClickListener { listener?.onItemClick(s, Order.A.name) }
                itemView.tvB.setOnClickListener { listener?.onItemClick(s, Order.B.name) }
                itemView.tvC.setOnClickListener { listener?.onItemClick(s, Order.C.name) }
                itemView.tvD.setOnClickListener { listener?.onItemClick(s, Order.D.name) }
                itemView.ivMore.setOnClickListener { listener?.onItemMoreClick(s); }
                itemView.tvName.setOnClickListener { listener?.onItemTitleClick(s); }

                itemView.tvName.text = s.title
                itemView.tvTime.text = context.getString(
                    R.string.last_update_time,
                    Utime.getDayWithFormatFour(s.localTime)
                )
                renderWithMarkdown(context, itemView.tvA, s.a)
                renderWithMarkdown(context, itemView.tvB, s.b)
                renderWithMarkdown(context, itemView.tvC, s.c)
                renderWithMarkdown(context, itemView.tvD, s.d)
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


    class ScheduleListHolder(view: View) : RecyclerView.ViewHolder(view)

}