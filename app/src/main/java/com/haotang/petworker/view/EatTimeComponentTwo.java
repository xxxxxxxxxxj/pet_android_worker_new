package com.haotang.petworker.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blog.www.guideview.Component;
import com.haotang.petworker.R;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-18 11:48
 */
public class EatTimeComponentTwo implements Component {
    private final Activity context;
    private ImageView iv_guide_eattime2;

    public EatTimeComponentTwo(Activity context) {
        this.context = context;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.guide_etatime2, null);
        iv_guide_eattime2 = ll.findViewById(R.id.iv_guide_eattime2);
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
        return -120;
    }

    @Override
    public int getYOffset() {
        return 120;
    }
}