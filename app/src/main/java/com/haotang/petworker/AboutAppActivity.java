package com.haotang.petworker;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.petworker.utils.Global;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAppActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_about_version)
    TextView tvAboutVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();

    }

    private void setView() {
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
        tvAboutVersion.setText("美容师端V"+Global.getCurrentVersion(this));
        tvTitlebarTitle.setText("关于");
    }

    @OnClick(R.id.ib_titlebar_back)
    public void onViewClicked() {
        finish();
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
}
