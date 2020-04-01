package com.lyloou.flow.util;


import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.lyloou.flow.BuildConfig;

import java.io.File;

public class Ufile {

    public static Uri getUriByProvider(Context context, File file) {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
    }

    public static Uri getUriByUri(File file) {
        return Uri.fromFile(file);
    }

    public static final String TMP_DIR = Environment.getExternalStorageDirectory() + "/flow/";

    public static File getFile(String fileName) {
        File tmpDir = new File(TMP_DIR);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File file = new File(tmpDir.getAbsolutePath(), fileName);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }
}
