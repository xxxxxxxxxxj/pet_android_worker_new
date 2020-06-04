package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.petworker.adapter.AreaFeedBackAdapter;
import com.haotang.petworker.entity.FeedBackList;
import com.haotang.petworker.event.RefreshProdictionEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.MProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AreaFeedbackHistoryActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_history_list)
    RecyclerView rvHistoryList;
    protected MProgressDialog mPDialog;
    @BindView(R.id.ll_nohistory)
    LinearLayout llNohistory;
    private List<FeedBackList> feedLists = new ArrayList<>();
    private AreaFeedBackAdapter feedBackAdapter;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        getData();
    }

    private void initData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.myFeedBackList(this, type, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {


                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject obj = object.getJSONObject("data");
                        if (obj.has("requestTime")&&!obj.isNull("requestTime")){
                            EventBus.getDefault().post(new RefreshProdictionEvent(true));
                            switch (type){
                                case 0:
                                    spUtil.saveString("lastRequestTimeType0",obj.getString("requestTime"));
                                    break;
                                case 1:
                                    spUtil.saveString("lastRequestTimeType1",obj.getString("requestTime"));
                                    break;
                                case 2:
                                    spUtil.saveString("lastRequestTimeType2",obj.getString("requestTime"));
                                    break;
                            }
                        }
                        if (obj.has("editModule") && !obj.isNull("editModule")) {
                            FeedBackList feedBackList = new FeedBackList();
                            JSONObject objEdit = obj.getJSONObject("editModule");
                            if (objEdit.has("buttonText") && !objEdit.isNull("buttonText")) {
                                String buttonText = objEdit.getString("buttonText");
                                feedBackList.buttonText = buttonText;
                            }
                            if (objEdit.has("integralText") && !objEdit.isNull("integralText")) {
                                String integralText = objEdit.getString("integralText");
                                feedBackList.integralText = integralText;
                            }
                            if (objEdit.has("nowDate") && !objEdit.isNull("nowDate")) {
                                String nowDate = objEdit.getString("nowDate");
                                feedBackList.nowDate = nowDate;
                            }
                            //feedLists.add(feedBackList);
                        }
                        if (obj.has("feedBackList") && !obj.isNull("feedBackList")) {
                            JSONArray array = obj.getJSONArray("feedBackList");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    feedLists.add(FeedBackList.j2Entity(array.getJSONObject(i)));
                                }
                            }else {
                                rvHistoryList.setVisibility(View.GONE);
                                llNohistory.setVisibility(View.VISIBLE);
                            }
                            feedBackAdapter.notifyDataSetChanged();
                        }else {
                            llNohistory.setVisibility(View.VISIBLE);
                            rvHistoryList.setVisibility(View.GONE);
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

    private void setView() {
        setContentView(R.layout.activity_area_feedback_history);
        ButterKnife.bind(this);
        if (type == 0) {
            tvTitlebarTitle.setText("区域建议记录");
        } else if (type == 1) {
            tvTitlebarTitle.setText("评价店长历史");
        } else {
            tvTitlebarTitle.setText("应聘店长历史");
        }
        mPDialog = new MProgressDialog(this);
        feedBackAdapter = new AreaFeedBackAdapter(R.layout.item_feedback_history, feedLists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistoryList.setLayoutManager(layoutManager);
        rvHistoryList.setAdapter(feedBackAdapter);
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
