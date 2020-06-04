package com.haotang.petworker.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.petworker.R;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/24
 * @Description:积分详情图片适配器
 */
public class IntegralIconAdapter extends RecyclerView.Adapter<IntegralIconAdapter.MyViewHolder> {

    private Context context;
    private List<Integer> list;

    public IntegralIconAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public IntegralIconAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_integral_icon,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IntegralIconAdapter.MyViewHolder holder, int position) {
        holder.ivIntegral.setImageResource(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIntegral;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivIntegral = itemView.findViewById(R.id.iv_integral_icon);
        }
    }
}
