package com.lyloou.flow.temp

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.lyloou.flow.R
import com.lyloou.flow.databinding.ActivityTempBinding
import com.lyloou.flow.util.ImageHelper
import kotlinx.android.synthetic.main.activity_temp.*
import kotlinx.android.synthetic.main.fragment_dbdetail.*

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

        intent?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val transitionName = it.getStringExtra("day")
                imageView2.transitionName = transitionName

            }
            Glide.with(this)
                .load(ImageHelper.getBigImage(it.getStringExtra("day")))
                .into(imageView2)

        }

        binding.data = viewModel
        binding.lifecycleOwner = this

    }

//    override fun onBackPressed() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            finishAfterTransition()
//        } else {
//            finish()
//        }
//    }
}
