package com.haotang.petworker.adapter;

import android.view.View;
import android.widget.ImageView;
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
 * @date zhoujunxia on 2018/9/7 17:28
 */
public class UpdateAdapter extends BaseQuickAdapter<UpdateData, BaseViewHolder> {
    private int isVip;
    public OnAddClickListener onAddClickListener = null;

    public interface OnAddClickListener {
        public void OnAddClick(int position);
    }

    public void setOnAddClickListener(
            OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public UpdateAdapter(int layoutResId, List<UpdateData> data, int isVip) {
        super(layoutResId, data);
        this.isVip = isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final UpdateData item) {
        ImageView iv_item_update_add = helper.getView(R.id.iv_item_update_add);
        TextView tv_item_update_name = helper.getView(R.id.tv_item_update_name);
        TextView tv_item_update_time = helper.getView(R.id.tv_item_update_time);
        TextView tv_item_update_price = helper.getView(R.id.tv_item_update_price);
        if (item != null) {
            if (item.isSelect()) {
                iv_item_update_add.setImageResource(R.drawable.icon_service_sub);
            } else {
                iv_item_update_add.setImageResource(R.drawable.icon_service_add);
            }
            iv_item_update_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddClickListener != null) {
                        onAddClickListener.OnAddClick(helper.getLayoutPosition());
                    }
                }
            });
            Utils.setText(tv_item_update_name, item.getName(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_update_time, item.getDuration() + "分钟", "", View.VISIBLE, View.VISIBLE);
            if (isVip > 0) {
                Utils.setText(tv_item_update_price, "¥" + item.getVipPrice(), "", View.VISIBLE, View.VISIBLE);
            } else {
                Utils.setText(tv_item_update_price, "¥" + item.getPrice(), "", View.VISIBLE, View.VISIBLE);
            }
        }
    }
}
