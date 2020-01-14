package com.lyloou.flow.module.dblist

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
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.util.ImageHelper

class FlowAdapter : PagedListAdapter<DbFlow, FlowAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DbFlow>() {
            override fun areItemsTheSame(oldItem: DbFlow, newItem: DbFlow): Boolean {
                return oldItem.id == newItem.id
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
            LayoutInflater.from(parent.context).inflate(R.layout.cell_dblist, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            val day = it.day
            holder.textView.text = day
            Glide.with(holder.imageView.context)
                .load(ImageHelper.getBigImage(day))
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                doOnClickItem(day, holder.imageView)
            }
        }
    }

    private fun doOnClickItem(day: String, imageView: ImageView) {
        val intent = Intent(imageView.context, DetailActivity::class.java)
        intent.putExtra("day", day)

        var options: ActivityOptions? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(
                imageView.context as Activity,
                imageView,
                day
            )
        }
        imageView.context.startActivity(intent, options?.toBundle())
    }
}