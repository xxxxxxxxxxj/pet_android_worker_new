package com.haotang.petworker.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.petworker.R;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-04 11:46
 */
public class AlertDialogUpgradeOrder {
    private Activity context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private Display display;
    private TextView tv_upgradedialog_service;
    private TextView tv_upgradedialog_item;
    private TextView tv_upgradedialog_submit;

    public AlertDialogUpgradeOrder(Activity context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialogUpgradeOrder builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialogupgradeorder, null);
        // 获取自定义Dialog布局中的控件
        tv_upgradedialog_service = (TextView) view
                .findViewById(R.id.tv_upgradedialog_service);
        tv_upgradedialog_item = (TextView) view
                .findViewById(R.id.tv_upgradedialog_item);
        tv_upgradedialog_submit = (TextView) view
                .findViewById(R.id.tv_upgradedialog_submit);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_upgradedialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogUpgradeOrder setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public AlertDialogUpgradeOrder setServiceButton(final View.OnClickListener listener) {
        tv_upgradedialog_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogUpgradeOrder setItemButton(final View.OnClickListener listener) {
        tv_upgradedialog_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }
}
