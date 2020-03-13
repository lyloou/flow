package com.lyloou.flow.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lyloou.flow.model.*
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.flowApiWithoutAuth
import com.lyloou.flow.util.PasswordUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel() {
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun login(
        name: String,
        password: String,
        okFun: (UserResult) -> Unit,
        failFun: (Throwable) -> Unit
    ) {
        val encodedPassword = PasswordUtil.getEncodedPassword(password)
        Network.flowApiWithoutAuth()
            .login(name, encodedPassword)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(okFun, failFun)
    }

    fun saveUserInfo(user: User) {
        // 保存user信息
        UserHelper.saveUser(user)

        // 保存userPassword信息
        val password = PasswordUtil.getEncodedPassword(password.value)
        UserPasswordHelper.saveUserPassword(UserPassword(user.id, user.name, password))
    }
}