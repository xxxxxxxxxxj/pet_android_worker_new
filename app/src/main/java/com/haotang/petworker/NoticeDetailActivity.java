package com.haotang.petworker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.petworker.entity.Notice;
import com.haotang.petworker.pulltorefresh.PullToRefreshBase;
import com.haotang.petworker.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.petworker.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.petworker.pulltorefresh.PullToRefreshWebView;
import com.umeng.analytics.MobclickAgent;

public class NoticeDetailActivity extends SuperActivity {

	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshWebView webView_show_notice;
	// private long AnnouncementId;
	private String url;
	private WebView mWebView;
	private TextView tv_noticedetail_wenan;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_detail);
		findView();
		getSerializable();
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

	private void getSerializable() {
		Bundle bundle = getIntent().getExtras();
		Notice notice = (Notice) bundle.getSerializable("notice");
		if (notice != null) {
			tvTitle.setText("通知详情");
			url = notice.url;
			content = notice.content;
			if (content != null && !TextUtils.isEmpty(content)) {
				tvTitle.setText("美容师公告");
			}
		} else {
			tvTitle.setText("等级规则");
			url = getIntent().getStringExtra("h5url");
		}
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		tv_noticedetail_wenan = (TextView) findViewById(R.id.tv_noticedetail_wenan);
		webView_show_notice = (PullToRefreshWebView) findViewById(R.id.webView_show_notice);
	}

	private void setView() {
		initlistener();
		if (url != null && !TextUtils.isEmpty(url)) {
			webView_show_notice.setVisibility(View.VISIBLE);
			tv_noticedetail_wenan.setVisibility(View.GONE);
			webView_show_notice.setMode(Mode.PULL_FROM_START);
			mWebView = webView_show_notice.getRefreshableView();
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onReceivedTitle(WebView view, String title) {
					NoticeDetailActivity.this.onReceivedTitle(view, title);
				}
			});
			mWebView.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					view.loadUrl(url);
					// return super.shouldOverrideUrlLoading(view, url);
					return true;
				}

			});
			mWebView.loadUrl(url);
			webView_show_notice
					.setOnRefreshListener(new OnRefreshListener<WebView>() {

						@Override
						public void onRefresh(
								PullToRefreshBase<WebView> refreshView) {
							// TODO Auto-generated method stub
							Mode mode = refreshView.getCurrentMode();
							if (mode == Mode.PULL_FROM_START) {
								mWebView.loadUrl(url);
							}
						}
					});
		}else if (content != null && !TextUtils.isEmpty(content)) {
			webView_show_notice.setVisibility(View.GONE);
			tv_noticedetail_wenan.setVisibility(View.VISIBLE);
			tv_noticedetail_wenan.setText(content);
		}
	}

	private void initlistener() {
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
	}

	protected void onReceivedTitle(WebView view, String title) {
		tvTitle.setText(title);
	}
}
