package com.haotang.petworker.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.AboutAppActivity;
import com.haotang.petworker.AgreementActivity;
import com.haotang.petworker.AreaAndEvaFeedBackActivity;
import com.haotang.petworker.BeauticianIntroduceActivity;
import com.haotang.petworker.EatTimeActivity;
import com.haotang.petworker.LoginNewActivity;
import com.haotang.petworker.MainActivity;
import com.haotang.petworker.MyEvaluateActivity;
import com.haotang.petworker.MyLevelActivity;
import com.haotang.petworker.MyProductionActivity;
import com.haotang.petworker.NoticeActivity;
import com.haotang.petworker.R;
import com.haotang.petworker.ServiceTimeChoosePetActivity;
import com.haotang.petworker.WorkTimeActivity;
import com.haotang.petworker.event.RefreshProdictionEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.view.AlertDialogDefault;
import com.haotang.petworker.view.NiceImageView;
import com.haotang.petworker.view.ObservableScrollView;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author:姜谷蓄
 * @Date:2019/8/31
 * @Description:新版我的页面
 */
public class MyNewFragment extends BaseFragment {

    @BindView(R.id.tv_jobnumber)
    TextView tvJobnumber;
    @BindView(R.id.tv_worker_name)
    TextView tvWorkerName;
    @BindView(R.id.iv_myfragment_level)
    ImageView ivMyfragmentLevel;
    @BindView(R.id.iv_myfragment_head)
    NiceImageView ivMyfragmentHead;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_feedback_num)
    TextView tvFeedbackNum;
    @BindView(R.id.rv_myfragment_top)
    RelativeLayout rvMyfragmentTop;
    @BindView(R.id.ll_myfragment_production)
    LinearLayout llMyfragmentProduction;
    @BindView(R.id.ll_myfragment_evaluate)
    LinearLayout llMyfragmentEvaluate;
    @BindView(R.id.ll_myfragment_level)
    LinearLayout llMyfragmentLevel;
    @BindView(R.id.ll_myfragment_middle)
    LinearLayout llMyfragmentMiddle;
    @BindView(R.id.ll_myfragment_ordertime)
    LinearLayout llMyfragmentOrdertime;
    @BindView(R.id.ll_myfragment_servicetime)
    LinearLayout llMyfragmentServicetime;
    @BindView(R.id.ll_myfragment_areasug)
    LinearLayout llMyfragmentAreasug;
    @BindView(R.id.ll_myfragment_evaluateboss)
    LinearLayout llMyfragmentEvaluateboss;
    @BindView(R.id.ll_myfragment_beboss)
    LinearLayout llMyfragmentBeboss;
    @BindView(R.id.ll_myfragment_study)
    LinearLayout llMyfragmentStudy;
    @BindView(R.id.ll_myfragment_about)
    LinearLayout llMyfragmentAbout;
    @BindView(R.id.ll_myfragment_bottom)
    LinearLayout llMyfragmentBottom;
    @BindView(R.id.btn_quite)
    Button btnQuite;
    Unbinder unbinder;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rl_my_top)
    RelativeLayout rlMyTop;
    @BindView(R.id.tv_ordernum_tip)
    TextView tvOrdernumTip;
    @BindView(R.id.ll_myfragment_eattime)
    LinearLayout llMyfragmentEattime;
    @BindView(R.id.iv_myfragment_top)
    ImageView ivMyfragmentTop;
    @BindView(R.id.osv_myfragment)
    ObservableScrollView osvMyfragment;
    @BindView(R.id.rl_myfragment_title)
    RelativeLayout rvTop;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.v_red_one)
    View vRedOne;
    @BindView(R.id.v_red_two)
    View vRedTwo;
    @BindView(R.id.v_red_three)
    View vRedThree;
    private SharedPreferenceUtil spUtil;
    private int imageHeight;

    //如果读了消息重新请求
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshProdictionEvent event) {
        if (event != null && event.isRefresh()) {
            CommUtil.havingNewReplay(getActivity(), spUtil.getString("lastRequestTimeType0",""),spUtil.getString("lastRequestTimeType1",""),spUtil.getString("lastRequestTimeType2",""), getNewReplayHandler);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_newfragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(getActivity());
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        spUtil = SharedPreferenceUtil.getInstance(getActivity());
        tvWorkerName.setText(spUtil.getString("wusername", ""));
        GlideUtil.loadImg(getActivity(), spUtil.getString("wimage", ""), ivMyfragmentHead, R.drawable.icon_beautician_default);
        ibTitlebarBack.setVisibility(View.GONE);
        tvTitlebarTitle.setText("我");
        tvTitlebarTitle.setVisibility(View.GONE);
        ibTitlebarOther.setVisibility(View.VISIBLE);
        ibTitlebarOther.setBackgroundResource(R.drawable.icon_main_notic);
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = ivMyfragmentTop.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivMyfragmentTop.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = ivMyfragmentTop.getHeight();
                osvMyfragment.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        if (y <= 0) {
                            tvTitlebarTitle.setVisibility(View.GONE);
                            rvTop.setBackgroundColor(Color.argb((int) 0, 0, 34, 65));
                        } else if (y > 0 && y <= imageHeight) {
                            tvTitlebarTitle.setVisibility(View.VISIBLE);
                            float scale = (float) y / imageHeight;
                            float alpha = (255 * scale);
                            // 只是layout背景透明(仿知乎滑动效果)
                            rvTop.setBackgroundColor(Color.argb((int) alpha, 0, 34, 65));
                        } else {
                            rvTop.setBackgroundColor(Color.argb((int) 255, 0, 34, 65));
                        }
                    }
                });
            }
        });
        getData();
    }

    private void clearUserInfo() {
        spUtil.removeData("wuserid");
        spUtil.removeData("wordernum");
        spUtil.removeData("wgrade");
        spUtil.removeData("titlelevel");
        spUtil.removeData("wusername");
        spUtil.removeData("wimage");
        spUtil.removeData("wcellphone");
        if (MainActivity.act != null) {
            MainActivity.act.finish();
        }
        Intent intent = new Intent(getActivity(), LoginNewActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void getData() {
        CommUtil.getWorkerInfo(getActivity(), spUtil.getString("wcellphone", ""),
                Global.getIMEI(getActivity()), Global.getCurrentVersion(getActivity()),
                beauticianHandler);
        CommUtil.getHistoricalData(getActivity(), getHistoricalDataHandler);
        CommUtil.havingNewReplay(getActivity(), spUtil.getString("lastRequestTimeType0",""),spUtil.getString("lastRequestTimeType1",""),spUtil.getString("lastRequestTimeType2",""), getNewReplayHandler);
    }

    private AsyncHttpResponseHandler getHistoricalDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("orderNumber") && !jdata.isNull("orderNumber")) {
                            tvOrderNum.setText(jdata.getInt("orderNumber") + "");
                        }
                        if (jdata.has("historicalRate") && !jdata.isNull("historicalRate")) {
                            String historicalRate = double2String(jdata.getDouble("historicalRate"));
                            tvFeedbackNum.setText(historicalRate + "%");
                        }
                    }
                } else {
                    ToastUtil.showToastBottomShort(getActivity(), msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastBottomShort(getActivity(), "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            //pDialog.closeDialog();
            ToastUtil.showToastBottomShort(getActivity(), "请求失败statusCode = " + statusCode);
        }
    };

    private String studentSpace;
    private AsyncHttpResponseHandler beauticianHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            //pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("level") && !jData.isNull("level")) {
                            JSONObject jl = jData.getJSONObject("level");
                            if (jl.has("id") && !jl.isNull("id")) {
                                int id = jl.getInt("id");
                                if (id == 1) {
                                    ivMyfragmentLevel.setImageResource(R.drawable.middle_level_red);
                                } else if (id == 2) {
                                    ivMyfragmentLevel.setImageResource(R.drawable.heigh_level_red);
                                } else {
                                    ivMyfragmentLevel.setImageResource(R.drawable.best_level_red);
                                }
                            }
                        }
                        if (jData.has("employeeNumber") && !jData.isNull("employeeNumber")) {
                            tvJobnumber.setText("工号  " + jData.getString("employeeNumber"));
                        }
                        if (jData.has("studentSpace") && !jData.isNull("studentSpace")) {
                            studentSpace = jData.getString("studentSpace");
                        }
                    }
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg"))
                        ToastUtil.showToastCenterShort(
                                getActivity(),
                                jobj.getString("msg"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            //pDialog.closeDialog();
        }

    };

    //小红点
    private AsyncHttpResponseHandler getNewReplayHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        //区域建议
                        if (jdata.has("havingNewReply0") && !jdata.isNull("havingNewReply0")) {
                            int havingNewReply0 = jdata.getInt("havingNewReply0");
                            if (havingNewReply0 == 0) {
                                vRedOne.setVisibility(View.GONE);
                            } else {
                                vRedOne.setVisibility(View.VISIBLE);
                            }
                        }
                        //评价店长
                        if (jdata.has("havingNewReply1") && !jdata.isNull("havingNewReply1")) {
                            int havingNewReply1 = jdata.getInt("havingNewReply1");
                            if (havingNewReply1 == 0) {
                                vRedTwo.setVisibility(View.GONE);
                            } else {
                                vRedTwo.setVisibility(View.VISIBLE);
                            }
                        }
                        //申请当店长
                        if (jdata.has("havingNewReply2") && !jdata.isNull("havingNewReply2")) {
                            int havingNewReply2 = jdata.getInt("havingNewReply2");
                            if (havingNewReply2 == 0) {
                                vRedThree.setVisibility(View.GONE);
                            } else {
                                vRedThree.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastBottomShort(getActivity(), msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastBottomShort(getActivity(), "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastBottomShort(getActivity(), "请求失败statusCode = " + statusCode);
        }
    };

    public static String double2String(double d) {
        BigDecimal bg = new BigDecimal(d * 100);
        double doubleValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.valueOf((int) doubleValue);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_myfragment_production, R.id.ll_myfragment_evaluate, R.id.ll_myfragment_level, R.id.ll_myfragment_middle, R.id.ll_myfragment_ordertime, R.id.ll_myfragment_servicetime, R.id.ll_myfragment_areasug, R.id.ll_myfragment_evaluateboss, R.id.ll_myfragment_beboss, R.id.ll_myfragment_study, R.id.ll_myfragment_about, R.id.ll_myfragment_bottom, R.id.btn_quite, R.id.iv_myfragment_head, R.id.ll_myfragment_eattime, R.id.ib_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_myfragment_production:
                Intent intent = new Intent(getActivity(), MyProductionActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.iv_myfragment_head:
                Intent intent_introduce = new Intent(getActivity(), BeauticianIntroduceActivity.class);
                startActivity(intent_introduce);
                break;
            case R.id.ll_myfragment_evaluate:
                Intent intent_evaluate = new Intent(getActivity(), MyEvaluateActivity.class);
                startActivity(intent_evaluate);
                break;
            case R.id.ll_myfragment_level:
                Intent intent_level = new Intent(getActivity(), MyLevelActivity.class);
                startActivity(intent_level);
                break;
            case R.id.ll_myfragment_middle:
                break;
            case R.id.ll_myfragment_ordertime:
                Intent intent_ordertime = new Intent(getActivity(), WorkTimeActivity.class);
                startActivity(intent_ordertime);
                break;
            case R.id.ll_myfragment_servicetime:
                Intent intent_service = new Intent(getActivity(), ServiceTimeChoosePetActivity.class);
                startActivity(intent_service);
                break;
            case R.id.ll_myfragment_areasug:
                Intent intent_areasug = new Intent(getActivity(), AreaAndEvaFeedBackActivity.class);
                intent_areasug.putExtra("title", "区域建议");
                intent_areasug.putExtra("type", 0);
                getActivity().startActivity(intent_areasug);
                break;
            case R.id.ll_myfragment_evaluateboss:
                Intent intent_evaboss = new Intent(getActivity(), AreaAndEvaFeedBackActivity.class);
                intent_evaboss.putExtra("title", "评价店长");
                intent_evaboss.putExtra("type", 1);
                getActivity().startActivity(intent_evaboss);
                break;
            case R.id.ll_myfragment_beboss:
                Intent intent_beboss = new Intent(getActivity(), AreaAndEvaFeedBackActivity.class);
                intent_beboss.putExtra("title", "应聘店长");
                intent_beboss.putExtra("type", 2);
                getActivity().startActivity(intent_beboss);
                break;
            case R.id.ll_myfragment_study:
                startActivity(new Intent(getActivity(), AgreementActivity.class).putExtra("url", studentSpace));
                break;
            case R.id.ll_myfragment_about:
                Intent intent_about = new Intent(getActivity(), AboutAppActivity.class);
                getActivity().startActivity(intent_about);
                break;
            case R.id.btn_quite:
                new AlertDialogDefault(getActivity()).builder()
                        .setTitle("提示").setMsg("您确定要退出登录吗？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearUserInfo();
                    }
                }).show();
                break;
            case R.id.ll_myfragment_eattime:
                startActivity(new Intent(getActivity(), EatTimeActivity.class));
                break;
            case R.id.ib_titlebar_other:
                startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
        }

    }
}
