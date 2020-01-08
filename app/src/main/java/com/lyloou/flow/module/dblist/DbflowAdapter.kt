package com.lyloou.flow.module.dblist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lyloou.flow.R
import com.lyloou.flow.repository.DbFlowDay
import com.lyloou.flow.util.ImageHelper

class DbflowAdapter : PagedListAdapter<DbFlowDay, DbflowAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DbFlowDay>() {
            override fun areItemsTheSame(oldItem: DbFlowDay, newItem: DbFlowDay): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: DbFlowDay, newItem: DbFlowDay): Boolean {
                return oldItem.day == newItem.day
            }

        }
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_dblist, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val flowDay = getItem(position)
        flowDay?.let {
            holder.textView.text = it.day
            Glide.with(holder.textView.context)
                .load(ImageHelper.getBigImage(it.day))
                .into(holder.imageView)
        }
    }
}