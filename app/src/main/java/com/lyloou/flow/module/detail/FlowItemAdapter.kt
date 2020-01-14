package com.lyloou.flow.module.detail

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.util.Utime

class FlowItemAdapter(private val data: MutableList<FlowItem>) :
    RecyclerView.Adapter<FlowItemAdapter.MyViewHolder>() {

    var itemListener: OnItemListener? = null

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTimeStart: TextView = itemView.findViewById(R.id.tv_time_start)
        val tvTimeSep: TextView = itemView.findViewById(R.id.tv_time_sep)
        val tvTimeEnd: TextView = itemView.findViewById(R.id.tv_time_end)
        val tvSpend: TextView = itemView.findViewById(R.id.tv_spend)
        val etContent: EditText = itemView.findViewById(R.id.et_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_dbdetail, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position].let { item ->
            holder.tvTimeStart.text = Utime.getFormatTime(item.timeStart)
            holder.tvTimeSep.text = item.timeSep
            holder.tvTimeEnd.text = Utime.getFormatTime(item.timeEnd)
            holder.tvSpend.text = item.spend
            holder.etContent.setText(item.content)
            holder.etContent.setSelection(item.content?.length ?: 0)
            addChangeListener(holder.etContent, item, position)

            holder.tvTimeStart.setOnClickListener {
                itemListener?.onClickTimeStart(item, position)
            }
            holder.tvTimeEnd.setOnClickListener {
                itemListener?.onClickTimeEnd(item, position)
            }
            holder.tvTimeStart.setOnLongClickListener {
                itemListener?.onLongClickTimeStart(item, position)
                true
            }
            holder.tvTimeEnd.setOnLongClickListener {
                itemListener?.onLongClickTimeEnd(item, position)
                true
            }
            holder.itemView.setOnLongClickListener {
                itemListener?.onLongClickItem(item, position)
                false
            }
        }
    }

    private fun addChangeListener(
        editText: EditText,
        item: FlowItem,
        position: Int
    ) {
        // [How to get the Edit text position from Recycler View adapter using Text Watcher in android - Stack Overflow](https://stackoverflow.com/a/37916021)
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (editText.hasFocus()) {
                    itemListener?.onTextChanged(item, s, position)
                }
            }
        }
        editText.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            itemListener?.onEditTextFocused(hasFocus, item, position)
            if (hasFocus) {
                editText.addTextChangedListener(watcher)
            } else {
                editText.removeTextChangedListener(watcher)
            }
        }
    }

    override fun getItemCount(): Int = data.size
}