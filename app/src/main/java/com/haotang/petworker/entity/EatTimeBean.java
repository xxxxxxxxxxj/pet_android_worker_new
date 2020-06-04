package com.haotang.petworker.entity;

import android.util.Log;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-16 10:37
 */
public class EatTimeBean {
    private String month;
    private int isToday;//是否是当天(1:是、0:否),非当天不返回该参数
    private int lunchSwitch;//中午是否吃饭(0:否、1:是)
    private String day;
    private String date;

    @Override
    public String toString() {
        return "EatTimeBean{" +
                "month='" + month + '\'' +
                ", isToday=" + isToday +
                ", lunchSwitch=" + lunchSwitch +
                ", day='" + day + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public EatTimeBean(String month, int isToday, int lunchSwitch, String day, String date) {
        this.month = month;
        this.isToday = isToday;
        this.lunchSwitch = lunchSwitch;
        this.day = day;
        this.date = date;
    }

    public static EatTimeBean json2Entity(JSONObject json) {
        EatTimeBean eatTimeBean = new EatTimeBean();
        try {
            if (json.has("isToday") && !json.isNull("isToday")) {
                eatTimeBean.setIsToday(json.getInt("isToday"));
            }
            if (json.has("lunchSwitch") && !json.isNull("lunchSwitch")) {
                eatTimeBean.setLunchSwitch(json.getInt("lunchSwitch"));
            }
            if (json.has("date") && !json.isNull("date")) {
                eatTimeBean.setDate(json.getString("date"));
            }
            if (json.has("day") && !json.isNull("day")) {
                eatTimeBean.setDay(json.getString("day"));
            }
            if (json.has("month") && !json.isNull("month")) {
                eatTimeBean.setMonth(json.getString("month"));
            }
        } catch (Exception e) {
            Log.e("TAG", "items()数据异常e = " + e.toString());
            e.printStackTrace();
        }
        return eatTimeBean;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getIsToday() {
        return isToday;
    }

    public void setIsToday(int isToday) {
        this.isToday = isToday;
    }

    public int getLunchSwitch() {
        return lunchSwitch;
    }

    public void setLunchSwitch(int lunchSwitch) {
        this.lunchSwitch = lunchSwitch;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EatTimeBean() {
    }
}
