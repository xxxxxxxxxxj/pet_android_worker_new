package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.petworker.event.RefreshProdictionEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.AlertDialogDefault;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductionDetailActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_production_name)
    TextView tvProductionName;
    @BindView(R.id.iv_production_icon)
    ImageView ivProductionIcon;
    @BindView(R.id.btn_del_production)
    Button btnDelProduction;
    @BindView(R.id.tv_production_data)
    TextView tvProductionData;
    @BindView(R.id.tv_production_audit)
    TextView tvProductionAudit;
    private MProgressDialog pDialog;
    private String productionName;
    private String uploadTIme;
    private int id;
    private String bigImg;
    private int audit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setVIew();
    }

    private void initData() {
        Intent intent = getIntent();
        productionName = intent.getStringExtra("name");
        uploadTIme = intent.getStringExtra("time");
        id = intent.getIntExtra("id", 0);
        bigImg = intent.getStringExtra("bigImg");
        audit = intent.getIntExtra("audit",-1);
    }

    private void setVIew() {
        setContentView(R.layout.activity_production_detail);
        pDialog = new MProgressDialog(this);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("我的作品");
        tvProductionName.setText(productionName);
        tvProductionData.setText(uploadTIme);
        GlideUtil.loadImg(this,bigImg,ivProductionIcon,R.drawable.icon_production_default);
        if (audit==0){
            tvProductionAudit.setVisibility(View.VISIBLE);
            tvProductionAudit.setText("待审核");
        }else if (audit==2){
            tvProductionAudit.setVisibility(View.VISIBLE);
            tvProductionAudit.setText("未通过");
        }else {
            tvProductionAudit.setVisibility(View.GONE);
        }
    }

    private AsyncHttpResponseHandler deleteHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            Utils.mLogError("删除美容师作品：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    EventBus.getDefault().post(new RefreshProdictionEvent(true));
                    ToastUtil.showToastCenterShort(
                            ProductionDetailActivity.this, msg);
                    finish();
                } else {
                    ToastUtil.showToastCenterShort(
                            ProductionDetailActivity.this, msg);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ToastUtil
                        .showToastCenterShort(
                                ProductionDetailActivity.this,
                                "网络异常，请重新删除");

            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            pDialog.closeDialog();
            ToastUtil.showToastCenterShort(
                    ProductionDetailActivity.this, "网络异常，请重新删除");
        }

    };

    @OnClick({R.id.ib_titlebar_back, R.id.btn_del_production})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_del_production:
                new AlertDialogDefault(mActivity).builder()
                        .setTitle("确定删除？").setMsg("删除后不可恢复该作品").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommUtil.deleteProduction(ProductionDetailActivity.this,spUtil.getString("wcellphone", ""),
                                Global.getIMEI(ProductionDetailActivity.this), Global.getCurrentVersion(ProductionDetailActivity.this),
                                id, deleteHandler);
                    }
                }).show();
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
