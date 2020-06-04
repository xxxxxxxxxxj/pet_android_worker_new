package com.haotang.petworker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.utils.Utils;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/10/20 13:50
 */
public class TimeTextView extends TextView {
    private Context mContext;
    private long time;
    private boolean run = true; // 是否启动了

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 在控件被销毁时移除消息
        handler.removeMessages(0);
    }

    @SuppressLint("NewApi")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (run) {
                        if (time > 0) {
                            TimeTextView.this.setTextColor(mContext.getResources()
                                    .getColor(R.color.aD1494F));
                            TimeTextView.this.setText("倒计时："
                                    + Utils.formatTime(time));
                            time -= 1000;
                            handler.sendEmptyMessageDelayed(0, 1000);
                        } else {
                            TimeTextView.this.setTextColor(mContext
                                    .getResources().getColor(
                                            R.color.time_gray_color));
                            TimeTextView.this.setText("倒计时：00:00:00");
                            handler.removeMessages(0);
                        }
                    }else {
                        TimeTextView.this.setTextColor(mContext
                                .getResources().getColor(
                                        R.color.time_gray_color));
                        TimeTextView.this.setText("倒计时：00:00:00");
                        handler.removeMessages(0);
                    }
                    break;
            }
        }
    };

    public TimeTextView(Context context) {
        super(context);
        this.mContext = context;
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    @SuppressLint("NewApi")
    public void setTimes(long mT) {
        time = mT;
        if (time > 0) {
            handler.removeMessages(0);
            handler.sendEmptyMessage(0);
        }else {
            TimeTextView.this.setTextColor(mContext
                    .getResources().getColor(
                            R.color.time_gray_color));
            TimeTextView.this.setText("倒计时：00:00:00");
            handler.removeMessages(0);
        }
    }

    public void stop() {
        run = false;
    }
}
