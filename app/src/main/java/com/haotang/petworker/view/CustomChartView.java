package com.haotang.petworker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.haotang.petworker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author : micro_hx <p>
 * desc : <p>
 * email: javainstalling@163.com <p>
 * date : 2017/1/8 - 21:12 <p>
 * interface :
 */

public class CustomChartView extends View {
    private static final String TAG = CustomChartView.class.getSimpleName();

    //默认宽度/高度
    private static final int DEFAULT_WIDTH = 500, DEFAULT_HEIGHT = 400;
    private static final int TYPE_PERCENT = 1, TYPE_VALUE = 2;
    //默认底部字体的颜色
    private static final int DEFAULT_TEXT_COLOR = Color.BLUE;
    //默认底部字体大小
    private static final int DEFAULT_TEXT_SIZE = 20;
    //默认ChartView的个数
    private static final int DEFAULT_ITEM_COUNT = 12;
    //默认ChartView的最大值
    private static final int DEFAULT_MAX_VALUE = 500;
    //默认ChartVie的最小值
    private static final int DEFAULT_MIN_VALUE = 0;
    //y轴显示的刻度线的个数
    private static final int DEFAULT_Y_COORDINATE_ITEM = 5;

    //底部字体颜色
    private int mTextColor;
    //底部字体大小
    private int mTextSize;
    //文字的后缀
    private String mTextSuffix;
    //底部文字高度
    private float mTextBottomHeight;

    //chartView的颜色
    private int mStartChartViewColor;
    //chartView结束的颜色
    private int mEndChartViewColor;
    //chartView选中的颜色
    private int mChooseColor = Color.RED;
    //charView上的圆角
    private int mChartViewCircle;
    //chartView显示部分距离view的上部分
    private int mChartPaddingTop;
    //chartView显示部分立即view的下部分
    private int mChartPaddingBottom;

    //顶部TextView的颜色
    private int mTopTextColor;
    //顶部TextView的字体大小
    private int mTopTextSize;
    //顶部显示类型
    private int mTopTextShowType;
    //当前Item的个数
    private int mChartItemCount;
    //默认最大值
    private int mMaxValue;
    //默认最小值
    private int mMinValue;

    //textView的画笔，柱状图的画笔
    private Paint mTextPaint, mChartPaint, mTextTopPaint, mCoordinatePaint;
    //柱状图的渐变shader
    private LinearGradient mLinearGradient;

    private ItemChooseListener mListener;

    private int mWidth, //ChartView的宽度
            mHeight,    //ChartView的高度
            mChartWidth,  //单个View的宽度
            mBetweenWidth, //两个View之间的间隔宽度
            mTextLocationHeight; // 文字的位置高度相对于底部的高度

    private List<Integer> mValues;

    private Random mRandom;

    //当前选中的Index项
    private int mCurrentIndex = -1;

    public CustomChartView(Context context) {
        this(context, null);
    }

