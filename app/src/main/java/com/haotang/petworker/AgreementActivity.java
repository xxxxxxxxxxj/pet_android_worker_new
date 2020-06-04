package com.haotang.petworker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.view.AdvancedWebView;
import com.umeng.analytics.MobclickAgent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgreementActivity extends SuperActivity implements
        AdvancedWebView.Listener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private AdvancedWebView wv_adbanner;
    private String url;
    private String content;
    private TextView tv_agree_wenan;
    private RelativeLayout rl_titlebar;
    private MProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adbanner);
        mDialog = new MProgressDialog(this);
        mDialog.showDialog();
        long time = System.currentTimeMillis();
        String pre = getIntent().getStringExtra("pre");
        url = getIntent().getStringExtra("url");
        content = getIntent().getStringExtra("content");
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        if (pre != null && pre.equals("time")) {
            url = "http://gg.cwjia.cn/cwj/gz?time=" + time;
            tvTitle.setText("工作日设置规则");
        } else if (pre != null && pre.equals("service")) {
            url = "http://gg.cwjia.cn/cwj/gz1?time=" + time;
            tvTitle.setText("服务方式设置规则");
        } else {
            tvTitle.setText("美容师公告");
        }
        Log.e("TAG", "url = " + url);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        wv_adbanner = (AdvancedWebView) findViewById(R.id.wv_adbanner);
        tv_agree_wenan = (TextView) findViewById(R.id.tv_agree_wenan);
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        initWebView();
        if (url != null && !TextUtils.isEmpty(url)) {
            if (url.contains("backaction=1")) {
                rl_titlebar.setVisibility(View.GONE);
            } else {
                rl_titlebar.setVisibility(View.VISIBLE);
            }
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = CommUtil.getStaticUrl() + url;
            }
            if (url.contains("?")) {
                url = url
                        + "&system="
                        + CommUtil.getSource()
                        + "_"
                        + Global.getCurrentVersion(this)
                        + "&imei="
                        + Global.getIMEI(this)
                        + "&cellPhone="
                        + SharedPreferenceUtil.getInstance(this).getString(
                        "wcellphone", "") + "&phoneModel="
                        + android.os.Build.BRAND + " " + android.os.Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + android.os.Build.VERSION.RELEASE + "&time="
                        + System.currentTimeMillis();
            } else {
                url = url
                        + "?system="
                        + CommUtil.getSource()
                        + "_"
                        + Global.getCurrentVersion(this)
                        + "&imei="
                        + Global.getIMEI(this)
                        + "&cellPhone="
                        + SharedPreferenceUtil.getInstance(this).getString(
                        "wcellphone", "") + "&phoneModel="
                        + android.os.Build.BRAND + " " + android.os.Build.MODEL
                        + "&phoneSystemVersion=" + "Android "
                        + android.os.Build.VERSION.RELEASE + "&time="
                        + System.currentTimeMillis();
            }
            Log.e("TAG", "url = " + url);
            wv_adbanner.setVisibility(View.VISIBLE);
            tv_agree_wenan.setVisibility(View.GONE);
            wv_adbanner.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                }

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        Log.e("TAG", "wv_adbanner.getUrl() = " + wv_adbanner.getUrl());
                        Log.e("TAG", "wv_adbanner.getTitle() = " + wv_adbanner.getTitle());
                        if (wv_adbanner.getUrl().contains("backaction=1")) {
                            rl_titlebar.setVisibility(View.GONE);
                        } else {
                            rl_titlebar.setVisibility(View.VISIBLE);
                        }
                        tvTitle.setText(wv_adbanner.getTitle());
                    }
                }
            });
            wv_adbanner.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return shouldOverrideUrlBypet(view, url);
                }
            });
            wv_adbanner.loadUrl(url);
        } else if (content != null && !TextUtils.isEmpty(content)) {
            wv_adbanner.setVisibility(View.GONE);
            tv_agree_wenan.setVisibility(View.VISIBLE);
            tv_agree_wenan.setText(content);
        }
        ibBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });
        wv_adbanner.addJavascriptInterface(new JsObject1() {
            @JavascriptInterface
            public void goback() {
                Log.e("TAG", "goback()");
                back();
            }
        }, "fromh5lucy");
    }

    class JsObject1 {
        @JavascriptInterface
        public String toString() {
            return "fromh5lucy";
        }
    }

    // 初始化WebView配置
    protected void initWebView() {
        // mWebView.setDesktopMode(true);
        wv_adbanner.setListener(this, this);
        wv_adbanner.setGeolocationEnabled(false);
        wv_adbanner.setMixedContentAllowed(true);
        wv_adbanner.setCookiesEnabled(true);
        wv_adbanner.setThirdPartyCookiesEnabled(true);
    }

    protected boolean shouldOverrideUrlBypet(WebView view, String url) {
        mDialog.showDialog();
        if (TextUtils.isEmpty(url)) {
            return true;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        } else if (url.startsWith("sms:")) {
            String regex = "sms:([\\d]*?)\\?body=([\\w\\W]*)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(Uri.decode(url).replaceAll(" ", ""));
            if (m.find()) {
                String tel = m.group(1);
                String body = m.group(2);
                Uri smsto = Uri.parse("smsto:" + tel);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsto);
                sendIntent.putExtra("sms_body", body);
                startActivity(sendIntent);
                return true;
            }
        } else if (url.startsWith("tel:")) {
            Intent telIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            telIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(telIntent);
            return true;
        } else if (url.startsWith("mailto:")) {
            MailTo mailTo = MailTo.parse(url);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo.getTo()});
            intent.putExtra(Intent.EXTRA_CC, mailTo.getCc());
            intent.putExtra(Intent.EXTRA_TEXT, mailTo.getBody());
            intent.putExtra(Intent.EXTRA_SUBJECT, mailTo.getSubject());
            intent.setPackage("com.android.email");
            intent.setType("text/plain");
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(intent);
            return true;
        } else if (url.startsWith("intent:")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        wv_adbanner.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        wv_adbanner.onPause();
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        wv_adbanner.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        wv_adbanner.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageFinished(String url) {
        mDialog.closeDialog();
        wv_adbanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Toast.makeText(
                this,
                "onPageError(errorCode = " + errorCode + ",  description = "
                        + description + ",  failingUrl = " + failingUrl + ")",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadRequested(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
        Toast.makeText(
                this,
                "onDownloadRequested(url = " + url + ",  userAgent = "
                        + userAgent + ",  contentDisposition = "
                        + contentDisposition + ",  mimetype = " + mimetype
                        + ",  contentLength = " + contentLength + ")",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onExternalPageRequest(String url) {
        Toast.makeText(this, "onExternalPageRequest(url = " + url + ")",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            back();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void back() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (wv_adbanner.canGoBack()) {
                    wv_adbanner.goBack();
                } else {
                    setResult(Global.RESULT_OK);
                    finish();
                }
            }
        });
    }
}
