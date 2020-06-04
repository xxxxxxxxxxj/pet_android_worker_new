package com.haotang.petworker.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.petworker.OrderActivity;
import com.haotang.petworker.R;
import com.haotang.petworker.adapter.OrderNewAdapter;
import com.haotang.petworker.entity.Order;
import com.haotang.petworker.entity.OrderTag;
import com.haotang.petworker.event.AddArchivesEvent;
import com.haotang.petworker.event.OrderCompleteEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-08-30 10:53
 */
public class OrderNewFragment extends Fragment {
    private int index;
    private ArrayList<Order> list = new ArrayList<Order>();
    private ArrayList<Order> listTemp = new ArrayList<Order>();
    private OrderNewAdapter orderAdapter;
    private int page = 1;
    private double lat;
    private double lng;
    private MProgressDialog pDialog;
    private SwipeRefreshLayout srl_order_list;
    private RecyclerView rv_order_list;
    private int pageSize;
    private TextView tv_emptyview_desc;
    private Button btn_emptyview;
    private LinearLayout ll_order_default;
    private StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddArchivesEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).orderid == event.getOrderId()) {
                        list.get(i).carecount++;
                        break;
                    }
                }
                if (orderAdapter != null) {
                    orderAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OrderCompleteEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null) {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).orderid == event.getOrderId()) {
                        list.get(i).status = 4;
                        list.get(i).statusdes = "服务完成";
                        list.get(i).integral = "填写护理记录可得";
                        list.get(i).integralCopy = "0.5积分";
                        break;
                    }
                }
                if (orderAdapter != null) {
                    orderAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ordernew_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        findView(view);
        setView();
        initListener();
        refresh();
    }

    private void initListener() {
        orderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        srl_order_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void loadMore() {
        getData();
    }

    private void refresh() {
        OrderActivity orderActivity = (OrderActivity) getActivity();
        orderActivity.getData();
        orderAdapter.setEnableLoadMore(false);
        srl_order_list.setRefreshing(true);
        page = 1;
        getData();
    }

    private void getData() {
        pDialog.showDialog();
        CommUtil.orderList(getActivity(), index, lat, lng, page, listHandler);
    }

    private AsyncHttpResponseHandler listHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.dismiss();
            listTemp.clear();
            try {
                String msg = "";
                int resultCode = 0;
                JSONObject jdata = null;
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj.has("code") && !jobj.isNull("code")) {
                    resultCode = jobj.getInt("code");
                }
                if (jobj.has("data") && !jobj.isNull("data")) {
                    jdata = jobj.getJSONObject("data");
                }
                if (jobj.has("msg") && !jobj.isNull("msg")) {
                    msg = jobj.getString("msg");
                }
                if (0 == resultCode) {
                    if (jdata != null) {
                        if (jdata.has("orders") && !jdata.isNull("orders")) {
                            JSONArray jarr = jdata.getJSONArray("orders");
                            if (jarr.length() > 0) {
                                for (int i = 0; i < jarr.length(); i++) {
                                    listTemp.add(Order.json2Entity(jarr
                                            .getJSONObject(i)));
                                }
                                for (int i = 0; i < listTemp.size(); i++) {
                                    listTemp.get(i).orderTagList.add(new OrderTag(0, listTemp.get(i).addrtype));
                                    if (Utils.isStrNull(listTemp.get(i).firstorderflag)) {
                                        listTemp.get(i).orderTagList.add(new OrderTag(1, listTemp.get(i).firstorderflag));
                                    }
                                    if (Utils.isStrNull(listTemp.get(i).updateDescription)) {
                                        listTemp.get(i).orderTagList.add(new OrderTag(2, listTemp.get(i).updateDescription));
                                    }
                                    if (Utils.isStrNull(listTemp.get(i).pickUpTag)) {
                                        listTemp.get(i).orderTagList.add(new OrderTag(3, listTemp.get(i).pickUpTag));
                                    }
                                    if (listTemp.get(i).refType == 4) {
                                        listTemp.get(i).orderTagList.add(new OrderTag(4, "寄养"));
                                    }
                                }
                            }
                        }
                    }
                    if (page == 1) {
                        srl_order_list.setRefreshing(false);
                        orderAdapter.setEnableLoadMore(true);
                        list.clear();
                    }
                    orderAdapter.loadMoreComplete();
                    if (listTemp != null && listTemp.size() > 0) {
                        setLayout(1, 0, "");
                        if (page == 1) {
                            pageSize = listTemp.size();
                        } else {
                            if (listTemp.size() < pageSize) {
                                orderAdapter.loadMoreEnd(false);
                            }
                        }
                        list.addAll(listTemp);
                        page++;
                    } else {
                        if (page == 1) {
                            Log.e("TAG", "暂无订单list = " + list.size());
                            setLayout(2, 2, "暂无订单～");
                            orderAdapter.loadMoreEnd(true);
                        } else {
                            setLayout(1, 0, "");
                            orderAdapter.loadMoreEnd(false);
                        }
                    }
                    orderAdapter.notifyDataSetChanged();
                } else {
                    if (page == 1) {
                        setLayout(2, 1, msg);
                        orderAdapter.setEnableLoadMore(false);
                        srl_order_list.setRefreshing(false);
                    } else {
                        orderAdapter.setEnableLoadMore(true);
                        orderAdapter.loadMoreFail();
                    }
                }
            } catch (JSONException e) {
                if (page == 1) {
                    setLayout(2, 1, "数据异常");
                    orderAdapter.setEnableLoadMore(false);
                    srl_order_list.setRefreshing(false);
                } else {
                    orderAdapter.setEnableLoadMore(true);
                    orderAdapter.loadMoreFail();
                }
                e.printStackTrace();
            }
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    list.get(i).isShow = true;
                    list.get(i).headerId = i;
                } else {
                    String[] YearAndMonthOne = list.get(i - 1).servicetime.split(" ")[0].split("-");
                    String dataOne = YearAndMonthOne[0] + "-" + YearAndMonthOne[1];
                    String[] YearAndMonthTwo = list.get(i).servicetime.split(" ")[0].split("-");
                    String dataTwo = YearAndMonthTwo[0] + "-" + YearAndMonthTwo[1];
                    if (dataOne.equals(dataTwo)) {
                        list.get(i).isShow = false;
                        list.get(i).headerId = list.get(i - 1).headerId;
                    } else {
                        list.get(i).isShow = true;
                        list.get(i).headerId = i;
                    }
                }
            }
            orderAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            pDialog.dismiss();
            if (page == 1) {
                setLayout(2, 1, "请求失败");
                orderAdapter.setEnableLoadMore(false);
                srl_order_list.setRefreshing(false);
            } else {
                orderAdapter.setEnableLoadMore(true);
                orderAdapter.loadMoreFail();
            }
        }
    };

    private void setLayout(int type, int flag, String msg) {
        if (type == 1) {
            ll_order_default.setVisibility(View.GONE);
            srl_order_list.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            ll_order_default.setVisibility(View.VISIBLE);
            srl_order_list.setVisibility(View.GONE);
            if (flag == 1) {
                btn_emptyview.setVisibility(View.VISIBLE);
                btn_emptyview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
            } else if (flag == 2) {
                btn_emptyview.setVisibility(View.GONE);
            }
            Utils.setText(tv_emptyview_desc, msg, "", View.VISIBLE, View.VISIBLE);
        }
    }

    private void setView() {
        srl_order_list.setRefreshing(true);
        srl_order_list.setColorSchemeColors(Color.rgb(47, 223, 189));
        rv_order_list.setHasFixedSize(true);
        rv_order_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderAdapter = new OrderNewAdapter(R.layout.item_neworderlist, list, index);
        rv_order_list.setAdapter(orderAdapter);
        if (index != 0) {
            stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(orderAdapter);
            rv_order_list.addItemDecoration(stickyRecyclerHeadersDecoration);
        }
    }

    private void findView(View view) {
        srl_order_list = (SwipeRefreshLayout) view
                .findViewById(R.id.srl_order_list);
        rv_order_list = (RecyclerView) view
                .findViewById(R.id.rv_order_list);
        tv_emptyview_desc = (TextView) view
                .findViewById(R.id.tv_emptyview_desc);
        btn_emptyview = (Button) view
                .findViewById(R.id.btn_emptyview);
        ll_order_default = (LinearLayout) view
                .findViewById(R.id.ll_order_default);
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        pDialog = new MProgressDialog(getActivity());
        pDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        Bundle arguments = getArguments();
        index = arguments.getInt("index", 0);
        lat = arguments.getDouble("lat", 0.0);
        lng = arguments.getDouble("lng", 0.0);
        Log.e("TAG", "index = " + index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
