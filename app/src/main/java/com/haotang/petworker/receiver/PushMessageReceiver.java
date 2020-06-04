package com.haotang.petworker.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.haotang.petworker.AgreementActivity;
import com.haotang.petworker.AreaFeedbackHistoryActivity;
import com.haotang.petworker.LoginNewActivity;
import com.haotang.petworker.MainActivity;
import com.haotang.petworker.MyLevelActivity;
import com.haotang.petworker.OrderDetailNewActivity;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.Utils;

import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";
    private String url;
    private SharedPreferenceUtil spUtil;
    private String content;
    private int type;
    private int orderId;

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] 接收到推送下来的自定义消息:" + customMessage);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        if (spUtil == null) {
            spUtil = SharedPreferenceUtil.getInstance(context
                    .getApplicationContext());
        }
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        url = "";
        content = "";
        type = 0;
        orderId = 0;
        try {
            String extras = message.notificationExtras;
            if (Utils.isStrNull(extras)) {
                JSONObject jobj = new JSONObject(extras);
                if (jobj.has("type") && !jobj.isNull("type")) {
                    type = jobj.getInt("type");
                }
                if (jobj.has("orderId") && !jobj.isNull("orderId")) {
                    orderId = jobj.getInt("orderId");
                }
                if (jobj.has("url") && !jobj.isNull("url")) {
                    url = jobj.getString("url");
                }
                if (jobj.has("content") && !jobj.isNull("content")) {
                    content = jobj.getString("content");
                }
                goActivity(context);
            }
        } catch (Exception e) {
            Log.e("TAG", "e = " + e.toString());
            e.printStackTrace();
        }
    }

    private void goActivity(Context context) {
        if(Utils.checkLogin(context)){
            if (!isAppOpen(context)) {
                context.startActivity(new Intent(context, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            context.startActivity(getDefalutIntent(context));
        }else{
            context.startActivity(new Intent(context, LoginNewActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    private Intent getDefalutIntent(Context context) {
        Intent intent = new Intent();
        if (type == 3||type == 6) {//订单详情
            intent.putExtra("orderId", orderId);
            intent.setClass(context, OrderDetailNewActivity.class);
        } else if (type == 7) {//公告
            intent.putExtra("content", content);
            intent.putExtra("url", url);
            intent.setClass(context, AgreementActivity.class);
        } else if (type == 8) {//我的等级
            intent.setClass(context, MyLevelActivity.class);
        }else if ("1001".equals(type) || "2033".equals(type)) {// 区域建议列表
            intent.putExtra("type", 0);
            intent.setClass(context, AreaFeedbackHistoryActivity.class);
        } else if ("1002".equals(type) || "2034".equals(type)) {// 评价店长列表
            intent.putExtra("type", 1);
            intent.setClass(context, AreaFeedbackHistoryActivity.class);
        } else if ("1003".equals(type) || "2035".equals(type)) {// 申请店长列表
            intent.putExtra("type", 2);
            intent.setClass(context, AreaFeedbackHistoryActivity.class);
        } else {
            intent.setClass(context, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        spUtil = SharedPreferenceUtil.getInstance(context
                .getApplicationContext());
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
        if (!registrationId.isEmpty()) {
            Global.savePushID(context, registrationId);
        }
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    private boolean isAppOpen(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(
                    "com.haotang.petworker")
                    || info.baseActivity.getPackageName().equals(
                    "com.haotang.petworker")) {
                return true;
            }
        }
        return false;
    }
}
