package com.lyloou.flow.module.dblist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
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
                return oldItem.id == newItem.id
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
        getItem(position)?.let {
            val flowDay = it
            holder.textView.text = flowDay.day
            Glide.with(holder.textView.context)
                .load(ImageHelper.getBigImage(flowDay.day))
                .into(holder.imageView)
            holder.itemView.setOnClickListener {
                val navigation = Navigation.findNavController(holder.itemView)
                navigation.navigate(
                    R.id.action_dblistFragment_to_dbdetailFragment,
                    bundleOf("day" to flowDay.day)
                )
            }
        }
    }
}