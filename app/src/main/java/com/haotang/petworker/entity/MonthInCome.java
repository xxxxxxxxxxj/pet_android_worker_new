package com.haotang.petworker.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/1/2 20:07
 */
public class MonthInCome {
    private String name;
    private double price;

    public MonthInCome(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public MonthInCome() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
