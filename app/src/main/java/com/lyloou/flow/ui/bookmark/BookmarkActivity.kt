package com.lyloou.flow.ui.bookmark

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.common.toast
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.extension.snackbar
import com.lyloou.flow.model.Bookmark
import com.lyloou.flow.ui.web.NormalWebViewActivity
import com.lyloou.flow.util.Uapp
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Usystem
import com.lyloou.flow.widget.ItemOffsetDecoration
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_bookmark.*

class BookmarkActivity : BaseCompatActivity(), ToolbarManager, BookmarkAdapter.OnItemListener {

    lateinit var viewModel: BookmarkViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        viewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)

        initView()
        viewModel.data.observe(this, Observer {
            it?.let { data ->
                (rvBookmark.adapter as BookmarkAdapter).let { adapter ->
                    adapter.list = data.toMutableList()
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.reload()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        enableHomeAsUp { onBackPressed() }
        whiteToolbarText()

        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)

        rvBookmark.layoutManager = LinearLayoutManager(this)
        rvBookmark.addItemDecoration(ItemOffsetDecoration(dp2px(16f)))
        rvBookmark.adapter = BookmarkAdapter().apply { onItemListener = this@BookmarkActivity }
    }

    override val toolbar: Toolbar
        get() = myToolbar

    override fun onItemClicked(bookmark: Bookmark) {
        NormalWebViewActivity.start(this, bookmark.url)
    }

    override fun onItemMoreClicked(bookmark: Bookmark) {
        Udialog.AlertMultiItem.builder(this)
            .add("复制") { copyBookmark(bookmark) }
            .add("编辑") { showBookmarkEditDialog(bookmark) }
            .add("删除") { deleteBookmark(bookmark) }
            .show()
    }

    private fun copyBookmark(bookmark: Bookmark) {
        Usystem.copyString(this, "[${bookmark.title}](${bookmark.url})")
        toast("已复制")
    }

    private fun showBookmarkEditDialog(bookmark: Bookmark? = null) {
        Udialog.AlertMultipleInputDialog.builder(this)
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 1
                this.hint = "标题"
                this.defaultValue = bookmark?.title
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 2
                this.hint = "网址"
                this.defaultValue = bookmark?.url
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 3
                this.hint = "优先级"
                this.defaultValue = bookmark?.order?.toString() ?: "0"
                this.type = EditorInfo.TYPE_CLASS_NUMBER
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 4
                this.hint = "标签"
                this.defaultValue = bookmark?.tag
            })
            .addInputItem(Udialog.AlertMultipleInputDialog.InputItem().apply {
                this.id = 5
                this.hint = "描述"
                this.defaultValue = bookmark?.desc
            })
            .consumer {

                if (it[1].isNullOrEmpty() || it[2].isNullOrEmpty()) {
                    showBookmarkEditDialog(bookmark)
                    toast("标题和网址不能为空")
                    return@consumer
                }

                if (bookmark == null) {
                    viewModel.addBookmark(
                        Bookmark(
                            title = it[1]!!,
                            url = it[2]!!,
                            order = if (it[3].isNullOrEmpty()) 0 else it[3]!!.toInt(),
                            tag = it[4]!!,
                            desc = it[5]!!
                        )
                    )
                    viewModel.reload()
                    return@consumer
                }

                bookmark.title = it[1]!!
                bookmark.url = it[2]!!
                bookmark.order = if (it[3].isNullOrEmpty()) 0 else it[3]!!.toInt()
                bookmark.tag = it[4]!!
                bookmark.desc = it[5]!!

                viewModel.updateBookmark()
                viewModel.reload()
            }
            .show()
    }

    private fun deleteBookmark(bookmark: Bookmark) {
        viewModel.deleteBookmark(bookmark)
        viewModel.reload()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bookmark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun addShortcut() {
        Uapp.addShortCutCompat(
            this,
            BookmarkActivity::class.java.canonicalName,
            "bookmark",
            R.mipmap.road,
            resources.getString(R.string.my_bookmark)
        )
        val snackbar: Snackbar = snackbar("已添加到桌面", Snackbar.LENGTH_LONG)
        snackbar.setAction("添加失败？去授权") {
            Usystem.toAppSetting(this)
        }
        snackbar.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_shortcut -> {
                addShortcut()
            }
            R.id.add -> {
                showBookmarkEditDialog()
            }
            R.id.recover -> {
                recoverBookmark()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun recoverBookmark() {
        viewModel.recoverBookmark()
        viewModel.reload()
    }
}
