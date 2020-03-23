package com.lyloou.flow.ui.detail

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
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
import com.lyloou.flow.MainActivity
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.common.Key
import com.lyloou.flow.databinding.ActivityDetailBinding
import com.lyloou.flow.extension.snackbar
import com.lyloou.flow.model.CityHelper
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.model.FlowItemHelper
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.defaultSubscribe
import com.lyloou.flow.net.getKingSoftwareDaily
import com.lyloou.flow.net.weatherApi
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.ui.city.CitySelectorActivity
import com.lyloou.flow.ui.list.ListViewModel
import com.lyloou.flow.util.*
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseCompatActivity() {

    private lateinit var viewModel: ListViewModel
    private lateinit var day: String
    private lateinit var itemList: MutableList<FlowItem>
    private lateinit var adapter: DetailAdapter
    private var handler: Handler = Handler()
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val bounds = ChangeBounds()
            bounds.duration = 280
            bounds.interpolator = DecelerateInterpolator()
            window.sharedElementEnterTransition = bounds
        }

        initData()
        initView()
    }

    private var observed: Boolean = false
    private fun initData() {
        day = intent?.getStringExtra(Key.DAY.name) ?: Utime.today()
        // 没有数据的时候，初始化默认的
        viewModel.getDbFlow(day).observe(this, Observer {
            if (it == null) {
                viewModel.insertDbFlow(DbFlow(0, UserHelper.getUser().id, day, "[]", "", ""))
                return@Observer
            }
            if (!observed) {
                observed = true
                initRecyclerView(it.items)
                initWeatherAndMemo(it)
            }
        })


    }

    private fun initWeatherAndMemo(flow: DbFlow) {
        viewModel.weather.value = flow.weather
        if (flow.weather.isEmpty() && isToday()) {
            loadWeather()
        }
        tvWeather.setOnClickListener {
            // 当天的才可以修改天气
            if (!isToday()) {
                return@setOnClickListener
            }
            startActivityForResult(Intent(context, CitySelectorActivity::class.java), 100)
        }

        viewModel.memo.value = flow.memo
        viewModel.memo.observe(this, Observer {
            if (it.isNotEmpty()) {
                delayUpdateMemo()
            }
        })
    }

    private fun delayUpdateMemo() {
        handler.removeCallbacks(updateDbMemo)
        handler.postDelayed(updateDbMemo, 800)
    }

    private fun isToday() = day == Utime.getDayWithFormatTwo()

    private fun loadWeather() {
        Network.weatherApi()
            .getWeather(CityHelper.getCity()?.cityCode ?: "101280601")
            .defaultSubscribe {
                val fbs = it.data?.forecast
                if (fbs != null && fbs.isNotEmpty()) {
                    val fb = fbs[0]
                    val sb = StringBuilder()
                    sb.append(it.cityInfo?.city)
                        .append(" ")
                        .append(fb.week)
                        .append(" ")
                        .append(fb.type)
                        .append(" (")
                        .append(fb.low)
                        .append(" ~ ")
                        .append(fb.high)
                        .append(") ")
                    viewModel.weather.value = sb.toString()
                    viewModel.updateDbFlowWeather(day, sb.toString())
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            100 -> {
                if (resultCode == Activity.RESULT_OK) {
                    loadWeather()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = day
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)

        intent?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv_header.transitionName = day
            }
        }

        Glide.with(context)
            .asBitmap()
            .load(ImageHelper.getBigImage(day))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_background)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
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
            })

        getKingSoftwareDaily(Utime.transferTwoToOne(day)) {
            tv_header.text = it.content
            tv_header.tag = it.note
            tv_header.visibility = View.VISIBLE
        }

        tv_header.setOnClickListener {
            val netString = it.tag
            if (netString is String) {
                val oldString = tv_header.text.toString()
                tv_header.text = netString
                tv_header.tag = oldString
            }
        }

        fab.setOnClickListener {
            addNewItem()
        }
    }

    private fun initRecyclerView(items: String) {
        itemList = FlowItemHelper.fromJsonArray(items)
        adapter = DetailAdapter(itemList)
        adapter.detailListener = getItemListener()
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
        collapsing_toolbar_layout.contentScrim = Ucolor.getDrawable(color)
        val transparentColor = Ucolor.getTransparentColor(color)
        tv_header.setBackgroundColor(transparentColor)

        // https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        Uscreen.setStatusBarColor(this, Color.parseColor(hexColor))
        fab.backgroundTintList = ColorStateList.valueOf(color)
        fab.setRippleColor(ColorStateList.valueOf(transparentColor))
    }


    private val updateDbTask = Runnable {
        viewModel.updateDbFlowItems(day, itemList)
    }
    private val updateDbMemo = Runnable {
        viewModel.updateDbFlowMemo(day, viewModel.memo.value ?: "")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.flow_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                addNewItem()
            }
            R.id.home -> {
                toHome()
            }
            R.id.copy -> {
                Usystem.doCopy(this, day, itemList, false)
            }
            R.id.share -> {
                Usystem.shareText(this, day, FlowItemHelper.toPrettyText(itemList))
            }
            R.id.share_to_wps -> {
                Usystem.doCopy(this, day, itemList, true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toHome() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    fun updateDb(vararg noDelay: Boolean) {
        if (noDelay.isNotEmpty() && noDelay[0]) {
            updateDbTask.run()
            return
        }
        updateUIAndDelayUpdateDb()
    }

    private fun updateUIAndDelayUpdateDb() {
        Utransfer.sortItems(itemList)
        adapter.notifyDataSetChanged()

        handler.removeCallbacks(updateDbTask)
        handler.postDelayed(updateDbTask, 800)
    }

    private fun addNewItem() {
        with(itemList) {
            val newItem = FlowItem()
            val startArr = Utime.getValidTime(null)
            var currentTime = Utime.getTimeString(startArr[0], startArr[1])

            if (this.size > 0) {
                // 当前时间已经存在，不在则新建
                if (itemList.any { it.timeStart == currentTime }) {
                    snackbar("该时间点已经有了一个哦").show()
                    return
                }
                // 以上一个的结束时间作为当前的开始时间
                val item = this[0]
                if (!TextUtils.isEmpty(item.timeEnd)) {
                    currentTime = item.timeEnd
                }
            }
            newItem.timeStart = currentTime
            add(newItem)
            updateUIAndDelayUpdateDb()
            recyclerView.scrollToPosition(0)
        }
    }

    private fun getItemListener() = object :
        OnDetailListener {
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
                    updateUIAndDelayUpdateDb()
                }
                .show()
        }

        override fun onClickTimeStart(item: FlowItem, position: Int) {
            // 原文链接：https://blog.csdn.net/qq_17009881/article/details/75371406
            val listener =
                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                    item.timeStart = Utime.getTimeString(hourOfDay, minute)
                    updateUIAndDelayUpdateDb()
                }
            Udialog.showTimePicker(context, listener, Utime.getValidTime(item.timeStart))
        }

        override fun onClickTimeEnd(item: FlowItem, position: Int) {
            val listener =
                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                    item.timeEnd = Utime.getTimeString(hourOfDay, minute)
                    updateUIAndDelayUpdateDb()
                }
            Udialog.showTimePicker(context, listener, Utime.getValidTime(item.timeEnd))
        }

        override fun onLongClickTimeStart(item: FlowItem, position: Int) {
            Udialog.AlertOneItem.builder(context)
                .consumer {
                    if (it) {
                        item.timeStart = null
                        updateUIAndDelayUpdateDb()
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
                        updateUIAndDelayUpdateDb()
                    }
                }
                .message("清空结束时间")
                .show();
        }

        override fun onTextChanged(item: FlowItem, s: CharSequence, position: Int) {
            item.content = s.toString()
            // 只需要更新数据即可
            updateDb(true)
        }

        override fun onEditTextFocused(hasFocus: Boolean, item: FlowItem, position: Int) {

        }

    }

}
