package com.haotang.petworker.adapter;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.AddArchivesActivity;
import com.haotang.petworker.OrderDetailNewActivity;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.Order;
import com.haotang.petworker.entity.OrderTag;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.NiceImageView;
import com.haotang.petworker.view.ShadowLayout;
import com.haotang.petworker.view.TimeTextView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-08-30 14:34
 */
public class OrderNewAdapter extends BaseQuickAdapter<Order, BaseViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private int index;

    public OrderNewAdapter(int layoutResId, List<Order> data, int index) {
        super(layoutResId, data);
        this.index = index;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Order item) {
        TextView tv_neworder_statedesc = helper.getView(R.id.tv_neworder_statedesc);
        ImageView iv_neworder_isvip = helper.getView(R.id.iv_neworder_isvip);
        TextView tv_neworder_username = helper.getView(R.id.tv_neworder_username);
        TextView tv_neworder_time = helper.getView(R.id.tv_neworder_time);
        NiceImageView iv_neworder_petimg = helper.getView(R.id.iv_neworder_petimg);
        TextView tv_neworder_shichang = helper.getView(R.id.tv_neworder_shichang);
        TextView tv_neworder_servicename = helper.getView(R.id.tv_neworder_servicename);
        TagFlowLayout tfl_neworder_tag = helper.getView(R.id.tfl_neworder_tag);
        TextView tv_neworder_hljl = helper.getView(R.id.tv_neworder_hljl);
        LinearLayout ll_neworder_djs = helper.getView(R.id.ll_neworder_djs);
        TimeTextView ttv_neworder_djs = helper.getView(R.id.ttv_neworder_djs);
        TextView tv_neworder_shichang1 = helper.getView(R.id.tv_neworder_shichang1);
        TextView tv_neworder_hljldesc = helper.getView(R.id.tv_neworder_hljldesc);
        LinearLayout ll_neworder_hljl = helper.getView(R.id.ll_neworder_hljl);
        ShadowLayout ll_neworder_root = helper.getView(R.id.ll_neworder_root);
        View vw_neworder_username = helper.getView(R.id.vw_neworder_username);
        if (item != null) {
            Utils.setText(tv_neworder_statedesc, item.statusdes, "", View.VISIBLE, View.VISIBLE);
            if (item.memberLevelId > 0) {
                iv_neworder_isvip.setVisibility(View.VISIBLE);
                vw_neworder_username.setVisibility(View.GONE);
            } else {
                iv_neworder_isvip.setVisibility(View.GONE);
                vw_neworder_username.setVisibility(View.VISIBLE);
            }
            Utils.setText(tv_neworder_username, item.username, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_neworder_time, item.servicetime, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tv_neworder_servicename, item.petname + item.servicename, "", View.VISIBLE, View.VISIBLE);
            if (item.remainTime > 0) {
                ll_neworder_djs.setVisibility(View.VISIBLE);
                tv_neworder_shichang.setVisibility(View.GONE);
                ttv_neworder_djs.setTag(item.remainTime);
                ttv_neworder_djs.setText("倒计时 " + Utils.formatTime(item.remainTime));
                ttv_neworder_djs.setTimes(item.remainTime);
                Utils.setText(tv_neworder_shichang1, "(" + item.serviceMins + ")", "", View.VISIBLE, View.VISIBLE);
            } else {
                Utils.setText(tv_neworder_shichang, item.serviceMins, "", View.VISIBLE, View.GONE);
                ll_neworder_djs.setVisibility(View.GONE);
            }
            GlideUtil.loadImg(mContext, item.avatar, iv_neworder_petimg, R.drawable.icon_production_default);
            if ((item.status == 4 || item.status == 5) && item.carecount <= 0 && item.myPetId > 0) {
                ll_neworder_hljl.setVisibility(View.VISIBLE);
                if (index == 0) {
                    Utils.setText(tv_neworder_hljldesc, item.integral + item.integralCopy, "", View.VISIBLE, View.VISIBLE);
                } else {
                    tv_neworder_hljldesc.setVisibility(View.GONE);
                }
            } else {
                ll_neworder_hljl.setVisibility(View.GONE);
            }
            TagAdapter<OrderTag> tagAdapter = new TagAdapter<OrderTag>(item.orderTagList) {
                @Override
                public View getView(FlowLayout parent, final int position, OrderTag s) {
                    View view = (View) View.inflate(mContext, R.layout.item_ordertag,
                            null);
                    TextView tv_item_ordertag = (TextView) view.findViewById(R.id.tv_item_ordertag);
                    tv_item_ordertag.setText(item.orderTagList.get(position).getTag());
                    if (item.orderTagList.get(position).getId() == 0) {
                        tv_item_ordertag.setTextColor(mContext.getResources().getColor(R.color.aC29865));
                        tv_item_ordertag.setBackgroundResource(R.drawable.bg_dh_round);
                    } else if (item.orderTagList.get(position).getId() == 4) {
                        tv_item_ordertag.setTextColor(mContext.getResources().getColor(R.color.afc3962));
                        tv_item_ordertag.setBackgroundResource(R.drawable.bg_round_jy);
                    } else {
                        tv_item_ordertag.setTextColor(mContext.getResources().getColor(R.color.aFF3A1E));
                        tv_item_ordertag.setBackgroundResource(R.drawable.bg_round_df);
                    }
                    return view;
                }
            };
            tfl_neworder_tag.setAdapter(tagAdapter);
            tv_neworder_hljl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, AddArchivesActivity.class).putExtra("orderId", item.orderid));
                }
            });
            ll_neworder_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, OrderDetailNewActivity.class).putExtra("orderId", item.orderid));
                }
            });
        }
    }

    @Override
    public long getHeaderId(int position) {
        long headerId = 0;
        if (mData != null && mData.size() > 0 && getItem(position) != null) {
            headerId = getItem(position).headerId;
        }
        return headerId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_month_headview, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        if (index != 0) {
            if (mData != null && mData.size() > 0) {
                Order item = mData.get(position);
                if (item != null) {
                    textView.setVisibility(View.VISIBLE);
                    String[] split = item.servicetime.split(" ");
                    String time = item.servicetime;
                    if (split.length > 0) {
                        time = split[0];
                    }
                    Utils.setText(textView, time, "", View.VISIBLE, View.GONE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            } else {
                textView.setVisibility(View.GONE);
            }
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
