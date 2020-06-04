package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.petworker.adapter.UpdateAdapter;
import com.haotang.petworker.entity.DetailOrder;
import com.haotang.petworker.entity.OrderTag;
import com.haotang.petworker.entity.UpdateData;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.ComputeUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.NiceImageView;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 升级服务，追加单项界面
 */
public class UpgradeOrderActivity extends SuperActivity {
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_upgradeorder_price)
    TextView tvUpgradeorderPrice;
    @BindView(R.id.tv_upgradeorder_time)
    TextView tvUpgradeorderTime;
    @BindView(R.id.iv_upgradeorder_petimg)
    NiceImageView ivUpgradeorderPetimg;
    @BindView(R.id.tv_upgradeorder_petname)
    TextView tvUpgradeorderPetname;
    @BindView(R.id.tfl_upgradeorder_tag)
    TagFlowLayout tflUpgradeorderTag;
    @BindView(R.id.tv_upgradeorder_orderprice)
    TextView tvUpgradeorderOrderprice;
    @BindView(R.id.tv_upgradeorder_updown)
    TextView tvUpgradeorderUpdown;
    @BindView(R.id.iv_upgradeorder_updown)
    ImageView ivUpgradeorderUpdown;
    @BindView(R.id.tv_upgradeorder_servicename)
    TextView tvUpgradeorderServicename;
    @BindView(R.id.tv_upgradeorder_baseservicename)
    TextView tvUpgradeorderBaseservicename;
    @BindView(R.id.tv_upgradeorder_itemservicename)
    TextView tvUpgradeorderItemservicename;
    @BindView(R.id.ll_upgradeorder_itemservice)
    LinearLayout llUpgradeorderItemservice;
    @BindView(R.id.ll_upgradeorder_info)
    LinearLayout llUpgradeorderInfo;
    @BindView(R.id.tv_upgradeorder_ksjfw)
    TextView tvUpgradeorderKsjfw;
    @BindView(R.id.tv_upgradeorder_kzjdx)
    TextView tvUpgradeorderKzjdx;
    @BindView(R.id.tv_upgradeorder_pettnickname)
    TextView tvUpgradeorderPettnickname;
    @BindView(R.id.rv_upgradeorder)
    RecyclerView rvUpgradeorder;
    private int orderId;
    private DetailOrder order;
    private int previous;
    private int petId;
    private List<UpdateData> avilServiceList = new ArrayList<UpdateData>();
    private List<UpdateData> avilItemList = new ArrayList<UpdateData>();
    private List<UpdateData> list = new ArrayList<UpdateData>();
    private List<UpdateData> intentList = new ArrayList<UpdateData>();
    private int isVip;
    private String nextTaskStartDate;
    private UpdateAdapter updateAdapter;
    private double totalPrice;
    private int totalDuration;
    private int index = 1;
    private int serviceId;
    private boolean isOpen;
    public static SuperActivity act;
    private String typeName;
    private String itemIds = "";
    private String switchPetImg;
    private String switchTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        initData();
        findView();
        setView();
        setLinster();
        getOrderInfo();
    }

    private void initData() {
        act = this;
        orderId = getIntent().getIntExtra("orderId", 0);
        previous = getIntent().getIntExtra("previous", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_upgrade_order);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("订单升级");
        if (previous == Global.ORDERDETAIL_TO_ITEM) {
            tvUpgradeorderKsjfw.setVisibility(View.GONE);
            btTitlebarOther.setVisibility(View.GONE);
        } else if (previous == Global.ORDERDETAIL_TO_SERVICE) {
            tvUpgradeorderKsjfw.setVisibility(View.VISIBLE);
            btTitlebarOther.setVisibility(View.VISIBLE);
            btTitlebarOther.setText("切换犬种");
            btTitlebarOther.setTextColor(getResources().getColor(R.color.white));
        }
        rvUpgradeorder.setLayoutManager(new LinearLayoutManager(mContext));
        updateAdapter = new UpdateAdapter(R.layout.item_update, list, isVip);
        rvUpgradeorder.setAdapter(updateAdapter);
    }

    private void setLinster() {
        updateAdapter.setOnAddClickListener(new UpdateAdapter.OnAddClickListener() {
            @Override
            public void OnAddClick(int position) {
                if (list.size() > position) {
                    if (previous == Global.ORDERDETAIL_TO_SERVICE) {
                        if (index == 1) {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getServiceId() != list.get(position).getServiceId()) {
                                    list.get(i).setSelect(false);
                                }
                            }
                            list.get(position).setSelect(!list.get(position).isSelect());
                            if (list.get(position).isSelect()) {
                                serviceId = list.get(position).getServiceId();
                                for (int i = 0; i < avilItemList.size(); i++) {
                                    avilItemList.get(i).setSelect(false);
                                }
                                getItems();
                            } else {
                                for (int i = 0; i < avilItemList.size(); i++) {
                                    avilItemList.get(i).setSelect(false);
                                }
                            }
                        } else if (index == 2) {
                            list.get(position).setSelect(!list.get(position).isSelect());
                        }
                    } else if (previous == Global.ORDERDETAIL_TO_ITEM) {
                        list.get(position).setSelect(!list.get(position).isSelect());
                    }
                    updateAdapter.notifyDataSetChanged();
                    totalPrice = 0;
                    totalDuration = 0;
                    for (int i = 0; i < avilServiceList.size(); i++) {
                        if (avilServiceList.get(i).isSelect()) {
                            if (isVip > 0) {
                                totalPrice = ComputeUtil.add(avilServiceList.get(i).getVipPrice(), totalPrice);
                            } else {
                                totalPrice = ComputeUtil.add(avilServiceList.get(i).getPrice(), totalPrice);
                            }
                            totalDuration = avilServiceList.get(i).getDuration() + totalDuration;
                        }
                    }
                    for (int i = 0; i < avilItemList.size(); i++) {
                        if (avilItemList.get(i).isSelect()) {
                            if (isVip > 0) {
                                totalPrice = ComputeUtil.add(avilItemList.get(i).getVipPrice(), totalPrice);
                            } else {
                                totalPrice = ComputeUtil.add(avilItemList.get(i).getPrice(), totalPrice);
                            }
                            totalDuration = avilItemList.get(i).getDuration() + totalDuration;
                        }
                    }
                    tvUpgradeorderPrice.setText("¥" + totalPrice);
                    tvUpgradeorderTime.setText("耗时" + totalDuration + "分钟");
                }
            }
        });
    }

    private void getOrderInfo() {
        serviceId = 0;
        petId = 0;
        typeName = "";
        mPDialog.showDialog();
        CommUtil.orderDetail(mContext, orderId, queryOrderDetailWorker);
    }

    private AsyncHttpResponseHandler queryOrderDetailWorker = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        order = DetailOrder.json2Entity(jobj.getJSONObject("data"));
                    }
                } else {
                    ToastUtil.showToastCenterShort(mContext,
                            msg);
                }
            } catch (Exception e) {
                ToastUtil.showToastCenterShort(mContext,
                        "数据异常");
                Log.e("TAG", "e.getMessage() = " + e.getMessage());
                Log.e("TAG", "e.toString() = " + e.toString());
            }
            if (order != null) {
                Utils.setText(tvUpgradeorderOrderprice, "¥" + order.workerincome, "¥0", View.VISIBLE, View.VISIBLE);
                GlideUtil.loadImg(mContext, order.avatar, ivUpgradeorderPetimg, R.drawable.icon_production_default);
                Utils.setText(tvUpgradeorderPetname, order.typeName, "", View.VISIBLE, View.GONE);
                order.orderTagList.clear();
                order.orderTagList.add(new OrderTag(0, order.addrtype));
                if (Utils.isStrNull(order.firstorderflag)) {
                    order.orderTagList.add(new OrderTag(1, order.firstorderflag));
                }
                if (Utils.isStrNull(order.updateDescription)) {
                    order.orderTagList.add(new OrderTag(2, order.updateDescription));
                }
                if (Utils.isStrNull(order.pickUpTag)) {
                    order.orderTagList.add(new OrderTag(3, order.pickUpTag));
                }
                TagAdapter<OrderTag> tagAdapter = new TagAdapter<OrderTag>(order.orderTagList) {
                    @Override
                    public View getView(FlowLayout parent, final int position, OrderTag s) {
                        View view = (View) View.inflate(mContext, R.layout.item_ordertag,
                                null);
                        TextView tv_item_ordertag = (TextView) view.findViewById(R.id.tv_item_ordertag);
                        tv_item_ordertag.setText(order.orderTagList.get(position).getTag());
                        if (order.orderTagList.get(position).getId() == 0) {
                            tv_item_ordertag.setTextColor(mContext.getResources().getColor(R.color.aC29865));
                            tv_item_ordertag.setBackgroundResource(R.drawable.bg_dh_round);
                        } else {
                            tv_item_ordertag.setTextColor(mContext.getResources().getColor(R.color.aFF3A1E));
                            tv_item_ordertag.setBackgroundResource(R.drawable.bg_round_df);
                        }
                        return view;
                    }
                };
                tflUpgradeorderTag.setAdapter(tagAdapter);
                Utils.setText(tvUpgradeorderPettnickname, order.nickName, "", View.VISIBLE, View.GONE);
                Utils.setText(tvUpgradeorderServicename, order.servicename, "", View.VISIBLE, View.GONE);
                Utils.setText(tvUpgradeorderBaseservicename, order.servicecontent, "", View.VISIBLE, View.GONE);
                if (Utils.isStrNull(order.addserviceitems)) {
                    llUpgradeorderItemservice.setVisibility(View.VISIBLE);
                    Utils.setText(tvUpgradeorderItemservicename, order.addserviceitems, "", View.VISIBLE, View.GONE);
                } else {
                    llUpgradeorderItemservice.setVisibility(View.GONE);
                }
                petId = order.petid;
                serviceId = order.serviceid;
                typeName = order.typeName;
            }
            getData();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mContext,
                    "请求失败");
        }
    };

    private void getData() {
        if (previous == Global.ORDERDETAIL_TO_SERVICE) {
            getService();
        } else if (previous == Global.ORDERDETAIL_TO_ITEM) {
            getItems();
        }
    }

    private void getService() {
        isVip = 0;
        totalPrice = 0;
        totalDuration = 0;
        mPDialog.showDialog();
        avilServiceList.clear();
        nextTaskStartDate = "";
        CommUtil.services(mActivity, orderId, petId, servicesHanler);
    }

    private AsyncHttpResponseHandler servicesHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("isVip") && !jdata.isNull("isVip")) {
                            isVip = jdata.getInt("isVip");
                        }
                        if (jdata.has("nextTaskStartDate") && !jdata.isNull("nextTaskStartDate")) {
                            nextTaskStartDate = jdata.getString("nextTaskStartDate");
                        }
                        if (jdata.has("available") && !jdata.isNull("available")
                                && jdata.getJSONArray("available").length() > 0) {
                            JSONArray jsonArray = jdata.getJSONArray("available");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                avilServiceList.add(UpdateData
                                        .json2Entity(jsonArray
                                                .getJSONObject(i)));
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastCenterShort(mActivity,
                            msg);
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                ToastUtil.showToastCenterShort(mActivity,
                        "数据异常");
            }
            tvUpgradeorderPrice.setText("¥" + totalPrice);
            tvUpgradeorderTime.setText("耗时" + totalDuration + "分钟");
            index = 1;
            setTab();
            getItems();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mActivity,
                    "请求失败");
        }
    };

    private void getItems() {
        isVip = 0;
        mPDialog.showDialog();
        avilItemList.clear();
        if (previous == Global.ORDERDETAIL_TO_ITEM) {
            CommUtil.extraItems
                    (mActivity, orderId, petId, serviceId, "item", extraItemsHanler);
        } else if (previous == Global.ORDERDETAIL_TO_SERVICE) {
            CommUtil.extraItems
                    (mActivity, orderId, petId, serviceId, "", extraItemsHanler);
        }
    }

    private AsyncHttpResponseHandler extraItemsHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("isVip") && !jdata.isNull("isVip")) {
                            isVip = jdata.getInt("isVip");
                        }
                        if (jdata.has("available") && !jdata.isNull("available")
                                && jdata.getJSONArray("available").length() > 0) {
                            JSONArray jsonArray = jdata.getJSONArray("available");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                avilItemList.add(UpdateData
                                        .json2Entity(jsonArray
                                                .getJSONObject(i)));
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastCenterShort(mActivity,
                            msg);
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                ToastUtil.showToastCenterShort(mActivity,
                        "数据异常");
            }
            if (previous == Global.ORDERDETAIL_TO_ITEM) {
                index = 2;
                setTab();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mActivity,
                    "请求失败");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.bt_titlebar_other, R.id.tv_upgradeorder_submit, R.id.ll_upgradeorder_updown, R.id.tv_upgradeorder_ksjfw, R.id.tv_upgradeorder_kzjdx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.bt_titlebar_other:
                startActivityForResult(new Intent(mActivity,
                                ChoosePetActivityNew.class)
                                .putExtra("orderId", orderId).putExtra("previous", Global.UPGRADESERVICE_CHOOSEPET),
                        Global.UPGRADESERVICE_CHOOSEPET);
                break;
            case R.id.tv_upgradeorder_submit:
                intentList.clear();
                boolean isSelectService = false;
                boolean isSelectItem = false;
                for (int i = 0; i < avilServiceList.size(); i++) {
                    if (avilServiceList.get(i).isSelect()) {
                        intentList.add(avilServiceList.get(i));
                        isSelectService = true;
                        serviceId = avilServiceList.get(i).getServiceId();
                        break;
                    }
                }
                itemIds = "";
                for (int i = 0; i < avilItemList.size(); i++) {
                    if (avilItemList.get(i).isSelect()) {
                        intentList.add(avilItemList.get(i));
                        isSelectItem = true;
                        itemIds = itemIds + avilItemList.get(i).getItemId() + ",";
                    }
                }
                if (Utils.isStrNull(itemIds)) {
                    itemIds = itemIds.substring(0, itemIds.length() - 1);
                }
                if (!isSelectService && !isSelectItem) {
                    ToastUtil.showToastCenterShort(mActivity, "请先选择升级项目");
                } else {
                    startActivity(new Intent(mActivity,
                            UpgradeOrderConfirmActivity.class).putExtra("isVip", isVip)
                            .putExtra("petId", petId)
                            .putExtra("typeName", typeName)
                            .putExtra("serviceId", serviceId)
                            .putExtra("flag", 1)
                            .putExtra("nextTaskStartDate", nextTaskStartDate)
                            .putExtra("orderId", orderId)
                            .putExtra("itemIds", itemIds)
                            .putExtra("switchTypeName", switchTypeName)
                            .putExtra("switchPetImg", switchPetImg)
                            .putExtra("list", (Serializable) intentList)
                    );
                }
                break;
            case R.id.ll_upgradeorder_updown:
                setOrderInfoIsOpen();
                break;
            case R.id.tv_upgradeorder_ksjfw:
                index = 1;
                setTab();
                break;
            case R.id.tv_upgradeorder_kzjdx:
                index = 2;
                setTab();
                break;
        }
    }

    private void setOrderInfoIsOpen() {
        if (isOpen) {
            tvUpgradeorderUpdown.setText("展开");
            ivUpgradeorderUpdown.setImageResource(R.drawable.iv_upgrade_fz_down);
            llUpgradeorderInfo.setVisibility(View.GONE);
        } else {
            tvUpgradeorderUpdown.setText("收起");
            ivUpgradeorderUpdown.setImageResource(R.drawable.iv_upgrade_fz_up);
            llUpgradeorderInfo.setVisibility(View.VISIBLE);
        }
        isOpen = !isOpen;
    }

    private void setTab() {
        if (index == 1) {
            tvUpgradeorderKsjfw.setTextColor(getResources().getColor(R.color.aFF3A1E));
            tvUpgradeorderKsjfw.setBackgroundResource(R.drawable.bg_white_leftround20);
            tvUpgradeorderKzjdx.setTextColor(getResources().getColor(R.color.a717985));
            tvUpgradeorderKzjdx.setBackgroundColor(getResources().getColor(R.color.aF6F8FA));
            list.clear();
            if (avilServiceList.size() > 0) {
                list.addAll(avilServiceList);
            }
        } else if (index == 2) {
            if (previous == Global.ORDERDETAIL_TO_ITEM) {
                tvUpgradeorderKzjdx.setBackgroundResource(R.drawable.bg_white_leftround20);
            } else if (previous == Global.ORDERDETAIL_TO_SERVICE) {
                tvUpgradeorderKzjdx.setBackgroundColor(getResources().getColor(R.color.white));
            }
            tvUpgradeorderKsjfw.setTextColor(getResources().getColor(R.color.a717985));
            tvUpgradeorderKsjfw.setBackgroundResource(R.drawable.bg_f6f8fa_leftround20);
            tvUpgradeorderKzjdx.setTextColor(getResources().getColor(R.color.aFF3A1E));
            list.clear();
            if (avilItemList.size() > 0) {
                list.addAll(avilItemList);
            }
        }
        updateAdapter.setIsVip(isVip);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && requestCode == Global.UPGRADESERVICE_CHOOSEPET) {
            petId = data.getIntExtra("petid", 0);
            typeName = data.getStringExtra("petname");
            switchTypeName = data.getStringExtra("petname");
            switchPetImg = data.getStringExtra("petimg");
            GlideUtil.loadImg(mContext, switchPetImg, ivUpgradeorderPetimg, R.drawable.icon_production_default);
            Utils.setText(tvUpgradeorderPetname, typeName, "", View.VISIBLE, View.GONE);
            tvUpgradeorderPettnickname.setVisibility(View.GONE);
            Log.e("TAG", "petId = " + petId);
            getData();
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
