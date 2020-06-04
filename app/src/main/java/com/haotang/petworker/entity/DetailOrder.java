package com.haotang.petworker.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-09-24 16:07
 */
public class DetailOrder {
    public double workerincome;
    public double totalCommission;
    public String orderTitle;
    public String statusdes;
    public int status;
    public String actualStartDate;
    public String avatar;
    public String nickName;
    public String typeName;
    public int myPetId;
    public List<OrderTag> orderTagList = new ArrayList<OrderTag>();
    public String addrtype;
    public String firstorderflag;
    public String updateDescription;
    public String pickUpTag;
    public List<String> serviceMinsObjList = new ArrayList<String>();
    public String servicename;
    public String servicecontent;
    public String addserviceitems;
    public String addserviceitemsadd;
    public String username = "";
    public String userphone;
    public String servicetime;
    public String storeaddr;
    public String addr;
    public String note;
    public String bgRemarkToWorker;
    public String orderNo;
    public int enable;
    public int extraEnable;
    public int serviceloc;
    public String status21 = "开始服务";
    public String status22 = "服务完成";
    public String status2 = "准备服务";
    public String month;
    public int petid;
    public int serviceid;
    public int memberLevelId;
    public int cardAmount;
    public double cardBalance;
    public String tip;
    public String integral;
    public String integralCopy;
    public double totalPrice;
    public int careCount;
    public String hotelShopName;

