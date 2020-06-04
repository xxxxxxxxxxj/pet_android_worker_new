package com.haotang.petworker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.haotang.petworker.adapter.GuideAdapter;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.SystemBarTint;
import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;
import java.util.ArrayList;

public class GuideActivity extends SuperActivity {
	private ViewPager vp;
	private ImageButton ibNext;
	private int[] imagesIds;
	private ArrayList<ImageView> imageList;
	private GuideAdapter adapter;
	private SystemBarTint mtintManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mtintManager = new SystemBarTint(this);
		setStatusBarColor(Color.parseColor("#ff0099cc"));
		Utils.hideBottomUIMenu(this);
		setContentView(R.layout.guide);

		vp = (ViewPager) findViewById(R.id.vp_guide_pager);
		ibNext = (ImageButton) findViewById(R.id.ib_guide_next);
		imagesIds = new int[] { R.drawable.guide1, R.drawable.guide2,
				R.drawable.guide3, R.drawable.guide4 };
		imageList = new ArrayList<ImageView>();
		for (int i = 0; i < imagesIds.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setScaleType(ScaleType.FIT_XY);
			// iv.setBackgroundResource(imagesIds[i]);
			iv.setImageBitmap(readBitMap(this, imagesIds[i]));
			imageList.add(iv);
		}
		adapter = new GuideAdapter(this, imageList);
		vp.setAdapter(adapter);
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				// Utils.mLogError("位置："+position);
				if (position == imagesIds.length - 1) {
					ibNext.setVisibility(View.VISIBLE);
				} else {
					ibNext.setVisibility(View.GONE);

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});

		ibNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext();
			}
		});

	}
	
	protected void setStatusBarColor(int colorBurn) {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = setImmerseLayout();
			window.setStatusBarColor(colorBurn);
		} else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (mtintManager != null) {
				mtintManager.setStatusBarTintEnabled(true);
				mtintManager.setStatusBarTintColor(colorBurn);
			}
		}
	}

	private void goNext() {
		Intent intent = new Intent(this, LoginNewActivity.class);
		// overridePendingTransition(R.anim.left_in, R.anim.right_out);
		startActivity(intent);
		finishWithAnimation();
	}

	public static Bitmap readBitMap(Context context, int resId) {

		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		// 获取资源图片

		InputStream is = context.getResources().openRawResource(resId);

		return BitmapFactory.decodeStream(is, null, opt);

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
