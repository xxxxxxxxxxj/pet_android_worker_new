package com.haotang.petworker.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.FeedBackList;
import com.haotang.petworker.utils.Utils;

import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/6
 * @Description:
 */
public class AreaFeedBackAdapter extends BaseQuickAdapter<FeedBackList, BaseViewHolder> {
    public AreaFeedBackAdapter(int layoutResId, @Nullable List<FeedBackList> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedBackList item) {
        TextView tvTime = helper.getView(R.id.tv_feedback_time);
        TextView tvContent = helper.getView(R.id.tv_feedback_content);
        RecyclerView rvFeedIcon = helper.getView(R.id.rv_feedback_icon);
        LinearLayout llReply = helper.getView(R.id.ll_feedback_reply);
        TextView tvReply = helper.getView(R.id.tv_feedback_reply);
        tvTime.setText(item.date);
        tvContent.setText("我的建议: "+item.workerContent);
        if (item.replyContent==null){
            llReply.setVisibility(View.GONE);
        }else {
            llReply.setVisibility(View.VISIBLE);
            tvReply.setText(item.replyContent);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,5);
        rvFeedIcon.setLayoutManager(layoutManager);
        ArchivesIconAdapter iconAdapter = new ArchivesIconAdapter(mContext, item.listImg);
        rvFeedIcon.setAdapter(iconAdapter);
        iconAdapter.setListener(new ArchivesIconAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.imageBrower(mContext, position, item.listImg.toArray(new String[] {}));
            }
        });
    }
}
