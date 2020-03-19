package com.lyloou.flow.ui.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.model.City


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
        holder.tvName.text = city.cityName
        holder.tvCode.text = city.cityCode
        holder.view.setOnClickListener {
            listener?.let {
                listener!!.onClick(city)
            }
        }
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val view: View = view.findViewById(R.id.view)
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvCode: TextView = view.findViewById(R.id.tvCode)
}

interface OnClickItemListener {
    fun onClick(city: City)
}