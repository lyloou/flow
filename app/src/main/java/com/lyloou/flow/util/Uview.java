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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.07.11 13:28
 * <p>
 * Description:
 */
public class Uview {
    public static void initStatusBar(Activity context, int statusBarColor) {
        ImmersionBar.with(context)
                .statusBarDarkFont(false)
                .navigationBarDarkIcon(false)
                .statusBarColor(statusBarColor)
                .init();
    }

    public static View getRootView(Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    public static void toggleViewVisibleWhenAppBarLayoutScrollChanged(AppBarLayout appBarLayout, final View view) {
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                //Collapsed
                if (view.getVisibility() == View.GONE) {
                    return;
                }

                view.animate().alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                view.setVisibility(View.GONE);
                            }
                        })
                        .start();
            } else {
                //Expanded
                if (view.getVisibility() == View.VISIBLE) {
                    return;
                }
                view.setVisibility(View.VISIBLE);
                view.animate().alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                            }
                        })
                        .start();
            }
        });
    }


    // 双击 view 执行 runnable
    public static void setDoubleClickRunnable(View view, Runnable task) {
        new DoubleClick().click(view, task);
    }

    private static class DoubleClick {
        private int count = 0;

        // 双击 View 触发 task
        void click(View view, Runnable task) {
            view.setOnClickListener(v -> {
                if (++count >= 2) {
                    task.run();
                    return;
                }
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        count = 0;
                    }
                }, 500);
            });
        }
    }
}
