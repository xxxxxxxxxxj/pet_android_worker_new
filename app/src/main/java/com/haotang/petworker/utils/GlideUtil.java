package com.haotang.petworker.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haotang.petworker.view.GlideCircleTransform;

public class GlideUtil {
    public static void loadImg(Context mContext, String imgUrl,
                               ImageView imageView, int placeholderResourceId) {
        String localImgUrl = "";
        if (imgUrl != null && !TextUtils.isEmpty(imgUrl)) {
            if (imgUrl.startsWith("drawable://")) {
                localImgUrl = imgUrl;
            } else if (imgUrl.startsWith("file://")) {
                localImgUrl = imgUrl;
            } else {
                if (!imgUrl.startsWith("http://")
                        && !imgUrl.startsWith("https://")) {
                    localImgUrl = CommUtil.getStaticUrl() + imgUrl;
                } else {
                    localImgUrl = imgUrl;
                }
            }
        }
        try {
            if (localImgUrl.toUpperCase().endsWith(".GIF")) {
                Glide.with(mContext)
                        .load(localImgUrl)
                        .asGif()
                        .placeholder(placeholderResourceId)
                        .error(placeholderResourceId)
                        .dontAnimate() //去掉显示动画
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE) //DiskCacheStrategy.NONE
                        .into(imageView);
            } else {
                Glide.with(mContext)
                        .load(localImgUrl)
                        .placeholder(placeholderResourceId)
                        .error(placeholderResourceId)
                        .crossFade()
                        .centerCrop()
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCircleImg(Context mContext, String imgUrl,
                                     ImageView imageView, int placeholderResourceId) {
        String localImgUrl = "";
        if (imgUrl != null && !TextUtils.isEmpty(imgUrl)) {
            if (imgUrl.startsWith("drawable://")) {
                localImgUrl = imgUrl;
            } else if (imgUrl.startsWith("file://")) {
                localImgUrl = imgUrl;
            } else {
                if (!imgUrl.startsWith("http://")
                        && !imgUrl.startsWith("https://")) {
                    localImgUrl = CommUtil.getStaticUrl() + imgUrl;
                } else {
                    localImgUrl = imgUrl;
                }
            }
        }
        try {
            Glide.with(mContext).load(localImgUrl)
                    .placeholder(placeholderResourceId) // 占位图
                    .error(placeholderResourceId) // 出错的占位图
                    .bitmapTransform(new GlideCircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
