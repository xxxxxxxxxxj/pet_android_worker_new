package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.petworker.entity.DetailOrder;
import com.haotang.petworker.entity.Order;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.NiceImageView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 服务完成界面
 */
public class ServiceCompleteActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_service_complete_petimg)
    NiceImageView ivServiceCompletePetimg;
    @BindView(R.id.tv_service_complete_petname)
    TextView tvServiceCompletePetname;
    @BindView(R.id.tv_service_complete_hdjf)
    TextView tvServiceCompleteHdjf;
    @BindView(R.id.ll_service_complete_hljl)
    LinearLayout ll_service_complete_hljl;
    private DetailOrder order;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        getData();
    }

    private void initData() {
        orderId = getIntent().getIntExtra("orderId", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_service_complete);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("服务完成");
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.orderDetail(mContext, orderId, queryOrderDetailWorker);
    }

    private AsyncHttpResponseHandler queryOrderDetailWorker = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        order = DetailOrder.json2Entity(jobj.getJSONObject("data"));
                    }
                } else {
                    ToastUtil.showToastCenterShort(mContext,
                            msg);
                }
            } catch (Exception e) {
                ToastUtil.showToastCenterShort(mContext,
                        "数据异常");
                Log.e("TAG", "e.getMessage() = " + e.getMessage());
                Log.e("TAG", "e.toString() = " + e.toString());
            }
            if (order != null) {
                GlideUtil.loadImg(mContext, order.avatar, ivServiceCompletePetimg, R.drawable.icon_production_default);
                Utils.setText(tvServiceCompletePetname, order.typeName, "", View.VISIBLE, View.GONE);
                if (order.myPetId > 0) {
                    ll_service_complete_hljl.setVisibility(View.VISIBLE);
                    Utils.setText(tvServiceCompleteHdjf, order.integral + order.integralCopy, "", View.VISIBLE, View.VISIBLE);
                } else {
                    ll_service_complete_hljl.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mContext,
                    "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.tv_service_complete_next, R.id.iv_service_complete_qtx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_service_complete_next:
                if (OrderDetailNewActivity.act != null) {
                    OrderDetailNewActivity.act.finish();
                }
                startActivity(new Intent(mContext, OrderActivity.class));
                finish();
                break;
            case R.id.iv_service_complete_qtx:
                Intent intent = new Intent(mContext, AddArchivesActivity.class);
                intent.putExtra("orderId", orderId);
                startActivity(intent);
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
