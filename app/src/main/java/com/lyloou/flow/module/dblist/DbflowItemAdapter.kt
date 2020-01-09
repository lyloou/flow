package com.lyloou.flow.module.dblist

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.R
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.util.Utime

class DbflowItemAdapter(private val viewmodel: DbflowViewModel) :
    RecyclerView.Adapter<DbflowItemAdapter.MyViewHolder>() {

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
        viewmodel.flowItems.value?.let { list ->
            list[position].let {
                holder.tvTimeStart.text = Utime.getFormatTime(it.timeStart)
                holder.tvTimeSep.text = it.timeSep
                holder.tvTimeEnd.text = Utime.getFormatTime(it.timeEnd)
                holder.tvSpend.text = it.spend
                holder.etContent.setText(it.content)
                addChangeListener(holder.etContent, it)

                holder.tvTimeStart.setOnClickListener { view: View? ->
                    itemListener?.onClickTimeStart(it)
                }
                holder.tvTimeEnd.setOnClickListener { view: View? ->
                    itemListener?.onClickTimeEnd(it)
                }
                holder.tvTimeStart.setOnLongClickListener { view: View? ->
                    itemListener?.onLongClickTimeStart(it)
                    true
                }
                holder.tvTimeEnd.setOnLongClickListener { view: View? ->
                    itemListener?.onLongClickTimeEnd(it)
                    true
                }
                holder.itemView.setOnLongClickListener(OnLongClickListener { v: View? ->
                    itemListener?.onLongClickItem(it)
                    false
                })
            }
        }


    }

    private fun addChangeListener(
        editText: EditText,
        item: FlowItem
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
                    itemListener?.onTextChanged(item, s)
                }
            }
        }
        editText.onFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
            itemListener?.onEditTextFocused(hasFocus, item)
            if (hasFocus) {
                editText.addTextChangedListener(watcher)
            } else {
                editText.removeTextChangedListener(watcher)
            }
        }
    }

    override fun getItemCount(): Int = viewmodel.flowItems.value?.size ?: 0
}