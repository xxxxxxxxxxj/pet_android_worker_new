package com.haotang.petworker.entity;

import android.util.Log;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/9/8 16:44
 */
public class CareTag {
    private int id;
    private int num;
    private String tag;
    private boolean isSelect;

    @Override
    public String toString() {
        return "CareTag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    public static CareTag json2Entity(JSONObject json) {
        CareTag careTag = new CareTag();
        try {
            if (json.has("id") && !json.isNull("id")) {
                careTag.setId(json.getInt("id"));
            }
            if (json.has("tag") && !json.isNull("tag")) {
                careTag.setTag(json.getString("tag"));
            }
            if (json.has("num") && !json.isNull("num")) {
                careTag.setNum(json.getInt("num"));
            }
        } catch (Exception e) {
            Log.e("TAG", "items()数据异常e = " + e.toString());
            e.printStackTrace();
        }
        return careTag;
    }

    public CareTag() {
    }

    public CareTag(int id, String tag, boolean isSelect) {
        this.id = id;
        this.tag = tag;
        this.isSelect = isSelect;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
