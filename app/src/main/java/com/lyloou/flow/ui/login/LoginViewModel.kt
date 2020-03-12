package com.lyloou.flow.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lyloou.flow.model.UserResult
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.flowApi
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
        Network.flowApi()
            .login(name, encodedPassword)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(okFun, failFun)
    }
}