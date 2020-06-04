package com.haotang.petworker.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.Comment;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/9
 * @Description:我的评价列表适配器
 */
public class MyEvaluateAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    public MyEvaluateAdapter(int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        NiceImageView ivHead = helper.getView(R.id.iv_evaluate_head);
        TextView tvName = helper.getView(R.id.tv_evaluate_name);
        ImageView ivStar = helper.getView(R.id.iv_evaluate_star);
        TextView tvType = helper.getView(R.id.tv_evaluate_type);
        TextView tvContent = helper.getView(R.id.tv_evaluate_content);
        TextView tvTag = helper.getView(R.id.tv_evaluate_tag);
        TextView tvTime = helper.getView(R.id.tv_evaluate_time);
        RecyclerView rvIcon = helper.getView(R.id.rv_evaluate_icon);
        GlideUtil.loadImg(mContext, item.image, ivHead, R.drawable.icon_production_default);
        tvName.setText(item.userName);

        tvContent.setText(item.content);
        if (item.commentTags==null||item.commentTags.endsWith("")){
            tvTag.setVisibility(View.GONE);
        }else {
            tvTag.setText(item.commentTags);
        }

        tvTime.setText("服务时间:" + item.startTime);
        final List<String> list = new ArrayList<>();
        if (item.images != null && item.images.length > 0) {
            list.clear();
            for (int i = 0; i < item.images.length; i++) {
                list.add(item.images[i]);
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
            rvIcon.setLayoutManager(gridLayoutManager);
            rvIcon.setNestedScrollingEnabled(false);
            EvaluateIconAdapter iconAdapter = new EvaluateIconAdapter(mContext, list);
            rvIcon.setAdapter(iconAdapter);
            iconAdapter.setListener(new EvaluateIconAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Utils.imageBrower(mContext, position, list.toArray(new String[] {}));
                }
            });
        } else {
            rvIcon.setVisibility(View.GONE);
        }
        switch (item.grade){
            case 1:
                ivStar.setImageResource(R.drawable.icon_star_one);
                break;
            case 2:
                ivStar.setImageResource(R.drawable.icon_star_two);
                break;
            case 3:
                ivStar.setImageResource(R.drawable.icon_star_three);
                break;
            case 4:
                ivStar.setImageResource(R.drawable.icon_star_four);
                break;
            case 5:
                ivStar.setImageResource(R.drawable.icon_star_five);
                break;
        }
        switch (item.level) {
            case 0:
                tvType.setText("差评");
                break;
            case 1:
                tvType.setText("中评");
                break;
            case 2:
                tvType.setText("好评");
                break;
            case -1:
                tvType.setText("未评分");
                break;
        }

    }
}
