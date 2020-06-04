package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.adapter.NoticeAdapter;
import com.haotang.petworker.entity.Notice;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.pulltorefresh.PullToRefreshBase;
import com.haotang.petworker.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.petworker.pulltorefresh.PullToRefreshListView;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends SuperActivity {

	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshListView listView_notice;
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;
	private List<Notice> noticeList = new ArrayList<Notice>();
	private NoticeAdapter noticeAdapter;
	private int page = 1;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice);
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		noticeList.clear();
		findView();
		setView();
		getData(0);
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

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		listView_notice = (PullToRefreshListView) findViewById(R.id.listView_notice);
		no_has_data = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
	}

	private void setView() {
		tvTitle.setText("公告通知");
		noticeAdapter = new NoticeAdapter(this, noticeList);
		listView_notice.setMode(PullToRefreshBase.Mode.BOTH);
		listView_notice
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						PullToRefreshBase.Mode mode = refreshView
								.getCurrentMode();
						if (mode == Mode.PULL_FROM_START) {
							noticeList.clear();
							noticeAdapter.notifyDataSetChanged();
							getData(0);
						} else {
							getData(noticeList.get(noticeList.size() - 1).AnnouncementId);
						}
					}
				});
		listView_notice.setAdapter(noticeAdapter);
		initListener();
	}

	private void getData(long before) {
		pDialog.showDialog();
		CommUtil.queryAnnouncement(spUtil.getString("wcellphone", ""),
				Global.getIMEI(this), this, before, 10, queryAnnouncement);
	}

	private AsyncHttpResponseHandler queryAnnouncement = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			listView_notice.onRefreshComplete();
			pDialog.closeDialog();
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONArray array = jsonObject.getJSONArray("data");
						if (array.length() > 0) {
							for (int i = 0; i < array.length(); i++) {
								noticeList.add(Notice.json2Entity(array
										.getJSONObject(i)));
							}
						}
						for (int i = 0; i < noticeList.size(); i++) {
							Utils.mLogError("==-->noticeList "
									+ noticeList.get(i).title);
						}
						showMain(true);
						noticeAdapter.notifyDataSetChanged();
					} else {
						if (noticeList.size() <= 0) {
							showMain(false);
						}
						noticeAdapter.notifyDataSetChanged();
					}
				} else {
					if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
						ToastUtil.showToastCenterShort(NoticeActivity.this,
								jsonObject.getString("msg"));
					}
					if (noticeList.size() <= 0) {
						showMain(false);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtil.showToastBottomShort(NoticeActivity.this,
						"网络连接失败，请检查您的网络");
				if (noticeList.size() <= 0) {
					showMain(false);
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			listView_notice.onRefreshComplete();
			pDialog.closeDialog();
			ToastUtil.showToastBottomShort(NoticeActivity.this,
					"网络连接失败，请检查您的网络");
			if (noticeList.size() <= 0) {
				showMain(false);
			}
		}

	};

	private void initListener() {
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
		listView_notice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Notice notice = (Notice) parent.getItemAtPosition(position);
				Intent intent = new Intent(NoticeActivity.this,
						NoticeDetailActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				Bundle bundle = new Bundle();
				bundle.putSerializable("notice", notice);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
	}

	private void showMain(boolean ismain) {
		if (ismain) {
			listView_notice.setVisibility(View.VISIBLE);
			no_has_data.setVisibility(View.GONE);
		} else {
			listView_notice.setVisibility(View.GONE);
			no_has_data.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("暂时还没有公告");
		}
	}
}
