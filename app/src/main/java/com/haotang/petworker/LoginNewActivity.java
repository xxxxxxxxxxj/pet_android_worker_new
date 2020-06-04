package com.haotang.petworker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.updateapputil.Callback;
import com.haotang.petworker.updateapputil.ConfirmDialog;
import com.haotang.petworker.updateapputil.DownloadAppUtils;
import com.haotang.petworker.updateapputil.DownloadProgressDialog;
import com.haotang.petworker.updateapputil.MyNotification;
import com.haotang.petworker.updateapputil.UpdateAppEvent;
import com.haotang.petworker.updateapputil.UpdateUtil;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GetDeviceId;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MD5;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.SelectableRoundedImageView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 姜谷蓄
 * 新版登录页
 */
public class LoginNewActivity extends SuperActivity {
    private static LoginNewActivity act;
    private SharedPreferenceUtil spUtil;
    private TextView tv_phone;
    private RelativeLayout ll_phone;
    private EditText et_phone;
    private Button btn_next;
    private Timer timer;
    private TimerTask task;
    private int seconds = 60;
    private MProgressDialog pDialog;
    private double lat;
    private double lng;
    private int agreement = -1;
    Set<String> tagSet = new LinkedHashSet<String>();
    private static final int MSG_SET_ALIASANDTAGS = 1001;
    private MyNotification mNotification;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private String mobileKey;
    private SelectableRoundedImageView sriv_login_imgver;
    private String pictureOn = "";
    public final static int REQUEST_READ_PHONE_STATE = 1;
    private DownloadProgressDialog progressDialog;
    private String latestVersion;
    private String downloadPath;
    private String versionHint;
    private int isUpgrade;
    public final static int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private boolean isShow;
    private ImageView ivClear;
    private String[] permissions = new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initBD();
        findView();
        setView();
        setLinster();
        getLatestVersion();
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        MApplication.listAppoint.add(act);
        pDialog = new MProgressDialog(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
        String deviceId = GetDeviceId.readDeviceID(mContext);
        if (!Utils.isStrNull(deviceId)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermisson();
            } else {
                GetDeviceId.saveUniqueId(mActivity);
            }
        }
        Utils.mLogError("hhhh");
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
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    UpdateUtil.updateApk(mActivity,
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

    private void getVerification(String phone) {
        pDialog.showDialog();
        String slat_md5 = MD5.md5(Global.MD5_STR, et_phone.getText().toString().trim().replace(" ", ""));
        Log.e("TAG", "slat_md5 = " + slat_md5);
        CommUtil.genVerifyCode(this, phone, "", mobileKey, slat_md5, verificationHandler);
    }

    private AsyncHttpResponseHandler verificationHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    String phone = et_phone.getText().toString().trim();
                    Intent intent = new Intent(LoginNewActivity.this, VerifyCodeActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                } else if (10 == resultCode) {
                    ToastUtil.showToastCenterShort(LoginNewActivity.this,
                            "您没有注册为美容师，请先与客服人员联系成为美容师");
                } else if (resultCode == 11) {
                    if (Utils.isStrNull(msg)) {
                        //tv_login_imgyzmerror.setText(msg);
                        ToastUtil.showToastBottomShort(LoginNewActivity.this, msg);
                    }
                    Utils.goneJP(LoginNewActivity.this);
                } else if (resultCode == 12) {
                    if (Utils.isStrNull(msg)) {
                        //tv_login_imgyzmerror.setText(msg);
                        ToastUtil.showToastBottomShort(LoginNewActivity.this, msg);
                    }
                    Utils.goneJP(LoginNewActivity.this);
                    //时间戳
                    long l = System.currentTimeMillis();
                    Log.e("TAG", "时间戳 = " + l);
                    //产生6位数随机数
                    int num = (int) ((Math.random() * 9 + 1) * 100000);
                    Log.e("TAG", "随机数 = " + num);
                    mobileKey = String.valueOf(l) + String.valueOf(num);
                    Log.e("TAG", "时间戳+随机数 = " + mobileKey);
                    pDialog.showDialog();
                } else if (resultCode == 13) {
                    if (Utils.isStrNull(msg)) {
                        //tv_login_imgyzmerror.setText(msg);
                        ToastUtil.showToastBottomShort(LoginNewActivity.this, msg);
                    }
                    Utils.goneJP(LoginNewActivity.this);
                } else {
                    // 获取验证码失败 获取服务器提示信息展示给用户
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastBottomShort(LoginNewActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil
                        .showToastCenterShort(LoginNewActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastCenterShort(LoginNewActivity.this, "请求失败");
            pDialog.closeDialog();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //getPictureOnWorker();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void setLinster() {
        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setAnim();
            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Utils.isStrNull(et_phone.getText().toString().trim().replace(" ", ""))){
                    ivClear.setVisibility(View.VISIBLE);
                }else {
                    ivClear.setVisibility(View.GONE);
                }
                if (!Utils.isStrNull(et_phone.getText().toString().trim().replace(" ", "")) || !Utils.checkPhone1(LoginNewActivity.this, et_phone)) {
                    btn_next.setClickable(false);
                    btn_next.setBackgroundResource(R.drawable.bg_nextbtn_gray);
                } else {
                    btn_next.setClickable(true);
                    btn_next.setBackgroundResource(R.drawable.bg_nextbtn_blue);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerification(et_phone.getText().toString().trim().replace(" ", ""));
            }
        });
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_phone.setText("");
            }
        });
    }

    private void setAnim(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tv_phone, "TranslationY", 0, -78);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ll_phone.setVisibility(View.VISIBLE);
                showSoftInputFromWindow(et_phone);
                tv_phone.setClickable(false);
            }
        });
    }
    private void findView() {
        setContentView(R.layout.login_new);
        tv_phone = findViewById(R.id.tv_login_phone);
        ll_phone = findViewById(R.id.ll_login_phone);
        et_phone = findViewById(R.id.et_login_phone);
        btn_next = findViewById(R.id.btn_login_next);
        ivClear = findViewById(R.id.iv_login_clear);
    }

    public class MLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }

    // 百度定位
    private void initBD() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption clientOption = new LocationClientOption();
        clientOption.setLocationMode(LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setScanSpan(100);
        clientOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(clientOption);
        mLocationClient.start();
    }

    private void setView() {
        spUtil.saveBoolean("guide", true);
        if (spUtil.getString("loginPhone", "") != null && !"".equals(spUtil.getString("loginPhone", ""))){
            setAnim();
            if (Utils.isStrNull(spUtil.getString("loginPhone", ""))){
                ivClear.setVisibility(View.VISIBLE);
            }
            et_phone.setText(spUtil.getString("loginPhone", ""));
            et_phone.setSelection(spUtil.getString("loginPhone", "").length());//将光标移至文字末尾
            if (!Utils.isStrNull(et_phone.getText().toString().trim().replace(" ", "")) || !Utils.checkPhone1(LoginNewActivity.this, et_phone)) {
                btn_next.setClickable(false);
                btn_next.setBackgroundResource(R.drawable.bg_nextbtn_gray);
            } else {
                btn_next.setClickable(true);
                btn_next.setBackgroundResource(R.drawable.bg_nextbtn_blue);
            }
        }
    }

    private void goMain(Class clazz) {
        Intent intent = new Intent(this, clazz);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        startActivity(intent);
        finishWithAnimation();
    }

    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

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
                UpdateUtil.installAPK(mActivity, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mActivity, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mActivity, new File(DownloadAppUtils.downloadUpdateApkFilePath));
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
        CommUtil.getLatestVer(LoginNewActivity.this, latestHanler);
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
                                                Global.getCurrentVersion(mActivity));
                                if (isLatest) {// 需要下载安装最新版
                                    if (isUpgrade == 1) {
                                        // 强制升级
                                        UpdateUtil.showForceUpgradeDialog(mActivity, versionHint,
                                                downloadPath, latestVersion, new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                UpdateUtil.updateApk(mActivity,
                                                                        downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                            }
                                                        } else {
                                                            UpdateUtil.updateApk(mActivity,
                                                                    downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                        }
                                                    }
                                                });
                                    } else if (isUpgrade == 0) {
                                        // 非强制升级
                                        UpdateUtil.showUpgradeDialog(mActivity, versionHint,
                                                downloadPath, latestVersion, new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                                            } else {
                                                                UpdateUtil.updateApk(mActivity,
                                                                        downloadPath, latestVersion, UpdateUtil.UPDATEFORNOTIFICATION, isUpgrade);
                                                            }
                                                        } else {
                                                            UpdateUtil.updateApk(mActivity,
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    /* private void getVerification(String phone) {
        pDialog.showDialog();
        String slat_md5 = MD5.md5(Global.MD5_STR, etPhone.getText().toString().trim().replace(" ", ""));
        Log.e("TAG", "slat_md5 = " + slat_md5);
        CommUtil.genVerifyCode(this, phone, et_login_imgver.getText().toString().trim(), mobileKey, slat_md5, verificationHandler);
    }*/
}

