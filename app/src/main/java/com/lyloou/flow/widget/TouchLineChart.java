package com.lyloou.flow.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.lyloou.flow.util.Uscreen;

public class TouchLineChart extends LineChart {
    private final PointF downPoint = new PointF();

    public TouchLineChart(Context context) {
        super(context);
    }

    public TouchLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.x = evt.getX();
                downPoint.y = evt.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dp5 = Uscreen.dp2Px(getContext(), 5);
                if (Math.abs(evt.getX() - downPoint.x) > dp5) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.onTouchEvent(evt);
    }
}
