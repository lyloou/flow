package com.lyloou.flow.module.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lyloou.flow.R
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
            val context = holder.view.context
            Glide.with(context)
                .load(ImageHelper.getBigImage(it.day))
                .into(holder.ivImage)
            holder.view.setOnClickListener {
                val controller = Navigation.findNavController(holder.view)
                val bundle = Bundle()
                bundle.putInt(
                    context.resources.getString(R.string.arg_key_list_to_detail),
                    position
                )
                controller.navigate(R.id.action_listFragment_to_detailFragment, bundle)
            }
        }
    }
}