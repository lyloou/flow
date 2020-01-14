package com.lyloou.flow.module.detail

import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.transition.ChangeBounds
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.common.Key
import com.lyloou.flow.common.Url
import com.lyloou.flow.model.Flow
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.model.toDbFlow
import com.lyloou.flow.module.list.FlowViewModel
import com.lyloou.flow.net.KingsoftwareAPI
import com.lyloou.flow.net.Network
import com.lyloou.flow.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_dbdetail.*

class DetailActivity : BaseCompatActivity() {

    private lateinit var viewModel: FlowViewModel
    private lateinit var flow: Flow
    private lateinit var itemList: MutableList<FlowItem>
    private lateinit var adapter: FlowItemAdapter
    private var handler: Handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbdetail)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val bounds = ChangeBounds()
            bounds.duration = 280
            bounds.interpolator = DecelerateInterpolator()
            window.sharedElementEnterTransition = bounds
        };

        initData()
        initView()
    }

    private fun initData() {
        flow = intent?.getParcelableExtra(Key.FLOW.name) ?: Flow(Utime.today())
        viewModel = ViewModelProviders.of(this).get(FlowViewModel::class.java)
        // 没有数据的时候，添加默认的
        viewModel.getDbFlow(flow.day).observe(this, Observer {
            if (it == null) {
                viewModel.insertDbFlow(flow.toDbFlow())
            }
        })

    }

    private fun initView() {
        setSupportActionBar(toolbar);
        supportActionBar?.title = flow.day;
        toolbar.setNavigationOnClickListener { onBackPressed() };
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)
        Uview.toggleViewVisibleWhenAppBarLayoutScrollChanged(app_bar, tv_header)

        intent?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv_header.transitionName = flow.day
            }
        }

        Glide.with(context)
            .asBitmap()
            .load(ImageHelper.getBigImage(flow.day))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.ic_launcher)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false;
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    iv_header.background = BitmapDrawable(resources, resource)
                    // [Dynamic Colors With Glide Library and Android Palette](https://android.jlelse.eu/dynamic-colors-with-glide-library-and-android-palette-5be407049d97)
                    val palette = Palette.from(resource).generate()
                    val color =
                        palette.getMutedColor(ContextCompat.getColor(context, R.color.colorAccent))
                    resetThemeColor(color)

                }
            });

        Network.get(Url.Kingsoftware.url, KingsoftwareAPI::class.java)
            .getDaily(Utime.transferTwoToOne(flow.day))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tv_header.text = it.content
                tv_header.tag = it.note
                tv_header.visibility = View.VISIBLE
            }, Throwable::printStackTrace)

        tv_header.setOnClickListener {
            val netString = it.tag
            if (netString is String) {
                val oldString = tv_header.text.toString()
                tv_header.text = netString
                tv_header.tag = oldString
            }
        };

        initRecyclerView()
    }

    private fun initRecyclerView() {
        itemList = flow.items
        adapter = FlowItemAdapter(itemList)
        adapter.itemListener = getItemListener()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.descendantFocusability = RecyclerView.FOCUS_AFTER_DESCENDANTS
        // 通过延迟的方式，让界面出来的时更顺滑（动画）
        showViewAnimated(recyclerView)
    }

    private fun showViewAnimated(view: View) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(800)
            .start()
    }

    private fun resetThemeColor(color: Int) {
        val transparentColor = Ucolor.getTransparentColor(color)
        tv_header.setBackgroundColor(transparentColor)
        // [Android Material Design - How to change background color of Toolbar after CollapsingToolbarLayout is collapsed - Stack Overflow](https://stackoverflow.com/questions/30619598/android-material-design-how-to-change-background-color-of-toolbar-after-collap)

        // https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        Uscreen.setStatusBarColor(this, Color.parseColor(hexColor))
    }

    fun updateDb(vararg noDelay: Boolean) {
        if (noDelay.isNotEmpty() && noDelay[0]) {
            updateDbTask.run()
            return
        }
        delayUpdateDb()
    }

    private val updateDbTask = Runnable {
        viewModel.updateDbFlowItems(flow.day, itemList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dbflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                addNewItem()
            }
            R.id.about -> {
                Toast.makeText(this, "hihi", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun delayUpdateDb() {
        handler.removeCallbacks(updateDbTask)
        handler.postDelayed(updateDbTask, 800)
    }

    private fun addNewItem() {
        with(itemList) {
            val newItem = FlowItem()
            val startArr = Utime.getValidTime(null)
            var currentTime = Utime.getTimeString(startArr[0], startArr[1])

            if (this.size > 0) {
                val item = this[0]
                // 当前时间已经存在，则不在新建
                if (currentTime == item.timeStart) {
                    showTips("该时间点已经有了一个哦")
                    return
                }
                if (!TextUtils.isEmpty(item.timeEnd)) {
                    currentTime = item.timeEnd
                }
            }
            newItem.timeStart = currentTime
            add(0, newItem)
            recyclerView.scrollToPosition(0)
            adapter.notifyItemInserted(0)
            delayUpdateDb()
        }
    }

    private fun showTips(text: String) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show()
    }

    private fun getItemListener() = object :
        OnItemListener {
        override fun onLongClickItem(item: FlowItem, position: Int) {
            Udialog.AlertMultiItem.builder(context)
                .add("复制内容") { Usystem.copyString(context, item.content) }
                .add("复制全部") {
                    Usystem.copyString(
                        context, Utime.getFormatTime(item.timeStart)
                            .plus(item.timeSep)
                            .plus(Utime.getFormatTime(item.timeEnd))
                            .plus("\n")
                            .plus(item.content)
                    )
                }
                .add("删除此项") {
                    itemList.remove(item)
                    adapter.notifyDataSetChanged()
                    delayUpdateDb()
                }
                .show()
        }

        override fun onClickTimeStart(item: FlowItem, position: Int) {
            // 原文链接：https://blog.csdn.net/qq_17009881/article/details/75371406
            val listener =
                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                    item.timeStart = Utime.getTimeString(hourOfDay, minute)
                    Utransfer.sortItems(itemList)
                    adapter.notifyItemChanged(position)
                    delayUpdateDb()
                }
            Udialog.showTimePicker(context, listener, Utime.getValidTime(item.timeStart))
        }

        override fun onClickTimeEnd(item: FlowItem, position: Int) {
            val listener =
                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                    item.timeEnd = Utime.getTimeString(hourOfDay, minute)
                    Utransfer.sortItems(itemList)
                    adapter.notifyItemChanged(position)
                    delayUpdateDb()
                }
            Udialog.showTimePicker(context, listener, Utime.getValidTime(item.timeEnd))
        }

        override fun onLongClickTimeStart(item: FlowItem, position: Int) {
            Udialog.AlertOneItem.builder(context)
                .consumer {
                    if (it) {
                        item.timeStart = null
                        Utransfer.sortItems(itemList)
                        adapter.notifyItemChanged(position)
                        delayUpdateDb()
                    }
                }
                .message("清空开始时间")
                .show();
        }

        override fun onLongClickTimeEnd(item: FlowItem, position: Int) {
            Udialog.AlertOneItem.builder(context)
                .consumer {
                    if (it) {
                        item.timeEnd = null
                        Utransfer.sortItems(itemList)
                        adapter.notifyItemChanged(position)
                        delayUpdateDb()
                    }
                }
                .message("清空结束时间")
                .show();
        }

        override fun onTextChanged(item: FlowItem, s: CharSequence, position: Int) {
            item.content = s.toString()
            // 只需要更新数据即可
            updateDb()
        }

        override fun onEditTextFocused(hasFocus: Boolean, item: FlowItem, position: Int) {

        }

    }

}