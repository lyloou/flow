package com.lyloou.flow.module.list

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
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
import com.lyloou.flow.common.Key
import com.lyloou.flow.module.detail.DetailActivity
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.repository.toFlow
import com.lyloou.flow.util.ImageHelper

class FlowAdapter : PagedListAdapter<DbFlow, FlowAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DbFlow>() {
            override fun areItemsTheSame(oldItem: DbFlow, newItem: DbFlow): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DbFlow, newItem: DbFlow): Boolean {
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
            LayoutInflater.from(parent.context).inflate(R.layout.cell_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { flow ->
            val day = flow.day
            holder.textView.text = day
            Glide.with(holder.imageView.context)
                .load(ImageHelper.getBigImage(day))
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                doOnClickItem(flow, holder.imageView)
            }
        }
    }

    private fun doOnClickItem(flow: DbFlow, imageView: ImageView) {
        val intent = Intent(imageView.context, DetailActivity::class.java)
        intent.putExtra(Key.FLOW.name, flow.toFlow())

        var options: ActivityOptions? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(
                imageView.context as Activity,
                imageView,
                flow.day
            )
        }
        imageView.context.startActivity(intent, options?.toBundle())
    }
}