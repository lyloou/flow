package com.lyloou.flow.temp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import com.lyloou.flow.databinding.ActivityTempBinding

class TempActivity : AppCompatActivity() {

    private lateinit var viewModel: TempViewModel;
    private lateinit var binding: ActivityTempBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_temp)
        viewModel = ViewModelProviders.of(this).get(TempViewModel::class.java)
        viewModel.stringData.observe(this, Observer {
            Toast.makeText(this, "changed:$it", Toast.LENGTH_SHORT).show()
        })
        binding.data = viewModel
        binding.lifecycleOwner = this
    }
}
