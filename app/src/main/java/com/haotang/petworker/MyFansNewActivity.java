package com.haotang.petworker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.petworker.adapter.MyFansAdapter;
import com.haotang.petworker.entity.MyFans;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.pulltorefresh.PullToRefreshBase;
import com.haotang.petworker.pulltorefresh.PullToRefreshListView;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 姜谷蓄
 * 我的粉丝新版页面
 */

public class MyFansNewActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.prl_fans_list)
    PullToRefreshListView prlFansList;
    private TextView tvEmptyviewDesc;
    private Button btnEmptyview;
    private TextView tvFansTotalnum;
    private TextView tvFansNum;
    private TextView tvFansBuynum;
    private TextView tvFansContent;
    private LinearLayout ll_order_default;
    private LinearLayout ll_fans_bottom;
    private MyFansAdapter myFansAdapter;
    private List<MyFans> myFansLists = new ArrayList<>();
    private int page = 1;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        setView();
        getData();
    }

    private void getData() {
        getFansList();
        getTopData();
    }

    private void getFansList() {
        mPDialog.showDialog();
        CommUtil.workerFansList(mContext, page, handler);
    }

    private void setView() {
        setContentView(R.layout.activity_my_fans_new);
        ButterKnife.bind(this);

        btTitlebarOther.setVisibility(View.GONE);
        tvTitlebarTitle.setText("我的粉丝");
        myFansAdapter = new MyFansAdapter(mContext, myFansLists);
        prlFansList.setMode(PullToRefreshBase.Mode.BOTH);

        header = LayoutInflater.from(this).inflate(
                R.layout.header_myfans_layout, null);
        tvFansTotalnum = header.findViewById(R.id.tv_fans_totalnum);
        tvFansContent = header.findViewById(R.id.tv_fans_content);
        tvFansNum = header.findViewById(R.id.tv_fans_num);
        tvFansBuynum = header.findViewById(R.id.tv_fans_buynum);
        ll_order_default =header.findViewById(R.id.ll_order_default);
        ll_fans_bottom = header.findViewById(R.id.ll_fans_bottom);
        tvEmptyviewDesc = header.findViewById(R.id.tv_emptyview_desc);
        btnEmptyview = header.findViewById(R.id.btn_emptyview);
        prlFansList.getRefreshableView().addHeaderView(header);
        prlFansList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshBase refreshView) {
                PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
                if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
                    // 下拉刷新
                    page = 1;
                    myFansLists.clear();
                    getTopData();
                }
                getFansList();
            }
        });
        prlFansList.setAdapter(myFansAdapter);
    }

    private void setLayout(int type, int flag, String msg) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (type == 1) {
            ll_order_default.setVisibility(View.GONE);
        } else if (type == 2) {
            ll_order_default.setVisibility(View.VISIBLE);
            ll_fans_bottom.setLayoutParams(layoutParams);
            if (flag == 1) {
                btnEmptyview.setVisibility(View.VISIBLE);
                btnEmptyview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFansList();
                    }
                });
            } else if (flag == 2) {
                btnEmptyview.setVisibility(View.GONE);
            }
            Utils.setText(tvEmptyviewDesc, msg, "", View.VISIBLE, View.VISIBLE);
        }
    }

    private void getTopData() {
        mPDialog.showDialog();
        CommUtil.workerFansInfo(mContext, fansHandler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            prlFansList.onRefreshComplete();
            try {
                JSONObject jsonobj = new JSONObject(new String(responseBody));
                int code = jsonobj.getInt("code");
                if (code == 0) {
                    if (jsonobj.has("data") && !jsonobj.isNull("data")) {
                        JSONObject obj = jsonobj.getJSONObject("data");
                        if (obj.has("fans") && !obj.isNull("fans")) {
                            JSONArray array = obj.getJSONArray("fans");
                            if (array.length() > 0) {
                                page++;
                                for (int i = 0; i < array.length(); i++) {
                                    myFansLists.add(MyFans.j2Entity(array.getJSONObject(i)));
                                }
                            }
                        }
                        if (page==1&&myFansLists.size()==0){
                            setLayout(2,2,"暂无粉丝");
                        }
                    }
                }else {
                    setLayout(2,1,jsonobj.getString("msg"));
                }
                if (myFansLists.size() > 0) {
                    myFansAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            setLayout(2,1,"请求失败");
        }
    };

    private AsyncHttpResponseHandler fansHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject obj = object.getJSONObject("data");
                        if (obj.has("fansCount") && !obj.isNull("fansCount")) {
                            int fansCount = obj.getInt("fansCount");
                            tvFansTotalnum.setText(fansCount + "");
                        }
                        if (obj.has("fansContent") && !obj.isNull("fansContent")) {
                            String fansContent = obj.getString("fansContent");
                            tvFansContent.setText(fansContent + "");
                        }
                        if (obj.has("vipFansCount") && !obj.isNull("vipFansCount")) {
                            int vipFansCount = obj.getInt("vipFansCount");
                            tvFansNum.setText(vipFansCount + "");
                        }
                        if (obj.has("isBuyFansCount") && !obj.isNull("isBuyFansCount")) {
                            int isBuyFansCount = obj.getInt("isBuyFansCount");
                            tvFansBuynum.setText(isBuyFansCount + "");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    @OnClick(R.id.ib_titlebar_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
    }
}
