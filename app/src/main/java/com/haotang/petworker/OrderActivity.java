package com.haotang.petworker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.petworker.adapter.OrderViewPagerAdapter;
import com.haotang.petworker.fragment.OrderNewFragment;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.CountdownUtil;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单列表
 */
public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_order_jrddnum)
    TextView tvOrderJrddnum;
    @BindView(R.id.tv_order_wksnum)
    TextView tvOrderWksnum;
    @BindView(R.id.stl_order)
    SlidingTabLayout stlOrder;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private int currentTabIndex;
    private String[] titles = {"今日订单", "未开始", "已完成", "已取消"};
    private MProgressDialog pDialog;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private boolean isFirst = true;
    private double lat;
    private double lng;
    private int todayOrderNumber;
    private int notStartNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        initData();
        findView();
        setView();
        initListener();
        getData();
        initBD();
    }

    private void initBD() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption clientOption = new LocationClientOption();
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(clientOption);
        isFirst = true;
        pDialog.showDialog("定位中...");
        mLocationClient.start();
        CountdownUtil.getInstance().newTimer(5000, 1000, new CountdownUtil.ICountDown() {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                    mLocationClient.stop();
                    pDialog.closeDialog();
                    setFragment();
            }
        }, "LOCATION_TIMER");
    }

    public class MLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            pDialog.closeDialog();
            lat = location.getLatitude();
            lng = location.getLongitude();
            if (lat > 0 && lng > 0) {
                mLocationClient.stop();
            }
            if (isFirst) {
                isFirst = false;
                setFragment();
            }
            CountdownUtil.getInstance().cancel("LOCATION_TIMER");
        }
    }

    private void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(android.R.color.transparent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "1");
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("TAG", "2");
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    private void initData() {
        currentTabIndex = getIntent().getIntExtra("index",0);
        pDialog = new MProgressDialog(this);
        pDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
    }

    private void findView() {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("服务订单");
        stlOrder.setmTextSelectsize(stlOrder.sp2px(18));
    }

    private void setFragment() {
        for (int i = 0; i < titles.length; i++) {
            OrderNewFragment orderNewFragment = new OrderNewFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            bundle.putDouble("lat", lat);
            bundle.putDouble("lng", lng);
            orderNewFragment.setArguments(bundle);
            mFragments.add(orderNewFragment);
        }
        vpOrder.setAdapter(new OrderViewPagerAdapter(getSupportFragmentManager(), mFragments, titles));
        stlOrder.setViewPager(vpOrder);
        stlOrder.setCurrentTab(currentTabIndex);
        vpOrder.setCurrentItem(currentTabIndex);
    }

    private void initListener() {
        stlOrder.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.e("TAG", "position = " + position);
                currentTabIndex = position;
                vpOrder.setCurrentItem(currentTabIndex);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        vpOrder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                vpOrder.setCurrentItem(currentTabIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void getData() {
        pDialog.showDialog();
        todayOrderNumber = 0;
        notStartNumber = 0;
        CommUtil.getHistoricalData(this, getHistoricalDataHandler);
    }

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
                        }
                        if (jdata.has("notStartNumber") && !jdata.isNull("notStartNumber")) {
                            notStartNumber = jdata.getInt("notStartNumber");
                        }
                    }
                } else {
                    if(100003 == resultCode){
                        startActivity(new Intent(OrderActivity.this,LoginNewActivity.class));
                    }
                    ToastUtil.showToastBottomShort(OrderActivity.this, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastBottomShort(OrderActivity.this, "数据异常");
            }
            Utils.setText(tvOrderJrddnum, todayOrderNumber + "", "0", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvOrderWksnum, notStartNumber + "", "0", View.VISIBLE, View.VISIBLE);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastBottomShort(OrderActivity.this, "请求失败statusCode = " + statusCode);
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        CountdownUtil.getInstance().cancel("LOCATION_TIMER");
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
    }
}
