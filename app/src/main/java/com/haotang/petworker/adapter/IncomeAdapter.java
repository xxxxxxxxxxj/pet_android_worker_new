package com.haotang.petworker.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.NewIncome;
import com.haotang.petworker.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/11/1
 * @Description:本月收入适配器
 */
public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.MyViewHolder> {

    private List<NewIncome.DataBean.ListBeanXX> list = new ArrayList<>();
    private Context context;

    public IncomeAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<NewIncome.DataBean.ListBeanXX> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public IncomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IncomeAdapter.MyViewHolder holder, int position) {
        Utils.setText(holder.tv_item_income_title, list.get(position).getName(), "", View.VISIBLE, View.VISIBLE);
        Utils.setText(holder.tv_income_desc, list.get(position).getContainInfo(), "", View.VISIBLE, View.VISIBLE);
        Utils.setText(holder.tv_item_income_income, list.get(position).getAmount(), "", View.VISIBLE, View.VISIBLE);
        holder.ll_item_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick((position + 1) % 4, list.get(position));
            }
        });
        //通过下标设置颜色
        switch ((position + 1) % 4) {
            case 1:
                holder.tv_item_income_color.setBackgroundResource(R.drawable.bg_round1);
                break;
            case 2:
                holder.tv_item_income_color.setBackgroundResource(R.drawable.bg_round2);
                break;
            case 3:
                holder.tv_item_income_color.setBackgroundResource(R.drawable.bg_round3);
                break;
            case 0:
                holder.tv_item_income_color.setBackgroundResource(R.drawable.bg_round4);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int position, NewIncome.DataBean.ListBeanXX data);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_income_color;
        TextView tv_item_income_title;
        TextView tv_item_income_income;
        TextView tv_income_desc;
        LinearLayout ll_item_income;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_income_color = itemView.findViewById(R.id.tv_item_income_color);
            tv_item_income_title = itemView.findViewById(R.id.tv_item_income_title);
            tv_item_income_income = itemView.findViewById(R.id.tv_item_income_income);
            tv_income_desc = itemView.findViewById(R.id.tv_income_desc);
            ll_item_income = itemView.findViewById(R.id.ll_item_income);
        }
    }
}