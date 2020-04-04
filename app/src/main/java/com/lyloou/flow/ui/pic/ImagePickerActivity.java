package com.lyloou.flow.ui.pic;// [Android 拍照，从图库选择照片，并裁剪，上传到服务器 - 简书](https://www.jianshu.com/p/bfd9fe0592cb)
// [拍照/从相册读取图片后进行裁剪的方法 - developer_Kale - 博客园](http://www.cnblogs.com/tianzhijiexian/p/3989296.html)
// [Android 7.0适配 -- FileProvider 拍照、选择相册、裁切图片, 小米机型适配 - 简书](https://www.jianshu.com/p/bec4497c2a63)


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lyloou.flow.util.Udialog;
import com.lyloou.flow.util.Ufile;

import java.io.File;

public class ImagePickerActivity extends AppCompatActivity {
    private static final String TAG = ImagePickerActivity.class.getSimpleName();
    public static final String EXTRA_ERROR = "error";
    private int width;
    private int height;
    private File file;
    private Activity mContext;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    public static final int CROP_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        initIconFile();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            showDialog();
        } else {
            requestPermission();
        }
    }

    public static void startForResult(Activity context) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        context.startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    private void initIconFile() {

        // test Ufile.getCacheFileUri()
        // Ufile.getCacheFileUri(this, "/flow/", "hhh.txt");

        File flowDir = new File(getExternalCacheDir(), "/flow/");
        if (!flowDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            flowDir.mkdirs();
        }
        file = new File(flowDir.getAbsolutePath(), "icon.png");
        if (file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            showDialog();
        } else {
            //申请相机权限和STORAGE权限
            ActivityCompat.requestPermissions(
                    mContext,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                showDialog();
            } else {
                String msg = "没有相关运行权限";
                backWithError(msg);
            }
        }
    }

    private void showDialog() {
        Intent intent = getIntent();
        width = intent.getIntExtra("width", 0);
        height = intent.getIntExtra("height", 0);
        if (width == 0) {
            width = 350;
        }
        if (height == 0) {
            height = 350;
        }

        Udialog.AlertMultiItem.builder(this)
                .title("更换头像")
                .add("拍照", () -> PictureHelper.takePicture(mContext, file))
                .add("从相册选择", () -> PictureHelper.getFromAlbum(mContext))
                .cancelListener(dialog -> finish())
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            finish();
            return;
        }

        switch (requestCode) {
            case PictureHelper.REQUEST_CODE_CAPTURE:     // 从相机跳回来
                if (!file.exists()) {
                    backWithError("拍照异常");
                    return;
                }
                // 启动裁剪器
                PictureHelper.crop(mContext, Ufile.getUriForFile(mContext, file), file, width, height);
                break;
            case PictureHelper.REQUEST_CODE_PICK:     // 从图库跳回来
                if (data == null || data.getData() == null) {
                    finish();
                    return;
                }
                // 此处的uri 是content类型的。 还有一种是file 型的。应该转换为后者

                PictureHelper.crop(mContext, data.getData(), file, width, height);
                break;
            case PictureHelper.REQUEST_CODE_CROP:     // 从裁剪处跳回来
                backWithSuccess(Ufile.compressBitmapFile(this, file, 80));
                break;
        }
    }

    private void backWithSuccess(Uri uri) {
        Intent intent = new Intent();
        intent.setData(uri);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void backWithError(String errorMsg) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ERROR, errorMsg);
        intent.setData(null);
        setResult(RESULT_FIRST_USER, intent);
        finish();
    }


}