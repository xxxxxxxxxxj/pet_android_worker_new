package com.haotang.petworker.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.RankingListDetail;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.view.NiceImageView;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/7
 * @Description:排行榜详情适配器
 */
public class RankingListAdapter extends RecyclerView.Adapter<RankingListAdapter.MyViewHolder> {

    private Context context;
    private int tag;
    private List<RankingListDetail.DataBean.ListBean> cityList;
    private List<RankingListDetail.DataBean.ShopListBean> shopList;

    public RankingListAdapter(Context context, int tag) {
        this.context = context;
        this.tag = tag;
    }

    public void setCityList(List<RankingListDetail.DataBean.ListBean> cityList) {
        this.cityList = cityList;
        notifyDataSetChanged();
    }

    public void setShopList(List<RankingListDetail.DataBean.ShopListBean> shopList) {
        this.shopList = shopList;
        notifyDataSetChanged();
    }

    @Override
    public RankingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ranking_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RankingListAdapter.MyViewHolder holder, int position) {
        if (tag == 0) {
            GlideUtil.loadImg(context, shopList.get(position).getAvatar(), holder.ivRankHead, R.drawable.icon_beautician_default);
            holder.tvRankName.setText(shopList.get(position).getRealName());
            holder.tvRankShop.setText(shopList.get(position).getShopName());
            holder.tvRankMoney.setText(shopList.get(position).getIncome()+"");
        } else if (tag == 1) {
            GlideUtil.loadImg(context, cityList.get(position).getAvatar(), holder.ivRankHead, R.drawable.icon_beautician_default);
            holder.tvRankName.setText(cityList.get(position).getRealName());
            holder.tvRankShop.setText(cityList.get(position).getShopName());
            holder.tvRankMoney.setText(cityList.get(position).getIncome()+"");
        }
        if (position == 0) {
            holder.ivRank.setVisibility(View.VISIBLE);
            holder.tvRankNum.setVisibility(View.GONE);
            holder.ivRank.setImageResource(R.drawable.icon_rank_noe);
            holder.rlRoot.setBackgroundResource(R.drawable.bg_ff1f2_topround);
            holder.tvRankMoney.setTextColor(Color.parseColor("#FF3A1E"));
            holder.tvRankShop.setTextColor(Color.parseColor("#FF3A1E"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#FF3A1E"));
            holder.tvRankName.setTextColor(Color.parseColor("#FF3A1E"));
            holder.tvRankRmb.setTextColor(Color.parseColor("#FF3A1E"));
        } else if (position == 1) {
            holder.ivRank.setVisibility(View.VISIBLE);
            holder.tvRankNum.setVisibility(View.GONE);
            holder.ivRank.setImageResource(R.drawable.icon_rank_two);
            holder.rlRoot.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvRankShop.setTextColor(Color.parseColor("#717985"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvRankName.setTextColor(Color.parseColor("#384359"));
            holder.tvRankRmb.setTextColor(Color.parseColor("#717985"));

        }else if (position==2){
            holder.ivRank.setVisibility(View.VISIBLE);
            holder.tvRankNum.setVisibility(View.GONE);
            holder.ivRank.setImageResource(R.drawable.icon_rank_three);
            holder.rlRoot.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvRankShop.setTextColor(Color.parseColor("#717985"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvRankName.setTextColor(Color.parseColor("#384359"));
            holder.tvRankRmb.setTextColor(Color.parseColor("#717985"));
        }else if(position<9){
            holder.ivRank.setVisibility(View.INVISIBLE);
            holder.tvRankNum.setVisibility(View.VISIBLE);
            holder.tvRankNum.setText("0"+(position+1));
            holder.rlRoot.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvRankShop.setTextColor(Color.parseColor("#717985"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvRankName.setTextColor(Color.parseColor("#384359"));
            holder.tvRankRmb.setTextColor(Color.parseColor("#717985"));
        }else {
            holder.ivRank.setVisibility(View.INVISIBLE);
            holder.tvRankNum.setVisibility(View.VISIBLE);
            holder.tvRankNum.setText((position+1)+"");
            holder.rlRoot.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvRankShop.setTextColor(Color.parseColor("#717985"));
            holder.tvRankMoney.setTextColor(Color.parseColor("#717985"));
            holder.tvRankName.setTextColor(Color.parseColor("#384359"));
            holder.tvRankRmb.setTextColor(Color.parseColor("#717985"));
        }
    }

    @Override
    public int getItemCount() {
        if (tag == 0) {
            return shopList == null ? 0 : shopList.size();
        } else if (tag == 1) {
            return cityList == null ? 0 : cityList.size();
        }
        return shopList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlRoot;
        private ImageView ivRank;
        private NiceImageView ivRankHead;
        private TextView tvRankName;
        private TextView tvRankShop;
        private TextView tvRankMoney;
        private TextView tvRankNum;
        private TextView tvRankRmb;

        public MyViewHolder(View itemView) {
            super(itemView);
            rlRoot = itemView.findViewById(R.id.rl_ranking_root);
            ivRank = itemView.findViewById(R.id.iv_ranking_rank);
            ivRankHead = itemView.findViewById(R.id.iv_ranking_head);
            tvRankName = itemView.findViewById(R.id.tv_ranking_name);
            tvRankShop = itemView.findViewById(R.id.tv_ranking_shop);
            tvRankMoney = itemView.findViewById(R.id.tv_ranking_money);
            tvRankNum = itemView.findViewById(R.id.tv_ranking_num);
            tvRankRmb = itemView.findViewById(R.id.tv_ranking_rmb);
        }
    }
}
