package com.lyloou.flow.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.extension.apiForCResult
import com.lyloou.flow.model.User
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.userWithAuthApi

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val user: MutableLiveData<User> = MutableLiveData(UserHelper.getUser())

    fun saveUser() {
        user.value?.let {
            UserHelper.saveUser(it)
        }
    }

    fun updateUserInfo(
        user: User,
        okFun: (String?) -> Unit,
        failFun: (String) -> Unit
    ) {
        apiForCResult(
            { Network.userWithAuthApi().update(user) },
            okFun,
            failFun
        )
    }
}