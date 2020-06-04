package com.haotang.petworker.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.ServiceOrderDetail;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2020/4/14
 * @Description:服务订单点击展开
 */
public class ServiceOrderMoreAdapter extends RecyclerView.Adapter<ServiceOrderMoreAdapter.MyViewHolder> {

    private Context context;
    private List<ServiceOrderDetail.DataBeanX.DataBean.ServiceInfoBean> list;

    public ServiceOrderMoreAdapter(Context context, List<ServiceOrderDetail.DataBeanX.DataBean.ServiceInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ServiceOrderMoreAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_serviceorder_detailmore, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceOrderMoreAdapter.MyViewHolder holder, int position) {
        holder.tvOrderTime.setText(list.get(position).getAppointment());
        holder.tvOrderName.setText(list.get(position).getServiceName());
        holder.tvOrderMoney.setText(list.get(position).getServicePrice()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderTime;
        private TextView tvOrderName;
        private TextView tvOrderMoney;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvOrderTime = itemView.findViewById(R.id.tv_serviceincome_moretime);
            tvOrderName = itemView.findViewById(R.id.tv_serviceincome_morename);
            tvOrderMoney = itemView.findViewById(R.id.tv_serviceincome_moremoney);
        }
    }
}
