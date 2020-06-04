package com.haotang.petworker.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.CareHistoryNew;
import com.haotang.petworker.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:姜谷蓄
 * @Date:2019/9/5
 * @Description:宠物档案历史
 */
public class ArchivesHistoryAdapter extends BaseQuickAdapter<CareHistoryNew, BaseViewHolder> {
    public ArchivesHistoryAdapter(int layoutResId, @Nullable List<CareHistoryNew> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CareHistoryNew item) {
        TextView tvTime = helper.getView(R.id.tv_history_time);
        TextView tvContent = helper.getView(R.id.tv_history_content);
        RecyclerView rvIcon = helper.getView(R.id.rv_history_icon);
        tvTime.setText(item.getCreateTimes());
        tvContent.setText(item.getBabyContent());
        final List<String> iconList = new ArrayList<>();
        for (int i = 0; i < item.getPicsList().size(); i++) {
            iconList.add(item.getPicsList().get(i));
        }
        for (int i = 0; i < item.getBeforePicsList().size(); i++) {
            iconList.add(item.getBeforePicsList().get(i));
        }
        if (iconList.size()==0){
            rvIcon.setVisibility(View.GONE);
        }else {
            GridLayoutManager layoutManager = new GridLayoutManager(mContext,4);
            rvIcon.setLayoutManager(layoutManager);
            ArchivesIconAdapter iconAdapter = new ArchivesIconAdapter(mContext, iconList);
            rvIcon.setAdapter(iconAdapter);
            iconAdapter.setListener(new ArchivesIconAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Utils.imageBrower(mContext, position, iconList.toArray(new String[] {}));

                }
            });
        }


    }
}
