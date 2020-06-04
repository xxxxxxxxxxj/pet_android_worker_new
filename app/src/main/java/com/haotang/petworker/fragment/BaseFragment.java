package com.haotang.petworker.fragment;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;

import com.haotang.petworker.AgreementLaunchActivity;
import com.haotang.petworker.FlashActivity;
import com.haotang.petworker.LoginNewActivity;
import com.haotang.petworker.VerifyCodeActivity;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.SharedPreferenceUtil;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-20 17:10
 */
public class BaseFragment extends Fragment {
    protected SharedPreferenceUtil spUtil;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spUtil = SharedPreferenceUtil.getInstance(getActivity());
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
                if (resultCode == Global.CODE_EXIT) {
                    ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                    String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();
                    Log.e("TAG", "activityName = " + activityName);
                    Class class1 = Class.forName(activityName);
                    Log.e("TAG", "class1 = " + class1.toString());
                    if (!(class1 == AgreementLaunchActivity.class || class1 == FlashActivity.class || class1 == VerifyCodeActivity.class || class1 == LoginNewActivity.class)) {
                        Intent intent = new Intent(getActivity(), LoginNewActivity.class);
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
}
