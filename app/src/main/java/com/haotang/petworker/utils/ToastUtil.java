package com.haotang.petworker.utils;

import com.haotang.petworker.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
	private static Toast mToast = null;

	public static void showToast(Context context, String message,
			int showlocation, int duration) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mtoast, null);
		TextView tvMessage = (TextView) view
				.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);

		if (mToast == null)
			mToast = new Toast(context);
		mToast.setGravity(showlocation, 0, 0);
		mToast.setDuration(duration);
		mToast.setView(view);
		mToast.show();
	}

	public static void showToastCenterLong(Context context, String message) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mtoast, null);
		TextView tvMessage = (TextView) view
				.findViewById(R.id.tv_toast_message);
		View vEmpty = view.findViewById(R.id.view_toast);
		vEmpty.setVisibility(View.GONE);
		tvMessage.setText(message);
		tvMessage.setTextSize(15f);

		if (mToast == null)
			mToast = new Toast(context);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setView(view);
		mToast.show();
	}

	public static void showToastCenterShort(Context context, String message) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mtoast, null);
		TextView tvMessage = (TextView) view
				.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);
		tvMessage.setTextSize(15f);
		View vEmpty = view.findViewById(R.id.view_toast);
		vEmpty.setVisibility(View.GONE);

		if (mToast == null)
			mToast = new Toast(context);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setView(view);
		mToast.show();
	}

	public static void showToastBottomShort(Context context, String message) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mtoast, null);
		TextView tvMessage = (TextView) view
				.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);
		tvMessage.setTextSize(13f);
		if (mToast == null)
			mToast = new Toast(context);
		mToast.setGravity(Gravity.BOTTOM, 0, 0);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setView(view);
		mToast.show();
	}

	public static void showToastBottomLong(Context context, String message) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mtoast, null);
		TextView tvMessage = (TextView) view
				.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);
		tvMessage.setTextSize(13f);
		if (mToast == null)
			mToast = new Toast(context);
		mToast.setGravity(Gravity.BOTTOM, 0, 0);
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setView(view);
		mToast.show();
	}

}
