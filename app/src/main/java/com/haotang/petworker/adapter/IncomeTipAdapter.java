package com.haotang.petworker.adapter;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.NewIncome;
import com.haotang.petworker.entity.WorkerRotaltyInfoMode;
import com.haotang.petworker.utils.SuperTextView;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.AlertDialogDefault;
import com.haotang.petworker.view.ShadowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author:姜谷蓄
 * @Date:2019/9/2
 * @Description:收入明细详情页一级列表适配器
 */
public class IncomeTipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private Integer bgColor;
    private List<NewIncome.DataBean.ListBeanXX.ListBeanX> list = new ArrayList<>();

    public IncomeTipAdapter(Activity context, Integer bgColor) {
        this.context = context;
        this.bgColor = bgColor;
    }

    public void setList(List<NewIncome.DataBean.ListBeanXX.ListBeanX> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_income_server_order, parent, false);
            viewHolder = new ServiceHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_income_tip, parent, false);
            viewHolder = new MyViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        //服务订单布局
        NewIncome.DataBean.ListBeanXX.ListBeanX listBeanX = this.list.get(position);
        if (listBeanX.getWorkerRotaltyInfoMap() != null) {
            return 1;
        }

        //常规布局
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0)
            ((MyViewHolder) holder).display(list.get(position));
        else {
            ((ServiceHolder) holder).display(list.get(position));
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
        void onItemClick(NewIncome.DataBean.ListBeanXX.ListBeanX data);
        void onDetailClick(NewIncome.DataBean.ListBeanXX.ListBeanX data);
    }

    /**
     * 服务订单
     */
    public class ServiceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_income_desc)
        ImageView ivItemIncomeDesc;
        @BindView(R.id.super_tv_next)
        SuperTextView superTvNext;
        @BindView(R.id.detail_order)
        LinearLayout detailOrder;
        @BindView(R.id.tv_item_income_color)
        TextView tvItemIncomeColor;
        @BindView(R.id.tv_item_income_title)
        TextView tvItemIncomeTitle;
        @BindView(R.id.tv_item_income_income)
        TextView tvItemIncomeIncome;
        @BindView(R.id.tv_ratio)
        SuperTextView tvRatio;
        @BindView(R.id.tv_next_ratio)
        SuperTextView tvNextRatio;
        @BindView(R.id.progress_tv)
        SuperTextView progressTv;
        @BindView(R.id.distance)
        LinearLayout distance;
        @BindView(R.id.point_first)
        ShadowLayout pointFirst;
        @BindView(R.id.point_second)
        ShadowLayout pointSecond;
        @BindView(R.id.current_money)
        TextView currentMoney;
        @BindView(R.id.next_money)
        TextView nextMoney;
        @BindView(R.id.root_one)
        RelativeLayout rootOne;
        @BindView(R.id.tv_changeplace_tip)
        TextView tvChangeplaceTip;
        @BindView(R.id.v_color_old)
        View vColorOld;
        @BindView(R.id.tv_changeplace_oldname)
        TextView tvChangeplaceOldname;
        @BindView(R.id.tv_changeplace_oldprice)
        TextView tvChangeplaceOldprice;
        @BindView(R.id.tv_changeplace_oldpushomney)
        TextView tvChangeplaceOldpushomney;
        @BindView(R.id.v_color_new)
        View vColorNew;
        @BindView(R.id.tv_changeplace_newname)
        TextView tvChangeplaceNewname;
        @BindView(R.id.tv_changeplace_newprice)
        TextView tvChangeplaceNewprice;
        @BindView(R.id.tv_changeplace_newpushomney)
        TextView tvChangeplaceNewpushomney;
        @BindView(R.id.root_tow)
        LinearLayout rootTow;
        @BindView(R.id.v_color_old_tow)
        View vColorOldTow;
        @BindView(R.id.tv_changeplace_oldname_tow)
        TextView tvChangeplaceOldnameTow;
        @BindView(R.id.tv_changeplace_oldprice_tow)
        TextView tvChangeplaceOldpriceTow;
        @BindView(R.id.tv_changeplace_oldpushomney_tow)
        TextView tvChangeplaceOldpushomneyTow;
        @BindView(R.id.v_color_new_tow)
        View vColorNewTow;
        @BindView(R.id.tv_changeplace_newname_tow)
        TextView tvChangeplaceNewnameTow;
        @BindView(R.id.tv_changeplace_newprice_tow)
        TextView tvChangeplaceNewpriceTow;
        @BindView(R.id.tv_changeplace_newpushomney_tow)
        TextView tvChangeplaceNewpushomneyTow;
        @BindView(R.id.root_three)
        LinearLayout rootThree;
        @BindView(R.id.tow_root_one_child)
        LinearLayout towRootOneChild;
        @BindView(R.id.tow_root_tow_child)
        LinearLayout towRootTowChild;
        @BindView(R.id.three_root_one_child)
        LinearLayout threeRootOneChild;
        @BindView(R.id.three_root_tow_child)
        LinearLayout threeRootTowChild;
        @BindView(R.id.super_text_point_second)
        SuperTextView superTextPointSecond;

        public ServiceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void display(NewIncome.DataBean.ListBeanXX.ListBeanX listBeanX) {

            tvItemIncomeColor.setBackgroundResource(bgColor);
            vColorNew.setBackgroundResource(bgColor);
            vColorNewTow.setBackgroundResource(bgColor);
            vColorOld.setBackgroundResource(bgColor);
            vColorOldTow.setBackgroundResource(bgColor);
            tvItemIncomeIncome.setText(listBeanX.getAmount());
            tvItemIncomeTitle.setText(listBeanX.getName());
            //跳转到明细
            detailOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onDetailClick(listBeanX);
                    }

                }
            });

            //判断是否是服务订单
            if (listBeanX.getWorkerRotaltyInfoMap() != null && listBeanX.getWorkerRotaltyInfoMap().size() > 0) {
                WorkerRotaltyInfoMode infoModeFirst = listBeanX.getWorkerRotaltyInfoMap().get(0);
                WorkerRotaltyInfoMode infoModeSecond;

                //跨城市，并且跨新老店
                if (infoModeFirst.isCityFlag() && (infoModeFirst.isGroupShopFlag() || listBeanX.getWorkerRotaltyInfoMap().get(1).isGroupShopFlag())) {
                    infoModeSecond =  listBeanX.getWorkerRotaltyInfoMap().get(1);
                    rootTow.setVisibility(View.VISIBLE);
                    rootThree.setVisibility(View.VISIBLE);
                    //第一个城市跨新老店,取新老店信息
                    if (infoModeFirst.isGroupShopFlag()) {
                        towRootTowChild.setVisibility(View.VISIBLE);

                        //设置老店信息
                        tvChangeplaceOldname.setText(TextUtils.concat(Utils.getCityName(infoModeFirst.getCityId()),"老店")); //城市名字
                        tvChangeplaceOldprice.setText(String.valueOf(infoModeFirst.getOldShopDeductorAmount()));//金额
                        tvChangeplaceOldpushomney.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),infoModeFirst.getOldShopRate()));//比例
                        //设置新店信息
                        tvChangeplaceNewname.setText(TextUtils.concat(Utils.getCityName(infoModeFirst.getCityId()),"新店")); //城市名字
                        tvChangeplaceNewprice.setText(String.valueOf(infoModeFirst.getNewShopDeductorAmount()));//金额
                        tvChangeplaceNewpushomney.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),infoModeFirst.getNewShopRate()));//比例
                    }
                    //不跨新老店判断是新店还是老店在取信息
                    else{
                        //设置老店信息
                        tvChangeplaceOldname.setText(TextUtils.concat(Utils.getCityName(infoModeFirst.getCityId()),infoModeFirst.isIsNewShop() ?"新店" :"老店")); //城市名字
                        tvChangeplaceOldprice.setText(String.valueOf(infoModeFirst.isIsNewShop() ?
                                infoModeFirst.getNewShopDeductorAmount() : infoModeFirst.getOldShopDeductorAmount()));//金额
                        tvChangeplaceOldpushomney.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),
                                infoModeFirst.isIsNewShop() ? infoModeFirst.getNewShopRate() : infoModeFirst.getOldShopRate()));//比例
                    }

                    //弟二个城市跨新老店,取新老店信息
                    if (infoModeSecond.isGroupShopFlag()) {
                        threeRootTowChild.setVisibility(View.VISIBLE);

                        //设置老店信息
                        tvChangeplaceOldnameTow.setText(TextUtils.concat(Utils.getCityName(infoModeSecond.getCityId()),"老店")); //城市名字
                        tvChangeplaceOldpriceTow.setText(String.valueOf(infoModeSecond.getOldShopDeductorAmount()));//金额
                        tvChangeplaceOldpushomneyTow.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),infoModeSecond.getOldShopRate()));//比例
                        //设置新店信息
                        tvChangeplaceNewnameTow.setText(TextUtils.concat(Utils.getCityName(infoModeSecond.getCityId()),"新店")); //城市名字
                        tvChangeplaceNewpriceTow.setText(String.valueOf(infoModeSecond.getNewShopDeductorAmount()));//金额
                        tvChangeplaceNewpushomneyTow.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),infoModeSecond.getNewShopRate()));//比例
                    }
                    //不跨新老店判断是新店还是老店在取信息
                    else{
                        Utils.mLogError("老店信息："+infoModeSecond.getOldShopDeductorAmount());
                        //设置老店信息
                        tvChangeplaceOldnameTow.setText(TextUtils.concat(Utils.getCityName(infoModeSecond.getCityId()),infoModeSecond.isIsNewShop() ?"新店" :"老店")); //城市名字
                        tvChangeplaceOldpriceTow.setText(String.valueOf(infoModeSecond.isIsNewShop() ?
                                infoModeSecond.getNewShopDeductorAmount() : infoModeSecond.getOldShopDeductorAmount()));//金额
                        tvChangeplaceOldpushomneyTow.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),
                                infoModeSecond.isIsNewShop() ? infoModeSecond.getNewShopRate() : infoModeFirst.getOldShopRate()));//比例
                    }


                }
                //只跨城市
                else if (infoModeFirst.isCityFlag() && !infoModeFirst.isGroupShopFlag()) {
                    infoModeSecond =  listBeanX.getWorkerRotaltyInfoMap().get(1);
                    rootTow.setVisibility(View.VISIBLE);
                    towRootTowChild.setVisibility(View.VISIBLE);

                    //第一个城市信息
                    tvChangeplaceOldname.setText(TextUtils.concat(Utils.getCityName(infoModeFirst.getCityId()),infoModeFirst.isIsNewShop() ?"新店" :"老店")); //城市名字
                    tvChangeplaceOldprice.setText(String.valueOf(infoModeFirst.isIsNewShop() ?
                            infoModeFirst.getNewShopDeductorAmount() : infoModeFirst.getOldShopDeductorAmount()));//金额
                    tvChangeplaceOldpushomney.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),
                            infoModeFirst.isIsNewShop() ? infoModeFirst.getNewShopRate() : infoModeFirst.getOldShopRate()));//比例

                    //第二个城市信息
                    tvChangeplaceNewname.setText(TextUtils.concat(Utils.getCityName(infoModeSecond.getCityId()),infoModeSecond.isIsNewShop() ?"新店" :"老店")); //城市名字
                    tvChangeplaceNewprice.setText(String.valueOf(infoModeSecond.isIsNewShop() ?
                            infoModeSecond.getNewShopDeductorAmount() : infoModeSecond.getOldShopDeductorAmount()));//金额
                    tvChangeplaceNewpushomney.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),
                            infoModeSecond.isIsNewShop() ? infoModeSecond.getNewShopRate() : infoModeSecond.getOldShopRate()));//比例

                }
                //只跨新老店
                else if (infoModeFirst.isGroupShopFlag()) {
                    rootTow.setVisibility(View.VISIBLE);
                    towRootTowChild.setVisibility(View.VISIBLE);
                    //设置老店信息
                    tvChangeplaceOldname.setText(TextUtils.concat(Utils.getCityName(infoModeFirst.getCityId()),"老店")); //城市名字
                    tvChangeplaceOldprice.setText(String.valueOf(infoModeFirst.getOldShopDeductorAmount()));//金额
                    tvChangeplaceOldpushomney.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),infoModeFirst.getOldShopRate()));//比例
                    //设置新店信息
                    tvChangeplaceNewname.setText(TextUtils.concat(Utils.getCityName(infoModeFirst.getCityId()),"新店")); //城市名字
                    tvChangeplaceNewprice.setText(String.valueOf(infoModeFirst.getNewShopDeductorAmount()));//金额
                    tvChangeplaceNewpushomney.setText(String.format(context.getResources().getString(R.string.current_push_money_rate),infoModeFirst.getNewShopRate()));//比例

                }
                //不跨城市，也不跨新老店
                else {
                    rootOne.setVisibility(View.VISIBLE);
                    //设置老店信息
                    tvChangeplaceOldprice.setText(String.valueOf(infoModeFirst.isIsNewShop() ?
                            infoModeFirst.getNewShopDeductorAmount() : infoModeFirst.getOldShopDeductorAmount()));//金额
                    //当前提成比例
                    tvRatio.setText(String.format(context.getResources().getString(R.string.push_money_rate),
                            infoModeFirst.isIsNewShop() ? infoModeFirst.getNewShopRate() : infoModeFirst.getOldShopRate()));//比例
                    //当前销售金额
                    currentMoney.setText(String.valueOf(infoModeFirst.isIsNewShop() ? infoModeFirst.getNewShopDeductorAmount():
                            infoModeFirst.getOldShopDeductorAmount()));
                    distance.post(new Runnable() {
                        @Override
                        public void run() {
                            //重新设置位置
                            RelativeLayout.LayoutParams marginLayoutParams = (RelativeLayout.LayoutParams) distance.getLayoutParams();
                            marginLayoutParams.topMargin = Utils.dip2px(context,81) - distance.getHeight();
                            distance.setLayoutParams(marginLayoutParams);
                        }
                    });
                    //是否是最高档
                    if (infoModeFirst.isIsMaxRateFlag()){
                        tvNextRatio.setVisibility(View.GONE);
                        nextMoney.setVisibility(View.GONE);
                        superTextPointSecond.setVisibility(View.GONE);
                        superTvNext.setText("当前已是最高档位");
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) distance.getLayoutParams();

                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        layoutParams.rightMargin = Utils.dip2px(context,30);
                        distance.setLayoutParams(layoutParams);

                    }else {
                        //下一当提成比例
                        tvNextRatio.setText(String.format(context.getResources().getString(R.string.push_money_rate),infoModeFirst.getNextRates()));//比例
                        //下一档销售金额
                        nextMoney.setText(String.valueOf(infoModeFirst.getNextRateAmount()));
                        //距离下一档还差
                        superTvNext.setText(TextUtils.concat("还差¥",String.valueOf(infoModeFirst.getShortOfRateAmount()),"晋级下一档"));
                    }
                }
            }
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivTip;
        private TextView tvTipTitle;
        private TextView tvIncome;
        private ImageView ivIncomeDesc;
        private ImageView ivIncomeMore;
        private TextView tvIncomeDesc;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivTip = itemView.findViewById(R.id.iv_income_tip);
            tvTipTitle = itemView.findViewById(R.id.tv_income_tiptitle);
            tvIncome = itemView.findViewById(R.id.tv_item_income);
            ivIncomeDesc = itemView.findViewById(R.id.iv_income_desc);
            ivIncomeMore = itemView.findViewById(R.id.iv_income_more);
            tvIncomeDesc = itemView.findViewById(R.id.tv_item_income_desc);
        }

        public void display(NewIncome.DataBean.ListBeanXX.ListBeanX listBeanX) {
            ivTip.setBackgroundResource(bgColor);
            tvIncome.setText(listBeanX.getAmount());
            tvTipTitle.setText(listBeanX.getName());
            if (Utils.isStrNull(listBeanX.getInstro())) {
                ivIncomeDesc.setVisibility(View.VISIBLE);
            } else {
                ivIncomeDesc.setVisibility(View.GONE);
            }
            if (Utils.isStrNull(listBeanX.getContainInfo())) {
                ivIncomeMore.setVisibility(View.VISIBLE);
                tvIncomeDesc.setText(listBeanX.getContainInfo());
            }else if (!TextUtils.isEmpty(listBeanX.getRoyalty())){
                tvIncomeDesc.setText(TextUtils.concat("销售金额：",String.valueOf(listBeanX.getSaleAmount()),"     提成比例：",listBeanX.getRoyalty()));
            }
            else {
                tvIncomeDesc.setVisibility(View.GONE);
                ivIncomeMore.setVisibility(View.GONE);
            }


            ivIncomeDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialogDefault(context).builder()
                            .setTitle(listBeanX.getName() + "原因").setMsg(listBeanX.getInstro()).isOneBtn(true).setPositiveButton("我知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            });
            ivIncomeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(listBeanX);
                }
            });
        }
    }
}
