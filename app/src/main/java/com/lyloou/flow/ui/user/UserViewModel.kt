package com.lyloou.flow.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lyloou.flow.extension.apiForCResult
import com.lyloou.flow.model.User
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.model.UserPassword
import com.lyloou.flow.model.UserPasswordHelper
import com.lyloou.flow.net.Network
import com.lyloou.flow.net.userWithAuthApi
import com.lyloou.flow.util.PasswordUtil

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

    fun updateUserPassword(
        oldPassword: String,
        newPassword: String,
        okFun: (String?) -> Unit,
        failFun: (String) -> Unit
    ) {
        val encodedOldPassword = PasswordUtil.getEncodedPassword(oldPassword)
        val encodedNewPassword = PasswordUtil.getEncodedPassword(newPassword)
        apiForCResult(
            {
                Network.userWithAuthApi().resetPassword(encodedOldPassword, encodedNewPassword)
            },
            {
                // 更新app里的密码
                val userPassword = UserPasswordHelper.getUserPassword()!!
                UserPasswordHelper.saveUserPassword(
                    UserPassword(userPassword.userId, userPassword.name, encodedNewPassword)
                )
                // 回传给调用者
                okFun(it)
            },
            failFun
        )
    }
}