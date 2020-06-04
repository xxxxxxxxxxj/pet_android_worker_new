package com.haotang.petworker.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.UpdateData;
import com.haotang.petworker.utils.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-09 14:41
 */
public class UpdateConfirmAdapter extends BaseQuickAdapter<UpdateData, BaseViewHolder> {
    private int isVip;

    public UpdateConfirmAdapter(int layoutResId, List<UpdateData> data, int isVip) {
        super(layoutResId, data);
        this.isVip = isVip;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final UpdateData item) {
        TextView tv_item_upgradeorderconfirm_price = helper.getView(R.id.tv_item_upgradeorderconfirm_price);
        TextView tv_item_upgradeorderconfirm_name = helper.getView(R.id.tv_item_upgradeorderconfirm_name);
        TextView tv_item_upgradeorderconfirm_time = helper.getView(R.id.tv_item_upgradeorderconfirm_time);
        if (item != null) {
            Utils.setText(tv_item_upgradeorderconfirm_name, item.getName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_upgradeorderconfirm_time, item.getDuration() + "分钟", "", View.VISIBLE, View.VISIBLE);
            if (isVip > 0) {
                Utils.setText(tv_item_upgradeorderconfirm_price, "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
            } else {
                Utils.setText(tv_item_upgradeorderconfirm_price, "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
            }
        }
    }
}
