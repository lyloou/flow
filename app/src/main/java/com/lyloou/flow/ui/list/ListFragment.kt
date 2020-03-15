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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.lyloou.flow.R
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.kingSoftwareApi
import com.lyloou.flow.ui.detail.DetailActivity
import com.lyloou.flow.ui.kalendar.KalendarActivity
import com.lyloou.flow.ui.web.NormalWebViewActivity
import com.lyloou.flow.util.Uapp
import com.lyloou.flow.util.Ucolor
import com.lyloou.flow.util.Utime
import com.lyloou.flow.util.Uview
import com.lyloou.flow.widget.TitleViewPagerAdapter
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
        val partActive = Part(context, "进行中")
        val partArchived = Part(context, "已归档")

        viewModel.activeDbFlowList.observe(requireActivity(), Observer {
            it?.let {
                partActive.adapter.submitList(it)
            }
        })
        viewModel.archivedDbFlowList.observe(requireActivity(), Observer {
            it?.let {
                partArchived.adapter.submitList(it)
            }
        })

        val pagerAdapter = TitleViewPagerAdapter()
        pagerAdapter.addView(partActive.title, partActive.view)
        pagerAdapter.addView(partArchived.title, partArchived.view)
        vpFlow.adapter = pagerAdapter

        tabLayout.setupWithViewPager(vpFlow)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
    }

    private fun initToolbar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.supportActionBar?.title = resources.getString(R.string.app_name);
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
                addShortcut()
            }
            R.id.menu_today_flow_time -> {
                toDetail()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addShortcut() {
        Uapp.addShortCutCompat(
            requireActivity(),
            DetailActivity::class.java.canonicalName,
            "flow_time_day",
            R.mipmap.ic_launcher_round,
            resources.getString(R.string.flow_time_day)
        )
        val snackbar: Snackbar =
            Snackbar.make(requireView(), "已添加到桌面", Snackbar.LENGTH_LONG)
        snackbar.setAction("了解详情") {
            NormalWebViewActivity.newInstance(
                context,
                "https://kf.qq.com/touch/sappfaq/180705A3IB3Y1807056fMr6V.html"
            )
        }
        snackbar.show()
    }
}