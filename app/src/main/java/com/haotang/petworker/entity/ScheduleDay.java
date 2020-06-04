package com.haotang.petworker.entity;

import java.io.Serializable;

/**
 * 作者：灼星
 * 时间：2020-04-20
 */
public class ScheduleDay implements Serializable {
    private String days;
    private int type;
    private String name;

    public ScheduleDay() {
    }

    public ScheduleDay(String days, int type, String name) {
        this.days = days;
        this.type = type;
        this.name = name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
