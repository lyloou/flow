package com.lyloou.flow.ui.mine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lyloou.flow.R
import com.lyloou.flow.databinding.FragmentMineBinding
import com.lyloou.flow.extension.simpleStartActivity
import com.lyloou.flow.model.DEFAULT_USER
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.ui.about.AboutActivity
import com.lyloou.flow.ui.bookmark.BookmarkActivity
import com.lyloou.flow.ui.setting.SettingsActivity
import com.lyloou.flow.ui.user.LoginActivity
import com.lyloou.flow.ui.user.UserSettingActivity
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
            ViewModelProvider(this).get(MineViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false)
        binding.data = viewModel
        binding.lifecycleOwner = this
        viewModel.getCurrentTime().observe(requireActivity(), Observer {
            it?.let {
                Log.i("TAG", "onCreateView: $it");
            }
        })
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
                requireActivity().simpleStartActivity<LoginActivity>()
                return@setOnClickListener
            }
            requireActivity().simpleStartActivity<UserSettingActivity>()
        }

        cvSetting.setOnClickListener {
            requireActivity().simpleStartActivity<SettingsActivity>()
        }

        cvBookmark.setOnClickListener {
            requireActivity().simpleStartActivity<BookmarkActivity>()
        }

        cvAbout.setOnClickListener {
            requireActivity().simpleStartActivity<AboutActivity>()
        }


    }
}