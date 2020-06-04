package com.haotang.petworker.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/2 0002.
 * "date":"2017-11",          //历史日期
 * "replyTag":"已回复",        //是否回复标签
 * "workerContent":"店长不好", //我的建议内容
 * "workerImg":[xxx1.jpg,xxx2.jpg,xxx3.jpg],   //我的建议图片
 * "replyContent":"建议已收到"  //回复内容
 * <p>
 * "buttonText":"去填写区域建议", //按钮文案
 * "integralText":"每月首次填写区域建议可获得3积分",  //积分文案
 * "nowDate":"2017-12"     //当前年月
 */

public class FeedBackList {
    public String date;
    public String replyTag;
    public String workerContent;
    public String workerImg;
    public String replyContent;
    public List<String> listImg = new ArrayList<>();
    //editModule
    public String buttonText;
    public String integralText;
    public String nowDate;


    public static FeedBackList j2Entity(JSONObject object){
        FeedBackList feedBackList = new FeedBackList();
        try {
            if (object.has("date")&&!object.isNull("date")){
                feedBackList.date = object.getString("date");
            }
            if (object.has("replyTag")&&!object.isNull("replyTag")){
                feedBackList.replyTag = object.getString("replyTag");
            }
            if (object.has("workerContent")&&!object.isNull("workerContent")){
                feedBackList.workerContent = object.getString("workerContent");
            }
            if (object.has("workerImg")&&!object.isNull("workerImg")){
                JSONArray array = object.getJSONArray("workerImg");
                if (array.length()>0){
                    for (int i = 0;i<array.length();i++){
                        feedBackList.listImg.add(array.getString(i));
                    }
                }
            }
            if (object.has("replyContent")&&!object.isNull("replyContent")){
                feedBackList.replyContent = object.getString("replyContent");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return feedBackList;
    }
}
