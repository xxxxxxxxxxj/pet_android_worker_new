package com.haotang.petworker.entity;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/6/13 11:25
 */
public class SetTimeConfig {
    private String weekDay;
    private String dateTxt;
    private String date;
    private int type;
    private int free;
    private int overtime;
    private int disabled;
    private int addTime;
    private int subTime;
    private String isToday = "false";

    @Override
    public String toString() {
        return "SetTimeConfig{" +
                "weekDay='" + weekDay + '\'' +
                ", dateTxt='" + dateTxt + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                ", free=" + free +
                ", overtime=" + overtime +
                ", disabled=" + disabled +
                '}';
    }

    public String getIsToday() {
        return isToday;
    }

    public void setIsToday(String isToday) {
        this.isToday = isToday;
    }

    public int getAddTime() {
        return addTime;
    }

    public void setAddTime(int addTime) {
        this.addTime = addTime;
    }

    public int getSubTime() {
        return subTime;
    }

    public void setSubTime(int subTime) {
        this.subTime = subTime;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getDateTxt() {
        return dateTxt;
    }

    public void setDateTxt(String dateTxt) {
        this.dateTxt = dateTxt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public static SetTimeConfig json2Entity(JSONObject jday) {
        SetTimeConfig setTimeConfig = new SetTimeConfig();
        try {
            if (jday.has("weekDay") && !jday.isNull("weekDay")) {
                setTimeConfig.weekDay = jday.getString("weekDay");
            }
            if (jday.has("dateTxt") && !jday.isNull("dateTxt")) {
                setTimeConfig.dateTxt = jday.getString("dateTxt");
            }
            if (jday.has("date") && !jday.isNull("date")) {
                setTimeConfig.date = jday.getString("date");
            }
            if (jday.has("type") && !jday.isNull("type")) {
                setTimeConfig.type = jday.getInt("type");
            }
            if (jday.has("free") && !jday.isNull("free")) {
                setTimeConfig.free = jday.getInt("free");
            }
            if (jday.has("overtime")
                    && !jday.isNull("overtime")) {
                setTimeConfig.overtime = jday.getInt("overtime");
            }
            if (jday.has("disabled")
                    && !jday.isNull("disabled")) {
                setTimeConfig.disabled = jday.getInt("disabled");
            }
            if (jday.has("isToday") && !jday.isNull("isToday")) {
                setTimeConfig.isToday = jday.getString("isToday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setTimeConfig;
    }
}
