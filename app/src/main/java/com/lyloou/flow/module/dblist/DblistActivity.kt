package com.lyloou.flow.module.dblist

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lyloou.flow.R
import com.lyloou.flow.common.BaseCompatActivity
import com.lyloou.flow.common.Url
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.net.KingsoftwareAPI
import com.lyloou.flow.net.Network
import com.lyloou.flow.util.Ucolor
import com.lyloou.flow.util.Uscreen
import com.lyloou.flow.util.Utime
import com.lyloou.flow.util.Uview
import com.lyloou.flow.widget.ItemOffsetDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_dblist.*


class DblistActivity : BaseCompatActivity() {

    private lateinit var viewModel: DbflowViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dblist)
        viewModel = ViewModelProviders.of(this).get(DbflowViewModel::class.java)
        initView()

        val adapter = DbflowAdapter()
        recyclerView.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.addItemDecoration(ItemOffsetDecoration(dp2px(16f)))

            viewModel.dbFlowDayList.observe(this@DblistActivity, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        }

    }

    private fun initView() {
        setSupportActionBar(toolbar);
        supportActionBar?.title = "夫路";
        toolbar.setNavigationOnClickListener { onBackPressed() };
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)
        Uview.toggleViewVisibleWhenAppBarLayoutScrollChanged(app_bar, tv_header)

        Network.get(Url.Kingsoftware.url, KingsoftwareAPI::class.java)
            .getDaily(Utime.transferTwoToOne(Utime.getDayWithFormatOne()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tv_header.text = it.content
                tv_header.tag = it.note
                tv_header.visibility = View.VISIBLE
                initIvHeader(it.picture)
            }, Throwable::printStackTrace)

        tv_header.setOnClickListener {
            val netString = it.tag
            if (netString is String) {
                val oldString = tv_header.text.toString()
                tv_header.text = netString
                tv_header.tag = oldString
            }
        };

    }

    private fun initIvHeader(url: String?) {
        Glide.with(context).asBitmap()
            .load(url)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop().into(object : CustomTarget<Bitmap>() {
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
    }

    private fun resetThemeColor(color: Int) {
        val transparentColor = Ucolor.getTransparentColor(color)
        tv_header.setBackgroundColor(transparentColor)
        // https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        Uscreen.setStatusBarColor(this, Color.parseColor(hexColor))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.navigationIcon?.mutate()?.let {
                it.setTint((transparentColor))
                toolbar.navigationIcon = it
            }
        }
    }
}
