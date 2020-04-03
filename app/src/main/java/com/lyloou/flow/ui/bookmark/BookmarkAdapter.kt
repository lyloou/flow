package com.lyloou.flow.ui.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lyloou.flow.R
import com.lyloou.flow.model.Bookmark
import kotlinx.android.synthetic.main.cell_bookmark.view.*

class BookmarkAdapter(val list: MutableList<Bookmark>) :
    RecyclerView.Adapter<BookmarkAdapter.MyViewHolder>() {

    var onItemListener: OnItemListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    interface OnItemListener {
        fun onItemClicked(bookmark: Bookmark)
        fun onItemMoreClicked(bookmark: Bookmark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_bookmark, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        list[position].let { bookmark ->
            with(holder.itemView) {
                Glide.with(this)
                    .load(bookmark.iconUrl)
                    .placeholder(R.drawable.about_icon_link)
                    .into(this.ivIcon)
                this.tvTitle.text = bookmark.title
                this.tvUrl.text = bookmark.url
                this.tvOrder.text = bookmark.order.toString()
                this.tvTag.text = bookmark.tag
                this.ivMore.setOnClickListener { onItemListener?.onItemMoreClicked(bookmark) }
                this.setOnClickListener { onItemListener?.onItemClicked(bookmark) }
            }
        }
    }
}