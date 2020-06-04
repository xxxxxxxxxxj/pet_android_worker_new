package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.ExampleUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MD5;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.SecurityCodeView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 姜谷蓄
 * 验证码页面
 *
 */

public class VerifyCodeActivity extends SuperActivity implements SecurityCodeView.InputCompleteListener{

    private TextView tv_sendphone;
    private SecurityCodeView etCode;
    private String phone;
    private String mobileKey;
    private MProgressDialog pDialog;
    private TextView tvGetcode;
    private TextView tvTimeout;
    private int seconds = 60;
    private Timer timer;
    private TimerTask task;
    private LocationClient mLocationClient;
    private MLocationListener mLocationListener;
    private ImageView ivBack;
    private static final int MSG_SET_ALIASANDTAGS = 1001;
    private int noCareCount;
    private int fansCount;
    Set<String> tagSet = new LinkedHashSet<String>();
    private int agreement = -1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Global.LOGIN_STARTTIME_VERIFICATION:
                    if (seconds <= 0) {
                        tvGetcode.setVisibility(View.VISIBLE);
                        tvGetcode.setClickable(true);
                        tvTimeout.setVisibility(View.GONE);
                        stopTime();
                        return;
                    }
                    tvTimeout.setText(seconds + "s后重新获取");
                    seconds--;
                    break;
            }
        }
    };
    private final Handler jpushHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIASANDTAGS:
                    Utils.mLogError("Set aliasandtags in handler.");
                    TagAndAlias tagAndAlias = (TagAndAlias) msg.obj;
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            tagAndAlias.getAlias(), tagAndAlias.getTags(),
                            mAliasCallback);
                    break;
                default:
                    Utils.mLogError("Unhandled msg - " + msg.what);
            }
        }
    };
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        findView();
        setView();
        setListener();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        etCode.showSoftInputFromWindow();
    }

    private void setListener() {
        tvGetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerification(phone.trim().replace(" ", ""));
                seconds=60;
                startTime();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etCode.setInputCompleteListener(this);
    }

    private void setView() {
        pDialog = new MProgressDialog(this);
        tv_sendphone.setText("验证码已发送至 +86 "+phone);
        tvGetcode.setVisibility(View.GONE);
        startTime();
    }

    private void getData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
    }

    private void findView() {
        setContentView(R.layout.activity_verify_code);
        tv_sendphone = findViewById(R.id.tv_sendphone);
        tvGetcode = findViewById(R.id.tv_getcode);
        tvTimeout = findViewById(R.id.tv_outtime);
        etCode = findViewById(R.id.edit_security_code);
        ivBack = findViewById(R.id.iv_back);
    }

    private void getVerification(String phone) {
        pDialog.showDialog();
        startTime();
        String slat_md5 = MD5.md5(Global.MD5_STR, phone.trim().replace(" ", ""));
        Log.e("TAG", "slat_md5 = " + slat_md5);
        CommUtil.genVerifyCode(this, phone,"", mobileKey, slat_md5, verificationHandler);
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

                } else if (10 == resultCode) {
                    ToastUtil.showToastCenterShort(VerifyCodeActivity.this,
                            "您没有注册为美容师，请先与客服人员联系成为美容师");
                } else if (resultCode == 11) {
                    if (Utils.isStrNull(msg)) {
                        //tv_login_imgyzmerror.setText(msg);
                        ToastUtil.showToastBottomShort(VerifyCodeActivity.this, msg);
                    }
                    Utils.goneJP(VerifyCodeActivity.this);
                } else if (resultCode == 12) {
                    if (Utils.isStrNull(msg)) {
                        //tv_login_imgyzmerror.setText(msg);
                        ToastUtil.showToastBottomShort(VerifyCodeActivity.this, msg);
                    }
                    Utils.goneJP(VerifyCodeActivity.this);
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
                        ToastUtil.showToastBottomShort(VerifyCodeActivity.this, msg);
                    }
                    Utils.goneJP(VerifyCodeActivity.this);
                } else {
                    // 获取验证码失败 获取服务器提示信息展示给用户
                    if (Utils.isStrNull(msg)) {
                        ToastUtil.showToastBottomShort(VerifyCodeActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil
                        .showToastCenterShort(VerifyCodeActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastCenterShort(VerifyCodeActivity.this, "请求失败");
            pDialog.closeDialog();
        }
    };
    private void login(String phone, String code) {
        pDialog.showDialog();
        CommUtil.login(this, phone, Global.getIMEI(this),
                Global.getCurrentVersion(this), code, lat, lng, loginHandler);
    }

    private AsyncHttpResponseHandler loginHandler = new AsyncHttpResponseHandler() {

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
                        JSONObject jData = jobj.getJSONObject("data");
                        if (jData.has("cellPhone")
                                && !jData.isNull("cellPhone")) {
                            spUtil.saveString("wcellphone",
                                    jData.getString("cellPhone"));
                            spUtil.saveString("loginPhone",
                                    jData.getString("cellPhone"));
                            // 设置推送别名
                            setaliasAndTags();
                        }
                        if (jData.has("workEnd") && !jData.isNull("workEnd")) {
                            spUtil.saveString("wworkerendtime",
                                    jData.getString("workEnd"));
                        }
                        if (jData.has("agreementStatus")
                                && !jData.isNull("agreementStatus")) {
                            agreement = jData.getInt("agreementStatus");
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
                        if (jData.has("noCareCount") && !jData.isNull("noCareCount")) {
                            noCareCount = jData.getInt("noCareCount");
                            spUtil.saveInt("noCareCount", jData.getInt("noCareCount"));
                        }
                        if (jData.has("fansCount") && !jData.isNull("fansCount")) {
                            fansCount = jData.getInt("fansCount");
                            spUtil.saveInt("fansCount", jData.getInt("fansCount"));
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
                            if (JWorker.has("id")
                                    && !JWorker.isNull("id")) {
                                spUtil.saveInt("WorkerId",
                                        JWorker.getInt("id"));
                            }
                            if (JWorker.has("workLoc")
                                    && !JWorker.isNull("workLoc")) {
                                spUtil.saveInt("workLoc",
                                        JWorker.getInt("workLoc"));
                            }
                        }
                    }
                    if (spUtil.getInt("wuserid", 0) > 0) {
                        if (agreement == 0) {
                            goMain(AgreementLaunchActivity.class);
                        } else {
                            if (MApplication.listAppoint.size() > 0) {
                                for (int i = 0; i < MApplication.listAppoint.size(); i++) {
                                    MApplication.listAppoint.get(i).finish();
                                }
                            }
                            MApplication.listAppoint.clear();
                            Intent intent = new Intent(VerifyCodeActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        ToastUtil.showToastCenterShort(VerifyCodeActivity.this,
                                "您没有注册为美容师，请先与客服人员联系成为美容师");
                    }
                } else {
                    if (Utils.isStrNull(msg)) {
                        ToastUtil
                                .showToastCenterShort(VerifyCodeActivity.this,msg);
                    }
                    Utils.goneJP(VerifyCodeActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil
                        .showToastCenterShort(VerifyCodeActivity.this, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastCenterShort(VerifyCodeActivity.this, "请求失败");
            pDialog.closeDialog();
        }
    };

    private void goMain(Class clazz) {
        Intent intent = new Intent(this, clazz);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        startActivity(intent);
        finishWithAnimation();
    }

    private void setaliasAndTags() {
        // 获取电话号码
        String cellphone = spUtil.getString("cellphone", "");
        Utils.mLogError("cellphone = " + cellphone);
        if (cellphone != null && !TextUtils.isEmpty(cellphone)) {
            // 检查 tag 的有效性
            if (!ExampleUtil.isValidTagAndAlias(cellphone)) {
                Utils.mLogError("Invalid format");
            } else {
                tagSet.add(cellphone);
            }
            // 设置别名和tag
            JPushInterface.setAliasAndTags(getApplicationContext(), cellphone,
                    tagSet, mAliasCallback);
        }
    }
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Utils.mLogError("设置别名:" + logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Utils.mLogError("设置别名:" + logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        jpushHandler.sendMessageDelayed(
                                jpushHandler.obtainMessage(MSG_SET_ALIASANDTAGS,
                                        new TagAndAlias(alias, tags)), 1000 * 60);
                    } else {
                        Utils.mLogError("设置别名:" + "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Utils.mLogError("设置别名:" + logs);
            }
        }
    };

    class TagAndAlias {
        private String alias;
        private Set<String> tags;

        public TagAndAlias(String alias, Set<String> tags) {
            super();
            this.alias = alias;
            this.tags = tags;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public Set<String> getTags() {
            return tags;
        }

        public void setTags(Set<String> tags) {
            this.tags = tags;
        }
    }

    @Override
    public void inputComplete() {
        login(phone.trim().replace(" ", ""),etCode.getEditContent());
    }

    @Override
    public void deleteContent(boolean isDelete) {

    }

    private void startTime() {
        tvGetcode.setVisibility(View.GONE);
        tvTimeout.setVisibility(View.VISIBLE);
        task = new TimerTask() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(Global.LOGIN_STARTTIME_VERIFICATION);
            }
        };
        timer = new Timer();
        timer.schedule(task, 1, 1000);
    }

    private void stopTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
    // 百度定位
    private void initBD() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption clientOption = new LocationClientOption();
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setScanSpan(100);
        clientOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(clientOption);
        mLocationClient.start();
    }

    public class MLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTime();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
    }
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
}
