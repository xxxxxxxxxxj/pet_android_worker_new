package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.haotang.petworker.adapter.EatTimeAdapter;
import com.haotang.petworker.entity.EatTimeBean;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.EatTimeComponentOne;
import com.haotang.petworker.view.EatTimeComponentTwo;
import com.haotang.petworker.view.GridDecoration;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 吃饭时间设置界面
 */
public class EatTimeActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_eatetime_desc)
    TextView tvEatetimeDesc;
    @BindView(R.id.btn_etatime)
    Button btnEtatime;
    @BindView(R.id.rv_etatime)
    RecyclerView rvEtatime;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    private List<EatTimeBean> list = new ArrayList<EatTimeBean>();
    private EatTimeAdapter eatTimeAdapter;
    private boolean isEdit;
    private String timeRange;
    private String desc;
    private String example;
    private StringBuilder sbZhu = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        initData();
        findView();
        setView();
        initListener();
        getData();
    }

    private void initData() {

    }

    private void findView() {
        setContentView(R.layout.activity_eat_time);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("吃饭时间设置");
        ibTitlebarOther.setVisibility(View.VISIBLE);
        ibTitlebarOther.setBackgroundResource(R.drawable.icon_integral_question);
        rvEtatime.setLayoutManager(new GridLayoutManager(this, 3));
        eatTimeAdapter = new EatTimeAdapter(R.layout.item_eatetime
                , list);
        rvEtatime.setAdapter(eatTimeAdapter);
    }

    private void initListener() {

    }

    private void getData() {
        getEatTime();
    }

    private void getEatTime() {
        mPDialog.showDialog();
        list.clear();
        sbZhu.setLength(0);
        CommUtil.getEatTime(mActivity, getEatTimeHanler);
    }

    private AsyncHttpResponseHandler getEatTimeHanler = new AsyncHttpResponseHandler() {

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
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("topTip") && !jdata.isNull("topTip")) {
                            Utils.setText(tvEatetimeDesc, jdata.getString("topTip"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("timeRange") && !jdata.isNull("timeRange")) {
                            timeRange = jdata.getString("timeRange");
                        }
                        if (jdata.has("rules") && !jdata.isNull("rules")) {
                            JSONArray jarrrules = jdata.getJSONArray("rules");
                            if (jarrrules.length() > 1) {
                                desc = jarrrules.getString(0);
                                example = jarrrules.getString(1);
                            }
                        }
                        if (jdata.has("tips") && !jdata.isNull("tips")) {
                            JSONArray jarrtips = jdata.getJSONArray("tips");
                            if (jarrtips.length() > 0) {
                                for (int i = 0; i < jarrtips.length(); i++) {
                                    sbZhu.append(jarrtips.getString(i));
                                }
                            }
                        }
                        if (jdata.has("topTip") && !jdata.isNull("topTip")) {
                            Utils.setText(tvEatetimeDesc, jdata.getString("topTip"), "", View.VISIBLE, View.VISIBLE);
                        }
                        if (jdata.has("dataset") && !jdata.isNull("dataset")
                                && jdata.getJSONArray("dataset").length() > 0) {
                            JSONArray jsonArray = jdata.getJSONArray("dataset");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(EatTimeBean
                                        .json2Entity(jsonArray
                                                .getJSONObject(i)));
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastCenterShort(mActivity,
                            msg);
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                ToastUtil.showToastCenterShort(mActivity,
                        "数据异常");
            }
            GridDecoration decoration = new GridDecoration(mContext, list.size(), 3) {
                @Override
                public String getHeaderName(int pos) {
                    return list.get(pos).getMonth();
                }
            };
            decoration.setHeaderHeight(getResources().getDimensionPixelSize(R.dimen.dp_50));
            decoration.setHeaderContentColor(getResources().getColor(R.color.white));
            decoration.setTextColor(getResources().getColor(R.color.a002241));
            decoration.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp_24));
            decoration.setTextBold();
            rvEtatime.addItemDecoration(decoration);
            eatTimeAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mActivity,
                    "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other, R.id.btn_etatime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ib_titlebar_other:
                startActivity(new Intent(this, EatTimeIntroduceActivity.class)
                        .putExtra("timeRange", timeRange).putExtra("desc", desc)
                        .putExtra("example", example).putExtra("zhu", sbZhu.toString()));
                break;
            case R.id.btn_etatime:
                if (isEdit) {
                    setEatTime();
                } else {
                    btnEtatime.setText("保存");
                    btnEtatime.setBackgroundResource(R.drawable.bg_round_ff3a1e);
                    isEdit = !isEdit;
                    eatTimeAdapter.setEdit(isEdit);
                    showGuideView1();
                }
                break;
        }
    }

    public void showGuideView1() {
        boolean GUIDE_EATTIME = spUtil.getBoolean(
                "GUIDE_EATTIME", false);
        if (/*!GUIDE_EATTIME && */list.size() >= 1) {
            spUtil.saveBoolean("GUIDE_EATTIME", true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(rvEtatime.getChildAt(0)).setAlpha(200)
                    .setHighTargetCorner(getResources().getDimensionPixelSize(R.dimen.dp_20))
                    .setExitAnimationId(android.R.anim.fade_out);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {
                    showGuideView2();
                }
            });
            builder.addComponent(new EatTimeComponentOne(mActivity));
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(true);
            guide.show(mActivity);
        }
    }

    public void showGuideView2() {
        final GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(btnEtatime).setAlpha(200)
                .setHighTargetCorner(getResources().getDimensionPixelSize(R.dimen.dp_12))
                .setExitAnimationId(android.R.anim.fade_out);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
            }
        });
        builder.addComponent(new EatTimeComponentTwo(mActivity));
        Guide guide = builder.createGuide();
        guide.show(mActivity);
    }

    private void setEatTime() {
        mPDialog.showDialog();
        CommUtil.setEatTime(mActivity, list, setEatTimeHanler);
    }

    private AsyncHttpResponseHandler setEatTimeHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    ToastUtil.showToastCenterShort(mActivity, "吃饭时间已调整～");
                    btnEtatime.setText("编辑");
                    btnEtatime.setBackgroundResource(R.drawable.bg_round_384359);
                    isEdit = !isEdit;
                    eatTimeAdapter.setEdit(isEdit);
                } else {
                    ToastUtil.showToastCenterShort(mActivity,
                            msg);
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                ToastUtil.showToastCenterShort(mActivity,
                        "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mActivity,
                    "请求失败");
        }
    };

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
