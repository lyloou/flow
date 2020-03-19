package com.lyloou.flow.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.lyloou.flow.common.Consumer;

import java.util.List;

public class Uapp {
    public static void handlePackageIntent(Context context, String packageName, Consumer<Intent> intentConsumer) {
        PackageManager packageManager = context.getPackageManager();
        if (Uapp.checkPackInfo(context, packageName)) {
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            intentConsumer.accept(intent);
        } else {
            intentConsumer.accept(null);
        }
    }

    /**
     * 检查包是否存在
     *
     * @param packname 包名
     * @return 结果
     */
    public static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

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

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isRunOnForeground(Context context) {
        // Returns a list of application processes that are running on the device
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;


        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            Log.i("TTAG", "isRunOnForeground: ${appProcess.processName}:::" + appProcess.processName);
        }

        return false;
    }
}
