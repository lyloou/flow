package com.lyloou.flow.ui.income

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.model.Income
import kotlinx.android.synthetic.main.cell_income.view.*

class IncomeAdapter(var list: MutableList<Income> = mutableListOf()) :
    RecyclerView.Adapter<IncomeAdapter.MyViewHolder>() {

    var onItemListener: OnItemListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    interface OnItemListener {
        fun onItemClicked(income: Income)
        fun onItemMoreClicked(income: Income)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_income, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        list[position].let { income ->
            with(holder.itemView) {

                this.tvFirst.text = income.first()
                this.tvSecond.text = income.second()
                this.tvThird.text = income.third()
                this.tvForth.text = income.forth()
                this.tvOrder.text = income.order.toString()
                this.tvTag.text = income.tag
                this.ivMore.setOnClickListener { onItemListener?.onItemMoreClicked(income) }
                this.setOnClickListener { onItemListener?.onItemClicked(income) }
            }
        }
    }
}