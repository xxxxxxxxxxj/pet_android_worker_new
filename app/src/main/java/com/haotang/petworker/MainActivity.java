package com.haotang.petworker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.petworker.entity.MainBottomTab;
import com.haotang.petworker.fragment.MTabContent;
import com.haotang.petworker.fragment.MainNewFragment;
import com.haotang.petworker.fragment.MyNewFragment;
import com.haotang.petworker.fragment.RankingListFragment;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.updateapputil.Callback;
import com.haotang.petworker.updateapputil.ConfirmDialog;
import com.haotang.petworker.updateapputil.DownloadAppUtils;
import com.haotang.petworker.updateapputil.DownloadProgressDialog;
import com.haotang.petworker.updateapputil.UpdateAppEvent;
import com.haotang.petworker.updateapputil.UpdateUtil;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GetDeviceId;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity {
    public static MainActivity act;
    private LayoutInflater mInflater;
    private TabHost mTabHost;
    private FragmentTransaction ft;
    private FragmentManager fm;
    private MainNewFragment mainFragment;
    private RankingListFragment rankFragment;
    private MyNewFragment myFragment;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private SharedPreferenceUtil spUtil;
    private Calendar calendar;
    private String startworkingtime;
    private String endworkingtime;
    private double lat;
    private double lng;
    private boolean isFirst = true;
    private boolean isExit = false;
    private MainReceiver mReceiver;
    private int millseconds;
    private List<MainBottomTab> mainBottomTabList = new ArrayList<MainBottomTab>();
    private ImageView ivTabMain;
    private ImageView ivTabRank;
    private ImageView ivTabMy;
    private DownloadProgressDialog progressDialog;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Global.EXITE_SYSTEM:
                    isExit = false;
                    break;
            }
            super.handleMessage(msg);
        }
    };
    static final String[] LOCATIONGPS = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    public final static int REQUEST_READ_PHONE_STATE = 1;
    private String latestVersion;
    private String downloadPath;
    private String versionHint;
    private int isUpgrade;
    public final static int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private boolean isShow;
    private Activity mContext;
    private String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initData();
        initView();
        setView();
        initListener();
        initBD(millseconds);
        getLatestVersion();
        getUnderIcons();
        initReceiver();
        initWindows();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {// 没有权限，申请权限。
                ActivityCompat.requestPermissions((Activity) MainActivity.this, LOCATIONGPS, BAIDU_READ_PHONE_STATE);
            }
        }
        String deviceId = GetDeviceId.readDeviceID(mContext);
        if (!Utils.isStrNull(deviceId)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermisson();
            } else {
                GetDeviceId.saveUniqueId(mContext);
            }
        }
        CommUtil.loginAuto(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), spUtil.getInt("wuserid", 0),
                autoLoginHandler);
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (resultCode == Global.CODE_EXIT) {
                    Intent intent = new Intent(MainActivity.this, LoginNewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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

    private void checkPermisson() {
        boolean flag = true;//默认全部被申请过
        for (int i = 0; i < permissions.length; i++) {//只要有一个没有申请成功
            if (!(ActivityCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_GRANTED)) {
                flag = false;
            }
        }
        if (!flag) {
            //动态申请权限
            ActivityCompat.requestPermissions(this, permissions, REQUEST_READ_PHONE_STATE);
        } else {
            GetDeviceId.saveUniqueId(mContext);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    GetDeviceId.saveUniqueId(mContext);
                } else {
                    ToastUtil.showToastBottomShort(this, "请打开所需权限");
                }
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    UpdateUtil.updateApk(mContext,
                            downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                } else {
                    ToastUtil.showToastCenterLong(this, "请打开存储权限");
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HardwareIds")
    public void getDeviceId() {
        TelephonyManager tm = (TelephonyManager) getApplication().getSystemService(getApplication().TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        spUtil.saveString("IMEI", tm.getDeviceId());
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

    private void getUnderIcons() {
        CommUtil.underIcons(this, underIconsHanler);
    }

    private void initData() {
        mContext = this;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        spUtil = SharedPreferenceUtil.getInstance(this);
        calendar = Calendar.getInstance();
        startworkingtime = spUtil.getString("wworkerstarttime", "6:00:00");
        endworkingtime = spUtil.getString("wworkerendtime", "24:00:00");
        millseconds = spUtil.getInt("wintervaltime", 5) * 60 * 1000;
    }

    private void initView() {
        setContentView(R.layout.main);
        mInflater = LayoutInflater.from(this);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
    }

    private void setView() {
        mTabHost.setup();
        TabHost.OnTabChangeListener tabChangeListener = new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.e("TAG", "tabId = " + tabId);
                getFragment();
                if (tabId.equals("main")) {
                    setBottomTab(0);
                    addFragment(mainFragment,
                            new MainNewFragment(), "main");
                } else if (tabId.equals("rank")) {
                    setBottomTab(1);
                    addFragment(rankFragment, new RankingListFragment(), "rank");
                } else if (tabId.equals("my")) {
                    setBottomTab(2);
                    addFragment(myFragment, new MyNewFragment(), "my");
                }
                ft.commitAllowingStateLoss();
            }
        };
        mTabHost.setOnTabChangedListener(tabChangeListener);
        addTab("main", R.drawable.tab_main, "首页");
        addTab("rank", R.drawable.tab_rank, "排行榜");
        addTab("my", R.drawable.tab_my, "我的");
        goToMain();
    }

    private void initListener() {
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

    private AsyncHttpResponseHandler underIconsHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("首页底部tab：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj != null) {
                    if (jobj.has("code") && !jobj.isNull("code")) {
                        int resultCode = jobj.getInt("code");
                        if (0 == resultCode) {
                            if (jobj.has("data") && !jobj.isNull("data")) {
                                JSONObject jData = jobj.getJSONObject("data");
                                if (jData.has("iconList")
                                        && !jData.isNull("iconList")) {
                                    mainBottomTabList.clear();
                                    JSONArray jlist = jData.getJSONArray("iconList");
                                    if (jlist.length() > 0) {
                                        for (int i = 0; i < jlist.length(); i++) {
                                            JSONObject jitem = jlist.getJSONObject(i);
                                            String title = "";
                                            String pic = "";
                                            String picRed = "";
                                            String integralUrl = "";
                                            String integralIntroduce = "";
                                            if (jitem.has("title")
                                                    && !jitem.isNull("title")) {
                                                title = jitem.getString("title");
                                            }
                                            if (jitem.has("pic")
                                                    && !jitem.isNull("pic")) {
                                                pic = jitem.getString("pic");
                                            }
                                            if (jitem.has("picRed")
                                                    && !jitem.isNull("picRed")) {
                                                picRed = jitem.getString("picRed");
                                            }
                                            if (jitem.has("integralUrl")
                                                    && !jitem.isNull("integralUrl")) {
                                                integralUrl = jitem.getString("integralUrl");
                                            }
                                            if (jitem.has("integralIntroduce")
                                                    && !jitem.isNull("integralIntroduce")) {
                                                integralIntroduce = jitem.getString("integralIntroduce");
                                            }
                                            mainBottomTabList.add(new MainBottomTab(title, pic, picRed, integralUrl, integralIntroduce));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (getIntent().getIntExtra("point", 0) == 32) {
                goToRank();
            } else if (getIntent().getIntExtra("point", 0) == 33) {
                goToMy();
            } else {
                goToMain();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(UpdateAppEvent event) {
        if (event != null) {
            if (event.getState() == UpdateAppEvent.DOWNLOADING) {
                long soFarBytes = event.getSoFarBytes();
                long totalBytes = event.getTotalBytes();
                if (event.getIsUpgrade() == 0 && isShow) {

                } else {
                    Log.e("TAG", "下载中...soFarBytes = " + soFarBytes + "---totalBytes = " + totalBytes);
                    if (progressDialog != null && progressDialog.isShowing()) {

                    } else {
                        progressDialog = new DownloadProgressDialog(this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("下载提示");
                        progressDialog.setMessage("当前下载进度:");
                        progressDialog.setIndeterminate(false);
                        if (event.getIsUpgrade() == 1) {
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        } else {
                            progressDialog.setCancelable(true);
                            progressDialog.setCanceledOnTouchOutside(true);
                        }
                        progressDialog.show();
                    }
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.e("TAG", "onDismiss");
                            isShow = true;
                        }
                    });
                }
                if (progressDialog != null) {
                    progressDialog.setMax((int) totalBytes);
                    progressDialog.setProgress((int) soFarBytes);
                }
                isShow = true;
            } else if (event.getState() == UpdateAppEvent.DOWNLOAD_COMPLETE) {
                UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(true)
                            .setCancleBtnVisible(View.VISIBLE).setDialogCanceledOnTouchOutside(true).show();
                }
            } else if (event.getState() == UpdateAppEvent.DOWNLOAD_FAIL) {
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.retry();
                        }
                    }, true).setContent("下载失败\n确认是否重试？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.retry();
                        }
                    }, true).setContent("下载失败\n确认是否重试？").setDialogCancelable(true)
                            .setCancleBtnVisible(View.VISIBLE).setDialogCanceledOnTouchOutside(true).show();
                }
            }
        }
    }

    /**
     * 获取最新版本和是否强制升级
     */
    private void getLatestVersion() {
        latestVersion = "";
        downloadPath = "";
        versionHint = "";
        isUpgrade = 0;
        isShow = false;
        CommUtil.getLatestVer(MainActivity.this, latestHanler);
    }

    private AsyncHttpResponseHandler latestHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("最新版本：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj != null) {
                    if (jobj.has("code") && !jobj.isNull("code")) {
                        int resultCode = jobj.getInt("code");
                        if (0 == resultCode) {
                            if (jobj.has("data") && !jobj.isNull("data")) {
                                JSONObject jData = jobj.getJSONObject("data");
                                if (jData.has("nversion")
                                        && !jData.isNull("nversion")) {
                                    latestVersion = jData.getString("nversion");
                                }
                                if (jData.has("download")
                                        && !jData.isNull("download")) {
                                    downloadPath = jData.getString("download");
                                }
                                if (jData.has("text") && !jData.isNull("text")) {
                                    versionHint = jData.getString("text");
                                }
                                if (jData.has("mandate")
                                        && !jData.isNull("mandate")) {
                                    isUpgrade = jData.getInt("mandate");
                                }
                                boolean isLatest = UpdateUtil
                                        .compareVersion(
                                                latestVersion,
                                                Global.getCurrentVersion(MainActivity.this));
                                if (isLatest) {// 需要下载安装最新版
                                    if (isUpgrade == 1) {
                                        // 强制升级
                                        UpdateUtil.showForceUpgradeDialog(mContext, versionHint,
                                                downloadPath, latestVersion, new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                UpdateUtil.updateApk(mContext,
                                                                        downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                            }
                                                        } else {
                                                            UpdateUtil.updateApk(mContext,
                                                                    downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                        }
                                                    }
                                                });
                                    } else if (isUpgrade == 0) {
                                        // 非强制升级
                                        UpdateUtil.showUpgradeDialog(mContext, versionHint,
                                                downloadPath, latestVersion, new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                UpdateUtil.updateApk(mContext,
                                                                        downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                            }
                                                        } else {
                                                            UpdateUtil.updateApk(mContext,
                                                                    downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    public void goToRank() {
        mTabHost.setCurrentTabByTag("rank");
    }

    public void goToMain() {
        mTabHost.setCurrentTabByTag("main");
    }

    public void goToMy() {
        mTabHost.setCurrentTabByTag("my");
    }

    private void initReceiver() {
        mReceiver = new MainReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MainActivity");
        registerReceiver(mReceiver, filter);
    }

    private void getFragment() {
        fm = getSupportFragmentManager();
        mainFragment = (MainNewFragment) fm
                .findFragmentByTag("main");
        rankFragment = (RankingListFragment) fm.findFragmentByTag("rank");
        myFragment = (MyNewFragment) fm.findFragmentByTag("my");
        ft = fm.beginTransaction();
        detachFragment(mainFragment);
        detachFragment(rankFragment);
        detachFragment(myFragment);
    }

    private void detachFragment(Fragment fragment) {
        if (fragment != null)
            ft.detach(fragment);
    }

    private void addFragment(Fragment fragment, Fragment newfragment, String tag) {
        if (fragment == null) {
            ft.add(android.R.id.tabcontent, newfragment, tag);
        } else {
            ft.attach(fragment);
        }
    }

    private void addTab(String tag, int icon, String name) {
        TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(getTabView(tag, icon, name));
        tabSpec.setContent(new MTabContent(getBaseContext()));
        mTabHost.addTab(tabSpec);
    }

    private View getTabView(String tag, int icon, String name) {
        View view = mInflater.inflate(R.layout.tab_item, null);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.tab_icon);
        TextView tvName = (TextView) view.findViewById(R.id.tab_name);
        if ("main".equals(tag)) {
            ivTabMain = ivIcon;
            ivTabMain.setTag(tag);
        } else if ("rank".equals(tag)) {
            ivTabRank = ivIcon;
            ivTabRank.setTag(tag);
        } else if ("my".equals(tag)) {
            ivTabMy = ivIcon;
            ivTabMy.setTag(tag);
        }
        ivIcon.setImageResource(icon);
        tvName.setText(name);
        return view;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.showToastBottomShort(this, "再按一次退出程序");
            handler.sendEmptyMessageDelayed(Global.EXITE_SYSTEM,
                    Global.EXITE_SYSTEM_DELAYEDTIME);
        } else {
            this.onDestroy();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    // 判断当前时间是否在工作时间内
    private boolean isWorkingTime() {
        String[] startTimes = startworkingtime.split(":");
        String[] endTimes = endworkingtime.split(":");
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (Integer.parseInt(startTimes[0]) <= calendar
                .get(Calendar.HOUR_OF_DAY)
                && calendar.get(Calendar.HOUR_OF_DAY) < Integer
                .parseInt(endTimes[0])
                || Integer.parseInt(startTimes[0]) == calendar
                .get(Calendar.HOUR_OF_DAY)
                && Integer.parseInt(startTimes[1]) < calendar
                .get(Calendar.MINUTE)
                || calendar.get(Calendar.HOUR_OF_DAY) == Integer
                .parseInt(endTimes[0])
                && calendar.get(Calendar.MINUTE) < Integer
                .parseInt(endTimes[1])) {
            // 当前时间在工作时间内
            return true;
        }
        return false;
    }

    // 百度定位
    private void initBD(int millseconds) {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption clientOption = new LocationClientOption();
        clientOption.setLocationMode(LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setScanSpan(millseconds);
        clientOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(clientOption);
        mLocationClient.start();
    }

    public class MLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            if (isFirst) {
                isFirst = false;
                registGeTuiToService();
            }
            if (isWorkingTime()) {
                sendLocation(lat, lng);
            }
        }
    }

    private void sendLocation(double lat, double lng) {
        CommUtil.sendLocation(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), lat, lng,
                sendLocationHanler);
    }

    // 自动升级
    private AsyncHttpResponseHandler sendLocationHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("上传坐标：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    // 向后台注册推送信息
    private void registGeTuiToService() {
        CommUtil.registGeTuitoService(this, spUtil.getString("wcellphone", ""),
                Global.getCurrentVersion(this), Global.getIMEI(this),
                spUtil.getInt("wuserid", 0), Global.getPushID(this), lat, lng,
                getuiHandler);
    }

    private AsyncHttpResponseHandler getuiHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("注册个推：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
        }
    };

    private class MainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int index = bundle.getInt("index");
            if (index == 0) {
                mTabHost.setCurrentTabByTag("main");
            } else if (index == 1) {
                mTabHost.setCurrentTabByTag("rank");
            } else if (index == 2) {
                mTabHost.setCurrentTabByTag("my");
            }
        }
    }

    private void setBottomTab(int index) {
        if (mainBottomTabList.size() > 0) {
            if (index == 0) {
                GlideUtil.loadImg(this, mainBottomTabList.get(0).getPicRed(), ivTabMain, 0);
                GlideUtil.loadImg(this, mainBottomTabList.get(1).getPic(), ivTabRank, 0);
                GlideUtil.loadImg(this, mainBottomTabList.get(2).getPic(), ivTabMy, 0);
            } else if (index == 1) {
                GlideUtil.loadImg(this, mainBottomTabList.get(0).getPic(), ivTabMain, 0);
                GlideUtil.loadImg(this, mainBottomTabList.get(1).getPicRed(), ivTabRank, 0);
                GlideUtil.loadImg(this, mainBottomTabList.get(2).getPic(), ivTabMy, 0);
            } else if (index == 2) {
                GlideUtil.loadImg(this, mainBottomTabList.get(0).getPic(), ivTabMain, 0);
                GlideUtil.loadImg(this, mainBottomTabList.get(1).getPic(), ivTabRank, 0);
                GlideUtil.loadImg(this, mainBottomTabList.get(2).getPicRed(), ivTabMy, 0);
            }
        }
    }

    /**
     * 保存MyTouchListener接口的列表
     */
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface MyTouchListener {
        /**
         * onTOuchEvent的实现
         */
        boolean onTouchEvent(MotionEvent event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 只要发生onSaveInstanceState就remove all Fragment
        if (outState != null) {
            outState.remove("android:support:fragments");
        }
    }
}
