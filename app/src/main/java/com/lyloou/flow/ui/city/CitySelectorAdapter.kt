package com.lyloou.flow.ui.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.model.City
import kotlinx.android.synthetic.main.cell_city.view.*


class CitySelectorAdapter(val list: List<City>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_city, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    var listener: OnClickItemListener? = null
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val city = list[position]
        holder.itemView.tvName.text = city.cityName
        holder.itemView.tvCode.text = city.cityCode
        holder.itemView.view.setOnClickListener {
            listener?.let {
                listener!!.onClick(city)
            }
        }
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

interface OnClickItemListener {
    fun onClick(city: City)
}