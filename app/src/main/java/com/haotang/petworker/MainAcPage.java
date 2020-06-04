package com.haotang.petworker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.haotang.petworker.entity.ActivityPage;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.GlideImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class MainAcPage extends SuperActivity implements OnBannerListener {
    private Banner show_main_img;
    private ImageView imageView_close;
    private List<ActivityPage> localBannerList = new ArrayList<ActivityPage>();
    private ArrayList<ActivityPage> bannerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        bannerList = (ArrayList<ActivityPage>) getIntent().getSerializableExtra("bannerList");
        setContentView(R.layout.activity_main_ac_page);
        show_main_img = (Banner) findViewById(R.id.show_main_img);
        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setBanner();
    }

    public void setBanner() {
        Log.e("TAG", "bannerList = " + bannerList);
        if (bannerList != null && bannerList.size() > 0) {
            localBannerList.clear();
            localBannerList.addAll(bannerList);
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < bannerList.size(); i++) {
                list.add(bannerList.get(i).getActivityPic());
            }
            show_main_img.setImages(list)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerListener(this)
                    .start();
        }
    }

    @Override
    public void OnBannerClick(int position) {
        if (localBannerList != null && localBannerList.size() > 0 && localBannerList.size() > position) {
            ActivityPage activityPage = localBannerList.get(position);
            if (activityPage != null) {
                Utils.goService(MainAcPage.this, activityPage.getPoint(), activityPage.getBackup());
                finishWithAnimation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
