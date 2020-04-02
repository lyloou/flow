package com.lyloou.flow.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.lyloou.flow.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Ufile {

    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
    }

    public static Uri getCacheFileUri(Context context, String dir, String fileName) {
        File cacheDir = new File(context.getExternalCacheDir(), dir);
        if (!cacheDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            cacheDir.mkdirs();
        }

        File file = new File(cacheDir, fileName);
        try {
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getUriForFile(context, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 把 Bitmap 文件压缩后保存回原文件，并返回 file 类型的 uri
     */
    public static Uri compressBitmapFile(Context context, File imgFile, int quality) {
        try {
            Uri uri = Ufile.getUriForFile(context, imgFile);
            Bitmap bm = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            FileOutputStream fos = new FileOutputStream(imgFile);
            bm.compress(Bitmap.CompressFormat.PNG, quality, fos);
            fos.flush();
            fos.close();
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
