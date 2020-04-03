package com.lyloou.flow.ui.bookmark

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyloou.flow.R
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.model.Bookmark
import com.lyloou.flow.model.bookmarkComparator
import com.lyloou.flow.ui.web.NormalWebViewActivity
import com.lyloou.flow.widget.ItemOffsetDecoration
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_bookmark.*

class BookmarkActivity : AppCompatActivity(), ToolbarManager, BookmarkAdapter.OnItemListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        setSupportActionBar(toolbar)
        enableHomeAsUp { onBackPressed() }
        whiteToolbarText()

        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)

        rvBookmark.layoutManager = LinearLayoutManager(this)
        rvBookmark.addItemDecoration(ItemOffsetDecoration(dp2px(16f)))
        val mutableListOf = mutableListOf(
            Bookmark("陈 皓", "https://coolshell.cn/"),
            Bookmark("刘未鹏", "http://mindhacks.cn/"),
            Bookmark("阮一峰", "http://ruanyifeng.com/blog/"),
            Bookmark("廖雪峰", "https://www.liaoxuefeng.com/"),
            Bookmark("王 垠", "http://www.yinwang.org/"),
            Bookmark("木子楼", "http://lyloou.com", order = 2)
        )

        rvBookmark.adapter = BookmarkAdapter(
            mutableListOf.sortedWith(bookmarkComparator()).toMutableList()
        ).apply {
            onItemListener = this@BookmarkActivity
        }
    }

    override val toolbar: Toolbar
        get() = myToolbar

    override fun onItemClicked(bookmark: Bookmark) {
        NormalWebViewActivity.start(this, bookmark.url)
    }

    override fun onItemMoreClicked(bookmark: Bookmark) {

    }
}
