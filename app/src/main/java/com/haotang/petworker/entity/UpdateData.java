package com.haotang.petworker.entity;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/9/7 16:37
 */
public class UpdateData implements Serializable {
    private double listPrice;
    private double price;
    private double vipPrice;
    private int serviceId;
    private int duration;
    private String name;
    private int itemId;
    private boolean isSelect;

    public static UpdateData json2Entity(JSONObject json) {
        UpdateData updateData = new UpdateData();
        try {
            if (json.has("listPrice") && !json.isNull("listPrice")) {
                updateData.setListPrice(json.getDouble("listPrice"));
            }
            if (json.has("price") && !json.isNull("price")) {
                updateData.setPrice(json.getDouble("price"));
            }
            if (json.has("price") && !json.isNull("price")) {
                updateData.setVipPrice(json.getDouble("price"));
            }
            if (json.has("serviceId") && !json.isNull("serviceId")) {
                updateData.setServiceId(json.getInt("serviceId"));
            }
            if (json.has("duration") && !json.isNull("duration")) {
                updateData.setDuration(json.getInt("duration"));
            }
            if (json.has("name") && !json.isNull("name")) {
                updateData.setName(json.getString("name"));
            }
            if (json.has("itemId") && !json.isNull("itemId")) {
                updateData.setItemId(json.getInt("itemId"));
            }
        } catch (Exception e) {
            Log.e("TAG", "items()数据异常e = " + e.toString());
            e.printStackTrace();
        }
        return updateData;
    }

    public UpdateData() {
    }

    public UpdateData(double listPrice, double price, double vipPrice, int serviceId, int duration, String name, int itemId) {
        this.listPrice = listPrice;
        this.price = price;
        this.vipPrice = vipPrice;
        this.serviceId = serviceId;
        this.duration = duration;
        this.name = name;
        this.itemId = itemId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
