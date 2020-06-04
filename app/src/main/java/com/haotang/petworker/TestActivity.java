package com.haotang.petworker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.haotang.petworker.view.CustomChartView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends SuperActivity implements OnChartValueSelectedListener {
    protected BarChart mChart;
    protected RectF mOnValueSelectedRectF = new RectF();
    protected String[] XData = new String[]{
            "s6499", "6500-7999", "8000-9499", "s9500", "s9500", "s9500", "s9500"};
    protected String[] YData = new String[]{
            "0", "15", "30", "45", "60", "75"};
    protected int[] Data = new int[]{3500, 4000, 4800, 5500, 6666, 7777, 8888};
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private CustomChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        setView();
        setLinsitner();

        chartView = (CustomChartView) findViewById(R.id.chart_view);
        chartView.setChartItemCount(4);
        chartView.setMaxValue(70);
        chartView.setTopTextShowType(2);
        List<Integer> list = new ArrayList<>();
        list.add(40);
        list.add(42);
        list.add(47);
        list.add(50);
        chartView.setValues(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void setLinsitner() {
        mChart.setOnChartValueSelectedListener(this);
    }

    private void setView() {
        /*Description description = new Description();
        description.setText("宠物家美容师收入表");
        description.setTextColor(R.color.black);
        description.setTextSize(10);
        mChart.setDescription(description);//表格描述
        mChart.setDrawBarShadow(false);//设置阴影
        mChart.setDrawValueAboveBar(true);//将Y数据显示在点的上方
        mChart.getDescription().setEnabled(true);//显示表格描述
        mChart.setMaxVisibleValueCount(60);//设置最大的能够在图表上显示数字的图数
        mChart.setPinchZoom(false);//如果设置为true，挤压缩放被打开。如果设置为false，x和y轴可以被单独挤压缩放。
        mChart.setDrawGridBackground(false);//设置背景是否网格显示

        //X轴的数据格式
        //IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);
        //IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter(XData);
        //得到X轴，设置X轴的样式
        XAxis xAxis = mChart.getXAxis();//x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//x轴位置
        xAxis.setTypeface(mTfLight);//设置特定的标签类型
        xAxis.setDrawGridLines(false);//设置是否绘制网格线
        xAxis.setGranularity(1f); //设置最小的区间，避免标签的迅速增多
        xAxis.setLabelCount(7);//设置进入时的标签数量
        xAxis.setValueFormatter(xAxisFormatter);//设置数据格式

        //IAxisValueFormatter custom = new MyAxisValueFormatter();
        //IAxisValueFormatter custom = new YAxisValueFormatter(YData);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);//设置进入时的标签数量
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);//设置图表中的最高值的顶部间距占最高值的值的百分比（设置的百分比 = 最高柱顶部间距/最高柱的值）。默认值是10f，即10% 。
        leftAxis.setAxisMinimum(0f);//设置Y轴最小的值
        //leftAxis.setAxisMaxValue(75f);//设置Y轴最大的值

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setEnabled(false);//隐藏Y轴右边轴线，此时标签数字也隐藏
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();//图例
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
        setData();*/
    }

    private void initView() {
        setContentView(R.layout.activity_test);
        mChart = (BarChart) findViewById(R.id.chart1);
    }

    private void initData() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }

    private void setData() {
        mChart.setScaleMinima(1.0f, 1.0f);
        mChart.getViewPortHandler().refresh(new Matrix(), mChart, true);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < Data.length; i++) {
            yVals1.add(new BarEntry(i, Data[i], getResources().getDrawable(R.drawable.star)));
        }
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        BarDataSet set1 = new BarDataSet(yVals1, "订单金额(元)");
        set1.setDrawIcons(false);
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.BLACK);
        colors.add(Color.GRAY);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        set1.setColors(colors);
        ///set1.setColors(ColorTemplate.MATERIAL_COLORS);
        //set1.setColor(R.drawable.red_jianbian_color);
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTfLight);
        data.setBarWidth(0.9f);
        mChart.setData(data);
        mChart.animateXY(3000, 3000);//图表数据显示动画
        mChart.setVisibleXRangeMaximum(15);//设置屏幕显示条数
        mChart.invalidate();
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
        Log.e("TAG", "bounds = " + bounds.toString());
        Log.e("TAG", "position = " + position.toString());
        Log.e("TAG", "x-index low = " + mChart.getLowestVisibleX() + ", high: "
                + mChart.getHighestVisibleX());
        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {
    }
}
