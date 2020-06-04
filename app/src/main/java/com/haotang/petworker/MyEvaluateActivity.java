package com.haotang.petworker;

import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.petworker.adapter.MyEvaluateAdapter;
import com.haotang.petworker.entity.Comment;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyEvaluateActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_evaluate_all)
    TextView tvEvaluateAll;
    @BindView(R.id.tv_evaluate_good)
    TextView tvEvaluateGood;
    @BindView(R.id.tv_evaluate_middle)
    TextView tvEvaluateMiddle;
    @BindView(R.id.tv_evaluate_bad)
    TextView tvEvaluateBad;
    @BindView(R.id.rv_evaluate_list)
    RecyclerView rvProductionList;
    @BindView(R.id.srl_evaluate_list)
    SwipeRefreshLayout srlProductionList;
    private MProgressDialog pDialog;
    private SharedPreferenceUtil spUtil;
    private int type = -1;
    private int page = 1;
    private ArrayList<Comment> localList = new ArrayList<Comment>();
    private ArrayList<Comment> list = new ArrayList<Comment>();
    private MyEvaluateAdapter adapter;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        refresh();
        setLinstener();
    }

    private void setLinstener() {
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srlProductionList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        adapter.setEnableLoadMore(false);
        srlProductionList.setRefreshing(true);
        page = 1;
        list.clear();
        adapter.notifyDataSetChanged();
        getData();
    }

    private void loadMore() {
        getData();
    }

    private void getData() {
        pDialog.showDialog();
        CommUtil.getComments(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), page,
                pageSize, type, commentHanler);
    }

    /*private AsyncHttpResponseHandler commentHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            srlProductionList.setRefreshing(false);
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");

                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jData = jobj.getJSONObject("data");
                    if (jData.has("totalComment")
                            && !jData.isNull("totalComment")) {
                    }
                    if (page == 1) {
                        srlProductionList.setRefreshing(false);
                        adapter.setEnableLoadMore(true);
                        commentList.clear();
                    }
                    if (jData.has("comments") && !jData.isNull("comments")) {
                        JSONArray commentArr = jData.getJSONArray("comments");
                        for (int i = 0; i < commentArr.length(); i++) {
                            Comment comment = Comment.json2Entity(commentArr
                                    .getJSONObject(i));
                            commentList.add(comment);
                        }
                        if (commentArr.length() > 0) {
                            page++;
                        }
                    }
                    adapter.loadMoreComplete();
                    if (commentList != null && commentList.size() > 0) {
                        if (page == 1) {
                            page = commentList.size();
                        } else {
                            if (commentList.size() < page) {
                                adapter.loadMoreEnd(false);
                            }
                        }
                        page++;
                    }else {
                        if (page == 1) {
                            adapter.setEmptyView(setEmptyViewBase(2, "暂无评价～",
                                    R.drawable.icon_noproduction, null));
                            adapter.loadMoreEnd(true);
                        } else {
                            adapter.loadMoreEnd(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg")) {
                        String msg = jobj.getString("msg");
                        ToastUtil.showToastCenterShort(
                                MyEvaluateActivity.this, msg);
                    }
                }
                //showNull();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ToastUtil.showToastCenterShort(
                        MyEvaluateActivity.this, "网络异常，请下拉重新加载");
                //showNull();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            //prlList.onRefreshComplete();
            pDialog.closeDialog();
            ToastUtil.showToastCenterShort(MyEvaluateActivity.this,
                    "网络异常，请下拉重新加载");
            //showNull();
        }

    };*/
    private AsyncHttpResponseHandler commentHanler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            pDialog.closeDialog();
            localList.clear();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0 ) {
                    JSONObject jData = jobj.getJSONObject("data");
                    if (jData.has("comments") && !jData.isNull("comments")) {
                        JSONArray commentArr = jData.getJSONArray("comments");
                        for (int i = 0; i < commentArr.length(); i++) {
                            Comment comment = Comment.json2Entity(commentArr
                                    .getJSONObject(i));
                            localList.add(comment);
                        }
                    }
                    if (page == 1) {
                        srlProductionList.setRefreshing(false);
                        adapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    adapter.loadMoreComplete();
                    if (localList != null && localList.size() > 0) {
                        if (page == 1) {
                            pageSize = localList.size();
                        } else {
                            if (localList.size() < pageSize) {
                                adapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(localList);
                        page++;
                    } else {
                        if (page == 1) {
                            adapter.setEmptyView(setEmptyViewBase(2, "暂无数据~",
                                    R.drawable.icon_noproduction, null));
                            adapter.loadMoreEnd(true);
                        } else {
                            adapter.loadMoreEnd(false);
                        }
                    }
                } else {
                    if (page == 1) {
                        adapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_noproduction,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        adapter.setEnableLoadMore(false);
                        srlProductionList.setRefreshing(false);
                    } else {
                        adapter.setEnableLoadMore(true);
                        adapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    adapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_noproduction, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    adapter.setEnableLoadMore(false);
                    srlProductionList.setRefreshing(false);
                } else {
                    adapter.setEnableLoadMore(true);
                    adapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            if (page == 1) {
                adapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_noproduction, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                adapter.setEnableLoadMore(false);
                srlProductionList.setRefreshing(false);
            } else {
                adapter.setEnableLoadMore(true);
                adapter.loadMoreFail();
            }
        }
    };

    private void setView() {
        setContentView(R.layout.activity_my_evaluate);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("我的评价");
        pDialog = new MProgressDialog(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
        adapter = new MyEvaluateAdapter(R.layout.item_evaluate, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyEvaluateActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvProductionList.setHasFixedSize(true);
        srlProductionList.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvProductionList.setLayoutManager(layoutManager);
        rvProductionList.setAdapter(adapter);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_evaluate_all, R.id.tv_evaluate_good, R.id.tv_evaluate_middle, R.id.tv_evaluate_bad})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_evaluate_all:
                type = -1;
                page = 1;
                tvEvaluateAll.setBackgroundResource(R.drawable.bg_red_round14);
                tvEvaluateGood.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateBad.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateMiddle.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateAll.setTextColor(getResources().getColor(R.color.white));
                tvEvaluateMiddle.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateBad.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateGood.setTextColor(getResources().getColor(R.color.a717985));
                refresh();
                break;
            case R.id.tv_evaluate_good:
                type = 2;
                page = 1;
                tvEvaluateAll.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateGood.setBackgroundResource(R.drawable.bg_red_round14);
                tvEvaluateBad.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateMiddle.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateAll.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateMiddle.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateBad.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateGood.setTextColor(getResources().getColor(R.color.white));
                refresh();
                break;
            case R.id.tv_evaluate_middle:
                type = 1;
                page = 1;
                tvEvaluateAll.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateGood.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateBad.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateMiddle.setBackgroundResource(R.drawable.bg_red_round14);
                tvEvaluateAll.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateMiddle.setTextColor(getResources().getColor(R.color.white));
                tvEvaluateBad.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateGood.setTextColor(getResources().getColor(R.color.a717985));
                refresh();
                break;
            case R.id.tv_evaluate_bad:
                type = 0;
                page = 1;
                tvEvaluateAll.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateGood.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateBad.setBackgroundResource(R.drawable.bg_red_round14);
                tvEvaluateMiddle.setBackgroundResource(R.drawable.bg_white_round14);
                tvEvaluateAll.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateMiddle.setTextColor(getResources().getColor(R.color.a717985));
                tvEvaluateBad.setTextColor(getResources().getColor(R.color.white));
                tvEvaluateGood.setTextColor(getResources().getColor(R.color.a717985));
                refresh();
                break;
        }
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
}
