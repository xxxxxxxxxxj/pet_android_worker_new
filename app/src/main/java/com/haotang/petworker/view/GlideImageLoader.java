package com.haotang.petworker.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haotang.petworker.R;
import com.youth.banner.loader.ImageLoader;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/4/19 18:12
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context.getApplicationContext())
                .load(path).error(R.drawable.icon_production_default)
                .placeholder(R.drawable.icon_production_default)
                .into(imageView);
    }
}
