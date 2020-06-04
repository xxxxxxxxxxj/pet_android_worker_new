package com.haotang.petworker.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.EatTimeBean;
import com.haotang.petworker.utils.Utils;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-16 10:38
 */
public class EatTimeAdapter extends BaseQuickAdapter<EatTimeBean, BaseViewHolder> {
    private boolean isEdit;

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    public EatTimeAdapter(int layoutResId, List<EatTimeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EatTimeBean item) {
        TextView tv_item_eattime_str = helper.getView(R.id.tv_item_eattime_str);
        LinearLayout ll_item_eattime_switch = helper.getView(R.id.ll_item_eattime_switch);
        ImageView iv_item_eattime_switch = helper.getView(R.id.iv_item_eattime_switch);
        TextView tv_item_eattime_today = helper.getView(R.id.tv_item_eattime_today);
        LinearLayout ll_item_eattime_day = helper.getView(R.id.ll_item_eattime_day);
        TextView tv_item_eattime_day = helper.getView(R.id.tv_item_eattime_day);
        if (item != null) {
            if (item.getLunchSwitch() == 1) {//打开
                tv_item_eattime_str.setText("开启");
            } else if (item.getLunchSwitch() == 0) {//关闭
                tv_item_eattime_str.setText("关闭");
            }
            if (isEdit) {//编辑
                ll_item_eattime_switch.setVisibility(View.VISIBLE);
                if (item.getLunchSwitch() == 1) {//打开
                    iv_item_eattime_switch.setImageResource(R.drawable.switch_open);
                } else if (item.getLunchSwitch() == 0) {//关闭
                    iv_item_eattime_switch.setImageResource(R.drawable.switch_close);
                }
                iv_item_eattime_switch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getLunchSwitch() == 1) {
                            item.setLunchSwitch(0);
                        } else if (item.getLunchSwitch() == 0) {
                            item.setLunchSwitch(1);
                        }
                        notifyDataSetChanged();
                    }
                });
            } else {//不编辑
                ll_item_eattime_switch.setVisibility(View.GONE);
            }
            if (item.getIsToday() == 1) {//今日
                tv_item_eattime_today.setVisibility(View.VISIBLE);
                ll_item_eattime_day.setVisibility(View.GONE);
            } else if (item.getIsToday() == 0) {//非今日
                tv_item_eattime_today.setVisibility(View.GONE);
                ll_item_eattime_day.setVisibility(View.VISIBLE);
                Utils.setText(tv_item_eattime_day, item.getDay(), "", View.VISIBLE, View.VISIBLE);
            }
        }
    }
}
