package com.haotang.petworker.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.utils.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-02 18:26
 */
public class OrderTimeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public OrderTimeAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        TextView tv_ordertime_title = helper.getView(R.id.tv_ordertime_title);
        TextView tv_ordertime = helper.getView(R.id.tv_ordertime);
        if (item != null) {
            String[] split = item.split(":");
            Utils.setText(tv_ordertime_title, split[0], "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_ordertime, split[1], "", View.VISIBLE, View.VISIBLE);
        }
    }
}