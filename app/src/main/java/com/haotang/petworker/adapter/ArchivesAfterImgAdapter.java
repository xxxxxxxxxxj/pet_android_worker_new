package com.haotang.petworker.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haotang.petworker.R;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/9/8 17:19
 */
public class ArchivesAfterImgAdapter  extends BaseQuickAdapter<String, BaseViewHolder> {

    public OnImgDelListener onImgDelListener = null;

    public interface OnImgDelListener {
        public void OnImgDel(int position);
    }

    public void setOnImgDelListener(
            OnImgDelListener onImgDelListener) {
        this.onImgDelListener = onImgDelListener;
    }

    public OnImgClickListener onImgClickListener = null;

    public interface OnImgClickListener {
        public void OnImgClick(int position);
    }

    public void setOnImgClickListener(
            OnImgClickListener onImgClickListener) {
        this.onImgClickListener = onImgClickListener;
    }
    public ArchivesAfterImgAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        ImageView iv_item_addpetarchives_imgdel = helper.getView(R.id.iv_item_addpetarchives_imgdel);
        ImageView iv_item_addpetarchives_img = helper.getView(R.id.iv_item_addpetarchives_img);
        if (item != null) {
            GlideUtil.loadImg(mContext, item, iv_item_addpetarchives_img, R.drawable.icon_production_default);
            Log.e("TAG", "item = " + item);
            if (Utils.isStrNull(item) && item.startsWith("drawable://")) {
                Glide.with(mContext).load(R.drawable.icon_img_add).into(iv_item_addpetarchives_img);
                iv_item_addpetarchives_imgdel.setVisibility(View.GONE);
            } else {
                File file = new File(item);
                Uri uri = Uri.fromFile(file);
                Glide.with(mContext).load(uri).into(iv_item_addpetarchives_img);
                iv_item_addpetarchives_imgdel.bringToFront();
                iv_item_addpetarchives_imgdel.setVisibility(View.VISIBLE);
            }
            iv_item_addpetarchives_imgdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onImgDelListener != null){
                        onImgDelListener.OnImgDel(helper.getLayoutPosition());
                    }
                }
            });
            iv_item_addpetarchives_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onImgClickListener != null){
                        onImgClickListener.OnImgClick(helper.getLayoutPosition());
                    }
                }
            });
        }
    }
}