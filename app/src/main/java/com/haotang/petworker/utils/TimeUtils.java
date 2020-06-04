package com.haotang.petworker.utils;

import android.content.Context;

/**
 * 作者：灼星
 * 时间：2020-04-26
 */
public class TimeUtils {
    public static boolean getHotfixTime(Context context){
        //没有时间，是第一次请求，查询补丁包
        long oldTime = SharedPreferenceUtil.getInstance(context).getHotfixTime();

        long totalSeconds = System.currentTimeMillis() / 1000;
        SharedPreferenceUtil.getInstance(context).setHotfixTime(totalSeconds);
        //间隔一个小时就查询新补丁
        return totalSeconds - oldTime > 3600;
    }
}
