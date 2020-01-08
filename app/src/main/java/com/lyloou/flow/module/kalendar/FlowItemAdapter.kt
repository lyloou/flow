package com.lyloou.flow.module.kalendar

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.common.ListAdapterWithHeader
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.module.kalendar.FlowItemAdapter.MyViewHolder
import com.lyloou.flow.temp.TempActivity


// [Extending the ListAdapter with a header view | Brightec, Brighton, UK](https://www.brightec.co.uk/ideas/extending-listadapter-header-view)
// [Android RecyclerView with header, footer and pagination - GadgetSaint](http://www.gadgetsaint.com/android/recyclerview-header-footer-pagination/#.XhMvEvy-uHv)
class FlowItemAdapter(private val myViewModel: MyViewModel) :

    ListAdapterWithHeader<FlowItem, MyViewHolder>(ITEM_COMPARATOR) {

    companion object {
        const val HEADER_POSITION = 0
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<FlowItem>() {
            override fun areItemsTheSame(oldItem: FlowItem, newItem: FlowItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: FlowItem, newItem: FlowItem): Boolean {
                return oldItem.content == newItem.content
                        && oldItem.timeStart == newItem.timeStart
                        && oldItem.timeEnd == newItem.timeEnd
            }

        }
    }


    fun notifyHeaderItem() {
        notifyItemChanged(HEADER_POSITION)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        when (viewType) {
            HEADER_POSITION -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.cell_header, parent, false)
                return MyViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.cell_item, parent, false)
                return MyViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (position) {
            HEADER_POSITION -> {
                holder.textView.text = myViewModel.flowDay.value?.day
            }
            else -> {
                val item = getItem(position)
                holder.textView.text = StringBuilder().apply {
                    this.append(item.timeStart)
                        .append(item.timeSep)
                        .append(item.timeEnd)
                        .append("\t")
                        .append("\t")
                        .append(item.spend)
                        .append("\n")
                        .append(item.content)
                        .append("\n");
                }.toString()
                holder.itemView.setOnClickListener {
                    Toast.makeText(
                        holder.textView.context,
                        holder.textView.text,
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.textView.context.startActivity(
                        Intent(
                            holder.textView.context,
                            TempActivity::class.java
                        )
                    )
                }
            }
        }

    }
}

