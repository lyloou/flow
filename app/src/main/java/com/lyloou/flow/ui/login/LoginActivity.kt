package com.lyloou.flow.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lyloou.flow.R
import com.lyloou.flow.databinding.ActivityLoginBinding
import com.lyloou.flow.model.User
import com.lyloou.flow.model.UserResult
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
        supportActionBar?.setDisplayShowHomeEnabled(true);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun login(view: View) {
        if (viewModel.name.value.isNullOrEmpty()) {
            Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show()
            return
        }
        if (viewModel.password.value.isNullOrEmpty()) {
            Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(
                        this,
                        "错误代码:${it.err_code}，错误信息：${it.err_msg}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            {
                Toast.makeText(
                    this,
                    "网络异常：${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            })
    }

    private fun doSuccess(it: UserResult) {
        if (it.data == null) {
            Toast.makeText(this, "出现了不可思议的BUG", Toast.LENGTH_SHORT).show()
            return
        }

        val user: User = it.data!!
        Toast.makeText(this, "登录成功：${user.name}", Toast.LENGTH_SHORT).show()

        viewModel.saveUserInfo(user)

        // 第一从网络获取数据
        val workRequest = OneTimeWorkRequestBuilder<FlowNetWork>().build()
        WorkManager.getInstance(this).enqueue(workRequest)

        // 退出
        onBackPressed()
    }


}
