package com.lyloou.flow.ui.list

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
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.ui.detail.DetailActivity
import com.lyloou.flow.util.ImageHelper
import kotlinx.android.synthetic.main.cell_list.view.*

class ListAdapter : PagedListAdapter<DbFlow, ListAdapter.MyViewHolder>(DIFF_CALLBACK) {

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

    var onItemLongClickListener: OnItemLongClickListener? = null

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView = itemView.tvName
        val tvWeather: TextView = itemView.tvWeather
        val tvMemo: TextView = itemView.tvMemo
        val imageView: ImageView = itemView.imageView
        val ivSyncStatus: ImageView = itemView.ivSyncStatus
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { flow ->
            val day = flow.day
            with(holder) {
                this.tvName.text = day
                val weather = flow.weather.replace("高温", "").replace("低温", "")
                this.tvWeather.text = weather
                this.tvWeather.visibility =
                    if (weather.isNotEmpty()) View.VISIBLE else View.GONE

                this.tvMemo.text = flow.memo
                this.tvMemo.visibility =
                    if (flow.memo.isNotEmpty()) View.VISIBLE else View.GONE
            }

            Glide.with(holder.imageView.context)
                .load(ImageHelper.getBigImage(day))
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                doOnClickItem(flow, holder.imageView)
            }
            holder.itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(flow)
                true
            }
            holder.ivSyncStatus.setImageResource(if (flow.isSynced) 0 else R.drawable.ic_sync_problem)
        }
    }

    private fun doOnClickItem(flow: DbFlow, imageView: ImageView) {
        val intent = Intent(imageView.context, DetailActivity::class.java)
        intent.putExtra(Key.DAY.name, flow.day)

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

interface OnItemLongClickListener {
    operator fun invoke(flow: DbFlow);
}