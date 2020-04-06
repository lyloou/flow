package com.lyloou.flow.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lyloou.flow.R;
import com.lyloou.flow.common.Consumer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Udialog {
    private static final String DEFAULT_POSITIVE_TIPS = "确定";
    private static final String DEFAULT_NEGATIVE_TIPS = "取消";

    public static class AlertOneItem {
        private Context context;
        private String title;
        private String message;
        private Consumer<Void> positiveConsumer = result -> {
        };
        private Consumer<Void> negativeConsumer = result -> {
        };
        private String positiveTips = DEFAULT_POSITIVE_TIPS;
        private String negativeTips = DEFAULT_NEGATIVE_TIPS;

        private AlertOneItem(Context context) {
            this.context = context;
        }

        public static AlertOneItem builder(Context context) {
            return new AlertOneItem(context);
        }

        public AlertOneItem positiveConsumer(Consumer<Void> consumer) {
            this.positiveConsumer = consumer;
            return this;
        }

        public AlertOneItem negativeConsumer(Consumer<Void> consumer) {
            this.negativeConsumer = consumer;
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
                builder.setNegativeButton(negativeTips, (dialog, which) -> negativeConsumer.accept(null));
            }

            if (!TextUtils.isEmpty(positiveTips)) {
                builder.setPositiveButton(positiveTips, (dialog, which) -> positiveConsumer.accept(null));
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

    public static class AlertInputDialog {
        private Context context;
        private String title;
        private String hint;
        private int type;
        private boolean cancelable;
        private boolean requestFocus;
        private String defaultValue;
        private Consumer<String> positiveConsumer = result -> {
        };
        private Consumer<Void> negativeConsumer = result -> {
        };
        private String positiveTips = DEFAULT_POSITIVE_TIPS;
        private String negativeTips = DEFAULT_NEGATIVE_TIPS;

        private AlertInputDialog(Context context) {
            this.context = context;
        }

        public static AlertInputDialog builder(Context context) {
            return new AlertInputDialog(context);
        }

        public AlertInputDialog positiveConsumer(Consumer<String> consumer) {
            this.positiveConsumer = consumer;
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
                builder.setNegativeButton(negativeTips,
                        (dialog, which) -> negativeConsumer.accept(null));
            }

            if (!TextUtils.isEmpty(positiveTips)) {
                builder.setPositiveButton(positiveTips,
                        (dialog, which) -> positiveConsumer.accept(et.getText().toString()));
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

    public static class AlertCustomViewDialog {
        private Context context;
        private String title;
        private View view;
        private boolean cancelable;
        private Consumer<View> positiveConsumer = result -> {
        };
        private Consumer<Void> negativeConsumer = result -> {
        };
        private String positiveTips = DEFAULT_POSITIVE_TIPS;
        private String negativeTips = DEFAULT_NEGATIVE_TIPS;

        private AlertCustomViewDialog(Context context) {
            this.context = context;
        }

        public static AlertCustomViewDialog builder(Context context) {
            return new AlertCustomViewDialog(context);
        }

        public AlertCustomViewDialog positiveConsumer(Consumer<View> consumer) {
            this.positiveConsumer = consumer;
            return this;
        }

        public AlertCustomViewDialog negativeConsumer(Consumer<Void> consumer) {
            this.negativeConsumer = consumer;
            return this;
        }

        public AlertCustomViewDialog title(String title) {
            this.title = title;
            return this;
        }

        public AlertCustomViewDialog view(@NotNull View view) {
            this.view = view;
            return this;
        }

        public AlertCustomViewDialog cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }


        public AlertCustomViewDialog positiveTips(String positiveTips) {
            this.positiveTips = positiveTips;
            return this;
        }

        public AlertCustomViewDialog negativeTips(String negativeTips) {
            this.negativeTips = negativeTips;
            return this;
        }

        public void show() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!TextUtils.isEmpty(title)) {
                builder.setTitle(title);
            }

            if (!TextUtils.isEmpty(negativeTips)) {
                builder.setNegativeButton(negativeTips, (dialog, which) -> negativeConsumer.accept(null));
            }

            if (!TextUtils.isEmpty(positiveTips)) {
                builder.setPositiveButton(positiveTips, (dialog, which) -> positiveConsumer.accept(this.view));
            }

            builder.setView(view);
            builder.setCancelable(cancelable);
            builder.create();
            builder.show();
        }
    }


    public static class AlertMultipleInputDialog {
        public static class InputItem {
            public int id;
            public String hint;
            public int type;
            public String defaultValue;
        }

        private Context context;
        private String title;
        private boolean cancelable;
        private List<InputItem> inputItemList = new ArrayList<>();

        private Consumer<Map<Integer, String>> positiveConsumer = result -> {
        };
        private Consumer<Void> negativeConsumer = result -> {
        };

        private String positiveTips = DEFAULT_POSITIVE_TIPS;
        private String negativeTips = DEFAULT_NEGATIVE_TIPS;

        private AlertMultipleInputDialog(Context context) {
            this.context = context;
        }

        public static AlertMultipleInputDialog builder(Context context) {
            return new AlertMultipleInputDialog(context);
        }

        public AlertMultipleInputDialog positiveConsumer(Consumer<Map<Integer, String>> consumer) {
            this.positiveConsumer = consumer;
            return this;
        }

        public AlertMultipleInputDialog negativeConsumer(Consumer<Void> negativeConsumer) {
            this.negativeConsumer = negativeConsumer;
            return this;
        }

        public AlertMultipleInputDialog addInputItem(InputItem inputItem) {
            this.inputItemList.add(inputItem);
            return this;
        }

        public AlertMultipleInputDialog title(String title) {
            this.title = title;
            return this;
        }

        public AlertMultipleInputDialog cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }


        public AlertMultipleInputDialog positiveTips(String positiveTips) {
            this.positiveTips = positiveTips;
            return this;
        }

        public AlertMultipleInputDialog negativeTips(String negativeTips) {
            this.negativeTips = negativeTips;
            return this;
        }

        public void show() {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.VERTICAL);

            for (InputItem inputItem : inputItemList) {
                final EditText et = new EditText(context);
                et.setTag(inputItem.id);
                et.setBackground(context.getResources().getDrawable(R.drawable.item_input_dialog_et_bg));
                if (inputItem.defaultValue != null) {
                    et.setText(inputItem.defaultValue);
                    et.setSelection(inputItem.defaultValue.length());
                }
                if (inputItem.hint != null) {
                    et.setHint(inputItem.hint);
                }
                if (inputItem.type != 0) {
                    et.setInputType(inputItem.type);
                }
                ll.addView(et, getLayoutParams());
            }


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
                    Map<Integer, String> map = new HashMap<>();
                    for (int i = 0; i < ll.getChildCount(); i++) {
                        EditText et = (EditText) ll.getChildAt(i);
                        map.put((Integer) et.getTag(), et.getText().toString());
                    }
                    positiveConsumer.accept(map);
                });
            }

            builder.setView(ll);
            builder.setCancelable(cancelable);
            builder.create();
            builder.setOnCancelListener(dialog -> {
                if (InputMethodUtils.isActive(ll.getContext())) {
                    InputMethodUtils.hideSoftInput(ll);
                }
                negativeConsumer.accept(null);
            });
            builder.show();

        }

        @NotNull
        private LinearLayout.LayoutParams getLayoutParams() {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int left = Uscreen.dp2Px(context, 16);
            int right = Uscreen.dp2Px(context, 16);
            int top = Uscreen.dp2Px(context, TextUtils.isEmpty(title) ? 26 : 8);
            lp.setMargins(left, top, right, 0);
            return lp;
        }
    }
}
