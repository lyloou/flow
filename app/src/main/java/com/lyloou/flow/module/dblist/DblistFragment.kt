package com.lyloou.flow.module.dblist


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.lyloou.flow.R
import com.lyloou.flow.common.Url
import com.lyloou.flow.extension.dp2px
import com.lyloou.flow.net.KingsoftwareAPI
import com.lyloou.flow.net.Network
import com.lyloou.flow.util.*
import com.lyloou.flow.widget.ItemOffsetDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_dblist.*

/**
 * A simple [Fragment] subclass.
 */
class DblistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dblist, container, false)
    }

    private lateinit var viewModel: DbflowViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(DbflowViewModel::class.java)
        initView()

        val adapter = DbflowAdapter()
        recyclerView.apply {
            startPostponedEnterTransition()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.addItemDecoration(ItemOffsetDecoration(requireContext().dp2px(16f)))
            viewModel.dbFlowDayList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        }
    }

    private fun initView() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        tv_title.text = "夫路"
        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)
        Uview.toggleViewVisibleWhenAppBarLayoutScrollChanged(app_bar, tv_header)

        Glide.with(requireActivity())
            .asBitmap()
            .load(ImageHelper.getBigImage(Utime.today()))
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
                        palette.getMutedColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorAccent
                            )
                        )
                    resetThemeColor(color)

                }
            });

        Network.get(Url.Kingsoftware.url, KingsoftwareAPI::class.java)
            .getDaily(Utime.transferTwoToOne(Utime.getDayWithFormatOne()))
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

    }

    private fun resetThemeColor(color: Int) {
        val transparentColor = Ucolor.getTransparentColor(color)
        tv_header.setBackgroundColor(transparentColor)
        // [Android Material Design - How to change background color of Toolbar after CollapsingToolbarLayout is collapsed - Stack Overflow](https://stackoverflow.com/questions/30619598/android-material-design-how-to-change-background-color-of-toolbar-after-collap)

        // https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        Uscreen.setStatusBarColor(requireActivity(), Color.parseColor(hexColor))
    }

}
