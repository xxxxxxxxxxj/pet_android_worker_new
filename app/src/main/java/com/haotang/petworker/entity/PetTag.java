package com.haotang.petworker.entity;

import android.util.Log;

import org.json.JSONObject;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/4/5 16:20
 */
public class PetTag {
    private String key;
    private int value;

    public static PetTag json2Entity(JSONObject json) {
        PetTag petTag = new PetTag();
        try {
            if (json.has("key") && !json.isNull("key")) {
                petTag.setKey(json.getString("key"));
            }
            if (json.has("value") && !json.isNull("value")) {
                petTag.setValue(json.getInt("value"));
            }
        } catch (Exception e) {
            Log.e("TAG", "items()数据异常e = " + e.toString());
            e.printStackTrace();
        }
        return petTag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
