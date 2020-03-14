package com.lyloou.flow.ui.mine

import android.app.Application
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lyloou.flow.R
import com.lyloou.flow.model.User
import com.lyloou.flow.model.UserHelper

class MineViewModel(application: Application) : AndroidViewModel(application) {

    val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    fun refreshData() {
        user.value = UserHelper.getUser()
    }

}

@BindingAdapter("profileImage")
fun loadImage(iv: ImageView, imgUrl: String) {
    Glide.with(iv.context)
        .load(imgUrl)
        .placeholder(R.drawable.ic_very_satisfied)
        .apply(RequestOptions().circleCrop())
        .into(iv);
}

