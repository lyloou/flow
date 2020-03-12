package com.lyloou.flow.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.lyloou.flow.R
import com.lyloou.flow.databinding.ActivityLoginBinding
import com.lyloou.flow.model.UserResult

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
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
                if (it.err_code != 0) {
                    Toast.makeText(
                        this,
                        "错误代码:${it.err_code}，错误信息：${it.err_msg}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@login
                }
                doSuccess(it)
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
        Toast.makeText(
            this,
            "登录成功：${it.data?.name}",
            Toast.LENGTH_SHORT
        ).show()

        // TODO
        // 保存信息到 sp
        // 退出
        // 重新加载mine页面信息
    }
}