    public static DetailOrder json2Entity(JSONObject json) {
        DetailOrder detailOrder = new DetailOrder();
        try {
            if (json.has("hotelShopName") && !json.isNull("hotelShopName")) {
                detailOrder.hotelShopName = json.getString("hotelShopName");
            }
            if (json.has("careCount") && !json.isNull("careCount")) {
                detailOrder.careCount = json.getInt("careCount");
            }
            if (json.has("totalPrice") && !json.isNull("totalPrice")) {
                detailOrder.totalPrice = json.getDouble("totalPrice");
            }
            if (json.has("actualStartTime") && !json.isNull("actualStartTime")) {
                detailOrder.actualStartDate = json.getString("actualStartTime");
            }
            if (json.has("pickUpTag") && !json.isNull("pickUpTag")) {
                detailOrder.pickUpTag = json.getString("pickUpTag");
            }
            if (json.has("integralCopy") && !json.isNull("integralCopy")) {
                detailOrder.integralCopy = json.getString("integralCopy");
            }
            if (json.has("integral") && !json.isNull("integral")) {
                detailOrder.integral = json.getString("integral");
            }
            if (json.has("statusDesc") && !json.isNull("statusDesc")) {
                detailOrder.statusdes = json.getString("statusDesc");
            }
            if (json.has("upgradeTag") && !json.isNull("upgradeTag")) {
                detailOrder.updateDescription = json.getString("upgradeTag");
            }
            if (json.has("totalPrice") && !json.isNull("totalPrice")) {
                detailOrder.workerincome = json.getDouble("totalPrice");
            }
            if (json.has("totalCommission") && !json.isNull("totalCommission")) {
                detailOrder.totalCommission = json.getDouble("totalCommission");
            }
            if (json.has("orderTitle") && !json.isNull("orderTitle")) {
                detailOrder.orderTitle = json.getString("orderTitle");
            }
            if (json.has("customer")
                    && !json.isNull("customer")) {
                //用户
                JSONObject customerObject = json.getJSONObject("customer");
                if (customerObject.has("cardAmount") && !customerObject.isNull("cardAmount")) {
                    detailOrder.cardAmount = customerObject
                            .getInt("cardAmount");
                }
                if (customerObject.has("cardBalance") && !customerObject.isNull("cardBalance")) {
                    detailOrder.cardBalance = customerObject
                            .getDouble("cardBalance");
                }
                if (customerObject.has("memberLevelId") && !customerObject.isNull("memberLevelId")) {
                    detailOrder.memberLevelId = customerObject
                            .getInt("memberLevelId");
                }
            }
            //订单信息
            if (json.has("orders") && !json.isNull("orders")) {
                JSONArray ordersArray = json.getJSONArray("orders");
                if (ordersArray != null && ordersArray.length() != 0) {
                    JSONObject orderObject = ordersArray.getJSONObject(0);
                    //宠物
                    if (orderObject.has("pet") && !orderObject.isNull("pet")) {
                        JSONObject petObject = orderObject.getJSONObject("pet");
                        if (petObject.has("id")) {
                            detailOrder.petid = petObject.getInt("id");
                        }
                        if (petObject.has("name") && !petObject.isNull("name")) {
                            detailOrder.typeName = petObject.getString("name");
                        }
                    }
                    //我的宠物
                    if (orderObject.has("customerPet") && !orderObject.isNull("customerPet")) {
                        JSONObject customerPet = orderObject.getJSONObject("customerPet");
                        if (customerPet.has("name") && !customerPet.isNull("name")) {
                            detailOrder.nickName = customerPet.getString("name");
                        }
                        if (customerPet.has("id") && !customerPet.isNull("id")) {
                            detailOrder.myPetId = customerPet.getInt("id");
                        }
                        if (customerPet.has("avatar") && !customerPet.isNull("avatar")) {
                            detailOrder.avatar = customerPet.getString("avatar");
                        }
                        if (customerPet.has("month") && !customerPet.isNull("month")) {
                            detailOrder.month = customerPet.getString("month");
                        }
                    }
                    //服务
                    if (orderObject.has("service") && !orderObject.isNull("service")) {
                        JSONObject serviceObject = orderObject.getJSONObject("service");
                        if (serviceObject.has("name") && !serviceObject.isNull("name")) {
                            detailOrder.servicename = serviceObject.getString("name");
                        }
                        if (serviceObject.has("items") && !serviceObject.isNull("items")) {
                            detailOrder.servicecontent = serviceObject.getString("items");
                        }
                        if (serviceObject.has("id")) {
                            detailOrder.serviceid = serviceObject.getInt("id");
                        }
                    }
                    //单项
                    if (orderObject.has("extraItems") && !orderObject.isNull("extraItems")) {
                        JSONArray extraItemsArray = orderObject.getJSONArray("extraItems");
                        if (extraItemsArray.length() > 0) {
                            StringBuffer extrasb = new StringBuffer();
                            StringBuffer extrasbadd = new StringBuffer();
                            extrasb.setLength(0);
                            extrasbadd.setLength(0);
                            for (int i = 0; i < extraItemsArray.length(); i++) {
                                JSONObject extraObject = (JSONObject) extraItemsArray.get(i);
                                if (extraObject.getInt("source") == 0) {
                                    extrasb.append(extraObject.getString("name") + " ");
                                } else {
                                    extrasbadd.append(extraObject.getString("name") + " ");
                                }
                            }
                            if (extrasb.length() > 0) {
                                detailOrder.addserviceitems = extrasb.toString();
                            }
                            if (extrasbadd.length() > 0) {
                                detailOrder.addserviceitemsadd = extrasbadd.toString();
                            }
                        }
                    }
                }
            }
            //升级订单信息
            if (json.has("upgradeOrder") && !json.isNull("upgradeOrder")) {
                JSONObject upgradeOrder = json.getJSONObject("upgradeOrder");
                if (upgradeOrder.has("upgradeService") && !upgradeOrder.isNull("upgradeService")) {
                    detailOrder.enable = upgradeOrder.getInt("upgradeService");
                }
                if (upgradeOrder.has("addExtraItem") && !upgradeOrder.isNull("addExtraItem")) {
                    detailOrder.extraEnable = upgradeOrder.getInt("addExtraItem");
                }
                if (upgradeOrder.has("tip") && !upgradeOrder.isNull("tip")) {
                    detailOrder.tip = upgradeOrder.getString("tip");
                }
            }
            //服务时长
            if (json.has("duration") && !json.isNull("duration")) {
                JSONObject durationObject = json.getJSONObject("duration");
                if (durationObject.has("detail") && !durationObject.isNull("detail")) {
                    JSONArray detailArray = durationObject.getJSONArray("detail");
                    if (detailArray.length() > 0) {
                        detailOrder.serviceMinsObjList.clear();
                        for (int i = 0; i < detailArray.length(); i++) {
                            detailOrder.serviceMinsObjList.add(detailArray.getString(i));
                        }
                    }
                }
            }
            //到店文字
            if (json.has("locTag") && !json.isNull("locTag")) {
                detailOrder.addrtype = json.getString("locTag");
            }
            //首单文字
            if (json.has("firstOrderTag") && !json.isNull("firstOrderTag")) {
                detailOrder.firstorderflag = json.getString("firstOrderTag");
            }
            if (json.has("bgRemarkToWorker") && !json.isNull("bgRemarkToWorker")) {
                detailOrder.bgRemarkToWorker = json.getString("bgRemarkToWorker");
            }
            if (json.has("appointment") && !json.isNull("appointment")) {
                detailOrder.servicetime = json.getString("appointment");
            }
            if (json.has("serviceLoc") && !json.isNull("serviceLoc")) {
                detailOrder.serviceloc = json.getInt("serviceLoc");
            }
            //服务联系信息
            if (json.has("serviceAddress") && !json.isNull("serviceAddress")) {
                JSONObject serviceAddress = json.getJSONObject("serviceAddress");
                //地址
                if (serviceAddress.has("address") && !serviceAddress.isNull("address")) {
                    detailOrder.addr = serviceAddress.getString("address");
                }
                //电话
                if (serviceAddress.has("telephone") && !serviceAddress.isNull("telephone")) {
                    detailOrder.userphone = serviceAddress.getString("telephone");
                }
                if (serviceAddress.has("linkman") && !serviceAddress.isNull("linkman")) {
                    detailOrder.username = serviceAddress.getString("linkman");
                }
            }
            if (json.has("shop") && !json.isNull("shop")) {
                JSONObject shopObject = json.getJSONObject("shop");
                if (shopObject.has("address") && !shopObject.isNull("address")) {
                    detailOrder.storeaddr = shopObject.getString("address");
                }
            }
            if (json.has("remark") && !json.isNull("remark")) {
                detailOrder.note = json.getString("remark");
            }
            if (json.has("orderNum") && !json.isNull("orderNum")) {
                detailOrder.orderNo = json.getString("orderNum");
            }
            if (json.has("status") && !json.isNull("status")) {
                detailOrder.status = json.getInt("status");
            }
        } catch (Exception e) {
            Log.e("TAG", "e = " + e.toString());
            e.printStackTrace();
        }
        return detailOrder;
    }
}
