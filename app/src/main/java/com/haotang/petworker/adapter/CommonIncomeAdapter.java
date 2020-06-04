package com.haotang.petworker.adapter;

import android.content.Context;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.ScheduleDay;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/4/15
 * @Description:基本薪资头部 */
public class CommonIncomeAdapter extends BaseQuickAdapter<ScheduleDay, CommonIncomeAdapter.MyViewHolder> {

    private Context context;
    private MyViewHolder myViewHolder;

    public CommonIncomeAdapter(int layoutResId, @Nullable List<ScheduleDay> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(MyViewHolder helper, ScheduleDay item) {
        helper.display(item);
        myViewHolder = helper;
    }

    public int getChildHeight(){
        return myViewHolder.getChildHeight();
    }

    public class MyViewHolder extends BaseViewHolder {
        private View vColor;
        private TextView tvCommonTip;
        private TextView tvCommonDay;
        private View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            vColor = itemView.findViewById(R.id.v_income_color);
            tvCommonTip = itemView.findViewById(R.id.tv_incomecommon_tip);
            tvCommonDay = itemView.findViewById(R.id.tv_commonincome_day);
        }
        private void display(ScheduleDay data){
            vColor.setBackgroundResource(mData.indexOf(data)%2==0? R.drawable.bg_round1 : R.drawable.bg_round_5);
            tvCommonTip.setText(data.getName());
            tvCommonDay.setText(TextUtils.concat(data.getDays()));
        }

        private int getChildHeight(){
            return this.itemView.getHeight();
        }
    }
}
