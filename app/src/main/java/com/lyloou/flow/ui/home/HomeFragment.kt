package com.lyloou.flow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_list.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.data = homeViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.supportActionBar?.title = "日程";

        homeViewModel.a.observe(this, Observer {
            homeViewModel.save()
        })
        homeViewModel.b.observe(this, Observer {
            homeViewModel.save()
        })
        homeViewModel.c.observe(this, Observer {
            homeViewModel.save()
        })
        homeViewModel.d.observe(this, Observer {
            homeViewModel.save()
        })
    }
}