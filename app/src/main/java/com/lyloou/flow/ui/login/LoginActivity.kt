package com.lyloou.flow.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import com.lyloou.flow.databinding.ActivityLoginBinding
import com.lyloou.flow.model.CResult
import com.lyloou.flow.model.User
import com.lyloou.flow.repository.FlowNetWork

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun login(v: View) {
        if (viewModel.name.value.isNullOrEmpty()) {
            toast("用户名为空")
            return
        }
        if (viewModel.password.value.isNullOrEmpty()) {
            toast("密码为空")
            return
        }

        viewModel.login(
            viewModel.name.value!!,
            viewModel.password.value!!,
            {
                if (it.err_code == 0) {
                    Log.i("TTAG", "user: ${it.data}");
                    doSuccess(it)
                } else {
                    toast("错误代码:${it.err_code}，错误信息：${it.err_msg}")
                }
            },
            {
                toast("网络异常：${it.message}")
            })
    }

    private fun doSuccess(it: CResult<User?>) {
        if (it.data == null) {
            toast("出现了不可思议的BUG")
            return
        }

        val user: User = it.data!!
        toast("登录成功：${user.name}")

        viewModel.saveUserInfo(user)

        // 第一从网络获取数据
        val workRequest = OneTimeWorkRequestBuilder<FlowNetWork>().build()
        WorkManager.getInstance(this).enqueue(workRequest)

        // 退出
        onBackPressed()
    }


}
