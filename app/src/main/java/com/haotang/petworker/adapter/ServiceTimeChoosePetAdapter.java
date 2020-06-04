package com.haotang.petworker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.NewPetBean;
import com.haotang.petworker.utils.ImageLoaderUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.CommonAdapter;
import com.haotang.petworker.view.SelectableRoundedImageView;
import com.haotang.petworker.view.ViewHolder;

import java.util.List;

/**
 * <p>
 * Title:ServiceTimeChoosePetAdapter
 * </p>
 * <p>
 * Description:调整服务时长宠物列表适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2017-5-16 上午11:31:22
 */
public class ServiceTimeChoosePetAdapter<T> extends CommonAdapter<T> implements SectionIndexer {

    public ServiceTimeChoosePetAdapter(Context mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_servicetime_choosepet, position);
        SelectableRoundedImageView sriv_servicetimepet_img = viewHolder
                .getView(R.id.sriv_servicetimepet_img);
        TextView tv_servicetimepet_name = viewHolder
                .getView(R.id.tv_servicetimepet_name);
        TextView tv_servicetimepet_tc = viewHolder
                .getView(R.id.tv_servicetimepet_tc);
        TextView catalog = viewHolder
                .getView(R.id.catalog);
        NewPetBean newPetBean = (NewPetBean) mDatas.get(position);
        if (newPetBean != null) {
            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                catalog.setVisibility(View.VISIBLE);
                catalog.setText(newPetBean.getSortLetters());
            } else {
                catalog.setVisibility(View.GONE);
            }
            ImageLoaderUtil.loadImg(newPetBean.avatarPath,
                    sriv_servicetimepet_img,
                    R.drawable.icon_beautician_default, null);
            Utils.setText(tv_servicetimepet_name, newPetBean.petName.replace("zang", "藏"), "",
                    View.VISIBLE, View.GONE);
        }
        return viewHolder.getConvertView();
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        NewPetBean newPetBean = (NewPetBean) mDatas.get(position);
        return newPetBean.getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            NewPetBean newPetBean = (NewPetBean) mDatas.get(i);
            String sortStr = newPetBean.getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
