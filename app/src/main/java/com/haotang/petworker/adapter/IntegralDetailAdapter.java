package com.haotang.petworker.adapter;

import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.IntegralDerail;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/10
 * @Description:
 */
public class IntegralDetailAdapter extends BaseQuickAdapter<IntegralDerail.DataBean.IntegralWaterBillBean, BaseViewHolder> {
    public IntegralDetailAdapter(int layoutResId, @Nullable List<IntegralDerail.DataBean.IntegralWaterBillBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralDerail.DataBean.IntegralWaterBillBean item) {
        TextView tvType = helper.getView(R.id.tv_integral_type);
        TextView tvTime = helper.getView(R.id.tv_integral_time);
        TextView tvPoint = helper.getView(R.id.tv_integral_point);
        tvType.setText(item.getName());
        tvTime.setText(item.getCreated());
        tvPoint.setText(item.getAmountStr());
    }
}
