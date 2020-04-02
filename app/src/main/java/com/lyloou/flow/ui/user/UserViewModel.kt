package com.lyloou.flow.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.model.User
import com.lyloou.flow.model.UserHelper

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val user: MutableLiveData<User> = MutableLiveData(UserHelper.getUser())

    fun saveUser() {
        user.value?.let {
            UserHelper.saveUser(it)
        }
    }
}