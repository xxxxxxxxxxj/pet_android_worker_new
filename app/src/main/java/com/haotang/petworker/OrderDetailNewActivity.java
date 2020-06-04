package com.haotang.petworker;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.haotang.petworker.entity.CareTag;
import com.haotang.petworker.entity.DetailOrder;
import com.haotang.petworker.entity.OrderTag;
import com.haotang.petworker.entity.UpdateData;
import com.haotang.petworker.event.OrderCompleteEvent;
import com.haotang.petworker.event.UpdateOrderEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.CountdownUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.AlertDialogDefault;
import com.haotang.petworker.view.AlertDialogOrderTag;
import com.haotang.petworker.view.AlertDialogServiceTime;
import com.haotang.petworker.view.AlertDialogUpgradeOrder;
import com.haotang.petworker.view.NiceImageView;
import com.haotang.petworker.view.ObservableScrollView;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单详情页
 */
public class OrderDetailNewActivity extends SuperActivity implements ObservableScrollView.ScrollViewListener {
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_orderdetail_price)
    TextView tvOrderdetailPrice;
    @BindView(R.id.tv_orderdetail_statedesc)
    TextView tvOrderdetailStatedesc;
    @BindView(R.id.iv_orderdetail_petimg)
    NiceImageView ivOrderdetailPetimg;
    @BindView(R.id.tv_orderdetail_pettnickname)
    TextView tvOrderdetailPettnickname;
    @BindView(R.id.tv_orderdetail_petname)
    TextView tvOrderdetailPetname;
    @BindView(R.id.tfl_orderdetail_tag)
    TagFlowLayout tflOrderdetailTag;
    @BindView(R.id.tv_orderdetail_fwsc)
    TextView tvOrderdetailFwsc;
    @BindView(R.id.tv_orderdetail_servicename)
    TextView tvOrderdetailServicename;
    @BindView(R.id.tv_orderdetail_baseservicename)
    TextView tvOrderdetailBaseservicename;
    @BindView(R.id.tv_orderdetail_itemservicename)
    TextView tvOrderdetailItemservicename;
    @BindView(R.id.ll_orderdetail_itemservice)
    LinearLayout llOrderdetailItemservice;
    @BindView(R.id.tv_orderdetail_itemservicenameadd)
    TextView tvOrderdetailItemservicenameAdd;
    @BindView(R.id.ll_orderdetail_itemserviceadd)
    LinearLayout llOrderdetailItemserviceAdd;
    @BindView(R.id.iv_orderdetail_isvip)
    ImageView ivOrderdetailIsvip;
    @BindView(R.id.tv_orderdetail_username)
    TextView tvOrderdetailUsername;
    @BindView(R.id.tv_orderdetail_userphone)
    TextView tvOrderdetailUserphone;
    @BindView(R.id.tv_orderdetail_appointmenttime)
    TextView tvOrderdetailAppointmenttime;
    @BindView(R.id.tv_orderdetail_lxdz)
    TextView tvOrderdetailLxdz;
    @BindView(R.id.tv_orderdetail_bz)
    TextView tvOrderdetailBz;
    @BindView(R.id.ll_orderdetail_bz)
    LinearLayout llOrderdetailBz;
    @BindView(R.id.tv_orderdetail_kfbz)
    TextView tvOrderdetailKfbz;
    @BindView(R.id.ll_orderdetail_kfbz)
    LinearLayout llOrderdetailKfbz;
    @BindView(R.id.tv_orderdetail_ddbh)
    TextView tvOrderdetailDdbh;
    @BindView(R.id.tv_orderdetail_ksfwtime)
    TextView tvOrderdetailKsfwtime;
    @BindView(R.id.btn_orderdetail_submit)
    Button btnOrderdetailSubmit;
    @BindView(R.id.osv_orderdetail)
    ObservableScrollView osvOrderdetail;
    @BindView(R.id.ll_orderdetail_hljl)
    LinearLayout llOrderdetailHljl;
    @BindView(R.id.ll_orderdetail_fwsc)
    LinearLayout llOrderdetailFwsc;
    @BindView(R.id.rl_orderdetail_price)
    RelativeLayout rlOrderdetailPrice;
    @BindView(R.id.rl_orderdetail_title)
    RelativeLayout rlOrderdetailTitle;
    @BindView(R.id.tv_orderdetail_cardnum)
    TextView tv_orderdetail_cardnum;
    @BindView(R.id.tv_orderdetail_cardbalance)
    TextView tv_orderdetail_cardbalance;
    @BindView(R.id.tv_orderdetail_tcprice)
    TextView tv_orderdetail_tcprice;
    private int orderId;
    private ClipboardManager cmb;
    private int locationstatus;
    private LocationClient mLClient;
    private MLocationListener mListener;
    public double lat;
    public double lng;
    private DetailOrder order;
    private String daddr;
    private StringBuffer sbUserTag = new StringBuffer();
    private StringBuffer sbPetTag = new StringBuffer();
    private List<CareTag> petTagList = new ArrayList<CareTag>();
    private List<CareTag> userTagList = new ArrayList<CareTag>();
    private int imageHeight;
    public static SuperActivity act;
    private List<UpdateData> selectServiceList = new ArrayList<UpdateData>();
    private List<UpdateData> selectItemList = new ArrayList<UpdateData>();
    private int isVip;
    private List<UpdateData> intentList = new ArrayList<UpdateData>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateOrderEvent event) {
        Log.e("TAG", "event = " + event);
        if (event != null && event.isRefersh()) {
            getData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        initBD();
        initData();
        findView();
        setView();
        initListener();
        getData();
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        act = this;
        setContentView(R.layout.activity_order_detail_new);
        cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        orderId = getIntent().getIntExtra("orderId", 0);
    }

    private void findView() {
        setContentView(R.layout.activity_order_detail_new);
        ButterKnife.bind(this);
    }

    private void setView() {
        rlOrderdetailTitle.bringToFront();
        tvTitlebarTitle.setText("服务订单");
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = rlOrderdetailPrice.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlOrderdetailPrice.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = rlOrderdetailPrice.getHeight();
                osvOrderdetail.setScrollViewListener(OrderDetailNewActivity.this);
            }
        });
    }

    private void initListener() {
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            rlOrderdetailTitle.setBackgroundColor(Color.argb((int) 0, 0, 34, 65));
        } else if (y > 0 && y <= imageHeight) {
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            rlOrderdetailTitle.setBackgroundColor(Color.argb((int) alpha, 0, 34, 65));
        } else {
            rlOrderdetailTitle.setBackgroundColor(Color.argb((int) 255, 0, 34, 65));
        }
    }

    @OnClick({R.id.ib_titlebar_back, R.id.bt_titlebar_other, R.id.ll_orderdetail_hljl, R.id.ll_orderdetail_fwsc, R.id.iv_orderdetail_ckbq, R.id.tv_orderdetail_userphone, R.id.iv_orderdetail_dh, R.id.tv_orderdetail_ddbhcopy, R.id.btn_orderdetail_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.bt_titlebar_other:
                getService();
                break;
            case R.id.ll_orderdetail_hljl:
                Intent intent = new Intent(mContext, PetArchivesNewActivity.class);
                intent.putExtra("id", order.myPetId);
                intent.putExtra("petmonth", order.month);
                startActivity(intent);
                break;
            case R.id.ll_orderdetail_fwsc:
                if (order.serviceMinsObjList.size() > 0) {
                    new AlertDialogServiceTime(mActivity).builder().setData(order.serviceMinsObjList).show();
                }
                break;
            case R.id.iv_orderdetail_ckbq:
                mPDialog.showDialog();
                sbUserTag.setLength(0);
                sbPetTag.setLength(0);
                petTagList.clear();
                userTagList.clear();
                CommUtil.tagDetail(mActivity, order.myPetId, tagDetailHandler);
                break;
            case R.id.tv_orderdetail_userphone:
                if (Utils.isStrNull(order.userphone)) {
                    new AlertDialogDefault(mActivity).builder()
                            .setTitle("拨打电话").setMsg("您确定要拨打客户电话吗？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton("拨打", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.telePhoneBroadcast(mContext,
                                    order.userphone);
                        }
                    }).show();
                } else {
                    ToastUtil.showToastCenterShort(mContext,
                            "对不起，没有获取到该用户的手机号码");
                }
                break;
            case R.id.iv_orderdetail_dh:
                new AlertDialogDefault(mActivity).builder()
                        .setTitle("提示").setMsg("是否需要导航到目的地").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("打开地图", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPDialog.showDialog("定位中...");
                        locationstatus = 2;
                        mLClient.start();
                    }
                }).show();
                break;
            case R.id.tv_orderdetail_ddbhcopy:
                cmb.setText(order.orderNo);
                ToastUtil.showToastBottomShort(mContext, "已复制");
                break;
            case R.id.btn_orderdetail_submit:
                if ((order.status == 4 || order.status == 5) && order.careCount <= 0 && order.myPetId > 0) {
                    mContext.startActivity(new Intent(mContext, AddArchivesActivity.class).putExtra("orderId", orderId));
                } else {
                    if (order.serviceloc == 1) {
                        if (order.status == 2) {
                            new AlertDialogDefault(mActivity).builder()
                                    .setTitle("是否开始服务订单").setMsg("开始服务将对订单开始进行计时").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setPositiveButton("开始服务", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getLocation();
                                }
                            }).show();
                        } else if (order.status == 22) {
                            new AlertDialogDefault(mActivity).builder()
                                    .setTitle("订单服务完成").setMsg("确定要结束服务订单？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setPositiveButton("订单完成", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getLocation();
                                }
                            }).show();
                        }
                    } else {
                        if (order.status == 2) {
                            getLocation();
                        } else if (order.status == 21) {
                            new AlertDialogDefault(mActivity).builder()
                                    .setTitle("是否开始服务订单").setMsg("开始服务将对订单开始进行计时").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setPositiveButton("开始服务", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getLocation();
                                }
                            }).show();
                        } else if (order.status == 22) {
                            new AlertDialogDefault(mActivity).builder()
                                    .setTitle("订单服务完成").setMsg("确定要结束服务订单？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).setPositiveButton("订单完成", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getLocation();
                                }
                            }).show();
                        }
                    }
                }
                break;
        }
    }

    private void getService() {
        mPDialog.showDialog();
        selectServiceList.clear();
        CommUtil.services(mActivity, orderId, order.petid, servicesHanler);
    }

    private void getItems() {
        mPDialog.showDialog();
        selectItemList.clear();
        CommUtil.extraItems
                (mActivity, orderId, order.petid, order.serviceid,"", extraItemsHanler);
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
                        if (jdata.has("selected") && !jdata.isNull("selected")
                                && jdata.getJSONArray("selected").length() > 0) {
                            JSONArray jsonArray = jdata.getJSONArray("selected");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                selectItemList.add(UpdateData
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
            if (selectServiceList.size() > 0 && selectItemList.size() > 0) {
                intentList.clear();
                intentList.addAll(selectServiceList);
                intentList.addAll(selectItemList);
                Intent intent = new Intent(mContext, UpgradeOrderConfirmActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("isVip", isVip);
                intent.putExtra("flag", 2);
                intent.putExtra("list", (Serializable) intentList);
                startActivity(intent);
            } else if (selectServiceList.size() > 0 && selectItemList.size() <= 0) {
                intentList.clear();
                intentList.addAll(selectServiceList);
                Intent intent = new Intent(mContext, UpgradeOrderConfirmActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("isVip", isVip);
                intent.putExtra("flag", 2);
                intent.putExtra("list", (Serializable) intentList);
                startActivity(intent);
            } else if (selectServiceList.size() <= 0 && selectItemList.size() > 0) {
                intentList.clear();
                intentList.addAll(selectItemList);
                Intent intent = new Intent(mContext, UpgradeOrderConfirmActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("isVip", isVip);
                intent.putExtra("flag", 3);
                intent.putExtra("list", (Serializable) intentList);
                startActivity(intent);
            } else if (selectServiceList.size() <= 0 && selectItemList.size() <= 0) {
                if (order.extraEnable == 1 && order.enable == 1) {
                    new AlertDialogUpgradeOrder(mActivity).builder().setServiceButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (order.enable == 1) {
                                Intent intent = new Intent(mContext, UpgradeOrderActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("previous", Global.ORDERDETAIL_TO_SERVICE);
                                startActivity(intent);
                            } else {
                                if (order.tip != null && !TextUtils.isEmpty(order.tip)) {
                                    ToastUtil.showToastCenterShort(mContext, order.tip);
                                }
                            }
                        }
                    }).setItemButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (order.extraEnable == 1) {
                                Intent intent = new Intent(mContext, UpgradeOrderActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("previous", Global.ORDERDETAIL_TO_ITEM);
                                startActivity(intent);
                            } else {
                                if (order.tip != null && !TextUtils.isEmpty(order.tip)) {
                                    ToastUtil.showToastCenterShort(mContext, order.tip);
                                }
                            }
                        }
                    }).show();
                } else if (order.extraEnable == 0 && order.enable == 1) {
                    Intent intent = new Intent(mContext, UpgradeOrderActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("previous", Global.ORDERDETAIL_TO_SERVICE);
                    startActivity(intent);
                } else if (order.extraEnable == 1 && order.enable == 0) {
                    Intent intent = new Intent(mContext, UpgradeOrderActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("previous", Global.ORDERDETAIL_TO_ITEM);
                    startActivity(intent);
                }
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
                        if (jdata.has("selected") && !jdata.isNull("selected")
                                && jdata.getJSONArray("selected").length() > 0) {
                            JSONArray jsonArray = jdata.getJSONArray("selected");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                selectServiceList.add(UpdateData
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

    private void getLocation() {
        mPDialog.showDialog("定位中...");
        locationstatus = 3;
        mLClient.start();
        CountdownUtil.getInstance().newTimer(5000, 1000, new CountdownUtil.ICountDown() {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mPDialog.closeDialog();
                locationstatus = 0;
                mLClient.stop();
                // 快捷操作
                workerUpdate(lat, lng);
            }
        }, "LOCATION_TIMER");
    }

    private AsyncHttpResponseHandler tagDetailHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                        JSONObject jData = jsonObject.getJSONObject("data");
                        if (jData.has("petTag") && !jData.isNull("petTag")) {
                            JSONArray jarrpetTag = jData.getJSONArray("petTag");
                            if (jarrpetTag.length() > 0) {
                                for (int i = 0; i < jarrpetTag.length(); i++) {
                                    petTagList.add(CareTag
                                            .json2Entity(jarrpetTag
                                                    .getJSONObject(i)));
                                }
                            }
                        }
                        if (jData.has("userTag") && !jData.isNull("userTag")) {
                            JSONObject juserTag = jData.getJSONObject("userTag");
                            if (juserTag.has("shop") && !juserTag.isNull("shop")) {
                                JSONArray jarrshop = juserTag.getJSONArray("shop");
                                if (jarrshop.length() > 0) {
                                    for (int i = 0; i < jarrshop.length(); i++) {
                                        userTagList.add(CareTag
                                                .json2Entity(jarrshop
                                                        .getJSONObject(i)));
                                    }
                                }
                            }
                            if (juserTag.has("worker") && !juserTag.isNull("worker")) {
                                JSONArray jarrworker = juserTag.getJSONArray("worker");
                                if (jarrworker.length() > 0) {
                                    for (int i = 0; i < jarrworker.length(); i++) {
                                        userTagList.add(CareTag
                                                .json2Entity(jarrworker
                                                        .getJSONObject(i)));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (jsonObject.has("msg")
                            && !jsonObject.isNull("msg")) {
                        String msg = jsonObject.getString("msg");
                        ToastUtil.showToastCenterShort(mActivity, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastCenterShort(mActivity, "数据异常");
            }
            if (userTagList.size() > 0) {
                for (int i = 0; i < userTagList.size(); i++) {
                    if (i == userTagList.size() - 1) {
                        sbUserTag.append(userTagList.get(i).getNum() > 1 ? userTagList.get(i).getTag() + "*" + userTagList.get(i).getNum() : userTagList.get(i).getTag());
                    } else {
                        sbUserTag.append(userTagList.get(i).getNum() > 1 ? userTagList.get(i).getTag() + "*" + userTagList.get(i).getNum() + "," : userTagList.get(i).getTag() + ",");
                    }
                }
            } else {
                sbUserTag.append("暂无");
            }
            if (petTagList.size() > 0) {
                for (int i = 0; i < petTagList.size(); i++) {
                    if (i == petTagList.size() - 1) {
                        sbPetTag.append(petTagList.get(i).getNum() > 1 ? petTagList.get(i).getTag() + "*" + petTagList.get(i).getNum() : petTagList.get(i).getTag());
                    } else {
                        sbPetTag.append(petTagList.get(i).getNum() > 1 ? petTagList.get(i).getTag() + "*" + petTagList.get(i).getNum() + "," : petTagList.get(i).getTag() + ",");
                    }
                }
            } else {
                sbPetTag.append("暂无");
            }
            new AlertDialogOrderTag(mActivity).builder()
                    .setPetTag(sbPetTag.toString())
                    .setUserTag(sbUserTag.toString()).show();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            if (error != null) {
                Log.e("TAG", "error = " + error.toString());
            }
            if (responseBody != null) {
                Log.e("TAG", "responseBody = " + responseBody.toString());
            }
            Log.e("TAG", "statusCode = " + statusCode);
            ToastUtil.showToastCenterShort(mActivity, "请求失败statusCode = " + statusCode);
        }
    };

    // 百度定位
    private void initBD() {
        mLClient = new LocationClient(this);
        mListener = new MLocationListener();
        mLClient.registerLocationListener(mListener);
        LocationClientOption clientOption = new LocationClientOption();
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setScanSpan(100);
        clientOption.setIsNeedAddress(true);
        mLClient.setLocOption(clientOption);
    }

    public class MLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.e("TAG", "获取到经纬度");
            lat = location.getLatitude();
            lng = location.getLongitude();
            mPDialog.closeDialog();
            if (locationstatus == 2) {
                // 点击导航图标
                goNavigation(lat, lng, location.getAddrStr(), daddr,
                        location.getCity());
            } else if (locationstatus == 3) {
                CountdownUtil.getInstance().cancel("LOCATION_TIMER");
                // 快捷操作
                workerUpdate(lat, lng);
            }
            locationstatus = 0;
        }
    }

    //服务快捷操作
    private void workerUpdate(double lat, double lng) {
        mPDialog.showDialog();
        CommUtil.workerUpdate(this, orderId, lat, lng, workerUpdateHanler);
    }

    private AsyncHttpResponseHandler workerUpdateHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                String msg = "";
                int resultCode = -1;
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj.has("code") && !jobj.isNull("code")) {
                    resultCode = jobj.getInt("code");
                    if (0 == resultCode) {
                        if (order.status == 22) {
                            EventBus.getDefault().post(new OrderCompleteEvent(orderId));
                            Intent intent = new Intent(mContext, ServiceCompleteActivity.class);
                            intent.putExtra("orderId", orderId);
                            startActivity(intent);
                        }
                        getData();
                    } else {
                        if (jobj.has("msg") && !jobj.isNull("msg")) {
                            msg = jobj.getString("msg");
                        }
                        if (Utils.isStrNull(msg)) {
                            ToastUtil.showToastBottomShort(mContext, msg);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showToastCenterShort(mContext, "数据异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mContext, "请求失败");
        }
    };

    private void goNavigation(double lat, double lng, String saddr,
                              String daddr, String city) {
        if (isInstallByread("com.baidu.BaiduMap")) {
            try {
                Intent intent = Intent
                        .getIntent("intent://map/direction?origin=latlng:"
                                + lat
                                + ","
                                + lng
                                + "|name:"
                                + saddr
                                + "B&destination="
                                + daddr
                                + "&mode=drivingion="
                                + city
                                + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent); // 启动调用
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isInstallByread("com.autonavi.minimap")) {
            try {
                Intent intent = Intent
                        .getIntent("androidamap://navi?sourceApplication=宠物家&poiname="
                                + daddr
                                + "&lat="
                                + lat
                                + "&lon="
                                + lng
                                + "&dev=0&style=2");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String url = "http://api.map.baidu.com/direction?origin=latlng:"
                    + lat + "," + lng + "|name:" + saddr + "&destination="
                    + daddr + "&mode=driving&region=" + city
                    + "&output=html&src=宠物家";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private void getData() {
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
                Utils.setText(tvOrderdetailPrice, "¥" + order.totalPrice, "¥0", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_orderdetail_tcprice, "可记提成金额 ¥" + order.totalCommission, "订单提成金额 ¥0", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_orderdetail_cardnum, order.cardAmount + "", "0", View.VISIBLE, View.VISIBLE);
                Utils.setText(tv_orderdetail_cardbalance, "¥" + order.cardBalance, "¥0", View.VISIBLE, View.VISIBLE);
                Utils.setText(tvTitlebarTitle, order.orderTitle, "服务订单", View.VISIBLE, View.VISIBLE);
                Utils.setText(tvOrderdetailStatedesc, order.statusdes, "", View.VISIBLE, View.GONE);
                //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tvOrderdetailPrice.getLayoutParams());
                if (order.status == 22) {//服务进行中
                    //lp.setMargins(0, DisplayUtil.dip2px(mContext, 25), 0, 0);
                    tvOrderdetailKsfwtime.setVisibility(View.VISIBLE);
                    Utils.setText(tvOrderdetailKsfwtime, order.actualStartDate + "开始服务", "", View.VISIBLE, View.GONE);
                } else {
                    //lp.setMargins(0, 0, 0, 0);
                    tvOrderdetailKsfwtime.setVisibility(View.GONE);
                }
                //lp.addRule(LinearLayout.ALIGN_PARENT_RIGHT, LinearLayout.TRUE);
                //tvOrderdetailPrice.setLayoutParams(lp);
                GlideUtil.loadImg(mContext, order.avatar, ivOrderdetailPetimg, R.drawable.icon_production_default);
                Utils.setText(tvOrderdetailPettnickname, order.nickName, "", View.VISIBLE, View.GONE);
                Utils.setText(tvOrderdetailPetname, order.typeName, "", View.VISIBLE, View.GONE);
                if (order.myPetId > 0) {
                    llOrderdetailHljl.setVisibility(View.VISIBLE);
                } else {
                    llOrderdetailHljl.setVisibility(View.GONE);
                }
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
                if (Utils.isStrNull(order.hotelShopName)) {
                    order.orderTagList.add(new OrderTag(4, "寄养"));
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
                        } else if (order.orderTagList.get(position).getId() == 4) {
                            tv_item_ordertag.setTextColor(mContext.getResources().getColor(R.color.afc3962));
                            tv_item_ordertag.setBackgroundResource(R.drawable.bg_round_jy);
                        } else {
                            tv_item_ordertag.setTextColor(mContext.getResources().getColor(R.color.aFF3A1E));
                            tv_item_ordertag.setBackgroundResource(R.drawable.bg_round_df);
                        }
                        return view;
                    }
                };
                tflOrderdetailTag.setAdapter(tagAdapter);
                if (order.serviceMinsObjList.size() > 0) {
                    llOrderdetailFwsc.setVisibility(View.VISIBLE);
                    Utils.setText(tvOrderdetailFwsc, order.serviceMinsObjList.get(0).split(":")[1], "", View.VISIBLE, View.GONE);
                } else {
                    llOrderdetailFwsc.setVisibility(View.GONE);
                }
                Utils.setText(tvOrderdetailServicename, (Utils.isStrNull(order.typeName) ? order.typeName : "") + order.servicename, "", View.VISIBLE, View.GONE);
                Utils.setText(tvOrderdetailBaseservicename, order.servicecontent, "", View.VISIBLE, View.GONE);
                if (Utils.isStrNull(order.addserviceitems)) {
                    llOrderdetailItemservice.setVisibility(View.VISIBLE);
                    Utils.setText(tvOrderdetailItemservicename, order.addserviceitems, "", View.VISIBLE, View.GONE);
                } else {
                    llOrderdetailItemservice.setVisibility(View.GONE);
                }
                if (Utils.isStrNull(order.addserviceitemsadd)) {
                    llOrderdetailItemserviceAdd.setVisibility(View.VISIBLE);
                    Utils.setText(tvOrderdetailItemservicenameAdd, order.addserviceitemsadd, "", View.VISIBLE, View.GONE);
                } else {
                    llOrderdetailItemserviceAdd.setVisibility(View.GONE);
                }
                Utils.setText(tvOrderdetailUsername, order.username, "", View.VISIBLE, View.GONE);
                SpannableString ss = new SpannableString(order.userphone);
                // 用下划线标记文本
                ss.setSpan(new UnderlineSpan(), 0, ss.length(),
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                tvOrderdetailUserphone.setText(ss);
                Utils.setText(tvOrderdetailAppointmenttime, order.servicetime, "", View.VISIBLE, View.GONE);
                if (order.serviceloc == 1) {
                    Utils.setText(tvOrderdetailLxdz, order.storeaddr, "", View.VISIBLE, View.GONE);
                    daddr = order.storeaddr;
                } else {
                    Utils.setText(tvOrderdetailLxdz, order.addr, "", View.VISIBLE, View.GONE);
                    daddr = order.addr;
                }
                if (Utils.isStrNull(order.note)) {
                    llOrderdetailBz.setVisibility(View.VISIBLE);
                    Utils.setText(tvOrderdetailBz, order.note, "", View.VISIBLE, View.GONE);
                } else {
                    llOrderdetailBz.setVisibility(View.GONE);
                }
                if (Utils.isStrNull(order.bgRemarkToWorker)) {
                    llOrderdetailKfbz.setVisibility(View.VISIBLE);
                    Utils.setText(tvOrderdetailKfbz, order.bgRemarkToWorker, "", View.VISIBLE, View.GONE);
                } else {
                    llOrderdetailKfbz.setVisibility(View.GONE);
                }
                Utils.setText(tvOrderdetailDdbh, order.orderNo, "", View.VISIBLE, View.GONE);
                if (order.extraEnable == 0 && order.enable == 0) {
                    btTitlebarOther.setVisibility(View.GONE);
                } else {
                    btTitlebarOther.setVisibility(View.VISIBLE);
                    if (order.extraEnable == 1 && order.enable == 1) {
                        btTitlebarOther.setText("修改订单");
                    } else if (order.extraEnable == 1 && order.enable == 0) {
                        btTitlebarOther.setText("追加单项");
                    } else if (order.extraEnable == 0 && order.enable == 1) {
                        btTitlebarOther.setText("修改订单");
                    }
                }
                if (order.status == 2 || order.status == 21 || order.status == 22) {
                    btnOrderdetailSubmit.setVisibility(View.VISIBLE);
                    if (order.serviceloc == 1) {
                        switch (order.status) {
                            case 2:
                                btnOrderdetailSubmit.setText("开始服务");
                                break;
                            case 22:
                                btnOrderdetailSubmit.setText(order.status22);
                                break;
                            default:
                                break;
                        }
                    } else {
                        switch (order.status) {
                            case 2:
                                btnOrderdetailSubmit.setText(order.status2);
                                break;
                            case 21:
                                btnOrderdetailSubmit.setText(order.status21);
                                break;
                            case 22:
                                btnOrderdetailSubmit.setText(order.status22);
                                break;
                            default:
                                break;
                        }
                    }
                } else if ((order.status == 4 || order.status == 5) && order.careCount <= 0 && order.myPetId > 0) {
                    btnOrderdetailSubmit.setVisibility(View.VISIBLE);
                    btnOrderdetailSubmit.setText("填写护理记录");
                } else {
                    btnOrderdetailSubmit.setVisibility(View.GONE);
                }
                if (order.memberLevelId > 0) {
                    ivOrderdetailIsvip.setVisibility(View.VISIBLE);
                } else {
                    ivOrderdetailIsvip.setVisibility(View.GONE);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        CountdownUtil.getInstance().cancel("LOCATION_TIMER");
        if (mLClient != null) {
            mLClient.unRegisterLocationListener(mListener);
            mLClient.stop();
        }
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
    }
}
