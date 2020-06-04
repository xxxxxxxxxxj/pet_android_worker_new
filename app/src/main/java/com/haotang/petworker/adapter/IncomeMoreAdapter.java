package com.haotang.petworker.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.NewIncome;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.AlertDialogDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/1
 * @Description:薪资明细更多适配器
 */
public class IncomeMoreAdapter extends RecyclerView.Adapter<IncomeMoreAdapter.MyViewHolder> {
    private Activity context;
    private List<NewIncome.DataBean.ListBeanXX.ListBeanX.ListBean> list = new ArrayList<>();

    public void setList(List<NewIncome.DataBean.ListBeanXX.ListBeanX.ListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public IncomeMoreAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public IncomeMoreAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_income_more,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IncomeMoreAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.tvIncome.setText(list.get(position).getAmount());
        if (!TextUtils.isEmpty(list.get(position).getOtherDates())){
            holder.hintText.setVisibility(View.VISIBLE);
            holder.hintText.setText(list.get(position).getOtherDates());
        }
        if (Utils.isStrNull(list.get(position).getInstro())){
            holder.ivTip.setVisibility(View.VISIBLE);
        }else {
            holder.ivTip.setVisibility(View.GONE);
        }
        holder.ivTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialogDefault(context).builder()
                        .setTitle(list.get(position).getName()+"原因").setMsg(list.get(position).getInstro()).isOneBtn(true).setPositiveButton("我知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivTip;
        private TextView tvIncome;
        private TextView hintText;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_income_more_name);
            ivTip = itemView.findViewById(R.id.iv_income_more_tip);
            tvIncome = itemView.findViewById(R.id.tv_income_more_income);
            hintText = itemView.findViewById(R.id.hint_text);
        }
    }
}
