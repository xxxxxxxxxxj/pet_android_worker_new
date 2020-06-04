package com.haotang.petworker.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-03 14:09
 */
public class IncomeBean {
    private int id;
    private int resid;
    private String title;
    private double income;
    private String desc;

    public IncomeBean() {
    }

    public IncomeBean(int id, String title, double income, String desc,int resid) {
        this.id = id;
        this.title = title;
        this.income = income;
        this.desc = desc;
        this.resid = resid;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
