package com.haotang.petworker.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.CareTag;
import com.haotang.petworker.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogOrderTag {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView btn_pos;
    private Display display;
    private LinearLayout ll_usertag;
    private LinearLayout ll_pettag;
    private TextView tv_usertag;
    private TextView tv_pettag;
    private List<CareTag> characterList = new ArrayList<CareTag>();

    public AlertDialogOrderTag(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialogOrderTag builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialogordertag, null);
        // 获取自定义Dialog布局中的控件
        ll_usertag = (LinearLayout) view
                .findViewById(R.id.ll_usertag);
        ll_pettag = (LinearLayout) view
                .findViewById(R.id.ll_pettag);
        tv_usertag = (TextView) view
                .findViewById(R.id.tv_usertag);
        tv_pettag = (TextView) view
                .findViewById(R.id.tv_pettag);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        btn_pos = (TextView) view.findViewById(R.id.btn_pos);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(characterList);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogOrderTag setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public AlertDialogOrderTag setPetTag(String petTag) {
        if (Utils.isStrNull(petTag)) {
            ll_pettag.setVisibility(View.VISIBLE);
            tv_pettag.setText(petTag);
        } else {
            ll_pettag.setVisibility(View.GONE);
        }
        return this;
    }

    public AlertDialogOrderTag setUserTag(String userTag) {
        if (Utils.isStrNull(userTag)) {
            ll_usertag.setVisibility(View.VISIBLE);
            tv_usertag.setText(userTag);
        } else {
            ll_usertag.setVisibility(View.GONE);
        }
        return this;
    }
}
