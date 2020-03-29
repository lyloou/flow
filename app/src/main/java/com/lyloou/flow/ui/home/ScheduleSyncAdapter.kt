package com.lyloou.flow.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.model.toPrettyJsonString
import com.lyloou.flow.repository.schedule.DbSchedule
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Utime
import kotlinx.android.synthetic.main.cell_schedule_sync.view.*


class ScheduleSyncAdapter(val list: MutableList<DbSchedule> = mutableListOf()) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_schedule_sync, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getData(): MutableList<DbSchedule> {
        return list
    }

    fun addData(data: List<DbSchedule>) {
        val index = data.size
        this.list.addAll(data)
        notifyItemRangeInserted(index, data.size)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val s = list[position]
        with(holder.itemView) {
            tvTitle.text = context.getString(R.string.schedule_sync_title, s.title)
            tvIsDisabled.text =
                context.getString(R.string.schedule_sync_disabled, s.isDisabled.toString())
            tvSyncTime.text =
                context.getString(R.string.schedule_sync_time, getFormatted(s.syncTime))
            tvRSyncTime.text =
                context.getString(R.string.schedule_sync_rtime, getFormatted(s.rsyncTime))
            tvUuid.text = context.getString(R.string.schedule_sync_uuid, s.uuid)
        }
        holder.itemView.view.setOnClickListener {
            Udialog.AlertOneItem.builder(it.context)
                .title(s.title)
                .message(s.toPrettyJsonString())
                .show()
        }
    }

    private fun getFormatted(syncTime: Long) = if (syncTime == 0L) "无" else
        Utime.getDayWithFormatFour(syncTime)

    fun clear() {
        this.list.clear()
        notifyDataSetChanged()
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)