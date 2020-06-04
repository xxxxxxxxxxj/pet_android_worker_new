package com.haotang.petworker.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NewPetBean implements Serializable {
    public int PetId;
    public String avatarPath;
    public String petName;
    private String sortLetters; // 显示数据拼音的首字母
    public String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getPetId() {
        return PetId;
    }

    public void setPetId(int petId) {
        PetId = petId;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public static NewPetBean json2Entity(JSONObject json) {
        NewPetBean newPetBean = new NewPetBean();
        try {
            if (json.has("id") && !json.isNull("id")) {
                newPetBean.PetId = json.getInt("id");
            }
            if (json.has("avatarPath") && !json.isNull("avatarPath")) {
                newPetBean.avatarPath = json.getString("avatarPath");
            }
            if (json.has("petName") && !json.isNull("petName")) {
                newPetBean.petName = json.getString("petName");
            }
            if (json.has("pinyin") && !json.isNull("pinyin")) {
                newPetBean.pinyin = json.getString("pinyin");
                String sortStr = newPetBean.pinyin.substring(0, 1)
                        .toUpperCase();
                if (sortStr.matches("[A-Z]")) {
                    newPetBean.setSortLetters(sortStr.toUpperCase());
                } else {
                    newPetBean.setSortLetters("#");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newPetBean;
    }
}
