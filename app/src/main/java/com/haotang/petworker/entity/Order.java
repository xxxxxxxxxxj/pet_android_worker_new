package com.haotang.petworker.entity;

import android.util.Log;

import com.haotang.petworker.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1698723L;

    public String orderNo;
    public int orderid;
    public String servicetime;
    public String statusdes;
    public int status;
    public String addr;
    public String username = "";
    public String userphone;
    public double listprice;
    public double workerincome;
    public double totalprice;
    public String dist;
    public String servicename;
    public String servicecontent;
    public String addserviceitems;
    public String servicepic;
    public String servicedes;
    public int servicetype;
    public int serviceid;
    public long remainTime = -1;
    public String petname;
    public int petid;
    public String petpic;
    public String addrtype;
    public String addrtypecolor;
    public String firstorderflag;
    public String firstorderflagcolor;
    public int serviceloc;
    public int petkind;
    public int payWay;
    public int carecount;
    public String note;
    public String storeaddr;
    public String storephone;
    public int customerId;

    public String extraServiceItems;
    public String bgRemarkToWorker;

    public String orderSource;
    public int setoutEnable;
    public String serviceMins;
    public int memberLevelId;
    public String pickUpTag;
    public String integralCopy;
    public String integral;
    public String integralTag;
    public String status21;
    public String status22;
    public boolean isShow;
    public String extraItem;
    public String avatar;
    public String month;
    public String nickName;
    public int sex;
    public String typeName;
    public int myPetId;
    public double payPrice;
    public long headerId;
    public List<OrderTag> orderTagList = new ArrayList<OrderTag>();
    public String updateDescription;
    public String orderTitle;
    public String actualStartDate;
    public List<String> serviceMinsObjList = new ArrayList<String>();
    public String status2;
    public int enable;
    public int extraEnable;
    public int refType;
    public String tip;

    public static Order json2Entity(JSONObject json) {
        Order order = new Order();
        try {
            if (json.has("updateEnableObj")
                    && !json.isNull("updateEnableObj")) {
                JSONObject jupdateEnableObj = json
                        .getJSONObject("updateEnableObj");
                if (jupdateEnableObj.has("enable")
                        && !jupdateEnableObj.isNull("enable")) {
                    order.enable = jupdateEnableObj.getInt("enable");
                }
                if (jupdateEnableObj.has("extra")
                        && !jupdateEnableObj.isNull("extra")) {
                    order.extraEnable = jupdateEnableObj.getInt("extra");
                }
                if (jupdateEnableObj.has("tip")
                        && !jupdateEnableObj.isNull("tip")) {
                    order.tip = jupdateEnableObj.getString("tip");
                }
            }
            if (json.has("serviceMinsObj")
                    && !json.isNull("serviceMinsObj")) {
                JSONArray jarrServiceMinsObj = json
                        .getJSONArray("serviceMinsObj");
                if (jarrServiceMinsObj.length() > 0) {
                    order.serviceMinsObjList.clear();
                    for (int i = 0; i < jarrServiceMinsObj.length(); i++) {
                        order.serviceMinsObjList.add(jarrServiceMinsObj.getString(i));
                    }
                }
            }
            if (json.has("task") && !json.isNull("task")) {
                JSONObject jtask = json.getJSONObject("task");
                if (jtask.has("actualStartDate")
                        && !jtask.isNull("actualStartDate")) {
                    order.actualStartDate = jtask
                            .getString("actualStartDate");
                }
            }
            if (json.has("extraItem")
                    && !json.isNull("extraItem")) {
                StringBuffer sb = new StringBuffer();
                JSONArray jarrextraItem = json.getJSONArray("extraItem");
                for (int i = 0; i < jarrextraItem.length(); i++) {
                    sb.append(jarrextraItem.getString(i) + " ");
                }
                order.extraItem = sb.toString();
            }
            if (json.has("updateOrderFeeObj") && !json.isNull("updateOrderFeeObj")) {
                JSONObject jupdateOrderFeeObj = json.getJSONObject("updateOrderFeeObj");
                if (jupdateOrderFeeObj.has("payPrice") && !jupdateOrderFeeObj.isNull("payPrice")) {
                    order.payPrice = jupdateOrderFeeObj.getDouble("payPrice");
                }
            }
            if (json.has("buttonText") && !json.isNull("buttonText")) {
                JSONObject jbuttonText = json.getJSONObject("buttonText");
                if (jbuttonText.has("2")
                        && !jbuttonText.isNull("2")) {
                    order.status2 = jbuttonText.getString("2");
                }
                if (jbuttonText.has("21") && !jbuttonText.isNull("21")) {
                    order.status21 = jbuttonText.getString("21");
                }
                if (jbuttonText.has("22") && !jbuttonText.isNull("22")) {
                    order.status22 = jbuttonText.getString("22");
                }
            }
            if (json.has("refType") && !json.isNull("refType")) {
                order.refType = json.getInt("refType");
            }
            if (json.has("orderTitle") && !json.isNull("orderTitle")) {
                order.orderTitle = json.getString("orderTitle");
            }
            if (json.has("integralTag") && !json.isNull("integralTag")) {
                order.integralTag = json.getString("integralTag");
            }
            if (json.has("integralCopy") && !json.isNull("integralCopy")) {
                order.integralCopy = json.getString("integralCopy");
            }
            if (json.has("integral") && !json.isNull("integral")) {
                order.integral = json.getString("integral");
            }
            if (json.has("memberLevelId") && !json.isNull("memberLevelId")) {
                order.memberLevelId = json.getInt("memberLevelId");
            }
            if (json.has("pickUpTag") && !json.isNull("pickUpTag")) {
                order.pickUpTag = json.getString("pickUpTag");
            }
            if (json.has("serviceMins") && !json.isNull("serviceMins")) {
                order.serviceMins = json.getString("serviceMins");
            }
            if (json.has("setoutEnable") && !json.isNull("setoutEnable")) {
                order.setoutEnable = json.getInt("setoutEnable");
            }
            if (json.has("orderId") && !json.isNull("orderId")) {
                order.orderid = json.getInt("orderId");
            }
            if (json.has("customerId") && !json.isNull("customerId")) {
                order.customerId = json.getInt("customerId");
            }
            if (json.has("payWay") && !json.isNull("payWay")) {
                order.payWay = json.getInt("payWay");
            }
            if (json.has("careCount") && !json.isNull("careCount")) {
                order.carecount = json.getInt("careCount");
            }
            if (json.has("serviceLoc") && !json.isNull("serviceLoc")) {
                order.serviceloc = json.getInt("serviceLoc");
            }
            if (json.has("orderNum") && !json.isNull("orderNum")) {
                order.orderNo = json.getString("orderNum");
            }
            if (json.has("remark") && !json.isNull("remark")) {
                order.note = json.getString("remark");
            }
            if (json.has("locTag") && !json.isNull("locTag")) {
                order.addrtype = json.getString("locTag");
            }
            if (json.has("locColor") && !json.isNull("locColor")) {
                order.addrtypecolor = json.getString("locColor");
            }
            if (json.has("firstOrderTag") && !json.isNull("firstOrderTag")) {
                order.firstorderflag = json.getString("firstOrderTag");
            }
            if (json.has("firstOrderColor") && !json.isNull("firstOrderColor")) {
                order.firstorderflagcolor = json.getString("firstOrderColor");
            }
            if (json.has("appointment") && !json.isNull("appointment")) {
                order.servicetime = json.getString("appointment");
            }
            if (json.has("statusDescription")
                    && !json.isNull("statusDescription")) {
                order.statusdes = json.getString("statusDescription");
            }
            if (json.has("status") && !json.isNull("status")) {
                order.status = json.getInt("status");
            }
            if (json.has("address") && !json.isNull("address")) {
                order.addr = json.getString("address");
            }

            if (json.has("customerName") && !json.isNull("customerName")) {
                order.username = json.getString("customerName");
            }
            if (json.has("customerMobile") && !json.isNull("customerMobile")) {
                order.userphone = json.getString("customerMobile");
            }
            if (json.has("listPrice") && !json.isNull("listPrice")) {
                order.listprice = Utils.formatDouble(
                        json.getDouble("listPrice"), 2);
            }
            if (json.has("workerIncome") && !json.isNull("workerIncome")) {
                order.workerincome = Utils.formatDouble(
                        json.getDouble("workerIncome"), 2);
            }
            if (json.has("totalPrice") && !json.isNull("totalPrice")) {
                order.totalprice = Utils.formatDouble(
                        json.getDouble("totalPrice"), 2);
            }
            if (json.has("dist") && !json.isNull("dist")) {
                order.dist = json.getString("dist");
            }
            if (json.has("countdown") && !json.isNull("countdown")) {
                order.remainTime = json.getLong("countdown");
            }
            if (json.has("pet") && !json.isNull("pet")) {
                JSONObject jpet = json.getJSONObject("pet");
                if (jpet.has("petId") && !jpet.isNull("petId")) {
                    order.petid = jpet.getInt("petId");
                }
                if (jpet.has("petKind") && !jpet.isNull("petKind")) {
                    order.petkind = jpet.getInt("petKind");
                }
                if (jpet.has("avatarPath") && !jpet.isNull("avatarPath")) {
                    order.petpic = jpet.getString("avatarPath");
                }
                if (jpet.has("petName") && !jpet.isNull("petName")) {
                    order.petname = jpet.getString("petName");
                }
            }
            if (json.has("shop") && !json.isNull("shop")) {
                JSONObject jshop = json.getJSONObject("shop");
                if (jshop.has("address") && !jshop.isNull("address")) {
                    order.storeaddr = jshop.getString("address");
                }
                if (jshop.has("phone") && !jshop.isNull("phone")) {
                    order.storephone = jshop.getString("phone");
                }
            }
            if (json.has("myPet") && !json.isNull("myPet")) {
                JSONObject jmyPet = json.getJSONObject("myPet");
                if (jmyPet.has("month") && !jmyPet.isNull("month")) {
                    order.month = jmyPet.getString("month");
                }
                if (jmyPet.has("nickName") && !jmyPet.isNull("nickName")) {
                    order.nickName = jmyPet.getString("nickName");
                }
                if (jmyPet.has("sex") && !jmyPet.isNull("sex")) {
                    order.sex = jmyPet.getInt("sex");
                }
                if (jmyPet.has("typeName") && !jmyPet.isNull("typeName")) {
                    order.typeName = jmyPet.getString("typeName");
                }
                if (jmyPet.has("id") && !jmyPet.isNull("id")) {
                    order.myPetId = jmyPet.getInt("id");
                }
                if (jmyPet.has("avatar") && !jmyPet.isNull("avatar")) {
                    order.avatar = jmyPet.getString("avatar");
                }
            }
            if (json.has("petService") && !json.isNull("petService")) {
                order.serviceid = json.getInt("petService");
            }
            if (json.has("petServicePojo") && !json.isNull("petServicePojo")) {
                JSONObject jserviceitems = json.getJSONObject("petServicePojo");
                if (jserviceitems.has("serviceType")
                        && !jserviceitems.isNull("serviceType")) {
                    order.servicetype = jserviceitems.getInt("serviceType");
                }
                if (jserviceitems.has("id")
                        && !jserviceitems.isNull("id")) {
                    order.serviceid = jserviceitems.getInt("id");
                }
                if (jserviceitems.has("name") && !jserviceitems.isNull("name")) {
                    order.servicename = jserviceitems.getString("name");
                }
                if (jserviceitems.has("pic") && !jserviceitems.isNull("pic")) {
                    order.servicepic = jserviceitems.getString("pic");
                }
                if (jserviceitems.has("description")
                        && !jserviceitems.isNull("description")) {
                    order.servicedes = jserviceitems.getString("description");
                }
                if (jserviceitems.has("serviceItem")
                        && !jserviceitems.isNull("serviceItem")) {
                    StringBuffer sb = new StringBuffer();
                    JSONArray items = jserviceitems.getJSONArray("serviceItem");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject sitem = items.getJSONObject(i);
                        if (sitem.has("itemName") && !sitem.isNull("itemName")) {
                            sb.append(sitem.getString("itemName") + "+");
                        }
                    }
                    order.servicecontent = sb.substring(0, sb.toString()
                            .length() - 1);
                }

            }
            if (json.has("extraServiceItems")
                    && !json.isNull("extraServiceItems")) {
                JSONArray items = json.getJSONArray("extraServiceItems");
                if (items != null && items.length() > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject sitem = items.getJSONObject(i);
                        if (sitem.has("name") && !sitem.isNull("name")) {
                            sb.append(sitem.getString("name") + "+");
                        }
                    }
                    order.addserviceitems = sb.substring(0,
                            sb.toString().length() - 1);
                }
            }

            if (json.has("updateOrder") && !json.isNull("updateOrder")) {
                JSONObject object = json.getJSONObject("updateOrder");
                if (object.has("status") && !object.isNull("status")) {
                    int upgradestatus = object.getInt("status");
                    if (2 == upgradestatus) {
                        if (object.has("extraServiceItems")
                                && !object.isNull("extraServiceItems")) {
                            JSONArray array = object
                                    .getJSONArray("extraServiceItems");
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < array.length(); i++) {
                                builder.append(array.getJSONObject(i)
                                        .getString("name") + "+");
                            }
                            order.extraServiceItems = builder.substring(0,
                                    builder.toString().length() - 1);
                        }
                    }
                }
                if (object.has("updateDescription") && !object.isNull("updateDescription")) {
                    order.updateDescription = object.getString("updateDescription");
                }
            }
            if (json.has("bgRemarkToWorker")
                    && !json.isNull("bgRemarkToWorker")) {
                order.bgRemarkToWorker = json.getString("bgRemarkToWorker");
            }
            if (json.has("orderSource") && !json.isNull("orderSource")) {
                order.orderSource = json.getString("orderSource");
            }
        } catch (Exception e) {
            Log.e("TAG", "e = " + e.toString());
            e.printStackTrace();
        }
        return order;
    }

}
