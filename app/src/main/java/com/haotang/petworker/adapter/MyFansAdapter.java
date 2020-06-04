package com.haotang.petworker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.MyFans;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.view.CommonAdapter;
import com.haotang.petworker.view.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public class MyFansAdapter<T> extends CommonAdapter<T> {

    public MyFansAdapter(Context mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyFans myFans = (MyFans) mDatas.get(position);
        ViewHolder viewHolder = ViewHolder.get(mContext,convertView,parent, R.layout.item_my_fans,position);
        ImageView img_icon = viewHolder.getView(R.id.img_icon);
        GlideUtil.loadCircleImg(mContext,myFans.avatar,img_icon,R.drawable.dog_icon_unnet);
        viewHolder.setText(R.id.textview_username,myFans.userName);
        viewHolder.setText(R.id.textview_dogname,myFans.nickName);
        viewHolder.setText(R.id.textview_tag,myFans.isBuyContent);
        viewHolder.setVisibility(R.id.img_vip_tag,View.GONE);
        if (myFans.memberLevelId>0){
            viewHolder.setVisibility(R.id.img_vip_tag,View.VISIBLE);
        }
        TextView textview_tag = viewHolder.getView(R.id.textview_tag);
        if (myFans.isBuy==0){
            viewHolder.setTextColor(R.id.textview_tag,"#FF3A1E");
            textview_tag.setBackgroundResource(R.drawable.bg_fanstag_red);
        }else if (myFans.isBuy==1){
            viewHolder.setTextColor(R.id.textview_tag,"#C29865");
            textview_tag.setBackgroundResource(R.drawable.bg_fanstag_yellow);
        }
        viewHolder.setVisibility(R.id.textview_appoint_nums,View.GONE);
        if (!TextUtils.isEmpty(myFans.completeServiceContent)){
            viewHolder.setVisibility(R.id.textview_appoint_nums,View.VISIBLE);
            viewHolder.setText(R.id.textview_appoint_nums,myFans.completeServiceContent);
        }
        return viewHolder.getConvertView();
    }
}
