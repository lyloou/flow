package com.lyloou.flow.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.lyloou.flow.model.FlowItem
import com.lyloou.flow.model.FlowItemHelper.toPrettyText

object Usystem {
    fun doCopy(context: Activity, day: String, list: MutableList<FlowItem>, jumpNow: Boolean) {
        val items = toPrettyText(list)
        val content = day + "\n" + items
        copyString(context, content)
        if (jumpNow) {
            toWps(context)
            return
        }
        val snackbar = Snackbar.make(Uview.getRootView(context), "复制成功", Snackbar.LENGTH_LONG)
        snackbar.setAction("跳转到便签") { toWps(context) }
        snackbar.show()
    }

    private fun toWps(context: Context) {
        val packageName = "cn.wps.note"
        Uapp.handlePackageIntent(context, packageName) { intent: Intent? ->
            if (intent == null) {
                Toast.makeText(context, "没有安装$packageName", Toast.LENGTH_SHORT).show()
                return@handlePackageIntent
            }
            context.startActivity(intent)
        }

    }

    fun copyString(context: Context, content: String?) {
        val manager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.setPrimaryClip(ClipData.newPlainText("test", content))
    }

    fun shareText(
        context: Context,
        subject: String?,
        shareBody: String?
    ) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context.startActivity(Intent.createChooser(sharingIntent, "分享文本"))
    }

    fun toAppSetting(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }
}