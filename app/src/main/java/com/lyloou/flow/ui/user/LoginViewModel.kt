package com.lyloou.flow.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lyloou.flow.extension.apiForCResult
import com.lyloou.flow.model.*
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.userApi
import com.lyloou.flow.util.PasswordUtil

class LoginViewModel : ViewModel() {
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val password2: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun login(
        name: String,
        password: String,
        okFun: (User?) -> Unit,
        failFun: (String) -> Unit
    ) {
        apiForCResult(
            { Network.userApi().login(name, PasswordUtil.getEncodedPassword(password)) },
            okFun,
            failFun
        )
    }

    fun register(
        name: String,
        password: String,
        okFun: (User?) -> Unit,
        failFun: (String) -> Unit
    ) {
        val encodedPassword = PasswordUtil.getEncodedPassword(password)
        apiForCResult(
            { Network.userApi().register(UserRegister(name, encodedPassword)) },
            okFun,
            failFun
        )
    }


    fun saveUserInfo(user: User) {
        // 保存user信息
        UserHelper.saveUser(user)

        // 保存userPassword信息
        val password = PasswordUtil.getEncodedPassword(password.value)
        UserPasswordHelper.saveUserPassword(UserPassword(user.id, user.name, password))
    }
}