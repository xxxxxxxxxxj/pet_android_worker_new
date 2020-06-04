package com.haotang.petworker.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blog.www.guideview.Component;
import com.haotang.petworker.R;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-18 11:16
 */
public class EatTimeComponentOne implements Component {
    private final Activity context;

    public EatTimeComponentOne(Activity context) {
        this.context = context;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_etatime1, null);
        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_OVER;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_END;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
