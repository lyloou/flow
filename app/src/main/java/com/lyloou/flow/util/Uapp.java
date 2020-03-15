package com.lyloou.flow.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

public class Uapp {

    /**
     * //    原文链接：https://blog.csdn.net/lyabc123456/article/details/86716857
     * 添加桌面快捷方式
     *
     * @param context
     * @param className 快捷方式的目标类(全包名的路径)
     * @param id        快捷方式对应的id
     * @param iconResId 快捷方式的图标资源
     * @param label     显示名称
     */
    public static void addShortCutCompat(Activity context, String className, String id, int iconResId, String label) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        String installShortcut = Manifest.permission.INSTALL_SHORTCUT;

        if (ContextCompat.checkSelfPermission(context, installShortcut)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String[] permissions = {installShortcut};
        ActivityCompat.requestPermissions(context, permissions, 0);

        Intent shortcutInfoIntent = new Intent();
        shortcutInfoIntent.setClassName(context, className);
        shortcutInfoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutInfoIntent.setAction(Intent.ACTION_VIEW);
        addShortCutCompatWithoutCheckPermission(context, shortcutInfoIntent, id, iconResId, label);
    }

    /**
     * 添加桌面快捷方式
     *
     * @param context
     * @param shortcutInfoIntent 点击快捷方式时的启动目标intent
     * @param id                 快捷方式对应的id
     * @param iconResId          快捷方式的图标资源
     * @param label
     */
    public static void addShortCutCompatWithoutCheckPermission(Context context, Intent shortcutInfoIntent, String id, int iconResId, String label) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, id)
                    .setIcon(IconCompat.createWithResource(context, iconResId))
                    .setShortLabel(label)
                    .setIntent(shortcutInfoIntent)
                    .build();
            //这里第二个参数可以传一个回调，用来接收当快捷方式被创建时的响应
            ShortcutManagerCompat.requestPinShortcut(context, info, null);
        }
    }

}
