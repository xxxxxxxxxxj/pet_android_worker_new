package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.view.NiceImageView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyLevelActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_level_nextlevel)
    TextView tvLevelNextlevel;
    @BindView(R.id.tv_level_information)
    TextView tvLevelInformation;
    @BindView(R.id.tv_totalorder_num)
    TextView tvTotalorderNum;
    @BindView(R.id.tv_totalorder_tonum)
    TextView tvTotalorderTonum;
    @BindView(R.id.tv_beautician_orders)
    TextView tvBeauticianOrders;
    @BindView(R.id.tv_beautician_toorders)
    TextView tvBeauticianToorders;
    @BindView(R.id.tv_goodnum)
    TextView tvGoodnum;
    @BindView(R.id.tv_togoodnum)
    TextView tvTogoodnum;
    @BindView(R.id.tv_month_badnum)
    TextView tvMonthBadnum;
    @BindView(R.id.tv_month_tobadnum)
    TextView tvMonthTobadnum;
    @BindView(R.id.iv_introduce_head)
    NiceImageView ivIntroduceHead;
    private MProgressDialog pDialog;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        setView();
        getData();
    }

    private void getData() {
        pDialog.showDialog();
        CommUtil.getWorkerInfo(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this),
                beauticianHandler);
    }

    private AsyncHttpResponseHandler beauticianHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("h5url") && !jData.isNull("h5url")) {
                            url = jData.getString("h5url");
                        }
                        if (jData.has("avatar")&&!jData.isNull("avatar")){
                            GlideUtil.loadImg(MyLevelActivity.this,jData.getString("avatar"),ivIntroduceHead,R.drawable.icon_beautician_default);
                        }
                        if (jData.has("level") && !jData.isNull("level")) {
                            JSONObject jl = jData.getJSONObject("level");
                            if (jl.has("name")&&!jl.isNull("name")){
                                tvLevel.setText(jl.getString("name")+"美容师");
                            }
                        }
                        if (jData.has("nextLevel")
                                && !jData.isNull("nextLevel")) {
                            JSONObject jnl = jData.getJSONObject("nextLevel");
                            if (jnl.has("name") && !jnl.isNull("name")) {
                                tvLevelNextlevel.setText("下一等级："+jnl.getString("name")+"美容师");
                            }
                        }
                        if (jData.has("levelDetail")
                                && !jData.isNull("levelDetail")) {
                            JSONObject jld = jData.getJSONObject("levelDetail");
                            if (jld.has("totalOrder")
                                    && !jld.isNull("totalOrder")) {
                                tvTotalorderNum.setText(jld
                                        .getString("totalOrder"));
                            }
                            if (jld.has("totalBeautyOrder")
                                    && !jld.isNull("totalBeautyOrder")) {
                                tvBeauticianOrders.setText(jld
                                        .getString("totalBeautyOrder"));
                            }
                            if (jld.has("positivePercent")
                                    && !jld.isNull("positivePercent")) {
                                tvGoodnum.setText(jld
                                        .getString("positivePercent"));
                            }
                            if (jld.has("recentNegtiveTotal")
                                    && !jld.isNull("recentNegtiveTotal")) {
                                tvMonthBadnum.setText(jld
                                        .getString("recentNegtiveTotal"));
                            }
                        }
                        if (jData.has("nextLevelDetail")
                                && !jData.isNull("nextLevelDetail")) {
                            JSONObject jld = jData
                                    .getJSONObject("nextLevelDetail");
                            if (jld.has("totalOrder")
                                    && !jld.isNull("totalOrder")) {
                                tvTotalorderTonum.setText(jld
                                        .getString("totalOrder"));
                            }
                            if (jld.has("totalBeautyOrder")
                                    && !jld.isNull("totalBeautyOrder")) {
                                tvBeauticianToorders.setText(jld
                                        .getString("totalBeautyOrder"));
                            }
                            if (jld.has("positivePercent")
                                    && !jld.isNull("positivePercent")) {
                                tvTogoodnum.setText(jld
                                        .getString("positivePercent"));
                            }
                            if (jld.has("recentNegtiveTotal")
                                    && !jld.isNull("recentNegtiveTotal")) {
                                tvMonthTobadnum.setText(jld
                                        .getString("recentNegtiveTotal"));
                            }
                        }

                    } else {

                    }
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastCenterShort(
                                MyLevelActivity.this,
                                jobj.getString("msg"));
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            pDialog.closeDialog();
        }

    };

    private void setView() {
        setContentView(R.layout.activity_beautician_level);
        ButterKnife.bind(this);
        pDialog = new MProgressDialog(this);
        btTitlebarOther.setVisibility(View.VISIBLE);
        btTitlebarOther.setText("规则");
        tvTitlebarTitle.setText("我的等级");

    }

    @OnClick({R.id.ib_titlebar_back, R.id.bt_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finishWithAnimation();
                break;
            case R.id.bt_titlebar_other:
                Intent intent = new Intent(MyLevelActivity.this,NoticeDetailActivity.class);
                intent.putExtra("h5url", url);
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
