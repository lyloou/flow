package com.lyloou.flow.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lyloou.flow.R;
import com.lyloou.flow.common.Consumer;

import java.util.ArrayList;
import java.util.List;

public class Udialog {
    public static class AlertOneItem {
        private Context context;
        private String title;
        private String message;
        private Consumer<Boolean> consumer = result -> {
        };
        private String positiveTips = "确定";
        private String negativeTips = "取消";

        private AlertOneItem(Context context) {
            this.context = context;
        }

        public static AlertOneItem builder(Context context) {
            return new AlertOneItem(context);
        }

        public AlertOneItem consumer(Consumer<Boolean> consumer) {
            this.consumer = consumer;
            return this;
        }

        public AlertOneItem title(String title) {
            this.title = title;
            return this;
        }

        public AlertOneItem message(String message) {
            this.message = message;
            return this;
        }

        public AlertOneItem positiveTips(String positiveTips) {
            this.positiveTips = positiveTips;
            return this;
        }

        public AlertOneItem negativeTips(String negativeTips) {
            this.negativeTips = negativeTips;
            return this;
        }


        public void show() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            if (!TextUtils.isEmpty(message)) {
                builder.setMessage(message);
            }
            if (!TextUtils.isEmpty(negativeTips)) {
                builder.setNegativeButton(negativeTips, (dialog, which) -> consumer.accept(false));
            }

            if (!TextUtils.isEmpty(positiveTips)) {
                builder.setPositiveButton(positiveTips, (dialog, which) -> consumer.accept(true));
            }

            builder.setCancelable(true);
            builder.create();
            builder.show();
        }
    }

    public static class AlertMultiItem {
        private List<String> nameList = new ArrayList<>();
        private List<Runnable> taskList = new ArrayList<>();
        private Context context;
        private String title;
        private DialogInterface.OnCancelListener onCancelListener;

        private AlertMultiItem(Context context) {
            this.context = context;
        }

        public static AlertMultiItem builder(Context context) {
            return new AlertMultiItem(context);
        }

        public AlertMultiItem title(String title) {
            this.title = title;
            return this;
        }

        public AlertMultiItem add(String title, Runnable runnable) {
            nameList.add(title);
            taskList.add(runnable);
            return this;
        }

        public AlertMultiItem cancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        private String[] getItemNames() {
            String[] result = new String[nameList.size()];
            for (int i = 0; i < nameList.size(); i++) {
                result[i] = nameList.get(i);
            }
            return result;
        }

        public void show() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }
            builder.setItems(getItemNames(), (dialog, which) -> taskList.get(which).run());
            builder.setOnCancelListener(this.onCancelListener);
            builder.create();
            builder.show();
        }
    }


    public static void showTimePicker(Context context, TimePickerDialog.OnTimeSetListener listener, int[] time) {
        if (time == null || time.length != 2) {
            Toast.makeText(context, "参数异常", Toast.LENGTH_LONG).show();
            return;
        }
        new TimePickerDialog(context, 0, listener, time[0], time[1], true).show();
    }

    public static void showHttpAuthRequest(WebView view, HttpAuthHandler handler) {
        Context context = view.getContext();
        // [Android-WebView's onReceivedHttpAuthRequest() not called again - Stack Overflow](https://stackoverflow.com/questions/20399339/android-webviews-onreceivedhttpauthrequest-not-called-again)
        final EditText usernameInput = new EditText(context);
        usernameInput.setHint("Username");

        final EditText passwordInput = new EditText(context);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(usernameInput);
        ll.addView(passwordInput);

        new AlertDialog
                .Builder(context)
                .setTitle("Authentication")
                .setView(ll)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, whichButton) -> {
                    String username = usernameInput.getText().toString();
                    String password = passwordInput.getText().toString();
                    handler.proceed(username, password);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    dialog.dismiss();
                    view.stopLoading();
                })
                .show();
    }


    public static class AlertInputDialog {
        private Context context;
        private String title;
        private String hint;
        private int type;
        private boolean cancelable;
        private boolean requestFocus;
        private String defaultValue;
        private Consumer<String> consumer = result -> {
        };
        private String positiveTips = "确定";
        private String negativeTips = "取消";

        private AlertInputDialog(Context context) {
            this.context = context;
        }

        public static AlertInputDialog builder(Context context) {
            return new AlertInputDialog(context);
        }

        public AlertInputDialog consumer(Consumer<String> consumer) {
            this.consumer = consumer;
            return this;
        }

        public AlertInputDialog title(String title) {
            this.title = title;
            return this;
        }

        public AlertInputDialog cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }


        public AlertInputDialog requestFocus(boolean requestFocus) {
            this.requestFocus = requestFocus;
            return this;
        }

        public AlertInputDialog type(int type) {
            this.type = type;
            return this;
        }

        public AlertInputDialog defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public AlertInputDialog hint(String hint) {
            this.hint = hint;
            return this;
        }

        public AlertInputDialog positiveTips(String positiveTips) {
            this.positiveTips = positiveTips;
            return this;
        }

        public AlertInputDialog negativeTips(String negativeTips) {
            this.negativeTips = negativeTips;
            return this;
        }

        public void show() {
            final EditText et = new EditText(context);
            et.setBackground(context.getResources().getDrawable(R.drawable.item_input_dialog_et_bg));
            if (defaultValue != null) {
                et.setText(defaultValue);
                et.setSelection(defaultValue.length());
            }
            if (hint != null) {
                et.setHint(hint);
            }
            if (type != 0) {
                et.setInputType(type);
            }

            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int left = Uscreen.dp2Px(context, 16);
            int right = Uscreen.dp2Px(context, 16);
            int top = Uscreen.dp2Px(context, TextUtils.isEmpty(title) ? 26 : 16);
            lp.setMargins(left, top, right, 0);
            ll.addView(et, lp);


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }

            if (!TextUtils.isEmpty(negativeTips)) {
                builder.setNegativeButton(negativeTips, (dialog, which) -> {
                });
            }

            if (!TextUtils.isEmpty(positiveTips)) {
                builder.setPositiveButton(positiveTips, (dialog, which) -> {
                    consumer.accept(et.getText().toString());
                });
            }

            if (requestFocus) {
                et.setFocusable(true);
                et.requestFocus();
            }

            builder.setView(ll);
            builder.setCancelable(cancelable);
            builder.create();
            builder.setOnCancelListener(dialog -> {
                if (InputMethodUtils.isActive(et.getContext())) {
                    InputMethodUtils.hideSoftInput(et);
                }
            });
            builder.show();

        }
    }

}
