package com.haotang.petworker.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.adapter.OrderTimeAdapter;
import com.haotang.petworker.utils.DisplayUtil;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-02 18:10
 */
public class AlertDialogServiceTime {
    private Activity context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private Display display;
    private RecyclerView rv_defaultdialog;
    private TextView tv_defaultdialog_submit;

    public AlertDialogServiceTime(Activity context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialogServiceTime builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialogservicetime, null);
        // 获取自定义Dialog布局中的控件
        rv_defaultdialog = (RecyclerView) view
                .findViewById(R.id.rv_defaultdialog);
        tv_defaultdialog_submit = (TextView) view
                .findViewById(R.id.tv_defaultdialog_submit);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_defaultdialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogServiceTime setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public AlertDialogServiceTime setData(List<String> timeList) {
        rv_defaultdialog.setHasFixedSize(true);
        rv_defaultdialog.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(context);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rv_defaultdialog.setLayoutManager(noScollFullLinearLayoutManager);
        rv_defaultdialog.addItemDecoration(new DividerLinearItemDecoration(context, LinearLayoutManager.VERTICAL,
                DisplayUtil.dip2px(context, 10),
                ContextCompat.getColor(context, R.color.transparent)));
        rv_defaultdialog.setAdapter(new OrderTimeAdapter(R.layout.item_ordertime, timeList));
        return this;
    }
}