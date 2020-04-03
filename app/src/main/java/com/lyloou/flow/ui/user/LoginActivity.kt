package com.lyloou.flow.ui.user

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import com.lyloou.flow.databinding.ActivityLoginBinding
import com.lyloou.flow.extension.simpleStartActivity
import com.lyloou.flow.extension.snackbar
import com.lyloou.flow.model.User
import com.lyloou.flow.repository.FlowNetWork
import com.lyloou.flow.ui.about.AboutActivity
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.item_toolbar.*

class LoginActivity : AppCompatActivity(), ToolbarManager {

    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar)
        whiteToolbarText()
        enableHomeAsUp { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        showView(R.id.login)
    }

    fun login(v: View) {
        Log.d("TTAG", "login: ${v.id}")
        val name = viewModel.name.value
        if (name.isNullOrEmpty()) {
            toast("用户名不能为空")
            return
        }
        val password = viewModel.password.value
        if (password.isNullOrEmpty()) {
            toast("密码不能为空")
            return
        }

        viewModel.login(name, password, ::doSuccess, ::toast)
    }

    private fun doSuccess(user: User?) {
        if (user == null) {
            toast("出现了不可思议的BUG")
            return
        }

        viewModel.saveUserInfo(user)

        // 第一从网络获取数据
        val workRequest = OneTimeWorkRequestBuilder<FlowNetWork>().build()
        WorkManager.getInstance(this).enqueue(workRequest)

        toast("欢迎您，${user.name}")

        // 退出
        onBackPressed()
    }

    fun register(view: View) {
        Log.d("TTAG", "register: ${view.id}")
        val name = viewModel.name.value
        if (name.isNullOrEmpty()) {
            toast("用户名不能为空")
            return
        }
        val password = viewModel.password.value
        if (password.isNullOrEmpty()) {
            toast("密码不能为空")
            return
        }
        val password2 = viewModel.password2.value
        if (password2.isNullOrEmpty()) {
            toast("确认密码不能为空")
            return
        }
        if (password != password2) {
            toast("两次密码不一致")
            return
        }

        viewModel.register(name, password, ::doSuccess, ::toast)
    }

    override val toolbar: Toolbar
        get() = myToolbar

    var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        showView(itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun showView(itemId: Int) {
        this.menu?.forEach { it.isVisible = false }
        when (itemId) {
            R.id.login -> {
                toolbarTitle = "登录"
                layoutLogin.visibility = View.VISIBLE
                layoutRegister.visibility = View.GONE
                this.menu?.findItem(R.id.register)?.isVisible = true
            }
            R.id.register -> {
                toolbarTitle = "注册"
                layoutLogin.visibility = View.GONE
                layoutRegister.visibility = View.VISIBLE
                this.menu?.findItem(R.id.login)?.isVisible = true
            }
        }
    }

    fun forgetPassword(view: View) {
        Log.d("TTAG", "register: ${view.id}")
        snackbar("请联系管理员").setAction("联系我") {
            simpleStartActivity<AboutActivity>()
        }.show()
    }
}
