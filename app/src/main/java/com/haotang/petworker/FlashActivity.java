package com.haotang.petworker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;

import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GetDeviceId;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.SystemBarTint;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class FlashActivity extends SuperActivity {
    private TimerTask task;
    private Timer timer;
    private SharedPreferenceUtil spUtil;
    private boolean guide;
    private long starttime;
    private long endtime;
    private int agreement;
    private SystemBarTint mtintManager;
    public final static int REQUEST_READ_PHONE_STATE = 1;
    private int userid;
    private ImageView iv_flash_icon;
    private String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String img_url;
    private String jump_url;
    private String backup;
    private int point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mtintManager = new SystemBarTint(this);
        setStatusBarColor(Color.parseColor("#ff0099cc"));
        Utils.hideBottomUIMenu(this);
        setContentView(R.layout.flash);
        iv_flash_icon = (ImageView) findViewById(R.id.iv_flash_icon);
        spUtil = SharedPreferenceUtil.getInstance(this);
        guide = spUtil.getBoolean("guide", false);
        spUtil.saveBoolean("MAINFRAG_DIALOG", false);
        spUtil.saveBoolean("MAINFRAG_GOLD_ANIM", false);
        userid = spUtil.getInt("wuserid", 0);
        String deviceId = GetDeviceId.readDeviceID(mContext);
        if (!Utils.isStrNull(deviceId)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermisson();
            } else {
                GetDeviceId.saveUniqueId(mActivity);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (guide && userid > 0) {
                    getUserInfo(userid);
                } else if (guide && userid <= 0) {
                    goGuide(true);
                } else {
                    goGuide(false);
                }
            }
        }, 2500);
    }

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
            GetDeviceId.saveUniqueId(mActivity);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnim();
                }
            }, 500);
        }
    }

    private void startAnim() {
        final AnimationDrawable animationDrawable = (AnimationDrawable) iv_flash_icon.getBackground();
        animationDrawable.start();
    }

    protected void setStatusBarColor(int colorBurn) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = setImmerseLayout();
            window.setStatusBarColor(colorBurn);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mtintManager != null) {
                mtintManager.setStatusBarTintEnabled(true);
                mtintManager.setStatusBarTintColor(colorBurn);
            }
        }
    }

    private void goGuide(final boolean flag) {
        task = new TimerTask() {

            @Override
            public void run() {
                if (flag) {
                    goNext(LoginNewActivity.class);
                } else {
                    goNext(GuideActivity.class);
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, Global.FLASH_DELAYEDTIME);
    }

    private void goNext(Class clazz) {
        Intent intent = new Intent(this, clazz);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        startActivity(intent);
        finish();
    }

    private void getUserInfo(int userid) {
        starttime = System.currentTimeMillis();
        CommUtil.loginAuto(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), userid,
                autoLoginHandler);
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("自动登录：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("cellPhone")
                                && !jData.isNull("cellPhone")) {
                            spUtil.saveString("wcellphone",
                                    jData.getString("cellPhone"));
                        }
                        if (jData.has("workEnd") && !jData.isNull("workEnd")) {
                            spUtil.saveString("wworkerendtime",
                                    jData.getString("workEnd"));
                        }
                        if (jData.has("workStart")
                                && !jData.isNull("workStart")) {
                            spUtil.saveString("wworkerstarttime",
                                    jData.getString("workStart"));
                        }
                        if (jData.has("hbInter") && !jData.isNull("hbInter")) {
                            spUtil.saveInt("wintervaltime",
                                    jData.getInt("hbInter"));
                        }
                        if (jData.has("worker") && !jData.isNull("worker")) {
                            JSONObject JWorker = jData.getJSONObject("worker");
                            if (JWorker.has("avatar")
                                    && !JWorker.isNull("avatar")) {
                                spUtil.saveString("wimage",
                                        JWorker.getString("avatar"));
                            }
                            if (JWorker.has("realName")
                                    && !JWorker.isNull("realName")) {
                                spUtil.saveString("wusername",
                                        JWorker.getString("realName"));
                            }
                            if (JWorker.has("title")
                                    && !JWorker.isNull("title")) {
                                spUtil.saveString("titlelevel",
                                        JWorker.getString("title"));
                            }
                            if (JWorker.has("workerGrade")
                                    && !JWorker.isNull("workerGrade")) {
                                spUtil.saveInt("wgrade",
                                        JWorker.getInt("workerGrade"));
                            }
                            if (JWorker.has("totalOrderAmount")
                                    && !JWorker.isNull("totalOrderAmount")) {
                                spUtil.saveInt("wordernum",
                                        JWorker.getInt("totalOrderAmount"));
                            }
                            if (JWorker.has("userId")
                                    && !JWorker.isNull("userId")) {
                                spUtil.saveInt("wuserid",
                                        JWorker.getInt("userId"));
                            }
                            if (JWorker.has("viemode")
                                    && !JWorker.isNull("viemode")) {
                                spUtil.saveInt("viemode",
                                        JWorker.getInt("viemode"));
                            }
                        }

                        if (jData.has("agreementStatus")
                                && !jData.isNull("agreementStatus")) {
                            agreement = jData.getInt("agreementStatus");
                        }
                        if (jData.has("noCareCount") && !jData.isNull("noCareCount")) {
                            spUtil.saveInt("noCareCount", jData.getInt("noCareCount"));
                        }
                        if (jData.has("fansCount") && !jData.isNull("fansCount")) {
                            spUtil.saveInt("fansCount", jData.getInt("fansCount"));
                        }
                    }
                    endtime = System.currentTimeMillis();
                    if (endtime - starttime < Global.FLASH_DELAYEDTIME) {
                        Thread.sleep(Global.FLASH_DELAYEDTIME + starttime
                                - endtime);
                    }
                    if (spUtil.getInt("wuserid", 0) > 0) {
                        if (agreement == 0) {
                            goNext(AgreementLaunchActivity.class);
                        } else {
                            startPage();
                        }
                    } else {
                        goNext(LoginNewActivity.class);
                    }
                } else {
                    endtime = System.currentTimeMillis();
                    if (endtime - starttime < Global.FLASH_DELAYEDTIME) {
                        Thread.sleep(Global.FLASH_DELAYEDTIME + starttime
                                - endtime);
                    }
                    goNext(LoginNewActivity.class);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // ToastUtil.showToastCenterShort(FlashActivity.this,
                // "网络异常，请重新登录");
                endtime = System.currentTimeMillis();
                if (endtime - starttime < Global.FLASH_DELAYEDTIME) {
                    try {
                        Thread.sleep(Global.FLASH_DELAYEDTIME + starttime
                                - endtime);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                goNext(LoginNewActivity.class);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            // ToastUtil.showToastCenterShort(FlashActivity.this, "网络异常，请重登录");
            endtime = System.currentTimeMillis();
            if (endtime - starttime < Global.FLASH_DELAYEDTIME) {
                try {
                    Thread.sleep(Global.FLASH_DELAYEDTIME + starttime - endtime);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            goNext(LoginNewActivity.class);
        }
    };

    private void startPage() {
        CommUtil.startPage(mContext, startPageHandler);
    }

    private AsyncHttpResponseHandler startPageHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            Utils.mLogError("启动页：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("backup") && !jData.isNull("backup")) {
                            backup = jData.getString("backup");
                        }
                        if (jData.has("point") && !jData.isNull("point")) {
                            point = jData.getInt("point");
                        }
                        if (jData.has("imgUrl") && !jData.isNull("imgUrl")) {
                            img_url = jData.getString("imgUrl");
                        }
                        if (jData.has("jumpUrl") && !jData.isNull("jumpUrl")) {
                            jump_url = jData.getString("jumpUrl");
                        }
                        if (img_url != null
                                && !TextUtils.isEmpty(img_url)) {
                            Intent intent = new Intent(mContext, StartPageActivity.class);
                            intent.putExtra("img_url", img_url);
                            intent.putExtra("jump_url", jump_url);
                            intent.putExtra("backup", backup);
                            intent.putExtra("point", point);
                            startActivity(intent);
                            finish();
                        } else {
                            goNext(MainActivity.class);
                        }
                    } else {
                        goNext(MainActivity.class);
                    }
                } else {
                    goNext(MainActivity.class);
                }
            } catch (Exception e) {
                goNext(MainActivity.class);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            goNext(MainActivity.class);
        }
    };

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
                    GetDeviceId.saveUniqueId(mActivity);
                } else {
                    ToastUtil.showToastBottomShort(this, "请打开所需权限");
                }
                break;
            default:
                break;
        }
    }

    public static String getIMEI(Context mContext) {
        return GetDeviceId.readDeviceID(mContext);
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
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
    }
}
