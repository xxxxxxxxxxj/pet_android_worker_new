package com.haotang.petworker.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.SetTimeConfig;
import com.haotang.petworker.utils.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-07-23 15:45
 */
public class WorkTimeAdapter extends BaseQuickAdapter<SetTimeConfig, BaseViewHolder> {

    public WorkTimeAdapter(int layoutResId, List<SetTimeConfig> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, SetTimeConfig item) {
        TextView tv_item_worktime_tag = helper.getView(R.id.tv_item_worktime_tag);
        TextView tv_item_worktime_zhou = helper.getView(R.id.tv_item_worktime_zhou);
        TextView tv_item_worktime_riqi = helper.getView(R.id.tv_item_worktime_riqi);
        View vw_item_worktime_jin = helper.getView(R.id.vw_item_worktime_jin);
        if (item != null) {
            Utils.setText(tv_item_worktime_zhou, item.getWeekDay(), "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_item_worktime_riqi, item.getDateTxt(), "", View.VISIBLE, View.VISIBLE);
            if (item.getIsToday().equals("true")) {
                tv_item_worktime_zhou.setTextColor(mContext.getResources().getColor(R.color.aFF3A1E));
                tv_item_worktime_riqi.setTextColor(mContext.getResources().getColor(R.color.aFF3A1E));
                vw_item_worktime_jin.setVisibility(View.VISIBLE);
            } else if (item.getIsToday().equals("false")) {
                tv_item_worktime_zhou.setTextColor(mContext.getResources().getColor(R.color.a384359));
                tv_item_worktime_riqi.setTextColor(mContext.getResources().getColor(R.color.a717985));
                vw_item_worktime_jin.setVisibility(View.GONE);
            }
            if (item.getType() == 1) {//上班
                tv_item_worktime_tag.setText("班");
                tv_item_worktime_tag
                        .setBackgroundResource(R.drawable.bg_red_circle);
            } else {//休息
                tv_item_worktime_tag.setText("假");
                tv_item_worktime_tag
                        .setBackgroundResource(R.drawable.bg_blue_circle);
            }
        }
    }
}
