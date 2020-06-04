package com.haotang.petworker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.utils.ui.SystemBarTintManager;

import org.apache.http.Header;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;

public class SuperActivity extends Activity {
    protected int statusBarHeight;
    protected Context mContext;
    protected Activity mActivity;
    protected MProgressDialog mPDialog;
    protected SharedPreferenceUtil spUtil;
    protected boolean isImmersionBar = true;
    protected SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        mPDialog = new MProgressDialog(mContext);
        spUtil = SharedPreferenceUtil.getInstance(this);
        CommUtil.loginAuto(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), spUtil.getInt("wuserid", 0),
                autoLoginHandler);

        tintManager = new SystemBarTintManager();
        tintManager.showStatusBar(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
    }

    private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (resultCode == Global.CODE_EXIT) {
                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();
                    Log.e("TAG", "activityName = " + activityName);
                    Class class1 = Class.forName(activityName);
                    Log.e("TAG", "class1 = " + class1.toString());
                    if (!(class1 == AgreementLaunchActivity.class || class1 == FlashActivity.class || class1 == VerifyCodeActivity.class || class1 == LoginNewActivity.class)) {
                        Intent intent = new Intent(SuperActivity.this, LoginNewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        JPushInterface.onResume(this);
		/*int nDir = getIntent().getIntExtra(Global.ANIM_DIRECTION(), -1);
		if (nDir == Global.ANIM_COVER_FROM_LEFT())
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
		else if (nDir == Global.ANIM_COVER_FROM_RIGHT())
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		else
			overridePendingTransition(0, 0);*/
    }

    protected void initWindows() {
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        JPushInterface.onPause(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected Window setImmerseLayout() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        statusBarHeight = getStatusBarHeight(this.getBaseContext());
        return window;
    }

    public int getStatusBarHeight(Context context) {
        if (statusBarHeight != 0)
            return statusBarHeight;

        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(
                    resourceId);
        }
        return statusBarHeight;
    }

    public void finishWithAnimation() {

        SuperActivity.this.finish();
    }

    protected View setEmptyViewBase(int flag, String msg, int resId, View.OnClickListener OnClickListener) {//1.无网络2.无数据或数据错误
        View emptyView = View.inflate(this, R.layout.recycler_emptyview, null);
        ImageView iv_emptyview_img = (ImageView) emptyView.findViewById(R.id.iv_emptyview_img);
        TextView tv_emptyview_desc = (TextView) emptyView.findViewById(R.id.tv_emptyview_desc);
        Button btn_emptyview = (Button) emptyView.findViewById(R.id.btn_emptyview);
        if (flag == 1) {
            btn_emptyview.setVisibility(View.VISIBLE);
            btn_emptyview.setOnClickListener(OnClickListener);
        } else if (flag == 2) {
            btn_emptyview.setVisibility(View.GONE);
        }
        Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        iv_emptyview_img.setImageResource(resId);
        return emptyView;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setColorBar() {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        StatusBarLightMode();//设置状态栏白底黑字
    }

    /**
     *状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public int StatusBarLightMode(){
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(MIUISetStatusBarLightMode(this, true)){
                result=1;
            }else if(FlymeSetStatusBarLightMode(this.getWindow(), true)){
                result=2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result=3;
            }
            else//6.0以下版本
            {
                tintManager.setStatusBarTintResource(R.drawable.gradient_common_c5_c6);
                tintManager.setNavigationBarTintColor(Color.BLACK);
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     * @param activity
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window=activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if(dark){
                        activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }catch (Exception e){

            }
        }
        return result;
    }
}
