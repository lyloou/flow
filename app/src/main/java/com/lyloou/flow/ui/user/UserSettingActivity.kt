package com.lyloou.flow.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import com.lyloou.flow.model.UserHelper
import com.lyloou.flow.util.PictureHelper
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Ufile
import com.lyloou.flow.widget.SettingLayout
import com.lyloou.flow.widget.SettingLayout.Item
import com.lyloou.flow.widget.ToolbarManager
import kotlinx.android.synthetic.main.item_avatar.view.*
import kotlinx.android.synthetic.main.item_toolbar.*
import kotlinx.android.synthetic.main.user_setting_activity.*


class UserSettingActivity : AppCompatActivity(), SettingLayout.IClickListener, ToolbarManager {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_setting_activity)

        initView()
    }

    private fun takePhoto() {
        Udialog.AlertMultiItem.builder(this)
            .title("更换头像")
            .add("拍照") { PictureHelper.takePicture(this, file) }
            .add("从相册选择") { PictureHelper.getFromAlbum(this) }
            .show()
    }


    private val file = Ufile.getFile("icon.png")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PictureHelper.REQUEST_CODE_PAIZHAO -> {
                val uri = data?.data
                if (uri == null) {
                    toast("图片无效")
                    return
                }
                PictureHelper.crop(this, uri, file, 350, 350);
            }
            PictureHelper.REQUEST_CODE_ZHAOPIAN -> {
                val uri = data?.data
                if (uri == null) {
                    toast("图片无效")
                    return
                }
                PictureHelper.crop(this, uri, file, 350, 350);
            }
            PictureHelper.REQUEST_CODE_CAIQIE -> {
                toast("${file}")
                // 可以上传保存了
                ivAvatar?.let {
                    Glide.with(this)
                        .load(Ufile.getUriByProvider(this, file))
                        .into(it)
                }
            }

        }
    }


    var ivAvatar: ImageView? = null
    private fun initView() {
        setSupportActionBar(toolbar)
        enableHomeAsUp { onBackPressed() }
        whiteToolbarText()

        val user = UserHelper.getUser()

        @SuppressLint("InflateParams")
        val vAvatar = layoutInflater.inflate(R.layout.item_avatar, null, false)
        ivAvatar = vAvatar.ivAvatar
        Glide.with(this).load(user.avatar)
            .placeholder(R.drawable.ic_very_satisfied)
            .apply(RequestOptions().circleCrop())
            .into(vAvatar.ivAvatar)
        vAvatar.setOnClickListener {
            takePhoto()
        }

        settingLayout
            .addCustomView(vAvatar)
            .addItem(
                Item(
                    R.string.user_name,
                    contentStr = user.name,
                    hasToRight = false,
                    listener = this
                )
            )
            .addItem(Item(R.string.user_nickname, contentStr = user.nickname, listener = this))
            .addItem(Item(R.string.user_avatar, contentStr = user.avatar, listener = this))
            .addItem(Item(R.string.user_phone, contentStr = "${user.phone}", listener = this))
            .addItem(Item(R.string.user_ps, contentStr = user.personalSignature, listener = this))
    }

    override fun invoke(item: Item) {

    }

    override val toolbar: Toolbar
        get() = myToolbar


}