    public CustomChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomChartView);
        mTextColor = array.getColor(R.styleable.CustomChartView_text_color, DEFAULT_TEXT_COLOR);
        mTextSize = array.getDimensionPixelSize(R.styleable.CustomChartView_text_size, DEFAULT_TEXT_SIZE);
        mTextSuffix = array.getString(R.styleable.CustomChartView_text_suffix);

        mStartChartViewColor = array.getColor(R.styleable.CustomChartView_chart_start_color, Color.GREEN);
        mEndChartViewColor = array.getColor(R.styleable.CustomChartView_chart_end_color, Color.YELLOW);
        mChooseColor = array.getColor(R.styleable.CustomChartView_chart_choose_color, Color.RED);
        mChartViewCircle = array.getInt(R.styleable.CustomChartView_chart_view_circle_radius, 4);
        mChartPaddingTop = (int) array.getDimension(R.styleable.CustomChartView_chart_padding_top, dip2px(15));
        mChartPaddingBottom = (int) array.getDimension(R.styleable.CustomChartView_chart_padding_bottom, dip2px(15));

        mTopTextColor = array.getColor(R.styleable.CustomChartView_text_top_color, Color.GREEN);
        mTopTextSize = array.getDimensionPixelSize(R.styleable.CustomChartView_text_top_size, DEFAULT_TEXT_SIZE);
        mTopTextShowType = array.getInt(R.styleable.CustomChartView_text_top_type, 1);

        mChartItemCount = array.getInt(R.styleable.CustomChartView_chart_item_count, DEFAULT_ITEM_COUNT);
        mMaxValue = array.getInt(R.styleable.CustomChartView_chart_max_value, DEFAULT_MAX_VALUE);
        mMinValue = array.getInt(R.styleable.CustomChartView_chart_min_value, DEFAULT_MIN_VALUE);

        array.recycle();

        init();
    }

    private void init() {
        if (TextUtils.isEmpty(mTextSuffix)) mTextSuffix = "";

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        mTextBottomHeight = metrics.ascent + metrics.descent;

        mChartPaint = new Paint();
        mChartPaint.setStyle(Paint.Style.FILL);
        mChartPaint.setAntiAlias(true);
        mChartPaint.setColor(mStartChartViewColor);

        mTextTopPaint = new Paint();
        mTextTopPaint.setStyle(Paint.Style.FILL);
        mTextTopPaint.setAntiAlias(true);
        mTextTopPaint.setTextSize(mTopTextSize);
        mTextTopPaint.setColor(mTopTextColor);

        mCoordinatePaint = new Paint();
        mCoordinatePaint.setColor(getResources().getColor(R.color.a979797));
        mCoordinatePaint.setAntiAlias(true);
        mCoordinatePaint.setStrokeWidth(1);

        mValues = new ArrayList<>();
        mRandom = new Random();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(widthSize, DEFAULT_WIDTH);
        } else {
            width = DEFAULT_WIDTH;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(heightSize, DEFAULT_HEIGHT);
        } else {
            height = DEFAULT_HEIGHT;
        }

        Log.d(TAG, "the measure width : " + width + " , height : " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mChartWidth = mBetweenWidth = (int) (mWidth * 1.0f / (mChartItemCount * 2 + 1));
        mTextLocationHeight = mHeight - mChartPaddingBottom - mChartPaddingTop;

        if (mChartViewCircle > mChartWidth / 2) {
            mChartViewCircle = mChartViewCircle / 2;
        }

        for (int i = 0; i < mChartItemCount; i++) {
            mValues.add(mRandom.nextInt(mMaxValue - mMinValue));
        }

        Log.d(TAG, "the charWidth : " + mChartWidth + " , mTextLocationHeight : " + mTextLocationHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == mValues || mValues.isEmpty()) return;

        //1.画坐标轴
        drawCoordinate(canvas);

        //2.画月份
        drawMonths(canvas);

        //3.画柱状图
        drawPictures(canvas);
    }

    private void drawCoordinate(Canvas canvas) {
        //画原点
        //canvas.drawText("O", mBetweenWidth / 2 - 20, mTextLocationHeight + mChartPaddingBottom + 20, mTextPaint);

        //画图像中X轴的坐标
        canvas.drawLine(mBetweenWidth / 2 - 15, mTextLocationHeight + mChartPaddingBottom, mWidth - mBetweenWidth / 2, mTextLocationHeight + mChartPaddingBottom, mCoordinatePaint);
        //canvas.drawLine(mWidth - mBetweenWidth / 2 - 20, mTextLocationHeight + mChartPaddingBottom - 10, mWidth - mBetweenWidth / 2, mTextLocationHeight + mChartPaddingBottom, mCoordinatePaint);
        //canvas.drawLine(mWidth - mBetweenWidth / 2 - 20, mTextLocationHeight + mChartPaddingBottom + 10, mWidth - mBetweenWidth / 2, mTextLocationHeight + mChartPaddingBottom, mCoordinatePaint);

        //画图中Y轴的坐标
        canvas.drawLine(mBetweenWidth / 2 - 15, mTextLocationHeight + mChartPaddingBottom, mBetweenWidth / 2 - 15, mChartPaddingTop, mCoordinatePaint);
        //canvas.drawLine(mBetweenWidth / 2 - 30, mChartPaddingTop + 10, mBetweenWidth / 2 - 15, mChartPaddingTop, mCoordinatePaint);
        //drawLine(mBetweenWidth / 2, mChartPaddingTop + 10, mBetweenWidth / 2 - 15, mChartPaddingTop, mCoordinatePaint);

        //画y轴上坐标点
        for (int i = 1; i <= DEFAULT_Y_COORDINATE_ITEM - 1; i++) {
            canvas.drawLine(mBetweenWidth / 2 - 15,
                    mTextLocationHeight + mChartPaddingBottom - mTextLocationHeight * i * 1.0f / DEFAULT_Y_COORDINATE_ITEM,
                    mWidth - mBetweenWidth / 2,
                    mTextLocationHeight + mChartPaddingBottom - mTextLocationHeight * i * 1.0f / DEFAULT_Y_COORDINATE_ITEM,
                    mCoordinatePaint);

            //画y轴的刻度
            mTextPaint.setColor(mTextColor);
            if (mTopTextShowType == TYPE_PERCENT) { //百分比
                canvas.drawText(String.valueOf(i * 10) + "%",
                        mBetweenWidth / 2 - 5,
                        mTextLocationHeight + mChartPaddingBottom - 5 - mTextLocationHeight * i * 1.0f / DEFAULT_Y_COORDINATE_ITEM,
                        mTextPaint);
            } else {
                int value = (mMaxValue - mMinValue) * i / DEFAULT_Y_COORDINATE_ITEM;
                canvas.drawText(String.valueOf(value),
                        mBetweenWidth / 2 - 5 - 55,
                        mTextLocationHeight + mChartPaddingBottom - 5 + 20 - mTextLocationHeight * i * 1.0f / DEFAULT_Y_COORDINATE_ITEM,
                        mTextPaint);
            }
        }
    }

    private void drawPictures(Canvas canvas) {
        for (int i = 1; i <= mChartItemCount; i++) {
            int tempValue = mValues.get(i - 1);

            RectF rectF = new RectF();
            rectF.top = mChartPaddingTop + mTextLocationHeight - mTextLocationHeight * tempValue * 1.0f / (mMaxValue - mMinValue);
            rectF.left = i * mBetweenWidth + (i - 1) * mChartWidth;
            rectF.right = i * (mBetweenWidth + mChartWidth);
            rectF.bottom = mTextLocationHeight + mChartPaddingBottom - 2;
            mChartPaint.setShader(getChartViewBgShader(rectF, i));
            canvas.drawRoundRect(rectF, mChartViewCircle, mChartViewCircle, mChartPaint);

            drawTopText(canvas, i, tempValue, rectF);
        }
    }

    private void drawTopText(Canvas canvas, int i, int tempValue, RectF rectF) {
        String result = "";
        //if (mTopTextShowType == TYPE_PERCENT) {
        //result = String.format("%.0f", tempValue * 100f / (mMaxValue - mMinValue)) + "%";
        result = String.valueOf(tempValue) + "%";
        /*} else if (mTopTextShowType == TYPE_VALUE) {
            result = String.valueOf(tempValue);
        } else {
            result = "";
        }*/

        float width = mTextPaint.measureText(result);
        mTextPaint.setColor(mCurrentIndex == i - 1 ? mChooseColor : mTopTextColor);
        canvas.drawText(result, rectF.left + mChartWidth / 2 - width / 2, rectF.top - 10, mTextTopPaint);
    }

    private Shader getChartViewBgShader(RectF rectF, int index) {
        mLinearGradient = new LinearGradient(rectF.left,
                rectF.top,
                rectF.right,
                rectF.bottom,
                mStartChartViewColor,
                mCurrentIndex == index - 1 ? mChooseColor : mEndChartViewColor,
                Shader.TileMode.MIRROR);
        return mLinearGradient;
    }

    private void drawMonths(Canvas canvas) {
        for (int i = 1; i <= mChartItemCount; i++) {
            float width = mTextPaint.measureText(i + mTextSuffix);
            float mTextTempWidth = i * mBetweenWidth + mChartWidth * (i - 0.5f) - width / 2;
            mTextPaint.setColor(mCurrentIndex == i - 1 ? mChooseColor : mTextColor);
            canvas.drawText(i + mTextSuffix, mTextTempWidth, mTextLocationHeight + mChartPaddingBottom - mTextBottomHeight + 10, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (contains(x, y)) {
                    invalidate();
                    if (null != mListener) {
                        mListener.onItemChoosed(mCurrentIndex, mValues.get(mCurrentIndex));
                    }
                }
                break;
        }

        return true;
    }

    private boolean contains(float x, float y) {
        for (int i = 1; i <= mChartItemCount; i++) {
            int tempValue = mValues.get(i - 1);

            RectF rectF = new RectF();
            rectF.top = mTextLocationHeight + mChartPaddingTop - tempValue * mHeight * 1.0f / (mMaxValue - mMinValue);
            rectF.left = (i - 1) * mBetweenWidth + (i - 1) * mChartWidth;
            rectF.right = i * (mBetweenWidth + mChartWidth) + mChartWidth;
            rectF.bottom = mTextLocationHeight + mChartPaddingTop;

            if (rectF.contains(x, y)) {
                mCurrentIndex = i - 1;
                return true;
            }
        }
        return false;
    }

    public void setValues(List<Integer> lists) {
        if (null == lists) return;
        mValues.clear();

        int len = lists.size();
        if (len >= mChartItemCount) {
            for (int i = 0; i < mChartItemCount; i++) {
                mValues.add(lists.get(i));
            }
        } else {
            for (int i = 0; i < len; i++) {
                mValues.add(lists.get(i));
            }

            for (int i = len; i < mChartItemCount; i++) {
                mValues.add(0);
            }
        }
        invalidate();
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setBetweenWidth(int mBetweenWidth) {
        this.mBetweenWidth = mBetweenWidth;
    }

    public void setChartItemCount(int mChartItemCount) {
        this.mChartItemCount = mChartItemCount;
        checkSize();
    }

    private void checkSize() {
        if (mValues == null) {
            mValues = new ArrayList<>(mChartItemCount);
        }

        if (mValues.size() < mChartItemCount) {
            int len = mValues.size();
            for (int i = len; i < mChartItemCount; i++) {
                mValues.add(0);
            }
        }

        invalidate();
    }

    public void setChartPaddingBottom(int mChartPaddingBottom) {
        this.mChartPaddingBottom = mChartPaddingBottom;
        invalidate();
    }

    public void setChartPaddingTop(int mChartPaddingTop) {
        this.mChartPaddingTop = mChartPaddingTop;
        invalidate();
    }

    public void setChartViewCircle(int mChartViewCircle) {
        this.mChartViewCircle = mChartViewCircle;
        invalidate();
    }

    public void setChartWidth(int mChartWidth) {
        this.mChartWidth = mChartWidth;
        invalidate();
    }

    public void setChooseColor(int mChooseColor) {
        this.mChooseColor = mChooseColor;
        invalidate();
    }

    public void setmCurrentIndex(int mCurrentIndex) {
        this.mCurrentIndex = mCurrentIndex;
        invalidate();
    }

    public void setEndChartViewColor(int mEndChartViewColor) {
        this.mEndChartViewColor = mEndChartViewColor;
        invalidate();
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
        invalidate();
    }

    public void setLinearGradient(LinearGradient mLinearGradient) {
        this.mLinearGradient = mLinearGradient;
        invalidate();
    }

    public void setMaxValue(int mMaxValue) {
        this.mMaxValue = mMaxValue;
        invalidate();
    }

    public void setMinValue(int mMinValue) {
        this.mMinValue = mMinValue;
        invalidate();
    }


    public void setStartChartViewColor(int mStartChartViewColor) {
        this.mStartChartViewColor = mStartChartViewColor;
        invalidate();
    }

    public void setTextBottomHeight(float mTextBottomHeight) {
        this.mTextBottomHeight = mTextBottomHeight;
        invalidate();
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        invalidate();
    }

    public void setmTextLocationHeight(int mTextLocationHeight) {
        this.mTextLocationHeight = mTextLocationHeight;
        invalidate();
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        invalidate();
    }

    public void setTextSuffix(String mTextSuffix) {
        this.mTextSuffix = mTextSuffix;
        invalidate();
    }

    public void setTextTopPaint(Paint mTextTopPaint) {
        this.mTextTopPaint = mTextTopPaint;
        invalidate();
    }

    public void setTopTextColor(int mTopTextColor) {
        this.mTopTextColor = mTopTextColor;
        invalidate();
    }

    public void setTopTextShowType(int mTopTextShowType) {
        this.mTopTextShowType = mTopTextShowType;
        invalidate();
    }

    public void setTopTextSize(int mTopTextSize) {
        this.mTopTextSize = mTopTextSize;
        invalidate();
    }

    public void setmValues(List<Integer> mValues) {
        this.mValues = mValues;
        invalidate();
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setOnItemChooseListener(ItemChooseListener l) {
        mListener = l;
    }

    public interface ItemChooseListener {
        void onItemChoosed(int index, int value);
    }


}
