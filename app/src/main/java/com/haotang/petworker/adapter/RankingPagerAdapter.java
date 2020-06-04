package com.haotang.petworker.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/8/23
 * @Description:
 */
public class RankingPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragmentList;
    private List<String> list_Title;

    public RankingPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<String> list_Title) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position%fragmentList.size());
        Bundle bundle = new Bundle();
        bundle.putInt("tag",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    /**
     * //此方法用来显示tab上的名字
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }
}