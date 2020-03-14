package com.lyloou.flow.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import com.lyloou.flow.databinding.FragmentMineBinding
import com.lyloou.flow.model.Cache
import com.lyloou.flow.model.DEFAULT_USER
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.repository.FlowDatabase
import com.lyloou.flow.ui.login.LoginActivity
import com.lyloou.flow.ui.setting.SettingsActivity
import com.lyloou.flow.ui.web.NormalWebViewActivity
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : Fragment() {

    private lateinit var viewModel: MineViewModel
    lateinit var binding: FragmentMineBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(MineViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false)
        binding.data = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initItem()
    }

    private fun initItem() {
        ivAvatar.setOnClickListener {
            if (UserHelper.getUser().id == DEFAULT_USER.id) {
                startActivity(Intent(context, LoginActivity::class.java))
            }
            Cache.clear()
            viewModel.refreshData()
            Thread {
                FlowDatabase.getInstance(context!!.applicationContext).clearAllTables()
            }.start()
        }

        tvFollow.setOnClickListener {
            NormalWebViewActivity.newInstance(
                context,
                "https://voice.baidu.com/act/newpneumonia/newpneumonia/?from=osari_pc_1"
            )
        }

        tvSetting.setOnClickListener {
            startActivity(Intent(context, SettingsActivity::class.java))
        }
    }
}