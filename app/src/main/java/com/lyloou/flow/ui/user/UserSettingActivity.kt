package com.lyloou.flow.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import com.lyloou.flow.model.User
import com.lyloou.flow.ui.pic.ImagePickerActivity
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.widget.SettingLayout
import com.lyloou.flow.widget.SettingLayout.Item
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.dialog_user_reset_password.view.*
import kotlinx.android.synthetic.main.item_avatar.view.*
import kotlinx.android.synthetic.main.item_toolbar.*
import kotlinx.android.synthetic.main.user_setting_activity.*


class UserSettingActivity : AppCompatActivity(), SettingLayout.IClickListener, ToolbarManager {

    lateinit var viewModel: UserViewModel
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setContentView(R.layout.user_setting_activity)

        initView()
    }

    private fun takePhoto() {
        ImagePickerActivity.startForResult(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ImagePickerActivity.CROP_REQUEST_CODE -> {
                data?.let {
                    // TODO 上传到网络

                    // 清除缓存，由于url一样，它有可能会不刷新
                    ivAvatar?.setImageURI(null)
                    ivAvatar?.let {
                        loadCircleImage(it, data.data)
                    }

                    toast("已更新头像")
                }
            }
        }
    }


    var ivAvatar: ImageView? = null
    private fun initView() {
        setSupportActionBar(toolbar)
        enableHomeAsUp { onBackPressed() }
        whiteToolbarText()

        viewModel.user.observe(this, Observer {
            it?.let {
                this.user = it
                initViewWithData()
            }
        })
    }

    private fun initViewWithData() {
        @SuppressLint("InflateParams")
        val vAvatar = layoutInflater.inflate(R.layout.item_avatar, null, false)
        ivAvatar = vAvatar.ivAvatar
        loadCircleImage(vAvatar.ivAvatar, user.avatar)
        vAvatar.setOnClickListener {
            takePhoto()
        }

        settingLayout
            .addCustomView(vAvatar)
            .addItem(Item(R.string.user_name, contentStr = user.name, hasToRight = false))
            .addItem(Item(R.string.user_nickname, contentStr = user.nickname, listener = this))
            .addItem(Item(R.string.user_phone, contentStr = "${user.phone}", listener = this))
            .addItem(Item(R.string.user_ps, contentStr = user.personalSignature, listener = this))
            .addItem(Item(R.string.user_reset_password, listener = this))
    }

    private fun loadCircleImage(imageView: ImageView, url: Any?) {
        Glide.with(this).load(url)
            .placeholder(R.drawable.ic_very_satisfied)
            .apply(RequestOptions().circleCrop())
            .into(imageView)
    }

    override fun invoke(item: Item) {
        when (item.titleStrId) {
            R.string.user_reset_password -> {
                showResetPasswordDialog()
            }
            else -> {
                Udialog.AlertInputDialog.builder(this)
                    .title(resources.getString(item.titleStrId))
                    .defaultValue(item.contentStr)
                    .type(getInputType(item))
                    .requestFocus(true)
                    .consumer {
                        if (item.contentStr != it) {
                            this.menu?.findItem(R.id.confirm)?.isVisible = true
                            updateItem(item, it)
                        }
                    }
                    .show()
            }
        }
    }

    private fun showResetPasswordDialog(view: View = getResetPasswordView()) {
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view)
        }
        Udialog.AlertCustomViewDialog.builder(this)
            .view(view)
            .title(resources.getString(R.string.user_reset_password))
            .consumer { resetPassword(it) }
            .show()
    }

    private fun resetPassword(view: View) {
        val oldPassword = view.etOldPassword.text.toString()
        val newPassword = view.etNewPassword.text.toString()
        val newConfirmPassword = view.etNewConfirmPassword.text.toString()
        if (oldPassword.isEmpty() or newPassword.isEmpty() or newConfirmPassword.isEmpty()) {
            toast("密码不能为空")
            showResetPasswordDialog(view)
            return
        }
        if (newPassword != newConfirmPassword) {
            toast("输入的两次新密码不一致")
            showResetPasswordDialog(view)
            return
        }
        if (newPassword == oldPassword) {
            toast("新密码和旧密码不能一样")
            showResetPasswordDialog(view)
            return
        }
        viewModel.updateUserPassword(
            oldPassword,
            newConfirmPassword,
            {
                toast("更新成功")
            },
            {
                toast(it)
                showResetPasswordDialog(view)
            })
    }

    @SuppressLint("InflateParams")
    private fun getResetPasswordView(): View {
        return LayoutInflater.from(this).inflate(R.layout.dialog_user_reset_password, null, false)
    }

    private fun updateItem(item: Item, content: String) {
        item.contentStr = content
        when (item.titleStrId) {
            R.string.user_nickname -> user.nickname = item.contentStr ?: user.name
            R.string.user_ps -> user.personalSignature = item.contentStr ?: ""
            R.string.user_phone -> user.phone = item.contentStr?.toLong() ?: 0
            R.string.user_email -> user.email = item.contentStr ?: ""
        }
        settingLayout.refreshItem(item)
    }

    private fun getInputType(item: Item) = when (item.titleStrId) {
        R.string.user_phone -> {
            EditorInfo.TYPE_CLASS_NUMBER
        }
        else -> 0
    }

    override val toolbar: Toolbar
        get() = myToolbar

    var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_setting, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.confirm -> {
                // 上传成功后，保存到本地
                viewModel.updateUserInfo(user, {
                    viewModel.saveUser()
                    toast("已保存")
                }, {
                    toast(it)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }
}