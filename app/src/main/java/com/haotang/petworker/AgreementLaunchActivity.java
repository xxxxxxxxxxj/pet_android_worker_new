package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.MScrollView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONObject;

public class AgreementLaunchActivity extends SuperActivity {

	private TextView tvContent;
	private TextView tvTitle;
	private TextView tvNullHint;
	private Button btAgree;
	private Button btRefresh;
	private MScrollView svMain;
	private RelativeLayout rlNull;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	private TextView tv_agreement_title_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agreement);
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);

		tvContent = (TextView) findViewById(R.id.tv_agreement_content);
		tvTitle = (TextView) findViewById(R.id.tv_agreement_title);
		tvNullHint = (TextView) findViewById(R.id.tv_null_hint);
		btAgree = (Button) findViewById(R.id.bt_agreement_agree);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
		svMain = (MScrollView) findViewById(R.id.sv_agreement);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tv_agreement_title_bottom = (TextView) findViewById(R.id.tv_agreement_title_bottom);

		btRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getAgreement();
			}
		});
		btAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setAgreement();
			}
		});
		getAgreement();

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

	private void showMain(boolean flag) {
		if (flag) {
			svMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
			btAgree.setVisibility(View.VISIBLE);
		} else {
			btAgree.setVisibility(View.GONE);
			svMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	private void getAgreement() {
		pDialog.showDialog();
		CommUtil.getAgreementContent(this,spUtil.getString("wcellphone", ""),
				Global.getIMEI(this), Global.getCurrentVersion(this),
				agreementHandler);
	}

	private void setAgreement() {
		pDialog.showDialog();
		CommUtil.workerAgreement(this,spUtil.getString("wcellphone", ""),
				Global.getIMEI(this), Global.getCurrentVersion(this),
				setAgreementHandler);
	}

	private AsyncHttpResponseHandler agreementHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			showMain(true);
			pDialog.closeDialog();
			Utils.mLogError("获取美容师协议：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("title") && !jdata.isNull("title")) {
						tvTitle.setText(jdata.getString("title"));
					}
					if (jdata.has("preface") && !jdata.isNull("preface")) {
						tv_agreement_title_bottom.setText(jdata.getString("preface"));
					}
					if (jdata.has("agreement") && !jdata.isNull("agreement")) {
						tvContent.setText(jdata.getString("agreement"));
					}
				} else {
					showMain(false);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showMain(false);
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			showMain(false);
			pDialog.closeDialog();

		}

	};
	private AsyncHttpResponseHandler setAgreementHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("同意美容师协议：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode) {
					goNext(MainActivity.class);
				} else {
					ToastUtil.showToastCenterShort(
							AgreementLaunchActivity.this, msg);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtil.showToastCenterShort(AgreementLaunchActivity.this,
						"网络异常，请重新提交");
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			ToastUtil.showToastCenterShort(AgreementLaunchActivity.this,
					"网络异常，请重新提交");

		}

	};

	private void goNext(Class clazz) {
		Intent intent = new Intent(this, clazz);
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
		startActivity(intent);
		finishWithAnimation();
	}

}
