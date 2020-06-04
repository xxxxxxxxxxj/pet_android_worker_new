package com.haotang.petworker.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-08-30 10:55
 */
public class OrderViewPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private ArrayList<Fragment> mFragments;

    public OrderViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, String[] titles) {
        super(fm);
        this.mFragments = mFragments;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}