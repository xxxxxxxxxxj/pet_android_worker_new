package com.haotang.petworker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.view.NiceImageView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeauticianIntroduceActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_introduce_name)
    TextView tvIntroduceName;
    @BindView(R.id.iv_introduce_level)
    ImageView ivIntroduceLevel;
    @BindView(R.id.tv_introduce_special)
    TextView tvIntroduceSpecial;
    @BindView(R.id.tv_introduce_self)
    TextView tvIntroduceSelf;
    @BindView(R.id.iv_introduce_head)
    NiceImageView ivIntroduceHead;
    private MProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        setView();
        getData();
    }

    private void getData() {

            pDialog.showDialog();
            CommUtil.getWorkerInfo(this,spUtil.getString("wcellphone", ""),
                    Global.getIMEI(this), Global.getCurrentVersion(this),
                    beauticianHandler);

    }

    private AsyncHttpResponseHandler beauticianHandler = new AsyncHttpResponseHandler() {

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

                        if (jData.has("avatar") && !jData.isNull("avatar")
                                && !"".equals(jData.getString("avatar"))) {
                            GlideUtil.loadImg(BeauticianIntroduceActivity.this,jData.getString("avatar"),ivIntroduceHead,R.drawable.icon_beautician_default);
                        }

                        if (jData.has("level")&&!jData.isNull("level")){
                            JSONObject lj = jData.getJSONObject("level");
                            int id = lj.getInt("id");
                            if (id==1){
                                ivIntroduceLevel.setImageResource(R.drawable.middle_level_red);
                            }else if (id==2){
                                ivIntroduceLevel.setImageResource(R.drawable.heigh_level_red);
                            }else {
                                ivIntroduceLevel.setImageResource(R.drawable.best_level_red);
                            }
                        }

                        if (jData.has("realName") && !jData.isNull("realName")) {
                            tvIntroduceName.setText(jData.getString("realName"));
                        }
                        if (jData.has("level") && !jData.isNull("level")) {
                            JSONObject jlevel = jData.getJSONObject("level");
                            if (jlevel.has("name") && !jlevel.isNull("name")) {

                            }
                        }
                        if (jData.has("introduction")
                                && !jData.isNull("introduction")) {
                            tvIntroduceSelf.setText(jData
                                    .getString("introduction"));
                        }
                        if (jData.has("expertises")
                                && !jData.isNull("expertises")) {
                            JSONArray earr = jData.getJSONArray("expertises");
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < earr.length(); i++) {
                                JSONObject ji = earr.getJSONObject(i);
                                if (ji.has("itemName")
                                        && !ji.isNull("itemName"))
                                    sb.append(ji.getString("itemName") + " ");
                            }
                            tvIntroduceSpecial.setText(sb.toString().substring(0,
                                    sb.toString().length() - 1));
                        }

                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            pDialog.closeDialog();
        }

    };

    private void setView() {
        setContentView(R.layout.activity_beautician_introduce);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("美容师介绍");
        pDialog = new MProgressDialog(this);
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

    @OnClick(R.id.ib_titlebar_back)
    public void onViewClicked() {
        finish();
    }
}
