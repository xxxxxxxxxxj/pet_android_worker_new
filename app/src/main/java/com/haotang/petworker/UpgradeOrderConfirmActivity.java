package com.haotang.petworker;

import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.adapter.UpdateConfirmAdapter;
import com.haotang.petworker.entity.DetailOrder;
import com.haotang.petworker.entity.OrderTag;
import com.haotang.petworker.entity.UpdateData;
import com.haotang.petworker.event.UpdateOrderEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.ComputeUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.AlertDialogDefault;
import com.haotang.petworker.view.NiceImageView;
import com.haotang.petworker.view.NoScollFullLinearLayoutManager;
import com.haotang.petworker.view.ObservableScrollView;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 升级服务确认页
 */
public class UpgradeOrderConfirmActivity extends SuperActivity implements ObservableScrollView.ScrollViewListener {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_upgradeorderconfirm_submit)
    TextView tvUpgradeorderconfirmSubmit;
    @BindView(R.id.tv_upgradeorderconfirm_desc)
    TextView tvUpgradeorderconfirmDesc;
    @BindView(R.id.iv_upgradeorderconfirm_petimg)
    NiceImageView ivUpgradeorderconfirmPetimg;
    @BindView(R.id.tv_upgradeorderconfirm_pettnickname)
    TextView tvUpgradeorderconfirmPettnickname;
    @BindView(R.id.tv_upgradeorderconfirm_petname)
    TextView tvUpgradeorderconfirmPetname;
    @BindView(R.id.tfl_upgradeorderconfirm_tag)
    TagFlowLayout tflUpgradeorderconfirmTag;
    @BindView(R.id.tv_upgradeorderconfirm_price)
    TextView tvUpgradeorderconfirmPrice;
    @BindView(R.id.tv_upgradeorderconfirm_servicename)
    TextView tvUpgradeorderconfirmServicename;
    @BindView(R.id.tv_upgradeorderconfirm_itemservicename)
    TextView tvUpgradeorderconfirmItemservicename;
    @BindView(R.id.ll_upgradeorderconfirm_itemservicename)
    LinearLayout llUpgradeorderconfirmItemservicename;
    @BindView(R.id.rv_upgradeorderconfirm)
    RecyclerView rvUpgradeorderconfirm;
    @BindView(R.id.tv_upgradeorderconfirm_hjprice)
    TextView tvUpgradeorderconfirmHjprice;
    @BindView(R.id.tv_upgradeorderconfirm_time)
    TextView tvUpgradeorderconfirmTime;
    @BindView(R.id.osv_upgradeorderconfirm)
    ObservableScrollView osvUpgradeorderconfirm;
    @BindView(R.id.rl_upgradeorderconfirm_top)
    RelativeLayout rlUpgradeorderconfirmTop;
    @BindView(R.id.rl_upgradeorderconfirm_title)
    RelativeLayout rlUpgradeorderconfirmTitle;
    private int orderId;
    private int flag;
    private int imageHeight;
    private DetailOrder order;
    private List<UpdateData> list;
    private int isVip;
    private double totalPrice;
    private int totalDuration;
    private String nextTaskStartDate;
    private int serviceId;
    private String itemIds;
    private int petId;
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
        list = (List<UpdateData>) getIntent().getSerializableExtra("list");
        isVip = getIntent().getIntExtra("isVip", 0);
        flag = getIntent().getIntExtra("flag", 0);
        orderId = getIntent().getIntExtra("orderId", 0);
        //升级确认需要的参数
        nextTaskStartDate = getIntent().getStringExtra("nextTaskStartDate");
        itemIds = getIntent().getStringExtra("itemIds");
        petId = getIntent().getIntExtra("petId", 0);
        serviceId = getIntent().getIntExtra("serviceId", 0);
        switchTypeName = getIntent().getStringExtra("switchTypeName");
        switchPetImg = getIntent().getStringExtra("switchPetImg");
    }

    private void findView() {
        setContentView(R.layout.activity_upgrade_order_confirm);
        ButterKnife.bind(this);
    }

    private void setView() {
        rlUpgradeorderconfirmTitle.bringToFront();
        if (flag == 1) {
            tvTitlebarTitle.setText("确认订单");
            tvUpgradeorderconfirmSubmit.setText("提交");
        } else if (flag == 2) {
            tvTitlebarTitle.setText("待升级服务");
            tvUpgradeorderconfirmSubmit.setText("取消升级");
        } else if (flag == 3) {
            tvTitlebarTitle.setText("待追加单项");
            tvUpgradeorderconfirmSubmit.setText("取消升级");
        }
        rvUpgradeorderconfirm.setHasFixedSize(true);
        rvUpgradeorderconfirm.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new NoScollFullLinearLayoutManager(this);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvUpgradeorderconfirm.setLayoutManager(noScollFullLinearLayoutManager);
        rvUpgradeorderconfirm.setAdapter(new UpdateConfirmAdapter(R.layout.item_updateconfirm, list, isVip));
        for (int i = 0; i < list.size(); i++) {
            if (isVip > 0) {
                totalPrice = ComputeUtil.add(list.get(i).getVipPrice(), totalPrice);
            } else {
                totalPrice = ComputeUtil.add(list.get(i).getPrice(), totalPrice);
            }
            totalDuration = list.get(i).getDuration() + totalDuration;
        }
        tvUpgradeorderconfirmHjprice.setText("¥" + totalPrice);
        tvUpgradeorderconfirmTime.setText("耗时" + totalDuration + "分钟");
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            rlUpgradeorderconfirmTitle.setBackgroundColor(Color.argb((int) 0, 0, 34, 65));
        } else if (y > 0 && y <= imageHeight) {
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            rlUpgradeorderconfirmTitle.setBackgroundColor(Color.argb((int) alpha, 0, 34, 65));
        } else {
            rlUpgradeorderconfirmTitle.setBackgroundColor(Color.argb((int) 255, 0, 34, 65));
        }
    }

    private void setLinster() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = rlUpgradeorderconfirmTop.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlUpgradeorderconfirmTop.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = rlUpgradeorderconfirmTop.getHeight();
                osvUpgradeorderconfirm.setScrollViewListener(UpgradeOrderConfirmActivity.this);
            }
        });
    }

    private void getOrderInfo() {
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
                if (Utils.isStrNull(switchPetImg)) {
                    tvUpgradeorderconfirmPettnickname.setVisibility(View.GONE);
                    GlideUtil.loadImg(mContext, switchPetImg, ivUpgradeorderconfirmPetimg, R.drawable.icon_production_default);
                    Utils.setText(tvUpgradeorderconfirmPetname, switchTypeName, "", View.VISIBLE, View.GONE);
                } else {
                    Utils.setText(tvUpgradeorderconfirmPettnickname, order.nickName, "", View.VISIBLE, View.GONE);
                    GlideUtil.loadImg(mContext, order.avatar, ivUpgradeorderconfirmPetimg, R.drawable.icon_production_default);
                    Utils.setText(tvUpgradeorderconfirmPetname, order.typeName, "", View.VISIBLE, View.GONE);
                }
                Utils.setText(tvUpgradeorderconfirmPrice, "¥" + order.workerincome, "¥0", View.VISIBLE, View.VISIBLE);
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
                tflUpgradeorderconfirmTag.setAdapter(tagAdapter);
                Utils.setText(tvUpgradeorderconfirmServicename, order.typeName + order.servicename, "", View.VISIBLE, View.GONE);
                if (Utils.isStrNull(order.addserviceitems)) {
                    llUpgradeorderconfirmItemservicename.setVisibility(View.VISIBLE);
                    Utils.setText(tvUpgradeorderconfirmItemservicename, order.addserviceitems, "", View.VISIBLE, View.GONE);
                } else {
                    llUpgradeorderconfirmItemservicename.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mContext,
                    "请求失败");
        }
    };

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

    @OnClick({R.id.ib_titlebar_back, R.id.tv_upgradeorderconfirm_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_upgradeorderconfirm_submit:
                if (flag == 1) {//升级
                    if (Utils.isStrNull(nextTaskStartDate)) {
                        new AlertDialogDefault(mActivity).builder()
                                .setTitle("提示").setMsg("下一单开始时间为" + nextTaskStartDate + ",追加单项所需时间为" + totalDuration + "分钟,是否确认追加单项").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPDialog.showDialog();
                                CommUtil.submit(mActivity, orderId, petId, serviceId, itemIds, totalPrice, submitHanler);
                            }
                        }).show();
                    } else {
                        mPDialog.showDialog();
                        CommUtil.submit(mActivity, orderId, petId, serviceId, itemIds, totalPrice, submitHanler);
                    }
                } else if (flag == 2 || flag == 3) {//取消升级
                    new AlertDialogDefault(mActivity).builder()
                            .setTitle("提示").setMsg("确定取消升级？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPDialog.showDialog();
                            CommUtil.cancel(mActivity, orderId, cancelHanler);
                        }
                    }).show();
                }
                break;
        }
    }

    private AsyncHttpResponseHandler submitHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    EventBus.getDefault().post(new UpdateOrderEvent(true));
                    ToastUtil.showToastCenterShort(mActivity,
                            "升级成功");
                    if (UpgradeOrderActivity.act != null) {
                        UpgradeOrderActivity.act.finish();
                    }
                    finish();
                } else {
                    ToastUtil.showToastCenterShort(mActivity,
                            msg);
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                ToastUtil.showToastCenterShort(mActivity,
                        "数据异常");
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

    private AsyncHttpResponseHandler cancelHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    EventBus.getDefault().post(new UpdateOrderEvent(true));
                    ToastUtil.showToastCenterShort(mActivity,
                            "取消成功");
                    finish();
                } else {
                    ToastUtil.showToastCenterShort(mActivity,
                            msg);
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                ToastUtil.showToastCenterShort(mActivity,
                        "数据异常");
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
}
