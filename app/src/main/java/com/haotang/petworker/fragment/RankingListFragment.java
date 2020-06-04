package com.haotang.petworker.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.haotang.petworker.R;
import com.haotang.petworker.adapter.RankingPagerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/7
 * @Description:
 */
public class RankingListFragment extends BaseFragment {

    private Activity mContext;
    private SlidingTabLayout tbRankingList;
    private ViewPager vpRankingPager;
    private List<String> list_Title;
    private List<Fragment> list_fragment;
    private TextView tvRankTitle;
    private RelativeLayout rlPopRoot;
    private int currentTabIndex;
    private FragmentManager childFragmentManager;
    private RankingPagerAdapter rankingPagerAdapter;
    private int position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onCreateView1");
        View view = inflater.inflate(R.layout.fragment_rankinglist,container,false);
        tbRankingList = view.findViewById(R.id.tl_rankinglist);
        vpRankingPager = view.findViewById(R.id.vp_rankinglist);
        tvRankTitle = view.findViewById(R.id.tv_titlebar_title);
        rlPopRoot = view.findViewById(R.id.rl_poproot_bg);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onViewCreated1");
        super.onViewCreated(view, savedInstanceState);
        list_Title = new ArrayList<>();
        list_fragment= new ArrayList<>();
        list_Title.add("本店排名");
        list_Title.add("平台排名");
        list_fragment.add(new RankingListDetailFragment());
        list_fragment.add(new RankingListDetailFragment());
        vpRankingPager.setOffscreenPageLimit(2);
        tbRankingList.setmTextSelectsize(tbRankingList.sp2px(18));
        rankingPagerAdapter = new RankingPagerAdapter(this.getChildFragmentManager(), mContext, list_fragment, list_Title);
        vpRankingPager.setAdapter(rankingPagerAdapter);
        tbRankingList.setViewPager(vpRankingPager);
        tvRankTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
            }
        });
        vpRankingPager.setCurrentItem(0);
        tbRankingList.setCurrentTab(currentTabIndex);
        vpRankingPager.setCurrentItem(currentTabIndex);
        tbRankingList.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentTabIndex = position;
                vpRankingPager.setCurrentItem(currentTabIndex);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        vpRankingPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                vpRankingPager.setCurrentItem(currentTabIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void showPop() {
        View popView = View.inflate(getActivity(),R.layout.dlg_choose_rank,null);
        TextView tvRank = popView.findViewById(R.id.tv_order_rank);
        TextView tvShop = popView.findViewById(R.id.tv_shop_rank);
        TextView tvRedpocket = popView.findViewById(R.id.tv_redpocket_rank);
        TextView tvClose = popView.findViewById(R.id.tv_rank_close);
        RelativeLayout rlDissmiss = popView.findViewById(R.id.rl_pop_dissmiss);
        final View parentView = View.inflate(getActivity(),R.layout.fragment_rankinglist,null);
        final PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM,0,0);
        rlPopRoot.setVisibility(View.VISIBLE);
        switch (position){
            case 0:
                tvRank.setTextColor(Color.parseColor("#FFFF3A1E"));
                break;
            case 1:
                tvRedpocket.setTextColor(Color.parseColor("#FFFF3A1E"));
                tvRank.setTextColor(Color.parseColor("#FF384359"));
                break;
            case 2:
                tvShop.setTextColor(Color.parseColor("#FFFF3A1E"));
                tvRank.setTextColor(Color.parseColor("#FF384359"));

                break;
        }
        tvRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRankTitle.setText("订单排名");
                position = 0;
                for (int i = 0; i < list_fragment.size(); i++) {
                    RankingListDetailFragment fragment = (RankingListDetailFragment) getChildFragmentManager().getFragments().get(i);
                    fragment.getData();
                }
                popupWindow.dismiss();
            }
        });
        tvRedpocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRankTitle.setText("红包订单");
                position = 1;
                for (int i = 0; i < list_fragment.size(); i++) {
                    RankingListDetailFragment fragment = (RankingListDetailFragment) getChildFragmentManager().getFragments().get(i);
                    fragment.getRedPocketRank();
                }
                popupWindow.dismiss();
            }
        });
        tvShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRankTitle.setText("商品订单");
                position = 2;
                for (int i = 0; i < list_fragment.size(); i++) {
                    RankingListDetailFragment fragment = (RankingListDetailFragment) getChildFragmentManager().getFragments().get(i);
                    fragment.getShopRank();
                }
                popupWindow.dismiss();
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlPopRoot.setVisibility(View.GONE);
            }
        });
        rlDissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }
}
