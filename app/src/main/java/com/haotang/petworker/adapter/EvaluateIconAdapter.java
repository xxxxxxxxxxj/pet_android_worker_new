package com.haotang.petworker.adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.petworker.R;
import com.haotang.petworker.utils.GlideUtil;

import java.util.List;


/**
 * @author:姜谷蓄
 * @Date:2019/9/6
 * @Description:
 */
public class EvaluateIconAdapter extends RecyclerView.Adapter<EvaluateIconAdapter.MyViewHolder>{

    private Context context;
    private List<String> list;

    public EvaluateIconAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public EvaluateIconAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(context, R.layout.item_evaluate_icon,null);
        View view = LayoutInflater.from(context).inflate(R.layout.item_evaluate_icon,parent,false);
        MyViewHolder viewHolder  = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EvaluateIconAdapter.MyViewHolder holder, final int position) {
        GlideUtil.loadImg(context,list.get(position),holder.ivIcon,R.drawable.icon_production_default);
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_evaluate_icon);
        }
    }
    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener{
        void onItemClick(int position);
    }

}
