package com.lyloou.flow.ui.list

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lyloou.flow.R
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.kingSoftwareApi
import com.lyloou.flow.ui.detail.DetailActivity
import com.lyloou.flow.ui.kalendar.KalendarActivity
import com.lyloou.flow.util.Ucolor
import com.lyloou.flow.util.Utime
import com.lyloou.flow.util.Uview
import com.lyloou.flow.widget.ItemOffsetDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(requireActivity()).get(ListViewModel::class.java)
        initView()

        recyclerView.apply {
            val adapter = ListAdapter()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.addItemDecoration(ItemOffsetDecoration(dp2px(16f)))

            viewModel.dbFlowList.observe(requireActivity(), Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
    }

    private fun initToolbar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.supportActionBar?.title = "夫路";
    }

    private fun initView() {
        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)
        Uview.toggleViewVisibleWhenAppBarLayoutScrollChanged(app_bar, tv_header)

        Network.kingSoftwareApi()
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

    private fun toDetail() {
        startActivity(Intent(activity, DetailActivity::class.java))
    }

    private fun initIvHeader(url: String?) {
        Glide.with(context!!).asBitmap()
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
                        palette.getMutedColor(
                            ContextCompat.getColor(
                                context!!,
                                R.color.colorAccent
                            )
                        )
                    resetThemeColor(color)
                }
            })
    }

    private fun resetThemeColor(color: Int) {
        val transparentColor = Ucolor.getTransparentColor(color)
        // https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android
        // val hexColor = String.format("#%06X", 0xFFFFFF and color)
        // Uscreen.setStatusBarColor(activity, Color.parseColor(hexColor))

        tv_header.setBackgroundColor(transparentColor)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.flow_list, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_kalendar -> {
                startActivity(Intent(context, KalendarActivity::class.java))
            }
            R.id.menu_sync_all_tab -> {

            }
            R.id.menu_add_shortcut -> {

            }
            R.id.menu_today_flow_time -> {
                toDetail()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}