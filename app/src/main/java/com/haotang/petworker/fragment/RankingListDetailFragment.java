package com.haotang.petworker.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.petworker.R;
import com.haotang.petworker.adapter.RankingListAdapter;
import com.haotang.petworker.entity.RankingListDetail;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/7
 * @Description:
 */
public class RankingListDetailFragment extends BaseFragment {

    private Activity mContext;
    private MProgressDialog pDialog;
    private ImageView ivRankingHead;
    private TextView tvRankingName;
    private TextView tvRankingNum;
    private TextView tvOrderNum;
    private TextView tvOrderTip;
    private RecyclerView rvRankingList;
    private RankingListDetail.DataBean listDetailData;
    private List<RankingListDetail.DataBean.ListBean> cityList = new ArrayList<>();
    private List<RankingListDetail.DataBean.ShopListBean> shopList = new ArrayList<>();
    private RankingListAdapter adapter;
    private TextView tvNone;
    private String noRanking;
    private int type;
    private int tag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onCreateView2");
        View view = inflater.inflate(R.layout.fragment_rankinglist_detail, container, false);
        ivRankingHead = view.findViewById(R.id.iv_ranking_head);
        tvRankingName = view.findViewById(R.id.tv_ranking_name);
        tvRankingNum = view.findViewById(R.id.tv_ranking_num);
        tvOrderNum = view.findViewById(R.id.tv_ranking_order);
        rvRankingList = view.findViewById(R.id.rv_ranking_list);
        tvNone = view.findViewById(R.id.tv_ranking_none);
        tvOrderTip = view.findViewById(R.id.tv_rankingorder_tip);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
        pDialog = new MProgressDialog(mContext);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        Bundle bundle = getArguments();
        tag = bundle.getInt("tag");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRankingList.setLayoutManager(layoutManager);
        adapter = new RankingListAdapter(getActivity(), tag);
        rvRankingList.setAdapter(adapter);

    }

    public void getData() {
        type = 0;
        pDialog.showDialog();
        CommUtil.rankinglList(mContext, 0, handler);
    }

    public void getShopRank() {
        type = 1;
        pDialog.showDialog();
        CommUtil.mallRewardlList(mContext, 0, handler);
    }

    public void getRedPocketRank() {
        type = 2;
        pDialog.showDialog();
        CommUtil.gratuityRankingList(mContext, 0, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            JSONObject jobj = null;
            pDialog.closeDialog();
            shopList.clear();
            cityList.clear();
            try {
                jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (resultCode == 0) {
                    Gson gson = new Gson();
                    RankingListDetail rankingListDetail = gson.fromJson(new String(responseBody), RankingListDetail.class);
                    listDetailData = rankingListDetail.getData();
                    if (listDetailData != null) {
                        RankingListDetail.DataBean.WorkerInfoBean workerInfo = listDetailData.getWorkerInfo();
                        GlideUtil.loadImg(getActivity(), workerInfo.getAvatar(), ivRankingHead, R.drawable.icon_beautician_default);
                        tvRankingName.setText(workerInfo.getRealName());
                        tvOrderNum.setText("¥ " + listDetailData.getCityAmount());
                        tvRankingNum.setText(listDetailData.getShopNum() + "");
                        noRanking = listDetailData.getNoRanking();
                        cityList = listDetailData.getList();
                        shopList = listDetailData.getShopList();
                        if (tag == 0) {
                            if (listDetailData != null) {
                                if (listDetailData.getShopNum()==0){
                                    tvRankingNum.setText("暂无排名");
                                }else {
                                    tvRankingNum.setText(listDetailData.getShopNum() + "");
                                }
                                if (shopList.size() != 0) {
                                    adapter.setShopList(shopList);
                                    rvRankingList.setVisibility(View.VISIBLE);
                                    tvNone.setVisibility(View.GONE);
                                } else {
                                    rvRankingList.setVisibility(View.GONE);
                                    tvNone.setVisibility(View.VISIBLE);
                                    tvNone.setText(noRanking);
                                }

                            }
                        } else if (tag == 1) {
                            if (listDetailData != null) {
                                if (listDetailData.getCityNum()==0){
                                    tvRankingNum.setText("暂无排名");
                                }else{
                                    tvRankingNum.setText(listDetailData.getCityNum() + "");
                                }
                                if (cityList.size() != 0) {
                                    adapter.setShopList(shopList);
                                    rvRankingList.setVisibility(View.VISIBLE);
                                    adapter.setCityList(cityList);
                                } else {
                                    rvRankingList.setVisibility(View.GONE);
                                    tvNone.setVisibility(View.VISIBLE);
                                    tvNone.setText(noRanking);
                                }
                            }
                        }
                        /*if (shopList.size() == 0 || shopList == null) {
                            adapter.setShopList(shopList);
                        } else {
                            shopList = listDetailData.getShopList();
                            adapter.setShopList(shopList);
                        }
                        if (shopList.size() != 0) {
                            tvNone.setVisibility(View.GONE);
                        } else {
                            rvRankingList.setVisibility(View.GONE);
                            tvNone.setVisibility(View.VISIBLE);
                            tvNone.setText(noRanking);
                        }*/
                        if (type == 0) {
                            tvOrderTip.setText("结算订单金额：");
                        } else if (type == 1) {
                            tvOrderTip.setText("商品提成：");
                        } else {
                            tvOrderTip.setText("红包：");
                        }
                    }

                } else {
                    ToastUtil.showToastBottomShort(getActivity(), msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            pDialog.closeDialog();
            ToastUtil.showToastBottomShort(getActivity(), "请求失败");
        }
    };
}
