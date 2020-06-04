package com.haotang.petworker.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/1/2 0002.
 * "userName": "豆豆妈妈",                     //昵称
 * "avatar": "/static/avatar/1473067185794_55555720.jpg",  //用户头像
 * "memberLevelId":0,                          //用户会员等级
 * "isBuy": 0,                                 //是否买过商品 0未买过,1 买过,
 * "isBuyContent":"未买过商品"                      //是否买过商品标签
 * "completeServiceCount":20                   //预约服务总次数,
 * "completeServiceContent":"约我20次"        //预约服务文案,
 * "nickName":"豆豆,毛球,哈"                        //用户宠物昵称,
 * "isNoBuyContent":"快让TA去宠物家商城买买买吧"   //未买过商品的提示文案
 */

public class MyFans {
    public String userName;
    public String avatar;
    public int memberLevelId;
    public int isBuy;
    public String isBuyContent;
    public int completeServiceCount;
    public String completeServiceContent;
    public String nickName;
    public String isNoBuyContent;

    public static MyFans j2Entity(JSONObject object){
        MyFans myFans = new MyFans();
        try {
            if (object.has("userName")&&!object.isNull("userName")){
                myFans.userName = object.getString("userName");
            }
            if (object.has("avatar")&&!object.isNull("avatar")){
                myFans.avatar = object.getString("avatar");
            }
            if (object.has("memberLevelId")&&!object.isNull("memberLevelId")){
                myFans.memberLevelId = object.getInt("memberLevelId");
            }
            if (object.has("isBuy")&&!object.isNull("isBuy")){
                myFans.isBuy = object.getInt("isBuy");
            }
            if (object.has("isBuyContent")&&!object.isNull("isBuyContent")){
                myFans.isBuyContent = object.getString("isBuyContent");
            }
            if (object.has("completeServiceCount")&&!object.isNull("completeServiceCount")){
                myFans.completeServiceCount = object.getInt("completeServiceCount");
            }
           if (object.has("completeServiceContent")&&!object.isNull("completeServiceContent")){
                myFans.completeServiceContent = object.getString("completeServiceContent");
            }
           if (object.has("nickName")&&!object.isNull("nickName")){
                myFans.nickName = object.getString("nickName");
            }
           if (object.has("isNoBuyContent")&&!object.isNull("isNoBuyContent")){
                myFans.isNoBuyContent = object.getString("isNoBuyContent");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myFans;
    }
}
