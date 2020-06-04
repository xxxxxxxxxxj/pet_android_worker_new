package com.haotang.petworker.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.AgreementActivity;
import com.haotang.petworker.IncomeActivity;
import com.haotang.petworker.MainActivity;
import com.haotang.petworker.MyFansNewActivity;
import com.haotang.petworker.MyIntegralActivity;
import com.haotang.petworker.NoticeActivity;
import com.haotang.petworker.OrderActivity;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.ActivityPage;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.SolveClickTouchConflictLayout;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * @author:姜谷蓄
 * @Date:2019/8/29
 * @Description:首页
 */
public class MainNewFragment extends BaseFragment implements View.OnClickListener {

    private RefreshLayout refreshLayout;
    private RelativeLayout rlMywork;
    private RelativeLayout rlMymoney;
    private TextView tvMywork;
    private TextView tvMymoney;
    private View lineWork;
    private View lineMoney;
    private TextView tvDay;
    private TextView tvYear;
    private LinearLayout llWorkInfo;
    private RelativeLayout rlMoneyInfo;
    private RelativeLayout rvMyfans;
    private RelativeLayout rlMyintegral;
    private Button btnIncome;
    private boolean canSee = false;
    private MProgressDialog pDialog;
    private SharedPreferenceUtil spUtil;
    private ImageView ivSee;
    private TextView tvEndTime;
    private TextView tvWaitservice;
    private TextView tvWaitwrite;
    private TextView tvFans;
    private TextView tvIntegral;
    private TextView tvTotalmoney;
    private TextView tvSlidToOrder;
    private TextView tvHello;
    private ImageView ivLevel;
    private RelativeLayout rlTodayOrder;
    private RelativeLayout rlWaitWrite;
    private RelativeLayout rvSildToOrder;
    private float mPosX, mPosY, mCurPosX, mCurPosY;
    private NestedScrollView mScrollView;
    private ImageView ivNotic;
    private TextView tvWaitNum;
    private int todayOrderNumber;
    private double sumincome;
    private String daytip;
    private double integral;
    private String year;
    private String month;
    private String day;
    private String selectMonth;
    GestureDetector mGestureDetector;  //手势检测对象
    private static final String TAG = "MainActivity";
    private static final int FLING_MIN_DISTANCE = 200;//滑动最小的距离
    private static final int FLING_MIN_VELOCITY = 0;//滑动最小的速度
    private ImageView iv_mainfragment_slidtoorder;
    private ImageView iv_mainfragment_slidtoorder1;
    private String workerRule;
    private List<ActivityPage> localBannerList = new ArrayList<ActivityPage>();
    //文件名称
    String fileName_one = "ad_mainfrag_one.txt";
    String fileName_one_day = "ad_mainfrag_one_day.txt";
    private ImageView iv_mainfragment_ncp;
    private String ncp_url;
    private String ncp_imgurl;
    private int reportSwitch;
    private String shopName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_main, container, false);
        iv_mainfragment_ncp = view.findViewById(R.id.iv_mainfragment_ncp);
        refreshLayout = view.findViewById(R.id.sl_mainfragment_all);
        rlMywork = view.findViewById(R.id.rl_mainfragment_mywork);
        rlMymoney = view.findViewById(R.id.rl_mainfragment_mymoney);
        tvHello = view.findViewById(R.id.tv_mainfragment_hello);
        lineWork = view.findViewById(R.id.v_work_line);
        tvEndTime = view.findViewById(R.id.tv_mainfragment_moneytime);
        ivNotic = view.findViewById(R.id.iv_mainfragment_notic);
        lineMoney = view.findViewById(R.id.v_money_line);
        tvDay = view.findViewById(R.id.tv_mainfragemnt_day);
        tvYear = view.findViewById(R.id.tv_mainfragment_year);
        tvMywork = view.findViewById(R.id.tv_mainfragment_mywork);
        tvMymoney = view.findViewById(R.id.tv_mainfragment_mymoney);
        ivLevel = view.findViewById(R.id.iv_mainfragment_level);
        llWorkInfo = view.findViewById(R.id.ll_mainfragment_mywork);
        rlMoneyInfo = view.findViewById(R.id.rl_mainfragment_moneyinfo);
        ivSee = view.findViewById(R.id.iv_mainfragment_see);
        rlTodayOrder = view.findViewById(R.id.rl_mainfragment_todayorder);
        rlWaitWrite = view.findViewById(R.id.rl_mainfragment_waitwrite);
        tvIntegral = view.findViewById(R.id.tv_mainfragment_integral);
        iv_mainfragment_slidtoorder = view.findViewById(R.id.iv_mainfragment_slidtoorder);
        iv_mainfragment_slidtoorder1 = view.findViewById(R.id.iv_mainfragment_slidtoorder1);
        tvFans = view.findViewById(R.id.tv_mainfragment_myfans);
        tvTotalmoney = view.findViewById(R.id.tv_mainfragment_totalmoney);
        rvMyfans = view.findViewById(R.id.rl_mainfragment_myfans);
        tvWaitwrite = view.findViewById(R.id.tv_mainfragment_waitwrite);
        rlMyintegral = view.findViewById(R.id.rl_mainfragment_integral);
        tvSlidToOrder = view.findViewById(R.id.tv_mainfragment_slidtoorder);
        rvSildToOrder = view.findViewById(R.id.rl_mainfragment_slidtoorder);
        mScrollView = view.findViewById(R.id.nv_mainfragmnet);
        btnIncome = view.findViewById(R.id.btn_mainfragment_income);
        tvWaitNum = view.findViewById(R.id.tv_mainfragment_waitservice);
        /** 触摸事件的注册 */
        ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pDialog = new MProgressDialog(getActivity());
        spUtil = SharedPreferenceUtil.getInstance(getActivity());
        getLocalTime();
        setView();
        getData();
        setListener();
        boolean mainfrag_dialog = spUtil.getBoolean("MAINFRAG_DIALOG", false);
        if (!mainfrag_dialog) {
            spUtil.saveBoolean("MAINFRAG_DIALOG", true);
            getMainActivity();
        }
    }

    private void getLocalTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR) + "";
        if (calendar.get(Calendar.MONTH) + 1 >= 10) {
            month = calendar.get(Calendar.MONTH) + 1 + "";
        } else {
            int i = calendar.get(Calendar.MONTH) + 1;
            month = "0" + i;
        }
        day = calendar.get(Calendar.DAY_OF_MONTH) + "";
    }

    private void getData() {
        CommUtil.healthReport(getActivity(), healthReportHandler);
        CommUtil.getHistoricalData(getActivity(), getHistoricalDataHandler);
        CommUtil.getMonthIncome(getActivity(), selectMonth, getMonthIncome);
        CommUtil.getMainInfo(getActivity(), spUtil.getString("lastFlushTime", ""), getMainInfoHandler);
        CommUtil.getWorkerInfo(getActivity(), spUtil.getString("wcellphone", ""),
                Global.getIMEI(getActivity()), Global.getCurrentVersion(getActivity()),
                beauticianHandler);
    }

    private AsyncHttpResponseHandler healthReportHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("switch") && !jData.isNull("switch")) {
                            reportSwitch = jData.getInt("switch");
                        }
                        if (jData.has("buttonImg") && !jData.isNull("buttonImg")) {
                            ncp_imgurl = jData.getString("buttonImg");
                        }
                        if (jData.has("url") && !jData.isNull("url")) {
                            ncp_url = jData.getString("url");
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
            if (reportSwitch == 0) {
                iv_mainfragment_ncp.setVisibility(View.GONE);
            } else {
                iv_mainfragment_ncp.setVisibility(View.VISIBLE);
                GlideUtil.loadImg(getActivity(), ncp_imgurl, iv_mainfragment_ncp, 0);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

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
                                    ivLevel.setImageResource(R.drawable.middle_level_gray);
                                } else if (id == 2) {
                                    ivLevel.setImageResource(R.drawable.heigh_level_gray);
                                } else {
                                    ivLevel.setImageResource(R.drawable.best_level_gray);
                                }
                            }
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

    private AsyncHttpResponseHandler getHistoricalDataHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("todayOrderNumber") && !jdata.isNull("todayOrderNumber")) {
                            todayOrderNumber = jdata.getInt("todayOrderNumber");
                            tvWaitNum.setText(todayOrderNumber + "");
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
            pDialog.closeDialog();
            ToastUtil.showToastBottomShort(getActivity(), "请求失败statusCode = " + statusCode);
        }
    };

    private AsyncHttpResponseHandler getMonthIncome = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            JSONObject jobj = null;
            try {
                jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("totalIncome")
                                && !jdata.isNull("totalIncome")) {
                            sumincome = jdata.getDouble("totalIncome");

                        }
                        if (jdata.has("monthDayInfo")
                                && !jdata.isNull("monthDayInfo")) {
                            daytip = jdata.getString("monthDayInfo");
                            tvEndTime.setText(daytip);
                        }
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastBottomShort(getActivity(), msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private AsyncHttpResponseHandler getMainInfoHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("workerRule") && !jdata.isNull("workerRule")) {
                            workerRule = jdata.getString("workerRule");
                        }
                        if (jdata.has("workerInfo") && !jdata.isNull("workerInfo")) {
                            JSONObject jWorkerInfo = jdata.getJSONObject("workerInfo");
                            if (jWorkerInfo.has("integral") && !jWorkerInfo.isNull("integral")) {
                                integral = jWorkerInfo.getDouble("integral");
                                tvIntegral.setText(formatDouble(integral));
                            }
                        }
                        if (jdata.has("gratuityHomePage") && !jdata.isNull("gratuityHomePage")) {
                            JSONObject jgratuityHomePage = jdata.getJSONObject("gratuityHomePage");
                            if (jgratuityHomePage.has("flushTime") && !jgratuityHomePage.isNull("flushTime")) {
                                spUtil.saveString("lastFlushTime", jgratuityHomePage.getString("flushTime"));
                            }
                            if (jgratuityHomePage.has("workerGratuityconfig") && !jgratuityHomePage.isNull("workerGratuityconfig")) {
                                showRedPop();
                            }
                        }

                    }
                } else {
                    ToastUtil.showToastBottomShort(getActivity(), msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    private void showRedPop() {
        View popView = View.inflate(getActivity(), R.layout.pop_redpop_layout, null);
        View parentView = View.inflate(getActivity(), R.layout.fragment_new_main, null);
        SolveClickTouchConflictLayout sl_popred = popView.findViewById(R.id.sl_popred_all);
        RelativeLayout rv_poproot = popView.findViewById(R.id.rl_popred_root);
        final PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.pop_anim);
        popupWindow.showAtLocation(parentView, Gravity.TOP, 0, 30);
        popupWindow.setTouchable(true);
        rv_poproot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(), IncomeActivity.class);
                startActivity(intent);
            }
        });
        sl_popred.setmSetOnSlideListener(new SolveClickTouchConflictLayout.OnSlideListener() {
            @Override
            public void onRightToLeftSlide() {

            }

            @Override
            public void onLeftToRightSlide() {

            }

            @Override
            public void onUpToDownSlide() {

            }

            @Override
            public void onDownToUpSlide() {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CommUtil.loginAuto(getActivity(), spUtil.getString("wcellphone", ""),
                Global.getIMEI(getActivity()), Global.getCurrentVersion(getActivity()), spUtil.getInt("wuserid", 0),
                autoLoginHandler);
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("noCareCount") && !jData.isNull("noCareCount")) {
                            spUtil.saveInt("noCareCount", jData.getInt("noCareCount"));
                            tvWaitwrite.setText(spUtil.getInt("noCareCount", 0) + "");
                        }
                        if (jData.has("fansCount") && !jData.isNull("fansCount")) {
                            spUtil.saveInt("fansCount", jData.getInt("fansCount"));
                            tvFans.setText(spUtil.getInt("fansCount", 0) + "");
                        }
                        if (jData.has("shopName") && !jData.isNull("shopName")) {
                            shopName = jData.getString("shopName");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private void setView() {
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()));
        tvTotalmoney.setText("*****");
        tvDay.setText(month + "-" + day);
        tvYear.setText("/" + year);
        if (isCurrentInTimeScope(0, 0, 8, 0)) {
            tvHello.setText("早上好 " + spUtil.getString("wusername", ""));
        }
        if (isCurrentInTimeScope(8, 0, 12, 0)) {
            tvHello.setText("上午好 " + spUtil.getString("wusername", ""));
        }
        if (isCurrentInTimeScope(12, 0, 14, 0)) {
            tvHello.setText("中午好 " + spUtil.getString("wusername", ""));
        }
        if (isCurrentInTimeScope(14, 0, 18, 0)) {
            tvHello.setText("下午好 " + spUtil.getString("wusername", ""));
        }
        if (isCurrentInTimeScope(18, 0, 24, 0)) {
            tvHello.setText("晚上好 " + spUtil.getString("wusername", ""));
        }
        Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        iv_mainfragment_slidtoorder.setAnimation(animation);

        Animation animation1 = new AlphaAnimation(0, 1); // Change alpha from fully visible to invisible
        animation1.setDuration(500); // duration - half a second
        animation1.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation1.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation1.setRepeatMode(Animation.REVERSE); //
        iv_mainfragment_slidtoorder1.setAnimation(animation1);
    }


    private void setListener() {
        iv_mainfragment_ncp.setOnClickListener(this);
        rlMywork.setOnClickListener(this);
        rlMymoney.setOnClickListener(this);
        ivSee.setOnClickListener(this);
        rvMyfans.setOnClickListener(this);
        rlMyintegral.setOnClickListener(this);
        btnIncome.setOnClickListener(this);
        ivNotic.setOnClickListener(this);
        rlTodayOrder.setOnClickListener(this);
        rlWaitWrite.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getData();
            refreshLayout.finishRefresh(2000);
        });
        rvSildToOrder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                        mPosX = motionEvent.getX();
                        mPosY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                        mCurPosX = motionEvent.getX();
                        mCurPosY = motionEvent.getY();
                        Log.i("TAG", mCurPosX + "");
                        break;
                    case MotionEvent.ACTION_UP:
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        if (mCurPosX - mPosX > 0
                                && (Math.abs(mCurPosX - mPosX) > 25)) {
                            Log.i("TAG", "wang向右");
                        } else if (mCurPosX - mPosX < 0
                                && (Math.abs(mCurPosX - mPosX) > 25)) {
                            Log.i("TAG", "wang向左");
                        }
                        break;
                }
                return false;
            }
        });
        mGestureDetector = new GestureDetector(getActivity(), new MyGestureListener());
    }

    private void getMainActivity() {
        localBannerList.clear();
        CommUtil.getActivityPage(getActivity(), 0, handlerHomeActivity);
    }

    private AsyncHttpResponseHandler handlerHomeActivity = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            int isExist = 0;
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject jdata = object.getJSONObject("data");
                        if (jdata.has("isExist") && !jdata.isNull("isExist")) {
                            isExist = jdata.getInt("isExist");
                        }
                        if (jdata.has("list") && !jdata.isNull("list")) {
                            JSONArray jarrlist = jdata.getJSONArray("list");
                            if (jarrlist.length() > 0) {
                                for (int i = 0; i < jarrlist.length(); i++) {
                                    localBannerList.add(ActivityPage.json2Entity(jarrlist
                                            .getJSONObject(i)));
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (localBannerList.size() > 0 && isExist == 0) {//有弹框
                for (int i = 0; i < localBannerList.size(); i++) {
                    ActivityPage activityPage = localBannerList.get(i);
                    if (activityPage != null) {
                        int id = activityPage.getId();
                        if (activityPage.getCountType() == 0) {//显示一次
                            String tag_mainfrag_one = Utils.readFileData(getActivity(), fileName_one);
                            Log.e("TAG", "tag_mainfrag_one = " + tag_mainfrag_one);
                            if (Utils.isStrNull(tag_mainfrag_one)) {
                                String[] split = tag_mainfrag_one.split(",");
                                if (split != null && split.length > 0) {
                                    boolean isXianShied = false;
                                    for (int j = 0; j < split.length; j++) {
                                        if (Integer.parseInt(split[j]) == id) {
                                            isXianShied = true;
                                            break;
                                        }
                                    }
                                    if (isXianShied) {
                                        activityPage.setDelete(true);
                                    } else {
                                        Utils.writeFileData(getActivity(), fileName_one, id + ",");
                                    }
                                } else {
                                    Utils.writeFileData(getActivity(), fileName_one, id + ",");
                                }
                            } else {
                                Utils.writeFileData(getActivity(), fileName_one, id + ",");
                            }
                        } else if (activityPage.getCountType() == 1) {//每次都显示

                        } else if (activityPage.getCountType() == 2) {//每日一次
                            Calendar c = Calendar.getInstance();
                            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                            int mDay = c.get(Calendar.DAY_OF_MONTH);
                            String tag_mainfrag_one_day = Utils.readFileData(getActivity(), fileName_one_day);
                            Log.e("TAG", "tag_mainfrag_one_day = " + tag_mainfrag_one_day);
                            if (Utils.isStrNull(tag_mainfrag_one_day)) {
                                String[] split = tag_mainfrag_one_day.split(",");
                                if (split != null && split.length > 0) {
                                    boolean isXianShied = false;
                                    for (int j = 0; j < split.length; j++) {
                                        String[] split1 = split[j].split("_");
                                        if (split1 != null && split1.length == 2) {
                                            if (Integer.parseInt(split1[0]) == id && Integer.parseInt(split1[1]) == mDay) {
                                                isXianShied = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (isXianShied) {
                                        activityPage.setDelete(true);
                                    } else {
                                        Utils.writeFileData(getActivity(), fileName_one_day, id + "_" + mDay + ",");
                                    }
                                } else {
                                    Utils.writeFileData(getActivity(), fileName_one_day, id + "_" + mDay + ",");
                                }
                            } else {
                                Utils.writeFileData(getActivity(), fileName_one_day, id + "_" + mDay + ",");
                            }
                        }
                    }
                }
                Iterator<ActivityPage> it = localBannerList.iterator();
                while (it.hasNext()) {
                    ActivityPage activityPage = it.next();
                    if (activityPage != null && activityPage.isDelete()) {
                        it.remove();
                    }
                }
            }
            if (localBannerList.size() > 0 && isExist == 0) {//有弹框
                Utils.ActivityPage(getActivity(), localBannerList);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                Intent leftIntent = new Intent(getActivity(), OrderActivity.class);
                startActivity(leftIntent);
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                /*Toast.makeText(getActivity(), "向右手势", Toast.LENGTH_SHORT).show();
                Intent rightIntent=new Intent(getActivity(),IncomeActivity.class);
                startActivity(rightIntent);*/
            }
            return false;
        }
    }

    /**
     * 接收MainActivity的Touch回调的对象，重写其中的onTouchEvent函数
     */
    MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //处理手势事件（根据个人需要去返回和逻辑的处理）
            return mGestureDetector.onTouchEvent(event);
        }
    };

    public static String formatDouble(double d) {
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        double num = bg.doubleValue();
        if (Math.round(num) - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

    /**
     * 判断当前系统时间是否在指定时间的范围内
     * <p>
     * beginHour 开始小时,例如22
     * beginMin  开始小时的分钟数,例如30
     * endHour   结束小时,例如 8
     * endMin    结束小时的分钟数,例如0
     * true表示在范围内, 否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();
        Time now = new Time();
        now.set(currentTimeMillis);
        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;
        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;
        // 跨天的特殊情况(比如22:00-8:00)
        if (!startTime.before(endTime)) {
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            //普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mainfragment_ncp:
                try {
                    Date d = new Date();
                    Log.e("TAG", "d：" + d);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateNowStr = sdf.format(d);
                    Log.e("TAG", "格式化后的日期：" + dateNowStr);
                    if (!ncp_url.startsWith("http://") && !ncp_url.startsWith("https://")) {
                        ncp_url = CommUtil.getStaticUrl() + ncp_url;
                    }
                    if (ncp_url.contains("?")) {
                        ncp_url = ncp_url + "&workname="
                                + URLEncoder.encode(spUtil.getString("wusername", ""), "UTF-8") + "&shopname="
                                + URLEncoder.encode(shopName, "UTF-8") + "&days="
                                + dateNowStr;
                    } else {
                        ncp_url = ncp_url + "?workname="
                                + URLEncoder.encode(spUtil.getString("wusername", ""), "UTF-8") + "&shopname="
                                + URLEncoder.encode(shopName, "UTF-8") + "&days="
                                + dateNowStr;
                    }
                    Log.e("TAG", "workname = " + spUtil.getString("wusername", ""));
                    Log.e("TAG", "编码workname = " + URLEncoder.encode(spUtil.getString("wusername", ""), "UTF-8"));
                    Log.e("TAG", "shopname = " + shopName);
                    Log.e("TAG", "编码shopname = " + URLEncoder.encode(shopName, "UTF-8"));
                    Log.e("TAG", "days = " + dateNowStr);
                    Log.e("TAG", "ncp_url = " + ncp_url);
                    startActivity(new Intent(getActivity(), AgreementActivity.class).putExtra("url", ncp_url));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "e = " + e);
                    if (!ncp_url.startsWith("http://") && !ncp_url.startsWith("https://")) {
                        ncp_url = CommUtil.getStaticUrl() + ncp_url;
                    }
                    Log.e("TAG", "ncp_url = " + ncp_url);
                    startActivity(new Intent(getActivity(), AgreementActivity.class).putExtra("url", ncp_url));
                }
                break;
            case R.id.rl_mainfragment_mywork:
                llWorkInfo.setVisibility(View.VISIBLE);
                rlMoneyInfo.setVisibility(View.GONE);
                tvMywork.setTextColor(getResources().getColor(R.color.a002241));
                tvMywork.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                lineWork.setVisibility(View.VISIBLE);
                lineMoney.setVisibility(View.GONE);
                tvMymoney.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                tvMymoney.setTextColor(getResources().getColor(R.color.a717985));
                break;
            case R.id.rl_mainfragment_mymoney:
                llWorkInfo.setVisibility(View.GONE);
                rlMoneyInfo.setVisibility(View.VISIBLE);
                tvMywork.setTextColor(getResources().getColor(R.color.a717985));
                tvMywork.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                lineWork.setVisibility(View.GONE);
                lineMoney.setVisibility(View.VISIBLE);
                tvMymoney.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvMymoney.setTextColor(getResources().getColor(R.color.a002241));
                break;
            case R.id.iv_mainfragment_see:
                if (canSee) {
                    ivSee.setImageResource(R.drawable.icon_notsee);
                    canSee = false;
                    tvTotalmoney.setText("*****");
                } else {
                    ivSee.setImageResource(R.drawable.icon_see);
                    canSee = true;
                    tvTotalmoney.setText("¥" + sumincome);
                }
                break;
            case R.id.rl_mainfragment_myfans:
                Intent intent = new Intent(getActivity(), MyFansNewActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.rl_mainfragment_integral:
                Intent intent2 = new Intent(getActivity(), MyIntegralActivity.class);
                intent2.putExtra("integral", formatDouble(integral));
                intent2.putExtra("workerRule", workerRule);
                getActivity().startActivity(intent2);
                break;
            case R.id.btn_mainfragment_income:
                Intent intent3 = new Intent(getActivity(), IncomeActivity.class);
                getActivity().startActivity(intent3);
                break;
            case R.id.iv_mainfragment_notic:
                Intent intent4 = new Intent(getActivity(), NoticeActivity.class);
                getActivity().startActivity(intent4);
                break;
            case R.id.rl_mainfragment_todayorder:
                Intent intent5 = new Intent(getActivity(), OrderActivity.class);
                intent5.putExtra("index", 0);
                getActivity().startActivity(intent5);
                break;
            case R.id.rl_mainfragment_waitwrite:
                Intent intent6 = new Intent(getActivity(), OrderActivity.class);
                intent6.putExtra("index", 2);
                getActivity().startActivity(intent6);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /** 触摸事件的注销 */
        ((MainActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
    }
}
