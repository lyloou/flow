package com.lyloou.flow.module.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lyloou.flow.R
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.util.ImageHelper

class FlowDayAdapter(private val listViewModel: ListViewModel) :
    RecyclerView.Adapter<FlowDayAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.imageView)
        val tvText: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listViewModel.flowDayList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        listViewModel.flowDayList.value?.get(position)?.let {
            holder.tvText.text = it.day
            Glide.with(holder.ivImage)
                .load(ImageHelper.getBigImage(it.day))
                .into(holder.ivImage)
            holder.view.setOnClickListener {
                val text =
                    FlowItemHelper.toPrettyText(listViewModel.flowDayList.value!![position].items)
                Toast.makeText(holder.view.context, text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}