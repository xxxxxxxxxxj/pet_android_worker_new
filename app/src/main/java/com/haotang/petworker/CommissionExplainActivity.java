package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haotang.petworker.utils.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author:姜谷蓄
 * @Description:提成说明页
 */
public class CommissionExplainActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_commission_ex)
    TextView tvCommissionEx;
    @BindView(R.id.iv_commission_icon)
    ImageView ivCommissionIcon;
    private String explain;
    private String icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_explain);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("收入提成说明");
        Intent intent = getIntent();
        explain = intent.getStringExtra("explain");
        icon = intent.getStringExtra("icon");
        tvCommissionEx.setText(explain);
        Glide.with(this).load(icon).into(ivCommissionIcon);
    }

    @OnClick(R.id.ib_titlebar_back)
    public void onViewClicked() {
        finish();
    }
}
