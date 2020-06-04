package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haotang.petworker.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 吃饭时间设置说明界面
 */
public class EatTimeIntroduceActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_eattime_intr_time)
    TextView tvEattimeIntrTime;
    @BindView(R.id.tv_eattime_intr_desc)
    TextView tvEattimeIntrDesc;
    @BindView(R.id.tv_eattime_intr_example)
    TextView tvEattimeIntrExample;
    @BindView(R.id.tv_eattime_intr_zhu)
    TextView tvEattimeIntrZhu;
    private String timeRange;
    private String desc;
    private String example;
    private String zhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        Intent intent = getIntent();
        timeRange = intent.getStringExtra("timeRange");
        desc = intent.getStringExtra("desc");
        example = intent.getStringExtra("example");
        zhu = intent.getStringExtra("zhu");
    }

    private void findView() {
        setContentView(R.layout.activity_eat_time_set);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("时间设置说明");
        Utils.setText(tvEattimeIntrTime, timeRange, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvEattimeIntrDesc, desc, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvEattimeIntrExample, example, "", View.VISIBLE, View.VISIBLE);
        Utils.setText(tvEattimeIntrZhu, zhu, "", View.VISIBLE, View.VISIBLE);
    }

    private void setLinster() {

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

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
    }
}
