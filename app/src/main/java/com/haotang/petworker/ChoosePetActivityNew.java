package com.haotang.petworker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.adapter.SortAdapter;
import com.haotang.petworker.entity.SortModel;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CharacterParser;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.PinyinComparator;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.view.ClearEditText;
import com.haotang.petworker.view.SideBar;
import com.haotang.petworker.view.SideBar.OnTouchingLetterChangedListener;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class ChoosePetActivityNew extends SuperActivity {
	private ImageButton ibBack;
	private Button btDog;
	private Button btCat;
	private ClearEditText cetSearch;
	private SideBar sbBar;
	private TextView tvBarHint;
	private ListView lvPets;
	private FrameLayout flMain;
	private RelativeLayout rlNull;
	private TextView tvMsg1;
	private TextView tvMsg2;
	private MProgressDialog pDialog;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private ArrayList<SortModel> petlist = new ArrayList<SortModel>();
	private SortAdapter petAdapter;
	private int petkind = 1;
	private int orderId;
	private int previous;
	public static SuperActivity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosepetnew);
		initData();
		findView();
		setView();
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
		Intent intent = getIntent();
		orderId = intent.getIntExtra("orderId", 0);
		previous = intent.getIntExtra("previous", 0);
		act = this;
		pDialog = new MProgressDialog(this);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_choosepet_back);
		btDog = (Button) findViewById(R.id.bt_choosepet_dog);
		btCat = (Button) findViewById(R.id.bt_choosepet_cat);
		cetSearch = (ClearEditText) findViewById(R.id.cet_choosepet_search);
		sbBar = (SideBar) findViewById(R.id.sb_choosepet_sidebar);
		tvBarHint = (TextView) findViewById(R.id.tv_choosepet_hint);
		lvPets = (ListView) findViewById(R.id.lv_choosepet_pets);
		flMain = (FrameLayout) findViewById(R.id.fl_choosepet_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
		tvMsg2 = (TextView) findViewById(R.id.tv_null_msg2);
	}

	private void setView() {
		tvMsg2.setVisibility(View.VISIBLE);
		tvMsg1.setText("汪星人的服务暂未开通哦");
		tvMsg2.setText("敬请期待吧");
		sbBar.setTextView(tvBarHint);
		if (previous == Global.UPGRADESERVICE_CHOOSEPET) {
			// 升级订单，屏蔽猫的点击
			btCat.setTextColor(getResources().getColor(R.color.white));
			btCat.setBackgroundResource(R.drawable.bg_member_right_gray);
		}else{
			btCat.setBackgroundResource(R.drawable.choosepet_right_unselect);
			btCat.setTextColor(getResources().getColor(R.color.aBB996C));
		}
		ibBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finishWithAnimation();
			}
		});
		btDog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				petkind = 1;
				btDog.setTextColor(getResources().getColor(R.color.black));
				btDog.setBackgroundResource(R.drawable.choosepet_left_select);
				if (previous == Global.UPGRADESERVICE_CHOOSEPET) {
					// 升级订单，屏蔽猫的点击
					btCat.setTextColor(getResources().getColor(R.color.white));
					btCat.setBackgroundResource(R.drawable.bg_member_right_gray);
				}else{
					btCat.setBackgroundResource(R.drawable.choosepet_right_unselect);
					btCat.setTextColor(getResources().getColor(R.color.aBB996C));
				}
				tvMsg1.setText("汪星人的服务暂未开通哦");
				tvMsg2.setText("敬请期待吧");
				getPets(petkind);
			}
		});
		btCat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (previous == Global.UPGRADESERVICE_CHOOSEPET) {
					ToastUtil.showToastBottomShort(ChoosePetActivityNew.this,
							"该服务暂不支持喵星人哦");
				} else {
					petkind = 2;
					btDog.setTextColor(getResources().getColor(R.color.aBB996C));
					btCat.setTextColor(getResources().getColor(R.color.black));
					btDog.setBackgroundResource(R.drawable.choosepet_left_unselect);
					btCat.setBackgroundResource(R.drawable.choosepet_right_select);
					tvMsg1.setText("喵星人的服务暂未开通哦");
					tvMsg2.setText("敬请期待吧");
					getPets(petkind);
				}
			}
		});
		cetSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sbBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				try {
					int position = petAdapter.getPositionForSection(s.charAt(0));
					if (position != -1) {
						lvPets.setSelection(position);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		petAdapter = new SortAdapter(this, petkind);
		lvPets.setAdapter(petAdapter);
		petAdapter.setData(petlist);
		lvPets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				goNext(position);
			}
		});
		getPets(petkind);
	}

	private void goNext(int position) {
		SortModel sm = (SortModel) petAdapter.getItem(position);
		Intent data = new Intent();
		data.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		data.putExtra("petid", sm.getPetId());
		data.putExtra("petkind", sm.getPetKind());
		data.putExtra("petname", sm.getName());
		data.putExtra("petimg", sm.getAvatarPath());
		setResult(RESULT_OK, data);
		finishWithAnimation();
	}

	private void showMain(boolean flag) {
		if (flag) {
			flMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			flMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	private void getPets(int petkind) {
		pDialog.showDialog();
		showMain(true);
		CommUtil.getPetList(this, petkind, orderId, petHandler);
	}

	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			String st = new String(responseBody);
			getJson(st, true);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	private void getJson(String json, boolean saveflag) {
		if (json == null || "".equals(json) || "null".equals(json))
			return;
		try {
			petlist.clear();
			JSONObject jobj = new JSONObject(json);
			int code = jobj.getInt("code");
			if (code == 0 && jobj.has("data") && !jobj.isNull("data")) {
				JSONObject jsonObject = jobj.getJSONObject("data");
				if (jsonObject.has("hot") && !jsonObject.isNull("hot")) {
					JSONArray jsonArrayHot = jsonObject.getJSONArray("hot");
					for (int k = jsonArrayHot.length() - 1; k >= 0; k--) {
						SortModel sm = new SortModel();
						JSONObject object = jsonArrayHot.getJSONObject(k);
						if (object.has("avatarPath")
								&& !object.isNull("avatarPath")) {
							sm.setAvatarPath(object.getString("avatarPath"));
						}
						if (object.has("id") && !object.isNull("id")) {
							sm.setPetId(object.getInt("id"));
						}
						sm.setHot(true);
						if (object.has("petKind") && !object.isNull("petKind")) {
							sm.setPetKind(object.getInt("petKind"));
						}
						if (object.has("petName") && !object.isNull("petName")) {
							sm.setName(object.getString("petName").trim());
						}
						if (object.has("pinyin") && !object.isNull("pinyin")) {
							sm.setPyhead(object.getString("pinyin"));
							sm.setSortLetters("@");
						}
						if (object.has("serviceHome")
								&& !object.isNull("serviceHome")) {
							JSONArray arrayService = object
									.getJSONArray("serviceHome");
							long ServiceHome[] = new long[arrayService.length()];
							for (int j = 0; j < arrayService.length(); j++) {
								ServiceHome[j] = arrayService.getLong(j);
							}
							sm.setServiceHome(ServiceHome);
						}
						if (object.has("serviceShop")
								&& !object.isNull("serviceShop")) {
							JSONArray arrayService = object
									.getJSONArray("serviceShop");
							long serviceShop[] = new long[arrayService.length()];
							for (int j = 0; j < arrayService.length(); j++) {
								serviceShop[j] = arrayService.getLong(j);
							}
							sm.setServiceShop(serviceShop);
						}
						petlist.add(sm);
					}
				}
				if (jsonObject.has("all") && !jsonObject.isNull("all")
						&& jsonObject.getJSONArray("all").length() > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("all");
					for (int i = 0; i < jsonArray.length(); i++) {
						SortModel sm = new SortModel();
						JSONObject object = jsonArray.getJSONObject(i);
						if (object.has("avatarPath")
								&& !object.isNull("avatarPath")) {
							sm.setAvatarPath(object.getString("avatarPath"));
						}
						if (object.has("id") && !object.isNull("id")) {
							sm.setPetId(object.getInt("id"));
						}
						sm.setHot(false);
						if (object.has("petKind") && !object.isNull("petKind")) {
							sm.setPetKind(object.getInt("petKind"));
						}
						if (object.has("petName") && !object.isNull("petName")) {
							sm.setName(object.getString("petName").trim());
						}
						if (object.has("pinyin") && !object.isNull("pinyin")) {
							String pinyin = object.getString("pinyin");
							sm.setPyhead(pinyin);

							String sortStr = pinyin.substring(0, 1)
									.toUpperCase();
							if (sortStr.matches("[A-Z]")) {
								sm.setSortLetters(sortStr.toUpperCase());
							} else {
								sm.setSortLetters("#");
							}
						}
						if (object.has("serviceHome")
								&& !object.isNull("serviceHome")) {
							JSONArray arrayService = object
									.getJSONArray("serviceHome");
							long ServiceHome[] = new long[arrayService.length()];
							for (int j = 0; j < arrayService.length(); j++) {
								ServiceHome[j] = arrayService.getLong(j);
							}
							sm.setServiceHome(ServiceHome);
						}
						if (object.has("serviceShop")
								&& !object.isNull("serviceShop")) {
							JSONArray arrayService = object
									.getJSONArray("serviceShop");
							long serviceShop[] = new long[arrayService.length()];
							for (int j = 0; j < arrayService.length(); j++) {
								serviceShop[j] = arrayService.getLong(j);
							}
							sm.setServiceShop(serviceShop);
						}
						petlist.add(sm);
					}
				}
				// 根据a-z进行排序源数据
				Collections.sort(petlist, pinyinComparator);
				petAdapter.setData(petlist);
				lvPets.setSelection(0);
			} else {
				showMain(false);
				if (jobj.has("msg") && !jobj.isNull("msg"))
					ToastUtil.showToastBottomShort(act, jobj.getString("msg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			showMain(false);
		}
	}

	private void filterData(String filterstr) {
		List<SortModel> filterDataList = new ArrayList<SortModel>();
		if (TextUtils.isEmpty(filterstr)) {
			filterDataList = petlist;
		} else {
			filterDataList.clear();
			for (SortModel model : petlist) {
				String name = model.getName();
				if (!model.isHot()
						&& (name.indexOf(filterstr.toString()) != -1 || characterParser
								.getSelling(name).startsWith(
										filterstr.toString()))) {
					filterDataList.add(model);
				}
			}
		}
		Collections.sort(filterDataList, pinyinComparator);
		petAdapter.updateListView(filterDataList);
	}

}
