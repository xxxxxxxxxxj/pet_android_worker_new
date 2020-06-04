package com.haotang.petworker.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.CareTag;
import com.haotang.petworker.utils.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/9/8 16:57
 */
public class ArchivesProblemTagAdapter extends BaseQuickAdapter<CareTag, BaseViewHolder> {

    public ArchivesProblemTagAdapter(int layoutResId, List<CareTag> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CareTag item) {
        TextView tv_item_addpetarchives_tag = helper.getView(R.id.tv_item_addpetarchives_tag);
        if (item != null) {
            if(item.isSelect()){
                tv_item_addpetarchives_tag.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_item_addpetarchives_tag.setBackgroundResource(R.drawable.bg_orange_round);
            }else{
                tv_item_addpetarchives_tag.setTextColor(mContext.getResources().getColor(R.color.a333333));
                tv_item_addpetarchives_tag.setBackgroundResource(R.drawable.bg_round_bbbborder);
            }
            Utils.setText(tv_item_addpetarchives_tag, item.getTag(), "", View.VISIBLE, View.VISIBLE);
        }
    }
}
