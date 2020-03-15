/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.flow.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public final class LouDialog {
    private final SparseArray<View> mViews;
    private final Dialog mDialog;
    private final View mLayoutView;

    private LouDialog(Context context, int layoutId, int themeId) {
        this(context, LayoutInflater.from(context).inflate(layoutId, null), themeId);
    }

    private LouDialog(Context context, View view, int themeId) {
        mLayoutView = view;
        mDialog = new Dialog(context, themeId);
        mDialog.setContentView(mLayoutView);
        mViews = new SparseArray<>();
    }

    /**
     * @param context
     * @param layoutId
     * @param themeId  如果使用默认主题，输入0即可；
     * @return
     */
    public static LouDialog newInstance(Context context, int layoutId, int themeId) {
        return new LouDialog(context, layoutId, themeId);
    }

    /**
     * @param context
     * @param view
     * @param themeId
     * @return
     */
    public static LouDialog newInstance(Context context, View view, int themeId) {
        return new LouDialog(context, view, themeId);
    }

    public LouDialog setDimAmount(float amount) {
        if (mDialog != null) {
            mDialog.getWindow().setDimAmount(amount);
        }
        return this;
    }

    public LouDialog setCancelable(boolean cancelable) {
        if (mDialog != null) {
            mDialog.setCancelable(cancelable);
        }
        return this;
    }

    /**
     * 设置弹框位置以及弹框透明度；
     *
     * @param gravity
     * @param x       单位：px；
     * @param y       单位：px；
     * @return
     */
    public LouDialog setPositionAndAlpha(int gravity, int x, int y, float alpha) {
        if (mDialog != null) {
            Window window = mDialog.getWindow();
            window.setGravity(gravity);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.x = x;
            lp.y = y;
            lp.alpha = alpha;
            window.setAttributes(lp);
        }
        return this;
    }

    public LouDialog setWH(int width, int height) {
        if (mDialog != null) {
            Window window = mDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            if (width != -1)
                lp.width = width;
            if (height != -1)
                lp.height = height;
            window.setAttributes(lp);
        }
        return this;
    }

    /**
     * 设置进入和退出动画；
     *
     * @param styleId
     * @return
     */
    public LouDialog setWindowAnimation(int styleId) {
        if (mDialog != null) {
            mDialog.getWindow().setWindowAnimations(styleId);
        }
        return this;
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (!isShowing()) {
            mDialog.show();
        }
    }

    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * 判断是否正在显示
     *
     * @return
     */
    public boolean isShowing() {
        boolean isShowing = false;
        if (mDialog != null && mDialog.isShowing()) {
            isShowing = true;
        }
        return isShowing;
    }

    /**
     * 隐藏对话框
     */
    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 获取具体的某个视图；
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mDialog.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getLayoutView() {
        return mLayoutView;
    }

    public LouDialog setVisible(int viewId, boolean show) {
        View v = getView(viewId);
        if (show) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
        return this;
    }

    public LouDialog putText(int viewId, String text) {
        View v = getView(viewId);
        if (v instanceof TextView) {
            ((TextView) v).setText(text);
        }
        return this;
    }

    public LouDialog putImg(int viewId, int resId) {
        View v = getView(viewId);
        if (v instanceof ImageView) {
            ((ImageView) v).setImageResource(resId);
        }
        return this;
    }

    public LouDialog putImg(int viewId, Bitmap bitmap) {
        View v = getView(viewId);
        if (v instanceof ImageView) {
            ((ImageView) v).setImageBitmap(bitmap);
        }
        return this;
    }

    public LouDialog putImg(int viewId, Drawable drawable) {
        View v = getView(viewId);
        if (v instanceof ImageView) {
            ((ImageView) v).setImageDrawable(drawable);
        }
        return this;
    }

    public interface DialogClickListener {
        void click(View v);
    }

}