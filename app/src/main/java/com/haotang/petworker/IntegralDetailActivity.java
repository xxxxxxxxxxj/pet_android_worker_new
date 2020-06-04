package com.haotang.petworker;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.petworker.adapter.IntegralDetailAdapter;
import com.haotang.petworker.entity.IntegralDerail;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntegralDetailActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_integrallist)
    RecyclerView rvIntegrallist;
    @BindView(R.id.tv_emptyview_desc)
    TextView tvEmptyviewDesc;
    @BindView(R.id.btn_emptyview)
    Button btnEmptyview;
    private LinearLayout ll_order_default;
    private int page = 1;
    private List<IntegralDerail.DataBean.IntegralWaterBillBean> integralWaterBill = new ArrayList<>();
    private IntegralDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        setView();
        getData();
    }

    private void getData() {
        CommUtil.workerIntegralList(this, page, 10, handler);
    }


    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Gson gson = new Gson();
            IntegralDerail integralDerail = gson.fromJson(new String(responseBody), IntegralDerail.class);
            if (integralDerail.getCode() == 0) {
                IntegralDerail.DataBean data = integralDerail.getData();
                if (data != null) {
                    if (data.getIntegralWaterBill() != null && data.getIntegralWaterBill().size() != 0) {
                        integralWaterBill.addAll(data.getIntegralWaterBill());
                        adapter.notifyDataSetChanged();
                    }
                    if (integralWaterBill.size()==0){
                        setLayout(2,2,"暂无积分明细");
                    }
                }
            } else {
                setLayout(2,2,integralDerail.getMsg());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            setLayout(2,1,"请求失败");
        }
    };

    private void setView() {
        setContentView(R.layout.activity_integral_detail);
        ButterKnife.bind(this);
        ll_order_default = findViewById(R.id.ll_order_default);
        tvTitlebarTitle.setText("积分明细");
        adapter = new IntegralDetailAdapter(R.layout.item_integral_list, integralWaterBill);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvIntegrallist.setLayoutManager(layoutManager);
        rvIntegrallist.setAdapter(adapter);
    }

    private void setLayout(int type, int flag, String msg) {
        if (type == 1) {
            ll_order_default.setVisibility(View.GONE);
            rvIntegrallist.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ll_order_default.setVisibility(View.VISIBLE);
            rvIntegrallist.setVisibility(View.GONE);
            if (flag == 1) {
                btnEmptyview.setVisibility(View.VISIBLE);
                btnEmptyview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                });
            } else if (flag == 2) {
                btnEmptyview.setVisibility(View.GONE);
            }
            Utils.setText(tvEmptyviewDesc, msg, "", View.VISIBLE, View.VISIBLE);
        }
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
