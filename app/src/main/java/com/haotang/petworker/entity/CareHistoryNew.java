package com.haotang.petworker.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2018/9/8 11:06
 */
public class CareHistoryNew implements Serializable {
    private String serviceName;
    private String appointment;
    private String extraItem;
    private String workerName;
    private String levelName;
    private String shopName;
    private String serviceLoc;
    private String content;
    private String babyContent;
    private String babySituation;
    private String nickName;
    private String avatar;
    private String workerAvatar;
    private String month;
    private String createTimes;
    private String petName;
    private List<String> beforePicsList = new ArrayList<String>();
    private List<String> picsList = new ArrayList<String>();

    public String getCreateTimes() {
        return createTimes;
    }

    public void setCreateTimes(String createTimes) {
        this.createTimes = createTimes;
    }



    public static CareHistoryNew json2Entity(JSONObject json) {
        CareHistoryNew careHistoryNew = new CareHistoryNew();
        try {
            if (json.has("serviceName") && !json.isNull("serviceName")) {
                careHistoryNew.setServiceName(json.getString("serviceName"));
            }
            if (json.has("appointment") && !json.isNull("appointment")) {
                careHistoryNew.setAppointment(json.getString("appointment"));
            }
            if (json.has("extraItem") && !json.isNull("extraItem")) {
                careHistoryNew.setExtraItem(json.getString("extraItem"));
            }
            if (json.has("workerName") && !json.isNull("workerName")) {
                careHistoryNew.setWorkerName(json.getString("workerName"));
            }
            if (json.has("levelName") && !json.isNull("levelName")) {
                careHistoryNew.setLevelName(json.getString("levelName"));
            }
            if (json.has("shopName") && !json.isNull("shopName")) {
                careHistoryNew.setShopName(json.getString("shopName"));
            }
            if (json.has("serviceLoc") && !json.isNull("serviceLoc")) {
                careHistoryNew.setServiceLoc(json.getString("serviceLoc"));
            }
            if (json.has("content") && !json.isNull("content")) {
                careHistoryNew.setContent(json.getString("content"));
            }
            if (json.has("babyContent") && !json.isNull("babyContent")) {
                careHistoryNew.setBabyContent(json.getString("babyContent"));
            }
            if (json.has("babySituation") && !json.isNull("babySituation")) {
                careHistoryNew.setBabySituation(json.getString("babySituation"));
            }
            if (json.has("nickName") && !json.isNull("nickName")) {
                careHistoryNew.setNickName(json.getString("nickName"));
            }
            if (json.has("createTimes")&&!json.isNull("createTimes")){
                careHistoryNew.setCreateTimes(json.getString("createTimes"));
            }
            if (json.has("avatar") && !json.isNull("avatar")) {
                careHistoryNew.setAvatar(json.getString("avatar"));
            }
            if (json.has("workerAvatar") && !json.isNull("workerAvatar")) {
                careHistoryNew.setWorkerAvatar(json.getString("workerAvatar"));
            }
            if (json.has("month") && !json.isNull("month")) {
                careHistoryNew.setMonth(json.getString("month"));
            }
            if (json.has("petName") && !json.isNull("petName")) {
                careHistoryNew.setPetName(json.getString("petName"));
            }
            if (json.has("beforePics") && !json.isNull("beforePics")) {
                JSONArray beforePics = json.getJSONArray("beforePics");
                if (beforePics.length() > 0) {
                    List<String> list = new ArrayList<String>();
                    list.clear();
                    for (int i = 0; i < beforePics.length(); i++) {
                        list.add(beforePics.getString(i));
                    }
                    careHistoryNew.setBeforePicsList(list);
                }
            }
            if (json.has("pics") && !json.isNull("pics")) {
                JSONArray pics = json.getJSONArray("pics");
                if (pics.length() > 0) {
                    List<String> list = new ArrayList<String>();
                    list.clear();
                    for (int i = 0; i < pics.length(); i++) {
                        list.add(pics.getString(i));
                    }
                    careHistoryNew.setPicsList(list);
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "items()数据异常e = " + e.toString());
            e.printStackTrace();
        }
        return careHistoryNew;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getExtraItem() {
        return extraItem;
    }

    public void setExtraItem(String extraItem) {
        this.extraItem = extraItem;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getServiceLoc() {
        return serviceLoc;
    }

    public void setServiceLoc(String serviceLoc) {
        this.serviceLoc = serviceLoc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBabyContent() {
        return babyContent;
    }

    public void setBabyContent(String babyContent) {
        this.babyContent = babyContent;
    }

    public String getBabySituation() {
        return babySituation;
    }

    public void setBabySituation(String babySituation) {
        this.babySituation = babySituation;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getWorkerAvatar() {
        return workerAvatar;
    }

    public void setWorkerAvatar(String workerAvatar) {
        this.workerAvatar = workerAvatar;
    }
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public List<String> getBeforePicsList() {
        return beforePicsList;
    }

    public void setBeforePicsList(List<String> beforePicsList) {
        this.beforePicsList = beforePicsList;
    }

    public List<String> getPicsList() {
        return picsList;
    }

    public void setPicsList(List<String> picsList) {
        this.picsList = picsList;
    }
}
