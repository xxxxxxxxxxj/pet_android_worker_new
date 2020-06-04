package com.haotang.petworker.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.ServiceOrderDetail;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/4/14
 * @Description:服务订单明细
 */
public class ServiceOrderDetailAdapter extends RecyclerView.Adapter<ServiceOrderDetailAdapter.MyViewHolder> {
    private Context context;
    private List<ServiceOrderDetail.DataBeanX.DataBean> list;

    public ServiceOrderDetailAdapter(Context context, List<ServiceOrderDetail.DataBeanX.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ServiceOrderDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_serviceorder_detail, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceOrderDetailAdapter.MyViewHolder holder, int position) {
        holder.tvItemData.setText(list.get(position).getDate());
        holder.tvOrderNum.setText(list.get(position).getOrderNum()+"单");
        holder.tvOrderMoney.setText(list.get(position).getTotalPrice()+"");
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rvOrderMore.setLayoutManager(layoutManager);
        holder.rvOrderMore.setNestedScrollingEnabled(false);
        if (list.get(position).getOrderNum()==0){
            holder.tvOrderNum.setVisibility(View.GONE);
        }else {
            holder.tvOrderNum.setVisibility(View.VISIBLE);
        }
        if (list.get(position).getServiceInfo()!=null&&list.get(position).getServiceInfo().size()>0){
            holder.rlRoot.setClickable(true);
            ServiceOrderMoreAdapter adapter = new ServiceOrderMoreAdapter(context, list.get(position).getServiceInfo());
            holder.rvOrderMore.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            holder.ivRight.setVisibility(View.VISIBLE);
            holder.vRoot.setVisibility(View.VISIBLE);
        }else {
            holder.rlRoot.setClickable(false);
            holder.ivRight.setVisibility(View.INVISIBLE);
            holder.vRoot.setVisibility(View.GONE);
        }
        //展开收起
        if (list.get(position).isSelected()){
            holder.vColor.setVisibility(View.VISIBLE);
            holder.tvItemData.setTextColor(Color.parseColor("#384359"));
            holder.tvOrderMoney.setTextColor(Color.parseColor("#384359"));
            holder.tvOrderNum.setTextColor(Color.parseColor("#384359"));
            holder.ivRight.setImageResource(R.drawable.icon_dwon_blue);
            holder.rvOrderMore.setVisibility(View.VISIBLE);
            holder.vLine.setVisibility(View.INVISIBLE);
        }else {
            holder.vColor.setVisibility(View.GONE);
            holder.tvItemData.setTextColor(Color.parseColor("#717985"));
            holder.tvOrderMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvOrderNum.setTextColor(Color.parseColor("#717985"));
            holder.ivRight.setImageResource(R.drawable.icon_right_gray);
            holder.rvOrderMore.setVisibility(View.GONE);
            holder.vLine.setVisibility(View.VISIBLE);
        }
        holder.vRoot.setOnClickListener(new View.OnClickListener() {
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

    private ItemClickListener listener;
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    public interface ItemClickListener{
        void onItemClick(int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View vColor;
        private TextView tvItemData;
        private TextView tvOrderNum;
        private TextView tvOrderMoney;
        private ImageView ivRight;
        private View vLine;
        private RecyclerView rvOrderMore;
        private RelativeLayout rlRoot;
        private View vRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            vColor = itemView.findViewById(R.id.tv_serviceincome_color);
            tvItemData = itemView.findViewById(R.id.tv_serviceincome_data);
            tvOrderNum = itemView.findViewById(R.id.tv_serviceincome_ordernum);
            tvOrderMoney = itemView.findViewById(R.id.tv_serviceincome_ordermoney);
            ivRight = itemView.findViewById(R.id.iv_serviceincome_right);
            vLine = itemView.findViewById(R.id.v_serviceordre_line);
            rvOrderMore = itemView.findViewById(R.id.rv_serviceorder_more);
            rlRoot = itemView.findViewById(R.id.rl_serviceorder_root);
            vRoot = itemView.findViewById(R.id.v_serviceorder_click);
        }
    }
}
