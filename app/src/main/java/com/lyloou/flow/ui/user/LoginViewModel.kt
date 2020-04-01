package com.lyloou.flow.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyloou.flow.common.toast
import com.lyloou.flow.model.*
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.defaultScheduler
import com.lyloou.flow.net.userApi
import com.lyloou.flow.net.userWithAuthApi
import com.lyloou.flow.util.PasswordUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    Network.userApi().login(name, PasswordUtil.getEncodedPassword(password))
                }
                if (result.err_code == 0) {
                    okFun(result.data)
                } else {
                    failFun(result.err_msg)
                }
            } catch (e: Exception) {
                failFun("${e.message}")
            }
        }
    }

    fun register(
        name: String,
        password: String,
        okFun: (User?) -> Unit,
        failFun: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val encodedPassword = PasswordUtil.getEncodedPassword(password)
                    Network.userApi().register(UserRegister(name, encodedPassword))
                }
                if (result.err_code == 0) {
                    okFun(result.data)
                } else {
                    failFun(result.err_msg)
                }
            } catch (e: Exception) {
                failFun("${e.message}")
            }
        }
    }

    fun updateUserInfo(user: User) {
        Network.userWithAuthApi()
            .update(user)
            .defaultScheduler()
            .subscribe({
                if (it.err_code == 0) {
                    // doSuccess
                } else {
                    // do error
                }
            }, {
                toast("网络异常：${it.message}")
            })
    }

    fun saveUserInfo(user: User) {
        // 保存user信息
        UserHelper.saveUser(user)

        // 保存userPassword信息
        val password = PasswordUtil.getEncodedPassword(password.value)
        UserPasswordHelper.saveUserPassword(UserPassword(user.id, user.name, password))
    }
}