package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.petworker.adapter.IntegralIconAdapter;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyIntegralActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_totalintegral)
    TextView tvTotalintegral;
    @BindView(R.id.tv_integral_detail)
    TextView tvIntegralDetail;
    @BindView(R.id.rv_integral_rule)
    RecyclerView rvIntegralRule;
    private String integral;
    private List<Integer> list = new ArrayList<>();
    private IntegralIconAdapter adapter;
    private String workerRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        getData();
        setView();
    }

    private void getData() {
        Intent intent = getIntent();
        integral = intent.getStringExtra("integral");
        workerRule = intent.getStringExtra("workerRule");
        //CommUtil.workerIntegralInfo(this,handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void setView() {
        setContentView(R.layout.activity_my_integral);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("我的积分");
        btTitlebarOther.setVisibility(View.VISIBLE);
        btTitlebarOther.setText("积分说明");
        tvTotalintegral.setText(integral);
        list.add(R.drawable.icon_integral_add);
        list.add(R.drawable.icon_integral_deduction);
        adapter = new IntegralIconAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvIntegralRule.setLayoutManager(layoutManager);
        rvIntegralRule.setAdapter(adapter);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_integral_detail, R.id.bt_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_integral_detail:
                Intent intent = new Intent(MyIntegralActivity.this, IntegralDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_titlebar_other:
                Intent intent1 = new Intent(MyIntegralActivity.this,AgreementActivity.class);
                intent1.putExtra("url",workerRule);
                startActivity(intent1);
                break;
        }
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
