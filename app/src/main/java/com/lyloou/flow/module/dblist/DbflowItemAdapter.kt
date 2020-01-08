package com.lyloou.flow.module.dblist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R

class DbflowItemAdapter(private val viewmodel: DbflowViewModel) :
    RecyclerView.Adapter<DbflowItemAdapter.MyViewHolder>() {


    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTimeStart: TextView = itemView.findViewById(R.id.tv_time_start)
        val tvTimeSep: TextView = itemView.findViewById(R.id.tv_time_sep)
        val tvTimeEnd: TextView = itemView.findViewById(R.id.tv_time_end)
        val tvSpend: TextView = itemView.findViewById(R.id.tv_spend)
        val etContent: EditText = itemView.findViewById(R.id.et_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_dbdetail, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        viewmodel.flowItems.value?.let { list ->
            list[position].let {
                holder.tvTimeStart.text = it.timeStart
                holder.tvTimeSep.text = it.timeSep
                holder.tvTimeEnd.text = it.timeEnd
                holder.tvSpend.text = it.spend
                holder.etContent.setText(it.content)
            }
        }

    }

    override fun getItemCount(): Int = viewmodel.flowItems.value?.size ?: 0
}