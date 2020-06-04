package com.haotang.petworker.adapter;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.Production;
import com.haotang.petworker.utils.GlideUtil;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/4
 * @Description:
 */
public class MyProductionAdapter extends BaseQuickAdapter<Production, BaseViewHolder> {
    public MyProductionAdapter(int layoutResId, @Nullable List<Production> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Production item) {
        ImageView ivProduction = helper.getView(R.id.iv_production_icon);
        TextView tvProductionTitle = helper.getView(R.id.tv_production_name);
        TextView tvData = helper.getView(R.id.tv_production_data);
        TextView tvAudit = helper.getView(R.id.tv_production_audit);
        GlideUtil.loadImg(mContext,item.smallimage,ivProduction,R.drawable.icon_production_default);
        tvProductionTitle.setText(item.title);
        tvData.setText(item.time);
        if (item.auditState==0){//待审核
            tvAudit.setVisibility(View.VISIBLE);
            tvAudit.setText("待审核");
        }else if (item.auditState==2){//审核拒绝
            tvAudit.setVisibility(View.VISIBLE);
            tvAudit.setText("未通过");
        }else {
            tvAudit.setVisibility(View.GONE);
        }
    }



    /*private Context context;
    private ArrayList<Production> list= new ArrayList<>();

    public MyProductionAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Production> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyProductionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_myproduction,null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyProductionAdapter.MyViewHolder holder, int position) {
        GlideUtil.loadImg(context,list.get(position).smallimage,holder.ivProduction,R.drawable.icon_production_default);
        holder.tvProductionTitle.setText(list.get(position).title);
        holder.tvData.setText(list.get(position).time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduction;
        private TextView tvProductionTitle;
        private TextView tvData;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivProduction =itemView.findViewById(R.id.iv_production_icon);
            tvProductionTitle = itemView.findViewById(R.id.tv_production_name);
            tvData = itemView.findViewById(R.id.tv_production_data);
        }
    }*/
}
