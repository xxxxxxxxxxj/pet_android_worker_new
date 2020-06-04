package com.haotang.petworker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.petworker.adapter.HomeWorkerServiceAdapter;
import com.haotang.petworker.adapter.HomeWorkerServiceAdapter.OnHomeAddListener;
import com.haotang.petworker.adapter.HomeWorkerServiceAdapter.OnHomeMinusListener;
import com.haotang.petworker.adapter.ShopWorkerServiceAdapter;
import com.haotang.petworker.adapter.ShopWorkerServiceAdapter.OnShopAddListener;
import com.haotang.petworker.adapter.ShopWorkerServiceAdapter.OnShopMinusListener;
import com.haotang.petworker.entity.HomeWorkerServiceList;
import com.haotang.petworker.entity.NewPetBean;
import com.haotang.petworker.entity.ShopWorkerServiceList;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.ImageLoaderUtil;
import com.haotang.petworker.utils.MDialog;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.MListview;
import com.haotang.petworker.view.SelectableRoundedImageView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title:AdjustmentServiceTimeActivity
 * </p>
 * <p>
 * Description:调整服务时长界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-5-16 上午11:39:13
 */
public class AdjustmentServiceTimeActivity extends Activity implements
		OnClickListener {
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private ImageButton ibBack;
	private TextView tvTitle;
	private Button btn_adjustment_service_submit;
	private SelectableRoundedImageView sriv_adjustment_service_img;
	private TextView tv_adjustment_service_name;
	private TextView tv_adjustment_service_tc;
	private MListview mlv_adjustment_service_shangmen;
	private MListview mlv_adjustment_service_daodian;
	private NewPetBean newPetBean;
	private TextView tv_adjustment_service_nodaodian;
	private TextView tv_adjustment_service_noshangmen;
	private List<HomeWorkerServiceList> listHomeWorkerServiceList = new ArrayList<HomeWorkerServiceList>();
	private List<ShopWorkerServiceList> listShopWorkerServiceList = new ArrayList<ShopWorkerServiceList>();
	private List<HomeWorkerServiceList> localListHomeWorkerServiceList = new ArrayList<HomeWorkerServiceList>();
	private List<ShopWorkerServiceList> localListShopWorkerServiceList = new ArrayList<ShopWorkerServiceList>();
	private List<ShopWorkerServiceList> idsList = new ArrayList<ShopWorkerServiceList>();
	protected String noSetTimeMsg;
	protected int time;
	private HomeWorkerServiceAdapter<HomeWorkerServiceList> homeWorkerServiceAdapter;
	private ShopWorkerServiceAdapter<ShopWorkerServiceList> shopWorkerServiceAdapter;
	private StringBuilder updateServiceTime = new StringBuilder();
	private boolean isShangmen;
	private boolean isDaodian;
	private String updateServiceTimeStr;
	private ScrollView sv_adjustment_service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		intiView();
		setView();
		initLinsetr();
		getData();
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

	private void initData() {
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		Intent intent = getIntent();
		newPetBean = (NewPetBean) intent.getSerializableExtra("newPetBean");
	}

	private void intiView() {
		setContentView(R.layout.activity_adjustment_service_time);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		btn_adjustment_service_submit = (Button) findViewById(R.id.btn_adjustment_service_submit);
		sriv_adjustment_service_img = (SelectableRoundedImageView) findViewById(R.id.sriv_adjustment_service_img);
		tv_adjustment_service_name = (TextView) findViewById(R.id.tv_adjustment_service_name);
		tv_adjustment_service_tc = (TextView) findViewById(R.id.tv_adjustment_service_tc);
		mlv_adjustment_service_shangmen = (MListview) findViewById(R.id.mlv_adjustment_service_shangmen);
		mlv_adjustment_service_daodian = (MListview) findViewById(R.id.mlv_adjustment_service_daodian);
		tv_adjustment_service_nodaodian = (TextView) findViewById(R.id.tv_adjustment_service_nodaodian);
		tv_adjustment_service_noshangmen = (TextView) findViewById(R.id.tv_adjustment_service_noshangmen);
		sv_adjustment_service = (ScrollView) findViewById(R.id.sv_adjustment_service);
	}

	private void setView() {
		tvTitle.setText("服务时长调整");
		if (newPetBean != null) {
			ImageLoaderUtil.loadImg(newPetBean.avatarPath,
					sriv_adjustment_service_img,
					R.drawable.icon_beautician_default, null);
			Utils.setText(tv_adjustment_service_name, newPetBean.petName, "",
					View.VISIBLE, View.GONE);
		}
		listHomeWorkerServiceList.clear();
		localListHomeWorkerServiceList.clear();
		homeWorkerServiceAdapter = new HomeWorkerServiceAdapter<HomeWorkerServiceList>(
				getApplicationContext(), listHomeWorkerServiceList);
		mlv_adjustment_service_shangmen.setAdapter(homeWorkerServiceAdapter);
		listShopWorkerServiceList.clear();
		localListShopWorkerServiceList.clear();
		shopWorkerServiceAdapter = new ShopWorkerServiceAdapter<ShopWorkerServiceList>(
				getApplicationContext(), listShopWorkerServiceList);
		mlv_adjustment_service_daodian.setAdapter(shopWorkerServiceAdapter);
	}

	private void initLinsetr() {
		ibBack.setOnClickListener(this);
		btn_adjustment_service_submit.setOnClickListener(this);
		homeWorkerServiceAdapter.setOnHomeAddListener(new OnHomeAddListener() {

			@Override
			public void OnHomeAdd(int position) {
				Log.e("TAG", "OnHomeAdd = " + position);
				if (listHomeWorkerServiceList.size() > 0
						&& listHomeWorkerServiceList.size() > position) {
					HomeWorkerServiceList homeWorkerServiceList = listHomeWorkerServiceList
							.get(position);
					if (homeWorkerServiceList != null) {
						if (homeWorkerServiceList.countHomeDuration < homeWorkerServiceList.homeMaxTime) {
							homeWorkerServiceList.homeDuration = homeWorkerServiceList.homeDuration
									+ time;
							homeWorkerServiceList.countHomeDuration = homeWorkerServiceList.baseHomeDuration
									+ homeWorkerServiceList.homeDuration;
						} else {
							ToastUtil.showToastCenterShort(
									AdjustmentServiceTimeActivity.this,
									"已调整到最大值");
						}
					}
				}
				homeWorkerServiceAdapter.setData(listHomeWorkerServiceList);
			}
		});
		homeWorkerServiceAdapter
				.setOnHomeMinusListener(new OnHomeMinusListener() {

					@Override
					public void OnHomeMinus(int position) {
						Log.e("TAG", "OnHomeMinus = " + position);
						if (listHomeWorkerServiceList.size() > 0
								&& listHomeWorkerServiceList.size() > position) {
							HomeWorkerServiceList homeWorkerServiceList = listHomeWorkerServiceList
									.get(position);
							if (homeWorkerServiceList != null) {
								if (homeWorkerServiceList.countHomeDuration > homeWorkerServiceList.homeMinTime) {
									homeWorkerServiceList.homeDuration = homeWorkerServiceList.homeDuration
											- time;
									homeWorkerServiceList.countHomeDuration = homeWorkerServiceList.baseHomeDuration
											+ homeWorkerServiceList.homeDuration;
								} else {
									ToastUtil.showToastCenterShort(
											AdjustmentServiceTimeActivity.this,
											"已调整到最小值");
								}
							}
						}
						homeWorkerServiceAdapter
								.setData(listHomeWorkerServiceList);
					}
				});

		shopWorkerServiceAdapter.setOnShopAddListener(new OnShopAddListener() {

			@Override
			public void OnShopAdd(int position) {
				Log.e("TAG", "OnShopAdd = " + position);
				if (listShopWorkerServiceList.size() > 0
						&& listShopWorkerServiceList.size() > position) {
					ShopWorkerServiceList shopWorkerServiceList = listShopWorkerServiceList
							.get(position);
					if (shopWorkerServiceList != null) {
						if (shopWorkerServiceList.countShopDuration < shopWorkerServiceList.shopMaxTime) {
							shopWorkerServiceList.shopDuration = shopWorkerServiceList.shopDuration
									+ time;
							shopWorkerServiceList.countShopDuration = shopWorkerServiceList.baseShopDuration
									+ shopWorkerServiceList.shopDuration;
						} else {
							ToastUtil.showToastCenterShort(
									AdjustmentServiceTimeActivity.this,
									"已调整到最大值");
						}
					}
				}
				shopWorkerServiceAdapter.setData(listShopWorkerServiceList);
			}
		});
		shopWorkerServiceAdapter
				.setOnShopMinusListener(new OnShopMinusListener() {

					@Override
					public void OnShopMinus(int position) {
						Log.e("TAG", "OnShopMinus = " + position);
						if (listShopWorkerServiceList.size() > 0
								&& listShopWorkerServiceList.size() > position) {
							ShopWorkerServiceList shopWorkerServiceList = listShopWorkerServiceList
									.get(position);
							if (shopWorkerServiceList != null) {
								if (shopWorkerServiceList.countShopDuration > shopWorkerServiceList.shopMinTime) {
									shopWorkerServiceList.shopDuration = shopWorkerServiceList.shopDuration
											- time;
									shopWorkerServiceList.countShopDuration = shopWorkerServiceList.baseShopDuration
											+ shopWorkerServiceList.shopDuration;
								} else {
									ToastUtil.showToastCenterShort(
											AdjustmentServiceTimeActivity.this,
											"已调整到最小值");
								}
							}
						}
						shopWorkerServiceAdapter
								.setData(listShopWorkerServiceList);
					}
				});
	}

	private void getData() {
		if (newPetBean != null) {
			pDialog.showDialog();
			CommUtil.queryWorkerService(this, spUtil.getInt("WorkerId", 0),
					newPetBean.PetId, queryWorkerServiceHandler);
		}
	}

	private AsyncHttpResponseHandler queryWorkerServiceHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				if (jobj.has("code") && !jobj.isNull("code")) {
					int code = jobj.getInt("code");
					if (code == 0) {
						if (jobj.has("data") && !jobj.isNull("data")) {
							JSONObject jdata = jobj.getJSONObject("data");
							if (jdata.has("time") && !jdata.isNull("time")) {
								time = jdata.getInt("time");
							}
							if (jdata.has("noSetTimeMsg")
									&& !jdata.isNull("noSetTimeMsg")) {
								noSetTimeMsg = jdata.getString("noSetTimeMsg");
							}
							if (jdata.has("homeWorkerServiceList")
									&& !jdata.isNull("homeWorkerServiceList")) {
								JSONArray jhomeWorkerServiceList = jdata
										.getJSONArray("homeWorkerServiceList");
								if (jhomeWorkerServiceList.length() > 0) {
									listHomeWorkerServiceList.clear();
									localListHomeWorkerServiceList.clear();
									for (int i = 0; i < jhomeWorkerServiceList
											.length(); i++) {
										localListHomeWorkerServiceList
												.add(HomeWorkerServiceList
														.json2Entity(jhomeWorkerServiceList
																.getJSONObject(i)));
										listHomeWorkerServiceList
												.add(HomeWorkerServiceList
														.json2Entity(jhomeWorkerServiceList
																.getJSONObject(i)));
									}
									homeWorkerServiceAdapter
											.setData(listHomeWorkerServiceList);
									mlv_adjustment_service_shangmen
											.setVisibility(View.VISIBLE);
									tv_adjustment_service_noshangmen
											.setVisibility(View.GONE);
								} else {
									Utils.setText(
											tv_adjustment_service_noshangmen,
											noSetTimeMsg, "", View.VISIBLE,
											View.GONE);
									mlv_adjustment_service_shangmen
											.setVisibility(View.GONE);
									tv_adjustment_service_noshangmen
											.setVisibility(View.VISIBLE);
								}
							} else {
								Utils.setText(tv_adjustment_service_noshangmen,
										noSetTimeMsg, "", View.VISIBLE,
										View.GONE);
								mlv_adjustment_service_shangmen
										.setVisibility(View.GONE);
								tv_adjustment_service_noshangmen
										.setVisibility(View.VISIBLE);
							}
							if (jdata.has("shopWorkerServiceList")
									&& !jdata.isNull("shopWorkerServiceList")) {
								JSONArray jshopWorkerServiceList = jdata
										.getJSONArray("shopWorkerServiceList");
								if (jshopWorkerServiceList.length() > 0) {
									listShopWorkerServiceList.clear();
									localListShopWorkerServiceList.clear();
									for (int i = 0; i < jshopWorkerServiceList
											.length(); i++) {
										localListShopWorkerServiceList
												.add(ShopWorkerServiceList
														.json2Entity(jshopWorkerServiceList
																.getJSONObject(i)));
										listShopWorkerServiceList
												.add(ShopWorkerServiceList
														.json2Entity(jshopWorkerServiceList
																.getJSONObject(i)));
									}
									shopWorkerServiceAdapter
											.setData(listShopWorkerServiceList);
									mlv_adjustment_service_daodian
											.setVisibility(View.VISIBLE);
									tv_adjustment_service_nodaodian
											.setVisibility(View.GONE);
								} else {
									Utils.setText(
											tv_adjustment_service_nodaodian,
											noSetTimeMsg, "", View.VISIBLE,
											View.GONE);
									mlv_adjustment_service_daodian
											.setVisibility(View.GONE);
									tv_adjustment_service_nodaodian
											.setVisibility(View.VISIBLE);
								}
							} else {
								Utils.setText(tv_adjustment_service_nodaodian,
										noSetTimeMsg, "", View.VISIBLE,
										View.GONE);
								mlv_adjustment_service_daodian
										.setVisibility(View.GONE);
								tv_adjustment_service_nodaodian
										.setVisibility(View.VISIBLE);
							}
							ScollTo();
						}
					} else {
						if (jobj.has("msg") && !jobj.isNull("msg")) {
							ToastUtil.showToastBottomShort(
									AdjustmentServiceTimeActivity.this,
									jobj.getString("msg"));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showToastCenterShort(
						AdjustmentServiceTimeActivity.this, "数据解析异常");
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			ToastUtil.showToastCenterShort(AdjustmentServiceTimeActivity.this,
					"请求失败");
			pDialog.closeDialog();
		}
	};

	private void ScollTo() {
		// 解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				sv_adjustment_service.fullScroll(ScrollView.FOCUS_UP);
			}
		}, 5);
	}

	private AsyncHttpResponseHandler updateWorkerServiceHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				if (jobj.has("code") && !jobj.isNull("code")) {
					int code = jobj.getInt("code");
					if (code == 0) {
						ToastUtil.showToastCenterShort(
								AdjustmentServiceTimeActivity.this, "调整成功");
						finish();
					} else {
						if (jobj.has("msg") && !jobj.isNull("msg")) {
							ToastUtil.showToastBottomShort(
									AdjustmentServiceTimeActivity.this,
									jobj.getString("msg"));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showToastCenterShort(
						AdjustmentServiceTimeActivity.this, "数据解析异常");
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			ToastUtil.showToastCenterShort(AdjustmentServiceTimeActivity.this,
					"请求失败");
			pDialog.closeDialog();
		}
	};

	private boolean isAdjust() {
		boolean isAdjust = false;
		if (localListHomeWorkerServiceList.toString().equals(
				listHomeWorkerServiceList.toString())) {
			isShangmen = false;
		} else {
			isShangmen = true;
		}
		if (localListShopWorkerServiceList.toString().equals(
				listShopWorkerServiceList.toString())) {
			isDaodian = false;
		} else {
			isDaodian = true;
		}
		if (isShangmen || isDaodian) {
			isAdjust = true;
		} else {
			isAdjust = false;
		}
		return isAdjust;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			if (isAdjust()) {
				showBackDialog();
			} else {
				finish();
			}
			break;
		case R.id.btn_adjustment_service_submit:
			if (isAdjust()) {
				idsList.clear();
				updateServiceTime.setLength(0);
				if (listHomeWorkerServiceList.size() > 0
						&& listShopWorkerServiceList.size() <= 0) {
					for (int i = 0; i < listHomeWorkerServiceList.size(); i++) {
						HomeWorkerServiceList homeWorkerServiceList = listHomeWorkerServiceList
								.get(i);
						if (homeWorkerServiceList != null) {
							updateServiceTime.append(homeWorkerServiceList.id
									+ "," + homeWorkerServiceList.homeDuration
									+ ",0_");
						}
					}
				} else if (listHomeWorkerServiceList.size() <= 0
						&& listShopWorkerServiceList.size() > 0) {
					for (int i = 0; i < listShopWorkerServiceList.size(); i++) {
						ShopWorkerServiceList shopWorkerServiceList = listShopWorkerServiceList
								.get(i);
						if (shopWorkerServiceList != null) {
							updateServiceTime
									.append(shopWorkerServiceList.id
											+ ",0,"
											+ +shopWorkerServiceList.shopDuration
											+ "_");
						}
					}
				} else if (listHomeWorkerServiceList.size() > 0
						&& listShopWorkerServiceList.size() > 0) {
					for (int i = 0; i < listShopWorkerServiceList.size(); i++) {
						for (int j = 0; j < listHomeWorkerServiceList.size(); j++) {
							if (listShopWorkerServiceList.get(i).id == listHomeWorkerServiceList
									.get(j).id) {
								idsList.add(listShopWorkerServiceList.get(i));
								updateServiceTime
										.append(listHomeWorkerServiceList
												.get(j).id
												+ ","
												+ listHomeWorkerServiceList
														.get(j).homeDuration
												+ ","
												+ listShopWorkerServiceList
														.get(i).shopDuration
												+ "_");
							}
						}
					}
					Log.e("TAG", "idsList.size() = " + idsList.size());
					if (idsList.size() > 0) {
						listShopWorkerServiceList.removeAll(idsList);
						for (int j = 0; j < listShopWorkerServiceList.size(); j++) {
							updateServiceTime
									.append(listShopWorkerServiceList.get(j).id
											+ ",0,"
											+ listShopWorkerServiceList.get(j).shopDuration
											+ "_");
						}
					}
				}
				if (updateServiceTime.toString().endsWith("_")) {
					updateServiceTimeStr = updateServiceTime.toString()
							.substring(0, updateServiceTime.length() - 1);
				}
				Log.e("TAG", "updateServiceTimeStr = " + updateServiceTimeStr);
				pDialog.showDialog();
				CommUtil.updateWorkerService(this, updateServiceTimeStr,
						updateWorkerServiceHandler);
			} else {
				ToastUtil.showToastCenterShort(
						AdjustmentServiceTimeActivity.this, "请修改需要调整的服务时长");
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isAdjust()) {
				showBackDialog();
			} else {
				finish();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private void showBackDialog() {
		MDialog mDialog = new MDialog.Builder(this)
				.setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage("你没有对修改的服务时长进行保存，确定返回吗？").setCancelStr("取消")
				.setOKStr("确定").positiveListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				}).build();
		mDialog.show();
	}
}
