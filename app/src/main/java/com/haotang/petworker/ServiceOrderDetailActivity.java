package com.haotang.petworker;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.petworker.adapter.ServiceOrderDetailAdapter;
import com.haotang.petworker.entity.ServiceOrderDetail;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author:姜谷蓄
 * @Date:2020/4/14
 * @Description:服务订单明细
 */
public class ServiceOrderDetailActivity extends SuperActivity {

    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.rv_service_order)
    RecyclerView rvServiceOrder;
    @BindView(R.id.iv_emptyview_img)
    ImageView ivEmptyviewImg;
    @BindView(R.id.tv_emptyview_desc)
    TextView tvEmptyviewDesc;
    @BindView(R.id.btn_emptyview)
    Button btnEmptyview;
    private LinearLayout llEmpty;
    private String data;
    private ServiceOrderDetailAdapter adapter;
    private List<ServiceOrderDetail.DataBeanX.DataBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorBar();
        setView();
        initData();
        getData();
        setListener();
    }

    private void setListener() {
        adapter.setListener(new ServiceOrderDetailAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == position) {
                            if (list.get(i).isSelected()) {
                                list.get(i).setSelected(false);
                            } else {
                                list.get(i).setSelected(true);
                            }
                        } else {
                            list.get(i).setSelected(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData() {
        data = getIntent().getStringExtra("data");
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.serviceOrderDetail(mContext, data, serviceOrderHandler);
    }

    private AsyncHttpResponseHandler serviceOrderHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj.getInt("code") == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        Gson gson = new Gson();
                        ServiceOrderDetail serviceOrderDetail = gson.fromJson(new String(responseBody), ServiceOrderDetail.class);
                        if (serviceOrderDetail.getData().getData() != null) {
                            rvServiceOrder.setVisibility(View.VISIBLE);
                            list.addAll(serviceOrderDetail.getData().getData());
                            adapter.notifyDataSetChanged();
                            llEmpty.setVisibility(View.GONE);
                        } else {
                            rvServiceOrder.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                            tvEmptyviewDesc.setText("暂无明细~");
                            btnEmptyview.setVisibility(View.GONE);
                        }
                    }
                } else {
                    rvServiceOrder.setVisibility(View.GONE);
                    llEmpty.setVisibility(View.VISIBLE);
                    tvEmptyviewDesc.setText(jobj.getString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastBottomShort(mContext, "请求失败");
        }
    };

    private void setView() {
        setContentView(R.layout.activity_service_order_detail);
        ButterKnife.bind(this);
        llEmpty = findViewById(R.id.ll_serviceorder_default);
        tvTitlebarTitle.setText("服务订单明细");
        adapter = new ServiceOrderDetailAdapter(mContext, list);
        rvServiceOrder.setLayoutManager(new LinearLayoutManager(mContext));
        rvServiceOrder.setAdapter(adapter);
    }


    @OnClick({R.id.ib_titlebar_back, R.id.btn_emptyview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_emptyview:
                getData();
                break;
        }
    }
}
