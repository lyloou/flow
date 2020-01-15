package com.lyloou.flow.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R

class MineFragment : Fragment() {

    private lateinit var mineViewModel: MineViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mineViewModel =
            ViewModelProviders.of(this).get(MineViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mine, container, false)
        val textView: TextView = root.findViewById(R.id.text_mine)
        mineViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}