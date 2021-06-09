package com.lyloou.flow.ui.income;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class ChartUtils {
    private LineChart lineChart;
    private LineData mLineData;
    private final List<Entry> lineList = new ArrayList<>();
    private final List<String> xData = new ArrayList<>();


    public void initStateChart(LineChart chart) {
        lineChart = chart;
        lineChart.setBackgroundColor(Color.rgb(255, 255, 255));
        // 不可以缩放
        lineChart.setScaleEnabled(true);

        //新建空数据
        LineDataSet dataSet = new LineDataSet(lineList, "收益浮动");
        //线条颜色
        dataSet.setColor(Color.parseColor("#60B0F2"));
        //圆点颜色
        dataSet.setCircleColor(Color.parseColor("#60B0F2"));
        dataSet.setCircleRadius(1.5f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setFillColor(Color.parseColor("#60B0F2"));
        dataSet.setFillAlpha(50);
        //线条宽度
        dataSet.setLineWidth(1f);

        //设置x轴、y轴样式
        YAxis rightAxis = lineChart.getAxisRight();
        YAxis leftAxis = lineChart.getAxisLeft();
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);
        //设置图表左边的y轴禁用
        //设置x轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        //是否绘制轴线
        xAxis.setDrawAxisLine(true);
        //设置x轴上每个点对应的线
        xAxis.setDrawGridLines(true);
        //绘制标签  指x轴上的对应数值
        xAxis.setDrawLabels(true);
        //设置x轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //禁止放大后x轴标签重绘
        xAxis.setGranularity(1f);
        //自定义x轴值
        xAxis.setValueFormatter(LineXiavf0);
        //图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        xAxis.setAvoidFirstLastClipping(true);

        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        //chart设置数据
        mLineData = new LineData(dataSet);
        //是否绘制线条上的文字
        mLineData.setDrawValues(true);

        lineChart.setData(mLineData);
        lineChart.animateX(500);
        lineChart.setNoDataText("暂无数据");
        lineChart.invalidate();
    }

    //自定义x轴值
    private ValueFormatter LineXiavf0 = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            int p = (int) value;
            if (p < xData.size() && p > -1) {
                return xData.get(p);
            } else {
                return "";
            }
        }
    };

    //添加数据
    public void addEntry(String xValue, float yValue) {
        //添加x轴值
        xData.add(xValue);
        //添加y轴值
        Entry entry = new Entry(xData.size(), yValue);
        mLineData.addEntry(entry, 0);
        //数据刷新
        mLineData.notifyDataChanged();
        //char图标刷新
        lineChart.notifyDataSetChanged();
        //x轴显示最大个数
        lineChart.setVisibleXRangeMaximum(100);
        //x轴移动
//        lineChart.moveViewToAnimated(xData.size(), 0, YAxis.AxisDependency.RIGHT, 500);
    }

    //批量添加数据
    public void addPairList(List<Pair<String, Float>> pairs) {
        for (Pair<String, Float> pair : pairs) {
            //添加x轴值
            xData.add(pair.getFirst());
            //添加y轴值
            Entry entry = new Entry(xData.size(), pair.getSecond());
            mLineData.addEntry(entry, 0);
        }
        //数据刷新
        mLineData.notifyDataChanged();
        //char图标刷新
        lineChart.notifyDataSetChanged();
        //x轴显示最大个数
        lineChart.setVisibleXRangeMaximum(100);
        //x轴移动
//        lineChart.moveViewToAnimated(xData.size(), 0, YAxis.AxisDependency.RIGHT, 500);
    }
}
