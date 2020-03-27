package com.lyloou.flow.ui.list

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.lyloou.flow.R
import com.lyloou.flow.extension.snackbar
import com.lyloou.flow.model.Daily
import com.lyloou.flow.model.UserHelper.isNotLogin
import com.lyloou.flow.net.getKingSoftwareDaily
import com.lyloou.flow.repository.DbFlow
import com.lyloou.flow.repository.toPrettyText
import com.lyloou.flow.ui.detail.DetailActivity
import com.lyloou.flow.ui.kalendar.KalendarActivity
import com.lyloou.flow.util.Uapp
import com.lyloou.flow.util.Ucolor
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Usystem
import com.lyloou.flow.widget.TitleViewPagerAdapter
import kotlinx.android.synthetic.main.dialog_sync.view.*
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(), OnItemLongClickListener {

    private lateinit var viewModel: ListViewModel
    private lateinit var partActive: Part
    private lateinit var partArchived: Part

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
        partActive = Part(context, "进行中")
        partArchived = Part(context, "已归档")
        partActive.adapter.onItemLongClickListener = this
        partArchived.adapter.onItemLongClickListener = this

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

        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)

        getKingSoftwareDaily("") {
            initIvHeader(it)
        }

        tv_header.setOnClickListener {
            val netString = it.tag
            if (netString is String) {
                val oldString = tv_header.text.toString()
                tv_header.text = netString
                tv_header.tag = oldString
            }
        }

    }

    private fun toDetail() {
        startActivity(Intent(activity, DetailActivity::class.java))
    }

    private fun initIvHeader(daily: Daily) {
        tv_header.text = daily.content
        tv_header.tag = daily.note
        tv_header.visibility = View.VISIBLE
        Glide.with(context!!).asBitmap()
            .load(daily.picture)
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
                toKalendar()
            }
            R.id.menu_sync_all_tab -> {
                syncData()
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

    private fun toKalendar() {
        if (isNotLogin(requireActivity())) return
        startActivity(Intent(context, KalendarActivity::class.java))
    }

    private fun syncData() {
        if (isNotLogin(requireActivity())) return
        var synced = false
        viewModel.getDbFlowsBySyncStatus(false)
            .observe(requireActivity(), Observer {
                if (synced) {
                    return@Observer
                }
                synced = true
                if (it.size == 0) {
                    Toast.makeText(context, "无需同步", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
                showSyncDialog(it)
            })
    }

    @SuppressLint("InflateParams")
    private fun showSyncDialog(flowList: MutableList<DbFlow>) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_sync, null, false)
        dialog.setContentView(view)
        dialog.show()

        val btnSync = view.btnSync
        btnSync?.setOnClickListener {
            btnSync.isEnabled = false
            viewModel.syncFlows(flowList, object : SyncListener {
                override fun handle(result: SyncResult) {
                    view.tvAll?.text = result.all.toString()
                    view.tvSuccess?.text =
                        result.successNum.toString()
                    view.tvFail?.text = result.failNum.toString()
                    view.tvFailMemo?.text =
                        if (result.failMemo.isEmpty()) "无" else result.failMemo
                }

                override fun progress(
                    all: Int,
                    successNum: Int,
                    failedNum: Int
                ) {
                    view.tvAll?.text = all.toString()
                    view.tvSuccess?.text = successNum.toString()
                    view.tvFail?.text = failedNum.toString()
                    view.progressBar?.progress =
                        100 * (successNum + failedNum) / all
                }

            })
        }

    }


    private fun addShortcut() {
        Uapp.addShortCutCompat(
            requireActivity(),
            DetailActivity::class.java.canonicalName,
            "flow_time_day",
            R.mipmap.lyloou,
            resources.getString(R.string.flow_time_day)
        )
        val snackbar: Snackbar = snackbar("已添加到桌面", Snackbar.LENGTH_LONG)
        snackbar.setAction("添加失败？去授权") {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.data = Uri.fromParts("package", context!!.packageName, null);
            startActivity(intent);
        }
        snackbar.show()
    }

    override fun invoke(flow: DbFlow) {
        val content = if (flow.isArchived) "恢复到进行中" else "加入归档"
        Udialog.AlertMultiItem.builder(context)
            .add("复制内容") {
                Usystem.copyString(context!!, flow.toPrettyText())
                snackbar("已复制到剪切板").show()
            }
            .add(content) {
                viewModel.updateDbFlowArchiveStatus(arrayOf(flow.day), !flow.isArchived)
                snackbar("已$content").show()
            }
            .add("删除") {
                viewModel.deleteDbFlow(flow)
                snackbar("已删除").show()
            }
            .show()
    }
}