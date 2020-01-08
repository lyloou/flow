/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lyloou.flow.module.detail

import android.content.Context
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
import java.util.*

internal class FlowAdapter(private val mContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mList: MutableList<FlowItem>
    private var mItemListener: OnItemListener? = null
    fun setOnItemListener(itemClickListener: OnItemListener?) {
        mItemListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.cell_detail, parent, false)
        return FlowHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val holder = viewHolder as FlowHolder
        val item = mList[position]
        holder.tvTimeStart.text = Utime.getFormatTime(item.timeStart)
        holder.tvTimeEnd.text = Utime.getFormatTime(item.timeEnd)
        holder.tvTimeSep.text = item.timeSep
        holder.tvSpend.text = item.spend
        val etContent = holder.etContent
        etContent.setText(item.content)
        addChangeListener(etContent, item)
        holder.tvTimeStart.setOnClickListener { view: View? ->
            if (mItemListener != null) {
                mItemListener!!.onClickTimeStart(item)
            }
        }
        holder.tvTimeEnd.setOnClickListener { view: View? ->
            if (mItemListener != null) {
                mItemListener!!.onClickTimeEnd(item)
            }
        }
        holder.tvTimeStart.setOnLongClickListener { view: View? ->
            if (mItemListener != null) {
                mItemListener!!.onLongClickTimeStart(item)
            }
            true
        }
        holder.tvTimeEnd.setOnLongClickListener { view: View? ->
            if (mItemListener != null) {
                mItemListener!!.onLongClickTimeEnd(item)
            }
            true
        }
        holder.view.setOnLongClickListener { v: View? ->
            if (mItemListener != null) {
                mItemListener!!.onLongClickItem(item)
                return@setOnLongClickListener true
            }
            false
        }
    }

    private fun addChangeListener(
        editText: EditText,
        item: FlowItem
    ) { // [How to get the Edit text position from Recycler View adapter using Text Watcher in android - Stack Overflow](https://stackoverflow.com/a/37916021)
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
                    if (mItemListener != null) {
                        mItemListener!!.onTextChanged(item, s)
                    }
                }
            }
        }
        editText.onFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
            if (mItemListener != null) {
                mItemListener!!.onEditTextFocused(hasFocus, item)
            }
            if (hasFocus) {
                editText.addTextChangedListener(watcher)
            } else {
                editText.removeTextChangedListener(watcher)
            }
        }
    }

    override fun getItemCount(): Int {
        return listSize
    }

    fun setList(items: MutableList<FlowItem>) {
        mList = items
    }

    fun clearAll() {
        mList.clear()
    }

    fun addAll(items: List<FlowItem>) {
        mList.addAll(items)
    }

    val listSize: Int
        get() = mList.size

    internal interface OnItemListener {
        fun onLongClickItem(item: FlowItem?)
        fun onClickTimeStart(item: FlowItem?)
        fun onClickTimeEnd(item: FlowItem?)
        fun onLongClickTimeStart(item: FlowItem?)
        fun onLongClickTimeEnd(item: FlowItem?)
        fun onTextChanged(item: FlowItem?, s: CharSequence?)
        fun onEditTextFocused(hasFocus: Boolean, item: FlowItem?)
    }

    private class FlowHolder internal constructor(var view: View) :
        RecyclerView.ViewHolder(view) {
        var tvTimeStart: TextView
        var tvTimeEnd: TextView
        var tvTimeSep: TextView
        var tvSpend: TextView
        var etContent: EditText

        init {
            tvTimeStart = view.findViewById(R.id.tv_time_start)
            tvTimeEnd = view.findViewById(R.id.tv_time_end)
            tvTimeSep = view.findViewById(R.id.tv_time_sep)
            tvSpend = view.findViewById(R.id.tv_spend)
            etContent = view.findViewById(R.id.et_content)
        }
    }

    init {
        mList = ArrayList()
    }
}