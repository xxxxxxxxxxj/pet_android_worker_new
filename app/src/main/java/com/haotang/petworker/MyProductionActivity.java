package com.haotang.petworker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.petworker.adapter.MyProductionAdapter;
import com.haotang.petworker.entity.Production;
import com.haotang.petworker.event.RefreshProdictionEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProductionActivity extends SuperActivity {
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.rv_production_list)
    RecyclerView rvProductionList;
    @BindView(R.id.srl_production_list)
    SwipeRefreshLayout srlProductionList;
    @BindView(R.id.btn_production_add)
    Button btnProductionAdd;
    @BindView(R.id.ll_noproduction)
    LinearLayout llNoproduction;
    private MProgressDialog pDialog;
    private int pageSize = 10;
    private int beforeid;
    private ArrayList<Production> list = new ArrayList<Production>();
    private ArrayList<Production> listTemp = new ArrayList<Production>();
    private MyProductionAdapter myProductionAdapter;

    //刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshProdictionEvent event) {
        if (event != null && event.isRefresh()) {
            refresh();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        setView();
        setLinstener();
        refresh();
    }

    private void setLinstener() {
        myProductionAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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
        myProductionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyProductionActivity.this, ProductionDetailActivity.class);
                intent.putExtra("name", list.get(position).title);
                intent.putExtra("time", list.get(position).time);
                intent.putExtra("id", list.get(position).id);
                intent.putExtra("bigImg", list.get(position).image);
                intent.putExtra("audit",list.get(position).auditState);
                startActivity(intent);
            }
        });
    }

    private void loadMore() {
        getData();
    }

    private void refresh() {
        myProductionAdapter.setEnableLoadMore(false);
        srlProductionList.setRefreshing(true);
        beforeid = 0;
        list.clear();
        listTemp.clear();
        myProductionAdapter.notifyDataSetChanged();
        getData();
    }

    private void getData() {
        pDialog.showDialog();
        CommUtil.getProduction(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this),
                spUtil.getInt("wuserid", 0), 0, beforeid, 0,
                pageSize, productionHanler);
    }

    private AsyncHttpResponseHandler productionHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.dismiss();
            listTemp.clear();
            try {
                String msg = "";
                int resultCode = 0;
                JSONArray jdata = null;
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj.has("code") && !jobj.isNull("code")) {
                    resultCode = jobj.getInt("code");
                }
                if (jobj.has("data") && !jobj.isNull("data")) {
                    jdata = jobj.getJSONArray("data");
                }
                if (jobj.has("msg") && !jobj.isNull("msg")) {
                    msg = jobj.getString("msg");
                }
                if (0 == resultCode) {
                    if (jdata != null && jdata.length() > 0) {
                        for (int i = 0; i < jdata.length(); i++) {
                            listTemp.add(Production.json2Entity(jdata
                                    .getJSONObject(i)));
                        }
                    }else {
                        if (beforeid == 0) {
                            myProductionAdapter.setEmptyView(setEmptyViewBase(2, "您还没有已发布的作品~", R.drawable.icon_noproduction, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }));
                            myProductionAdapter.setEnableLoadMore(false);
                            srlProductionList.setRefreshing(false);
                        }
                    }
                    if (beforeid == 0) {
                        srlProductionList.setRefreshing(false);
                        myProductionAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    myProductionAdapter.loadMoreComplete();
                    if (listTemp != null && listTemp.size() > 0) {
                        if (listTemp.size() < pageSize) {
                            myProductionAdapter.loadMoreEnd(false);
                        }
                        list.addAll(listTemp);
                        beforeid = list.get(list.size() - 1).id;
                    } else {
                        if (beforeid == 0) {
                            myProductionAdapter.loadMoreEnd(true);
                        } else {
                            myProductionAdapter.loadMoreEnd(false);
                        }
                    }
                    myProductionAdapter.notifyDataSetChanged();
                } else {
                    if (beforeid == 0) {
                        myProductionAdapter.setEmptyView(setEmptyViewBase(1, msg, R.drawable.icon_noproduction,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refresh();
                                    }
                                }));
                        myProductionAdapter.setEnableLoadMore(false);
                        srlProductionList.setRefreshing(false);
                    } else {
                        myProductionAdapter.setEnableLoadMore(true);
                        myProductionAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (beforeid == 0) {
                    myProductionAdapter.setEmptyView(setEmptyViewBase(1, "数据异常", R.drawable.icon_noproduction, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    }));
                    myProductionAdapter.setEnableLoadMore(false);
                    srlProductionList.setRefreshing(false);
                } else {
                    myProductionAdapter.setEnableLoadMore(true);
                    myProductionAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            myProductionAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.dismiss();
            if (beforeid == 0) {
                myProductionAdapter.setEmptyView(setEmptyViewBase(1, "请求失败", R.drawable.icon_noproduction, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                }));
                myProductionAdapter.setEnableLoadMore(false);
                srlProductionList.setRefreshing(false);
            } else {
                myProductionAdapter.setEnableLoadMore(true);
                myProductionAdapter.loadMoreFail();
            }
        }
    };

    private void setView() {
        setContentView(R.layout.activity_my_production);
        ButterKnife.bind(this);
        tvTitlebarTitle.setText("我的作品");
        pDialog = new MProgressDialog(this);
        myProductionAdapter = new MyProductionAdapter(R.layout.item_myproduction, list);
        myProductionAdapter.setEnableLoadMore(false);
        rvProductionList.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(MyProductionActivity.this, 2);
        rvProductionList.setLayoutManager(layoutManager);
        rvProductionList.setAdapter(myProductionAdapter);
        srlProductionList.setColorSchemeColors(Color.rgb(47, 223, 189));
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

    @OnClick({R.id.ib_titlebar_back, R.id.btn_production_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.btn_production_add:
                Intent intent = new Intent(MyProductionActivity.this, AddProductionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
