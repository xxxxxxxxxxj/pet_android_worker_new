package com.haotang.petworker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Administrator on 2014/12/10.
 */
public class SharedPreferenceUtil {
	private static SharedPreferenceUtil spUtil;
	private SharedPreferences sp;

	public static String IS_OPEN = "is_Open";
	public static String IS_OPEN_MESSAGE = "duanxin";
	public static String IS_OPEN_PUSH = "is_push";
	private static String HOTFIX_TIME = "hotfix_time";

	private SharedPreferenceUtil(Context context) {
		sp = context.getSharedPreferences("pet", Context.MODE_PRIVATE);
	}

	public static SharedPreferenceUtil getInstance(Context context) {
		if (null == spUtil)
			spUtil = new SharedPreferenceUtil(context);

		return spUtil;
	}

	public void setHotfixTime(long hotfixTime) {
		saveLong(HOTFIX_TIME,hotfixTime);
	}

	public long getHotfixTime() {
		return getLong(HOTFIX_TIME,0);
	}

	public void saveInt(String tag, int value) {
		Editor editor = sp.edit();
		editor.putInt(tag, value);
		editor.commit();
	}

	public void saveFloat(String tag, float value) {
		Editor editor = sp.edit();
		editor.putFloat(tag, value);
		editor.commit();
	}

	public void saveLong(String tag, long value) {
		Editor editor = sp.edit();
		editor.putLong(tag, value);
		editor.commit();
	}

	public void saveString(String tag, String value) {
		Editor editor = sp.edit();
		editor.putString(tag, value);
		editor.commit();
	}

	public void saveBoolean(String tag, boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean(tag, value);
		editor.commit();
	}

	public int getInt(String tag, int defaultValue) {
		return sp.getInt(tag, defaultValue);
	}

	public float getFloat(String tag, float defaultValue) {
		return sp.getFloat(tag, defaultValue);
	}

	public long getLong(String tag, long defaultValue) {
		return sp.getLong(tag, defaultValue);
	}

	public String getString(String tag, String defaultValue) {
		return sp.getString(tag, defaultValue);
	}

	public boolean getBoolean(String tag, boolean defaultValue) {
		return sp.getBoolean(tag, defaultValue);
	}

	public void clearData() {
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	public boolean containData(String tag) {
		return sp.contains(tag);
	}

	public void removeData(String tag) {
		Editor editor = sp.edit();
		editor.remove(tag);
		editor.commit();
	}
}
