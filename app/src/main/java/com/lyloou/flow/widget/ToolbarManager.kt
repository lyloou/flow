package com.lyloou.flow.widget

import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.lyloou.flow.extension.slideEnter
import com.lyloou.flow.extension.slideExit

// 参考 kotlin-for-android-developers-zh.pdf (p176)
interface ToolbarManager {
    val toolbar: Toolbar
    var toolbarTitle: String
        get() = toolbar.title.toString()
        set(value) {
            toolbar.title = value
        }

    fun enableHomeAsUp(up: () -> Unit) {
        toolbar.navigationIcon = createUpDrawable()
        toolbar.setNavigationOnClickListener { up() }
    }

    fun createUpDrawable() = with(DrawerArrowDrawable(toolbar.context)) {
        progress = 1f
        this
    }

    fun attachToScroll(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    toolbar.slideExit()
                } else {
                    toolbar.slideEnter()
                }
            }
        })
    }
}