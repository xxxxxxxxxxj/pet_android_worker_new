package com.haotang.petworker.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.haotang.petworker.entity.EatTimeBean;
import com.haotang.petworker.net.AsyncHttpClient;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.net.RequestParams;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.List;

public class CommUtil {
    private static int getEnvironmental() {
        return 3;//1.test环境---2.demo环境---3.线上环境
    }

    /**
     * 获取带端口的IP地址
     *
     * @return
     */
    public static String getServiceBaseUrl() {
        String url = "";
        switch (getEnvironmental()) {
            case 1://test环境
                url = "http://192.168.0.252/";
                break;
            case 2://demo环境
                url = "http://demo.cwjia.cn/";
                break;
            case 3://线上环境
                url = "https://api.cwjia.cn/";
                break;
            default:
                break;
        }
        return url;
    }

    public static String getServiceBaseUrl1() {
        String url = "";
        switch (getEnvironmental()) {
            case 1://test环境
                url = "http://192.168.0.28/pet-worker/";
                break;
            case 2://demo环境
                url = "http://demo.cwjia.cn/pet-worker/";
                break;
            case 3://线上环境
                url = "https://worker.ichongwujia.com/";
                break;
            default:
                break;
        }
        return url;
    }

    /**
     * 图片和H5的域名头
     *
     * @return
     */
    public static String getStaticUrl() {
        String url = "";
        switch (getEnvironmental()) {
            case 1://test环境
                url = "http://192.168.0.252/";
                break;
            case 2://demo环境
                url = "http://demo.cwjia.cn/";
                break;
            case 3://线上环境
                url = "http://static.ichongwujia.com/";
                break;
            default:
                break;
        }
        return url;
    }

    public static String getSource() {
        return "worker_android";
    }

    /**
     * 设置超时时间
     *
     * @return
     */
    public static int getTimeOut() {
        return 60 * 1000;
    }

    public static RequestParams LocalParmPost(Context context) {
        RequestParams params = new RequestParams();
        String imei = Global.getIMEI(context);
        if (imei != null && !TextUtils.isEmpty(imei)) {
            params.put("imei", imei);
        }
        params.put("account", 1);
        params.put("phoneModel", android.os.Build.BRAND + " "
                + android.os.Build.MODEL);
        params.put("phoneSystemVersion", "Android "
                + android.os.Build.VERSION.RELEASE);
        params.put("petTimeStamp", System.currentTimeMillis());
        return params;
    }

    public static RequestParams LocalParm(Context context) {
        RequestParams params = new RequestParams();
        String cellPhone = SharedPreferenceUtil.getInstance(context).getString(
                "wcellphone", "");
        String imei = Global.getIMEI(context);
        String system = getSource() + "_" + Global.getCurrentVersion(context);
        if (cellPhone != null && !TextUtils.isEmpty(cellPhone)) {
            params.put("cellPhone", cellPhone);
        }
        if (imei != null && !TextUtils.isEmpty(imei)) {
            params.put("imei", imei);
        }
        if (system != null && !TextUtils.isEmpty(system)) {
            params.put("system", system);
        }
        params.put("phoneModel", android.os.Build.BRAND + " "
                + android.os.Build.MODEL);
        params.put("phoneSystemVersion", "Android "
                + android.os.Build.VERSION.RELEASE);
        params.put("petWorkerTimeStamp", System.currentTimeMillis());
        return params;
    }

    /**
     * 自动登录
     *
     * @param phone
     * @param imei
     * @param handler
     */
    public static void loginAuto(Context context, String phone, String imei,
                                 String version, int userid, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/user/autoLogin";
        try {
            RequestParams params = LocalParm(context);
            params.put("id", userid);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("自动登录参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param phone
     * @param imei
     * @param context
     * @param code
     * @param handler
     */
    public static void login(Context context, String phone, String imei,
                             String version, String code, double lat, double lng,
                             AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/user/login";
        try {
            RequestParams params = new RequestParams();
            params.put("channelId", "Android_1");
            params.put("imei", Global.getIMEI(context));
            params.put("system",
                    getSource() + "_" + Global.getCurrentVersion(context));
            params.put("phoneModel", android.os.Build.BRAND + " "
                    + android.os.Build.MODEL);
            params.put("phoneSystemVersion", "Android "
                    + android.os.Build.VERSION.RELEASE);
            params.put("cellPhone", phone);
            params.put("petWorkerTimeStamp", System.currentTimeMillis());
            params.put("code", code);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("登录参数："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param handler
     */
    public static void genVerifyCode(Context context, String phone, String code, String mobileKey, String encryptionCode,
                                     AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/user/genVerifyCode";
        try {
            RequestParams params = LocalParm(context);
            params.put("phone", phone);
            params.put("code", code);
            params.put("mobileKey", mobileKey);
            params.put("encryptionCode", encryptionCode);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLog("验证码参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取美容师协议
     *
     * @param phone
     * @param imei
     * @param version
     * @param handler
     */
    public static void getAgreementContent(Context context, String phone,
                                           String imei, String version, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/user/getAgreementContent";
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取美容师协议参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 同意美容师协议
     *
     * @param phone
     * @param imei
     * @param version
     * @param handler
     */
    public static void workerAgreement(Context context, String phone,
                                       String imei, String version, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/user/workerAgreement";
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("同意美容师协议参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取美容师设置的时间
     *
     * @param phone
     * @param imei
     * @param version
     * @param startDate
     * @param days
     * @param handler
     */
    public static void getWorkerTime(Context context, String phone,
                                     String imei, String version, String startDate, int days,
                                     AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/schedule/list";
        try {
            RequestParams params = LocalParm(context);
            params.put("startDate", startDate);
            params.put("days", days);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取美容师时间参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取美容师服务方式设置
     *
     * @param phone
     * @param imei
     * @param version
     * @param startDate
     * @param days
     * @param handler
     */
    public static void getWorkLoc(Context context, String phone, String imei,
                                  String version, String startDate, int days,
                                  AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl()
                + "pet/worker/queryWorkLocSettingsForm";
        try {
            RequestParams params = LocalParm(context);
            params.put("startDate", startDate);
            params.put("days", days);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取美容师服务方式参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 保存美容师时间
     *
     * @param phone
     * @param imei
     * @param version
     * @param other   日期的json数组串
     * @param handler
     */
    public static void saveWorkerTime(Context context, String phone,
                                      String imei, String version, String other,
                                      AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1()
                + "worker/schedule/update";
        try {
            RequestParams params = LocalParm(context);
            params.put("other", other);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("保存美容师时间参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 保存美容服务方式设置
     *
     * @param phone
     * @param imei
     * @param version
     * @param other
     * @param handler
     */
    public static void saveWorkLoc(Context context, String phone, String imei,
                                   String version, String other, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl()
                + "pet/worker/addOrUpdateWorkLocSettings";
        try {
            RequestParams params = LocalParm(context);
            params.put("other", other);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("保存美容师服务方式参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取最新的app，是否强制升级
     *
     * @param handler
     */
    public static void getLatestVer(Context context, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1()
                + "worker/user/checkversion";
        try {
            RequestParams params = LocalParm(context);
            params.put("systemType", 2);
            params.put("version", Global.getCurrentVersion(context));
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("升级："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 向后台传当前坐标
     *
     * @param phone
     * @param imei
     * @param version
     * @param lat
     * @param lng
     * @param handler
     */
    public static void sendLocation(Context context, String phone, String imei,
                                    String version, double lat, double lng,
                                    AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/user/hb";
        try {
            RequestParams params = LocalParm(context);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("传坐标参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 去后台注册个推
     *
     * @param phone    可选
     * @param version
     * @param userId   可选
     * @param devToken 个推返回的cid
     * @param handler
     */
    public static void registGeTuitoService(Context context, String phone,
                                            String version, String imei, int userId, String devToken,
                                            double lat, double lng, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/user/registUserDevice";
        try {
            RequestParams params = LocalParm(context);
            params.put("devToken", devToken);
            params.put("userId", userId);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("注册个推参数："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 意见反馈
     *
     * @param phone   注册手机号
     * @param imei    手机IMEI码
     * @param version 应用版本号
     * @param content 反馈内容
     * @param handler
     */
    public static void feedBack(Context context, String phone, String imei,
                                String version, String content, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/user/feedback";
        try {
            RequestParams params = LocalParm(context);
            params.put("content", content);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("意见反馈参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取美容师的作品列表
     *
     * @param phone
     * @param imei
     * @param version
     * @param userid
     * @param orderid
     * @param before
     * @param after
     * @param pageSize
     * @param handler
     */
    public static void getProduction(Context context, String phone,
                                     String imei, String version, int userid, int orderid, long before,
                                     long after, int pageSize, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/queryWorksByUserId";
        try {
            RequestParams params = LocalParm(context);
            params.put("userId", userid);
            if (orderid > 0)
                params.put("orderId", orderid);
            params.put("before", before);
            params.put("after", after);
            params.put("pageSize", pageSize);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("美容师作品列表参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取用户对该美容师的评价
     *
     * @param phone
     * @param imei
     * @param version
     * @param before
     * @param after
     * @param pageSize
     * @param handler
     */
    public static void getComments(Context context, String phone, String imei,
                                   String version, int page, int pageSize, int credit,
                                   AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/queryCommentsForWorker";
        try {
            RequestParams params = LocalParm(context);
            params.put("page", page);
            params.put("pageSize", pageSize);
            if (credit != -1) {
                params.put("credit", credit);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("美容师评论列表参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取美容师信息
     *
     * @param phone
     * @param imei
     * @param version
     * @param handler
     */
    public static void getWorkerInfo(Context context, String phone,
                                     String imei, String version, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/queryWorkerInfoForWorker";
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("美容师信息参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param phone
     * @param imei
     * @param version
     * @param orderId 要升级的订单id
     * @param handler
     */
    public static void getUpgradeServiceInfo(Context context, String phone,
                                             String imei, String version, int orderId, int petId,
                                             AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/update/query";
        try {
            RequestParams params = LocalParm(context);
            params.put("petId", petId);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("可升级订单参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 升级订单
     *
     * @param phone
     * @param imei
     * @param version
     * @param orderId订单id
     * @param eItemIds升级项目的id用逗号分开
     * @param handler
     */
    public static void upgradeServiceInfo(Context context, String phone,
                                          String imei, String version, int orderId, String eItemIds,
                                          int isHome, int isBeauty, int petId, double payPrice,
                                          AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/update/submit";
        try {
            RequestParams params = LocalParm(context);
            params.put("isHome", isHome);
            params.put("isBeauty", isBeauty);
            params.put("petId", petId);
            params.put("payPrice", payPrice);
            params.put("id", orderId);
            params.put("eItemIds", eItemIds);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("升级订单参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取服务类型
     *
     * @param handler
     */
    public static void getServiceFull(AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/pet/petServiceFull";
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("获取服务类型参数：" + url);
            client.get(url, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 取消升级订单
     *
     * @param phone
     * @param imei
     * @param context
     * @param id
     * @param handler
     */
    public static void cancelOrder(String phone, String imei, Context context,
                                   int id, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/update/cancel";
        try {
            RequestParams params = LocalParm(context);
            params.put("id", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->取消"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 美容是添加作品
     *
     * @param phone
     * @param imei
     * @param version
     * @param orderid
     * @param workTitle标题
     * @param des描述
     * @param pic作品
     * @param petkind宠物类别
     * @param serviceid作品类别
     * @param handler
     */
    public static void addWork(Context context, String phone, String imei,
                               String version, int orderid, String title, String des, String pic,
                               int petkind, int serviceid, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/work/addWork?system=" + getSource() + "_" + Global.getCurrentVersion(context) + "&cellPhone=" + SharedPreferenceUtil.getInstance(context).getString(
                "wcellphone", "");
        try {
            RequestParams params = LocalParmPost(context);
            if (orderid > 0)
                params.put("orderId", orderid);
            params.put("workTitle", title);
            params.put("description", des);
            params.put("pic", pic);
            params.put("petKind", petkind);
            params.put("serviceId", serviceid);
            Log.e("TAG", "params = " + params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("添加作品参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.post(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 更新美容师签名
     *
     * @param phone
     * @param imei
     * @param version
     * @param sign
     * @param handler
     */
    public static void updateWorkerSign(Context context, String phone,
                                        String imei, String version, String sign,
                                        AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/updateWorkerSign";
        try {
            RequestParams params = LocalParm(context);
            params.put("mySign", sign);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("更新美容师签名参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除美容师作品
     *
     * @param phone
     * @param imei
     * @param version
     * @param workid
     * @param handler
     */
    public static void deleteProduction(Context context, String phone,
                                        String imei, String version, int workid,
                                        AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/work/deleteWorks";
        try {
            RequestParams params = LocalParm(context);
            params.put("workId", workid);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("删除美容师作品参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 申请拒单
     *
     * @param phone
     * @param imei
     * @param version
     * @param orderid
     * @param lat
     * @param lng
     * @param reason
     * @param handler
     */
    public static void cancelOrder(Context context, String phone, String imei,
                                   String version, int orderid, double lat, double lng, String reason,
                                   AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl() + "pet/order/rejectOrder";
        try {
            RequestParams params = LocalParm(context);
            params.put("id", orderid);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            params.put("rejectReason", reason);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("申请拒单参数："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 查询订单详情 /pet/order/queryOrderDetailWorker
     *
     * @param cellPhone
     * @param imei
     * @param context
     * @param id
     * @param handler
     */
    public static void orderDetail(Context context, long id, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "workerOrder/info";
        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("订单详情："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * /worker/queryAnnouncement
     *
     * @param cellPhone
     * @param imei
     * @param context
     * @param before
     * @param pageSize
     * @param handler
     */
    public static void queryAnnouncement(String cellPhone, String imei,
                                         Context context, long before, int pageSize,
                                         AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/queryAnnouncement";
        try {
            RequestParams params = LocalParm(context);
            if (before != 0) {
                params.put("before", before);
            }
            params.put("pageSize", pageSize);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->queryAnnouncement："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // /pet/order/addCareHistory
    public static void addCareHistory(Context context, long orderId, int petGender, String age,
                                      String content, String petName, String babyContent, String items,
                                      File[] beforeImgs, File[] imgs, int babySituation, int ageGroup, int petPosture, String petCharacter, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "careHistory/addCareHistory?system=" + getSource() + "_" + Global.getCurrentVersion(context) + "&cellPhone=" + SharedPreferenceUtil.getInstance(context).getString(
                "wcellphone", "");
        try {
            RequestParams params = LocalParmPost(context);
            if (Utils.isStrNull(petCharacter)) {
                params.put("petCharacter", petCharacter);
            }
            params.put("ageGroup", ageGroup);
            params.put("petPosture", petPosture);
            params.put("orderId", orderId);
            params.put("petGender", petGender);
            if (Utils.isStrNull(items)) {
                params.put("items", items);
            }
            if (beforeImgs != null && beforeImgs.length > 0) {
                params.put("beforeImgs", beforeImgs);
            }
            if (imgs != null && imgs.length > 0) {
                params.put("imgs", imgs);
            }
            if (Utils.isStrNull(content)) {
                params.put("content", content);
            }
            if (Utils.isStrNull(babyContent)) {
                params.put("babyContent", babyContent);
            }
            params.put("age", age);
            params.put("nickName", petName);
            params.put("babySituation", babySituation);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
            Utils.mLogError("==-->addCareHistory："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取宠物列表
     *
     * @param kind      宠物类型
     * @param serviceId 服务类型
     * @param handler
     */
    public static void getPetList(Context context, int kind, int orderId,
                                  AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/pet/petList";
        try {
            RequestParams params = LocalParm(context);
            params.put("system",
                    getSource() + "_" + Global.getCurrentVersion(context));
            if (kind > 0)
                params.put("kind", kind);
            if (orderId > 0)
                params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->获取宠物列表："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 月收入
     *
     * @param handler
     */
    public static void getmonthincome(Context context, String month, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "income/getmonthincome";
        try {
            RequestParams params = LocalParm(context);
            if (month != null && !TextUtils.isEmpty(month)) {
                params.put("month", month);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("==-->月收入:"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 护理记录历史
     *
     * @param context
     * @param orderId
     * @param addCareHistory
     */
    public static void toAddCareHistory(Context context, int orderId, AsyncHttpResponseHandler addCareHistory) {
        String url = getServiceBaseUrl1() + "careHistory/toAddCareHistory";
        try {
            RequestParams params = LocalParm(context);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, addCareHistory);
            Utils.mLogError("==-->护理记录历史:"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void petlistNew(Context mContext,
                                  AsyncHttpResponseHandler petlistNewHandler) {
        String url = getServiceBaseUrl1() + "worker/pet/petlistNew";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, petlistNewHandler);
            Utils.mLogError("新宠物列表"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryWorkerService(Context mContext, int workid,
                                          int petid, AsyncHttpResponseHandler queryWorkerServiceHandler) {
        String url = getServiceBaseUrl1() + "worker/service/list";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("workerId", workid);
            params.put("petId", petid);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, queryWorkerServiceHandler);
            Utils.mLogError("获取可设置的服务"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerService(Context mContext, String wsStr,
                                           AsyncHttpResponseHandler updateWorkerServiceHandler) {
        String url = getServiceBaseUrl1() + "worker/service/update";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("wsStr", wsStr);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, updateWorkerServiceHandler);
            Utils.mLogError("修改服务时长"
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void underIcons(Context mContext, AsyncHttpResponseHandler underIconsHanler) {
        String url = getServiceBaseUrl1() + "home/underIcons";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, underIconsHanler);
            Utils.mLogError("首页底部："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getMainInfo(Context mContext, String lastFlushTime, AsyncHttpResponseHandler getMainInfoHandler) {
        String url = getServiceBaseUrl1() + "worker/homePage";
        try {
            RequestParams params = LocalParm(mContext);
            if (Utils.isStrNull(lastFlushTime)) {
                params.put("lastFlushTime", lastFlushTime);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getMainInfoHandler);
            Utils.mLogError("首页数据："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void orderList(Context mContext, int status, double lat, double lng, int page, AsyncHttpResponseHandler orderListHandler) {
        String url = getServiceBaseUrl1() + "workerOrder/order";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("status", status);
            params.put("page", page);
            params.put("lat", lat == 4.9E-324 ? 0 : lat);
            params.put("lng", lng == 4.9E-324 ? 0 : lng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, orderListHandler);
            Utils.mLogError("订单列表："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getHistoricalData(Context mContext, AsyncHttpResponseHandler getHistoricalDataHandler) {
        String url = getServiceBaseUrl1() + "workerOrder/historicalData";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getHistoricalDataHandler);
            Utils.mLogError("历史订单数据下发："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void workerUpdate(Context mContext, int orderId, double operLat, double operLng, AsyncHttpResponseHandler workerUpdateHanler) {
        String url = getServiceBaseUrl1() + "workerOrder/workerUpdate";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("orderId", orderId);
            params.put("operLat", operLat == 4.9E-324 ? 0 : operLat);
            params.put("operLng", operLng == 4.9E-324 ? 0 : operLng);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, workerUpdateHanler);
            Utils.mLogError("快捷操作："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getincomeratio(Context mContext, int workerid, AsyncHttpResponseHandler getincomeratioHandler) {
        String url = getServiceBaseUrl1() + "income/getincomeratio";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("workerid", workerid);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, getincomeratioHandler);
            Utils.mLogError("获取提成比率信息："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void myIncomeRanking(Context mContext, AsyncHttpResponseHandler myIncomeRankingHandler) {
        String url = getServiceBaseUrl1() + "ranking/myIncomeRanking";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, myIncomeRankingHandler);
            Utils.mLogError("商品奖励统计数据："
                    + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 美容师粉丝列表
     *
     * @param mContext
     * @param page
     * @param handler
     */
    public static void workerFansList(Context mContext, int page, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/fans/workerFansList";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("page", page);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("美容师粉丝列表：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 美容师粉丝基础信息
     *
     * @param mContext
     * @param handler
     */
    public static void workerFansInfo(Context mContext, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "worker/fans/workerFansInfo";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("美容师粉丝基础信息：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 意见反馈个人信息列表
     *
     * @param mContext
     * @param handler
     */
    public static void myFeedBackList(Context mContext, int type, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "feedBack/myFeedBackList";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("type", type);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("意见反馈个人信息列表：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 意见反馈编辑展示
     *
     * @param mContext
     * @param handler
     */
    public static void editFeedBack(Context mContext, int type, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "feedBack/editFeedBack";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("type", type);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("意见反馈编辑展示：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 意见反馈信息保存
     *
     * @param mContext
     * @param handler
     */
    public static void addFeedBack(Context mContext, int type, int isAnonymous, String workerContent, String workerImg, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "feedBack/addFeedBack";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("type", type);
            params.put("isAnonymous", isAnonymous);
            params.put("workerContent", workerContent);
            params.put("workerImg", workerImg);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
            Utils.mLogError("意见反馈信息保存：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加意见反馈图片接口
     *
     * @param mContext
     * @param handler
     */
    public static void upLoadPicForFeedBack(Context mContext, File[] imgs, String[] imgsFileNames, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl1() + "feedBack/upLoadPicForFeedBack?system=" + getSource() + "_" + Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "wcellphone", "");
        try {
            RequestParams params = LocalParmPost(mContext);
            params.put("imgs", imgs);
            params.put("imgsFileNames", imgsFileNames);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Integer.MAX_VALUE);
            client.post(url, params, handler);
            Utils.mLogError("添加意见反馈图片接口：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pictureOnWorker(Context mContext, AsyncHttpResponseHandler pictureOnWorkerHandler) {
        String url = getServiceBaseUrl1() + "worker/user/pictureOn";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, pictureOnWorkerHandler);
            Utils.mLogError("美容师端校验是否开启图片验证码：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateImageCodeWorker(Context mContext, String mobileKey, AsyncHttpResponseHandler generateImageCodeWorkerHandler) {
        String url = getServiceBaseUrl1() + "worker/user/generateImageCode";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("mobileKey", mobileKey);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, generateImageCodeWorkerHandler);
            Utils.mLogError("生成图片验证码美容师端：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void services(Context mContext, int orderId, int petId, AsyncHttpResponseHandler servicesHanler) {
        String url = getServiceBaseUrl1() + "worker/update/services";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("orderId", orderId);
            params.put("petId", petId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, servicesHanler);
            Utils.mLogError("可升级项目列表：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extraItems(Context mContext, int orderId, int petId, int serviceId,String source, AsyncHttpResponseHandler extraItemsHanler) {
        String url = getServiceBaseUrl1() + "worker/update/extraItems";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("orderId", orderId);
            params.put("petId", petId);
            params.put("serviceId", serviceId);
            if(Utils.isStrNull(source)){
                params.put("source", source);
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, extraItemsHanler);
            Utils.mLogError("可追加单项列表：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void submit(Context mContext, int orderId, int petId, int serviceId, String itemIds, double totalPrice, AsyncHttpResponseHandler submitHanler) {
        String url = getServiceBaseUrl1() + "worker/update/submit";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("orderId", orderId);
            params.put("petId", petId);
            if (Utils.isStrNull(itemIds)) {
                params.put("eItemIds", itemIds);
            }
            params.put("serviceId", serviceId);
            params.put("payPrice", totalPrice);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, submitHanler);
            Utils.mLogError("提交升级：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancel(Context mContext, int orderId, AsyncHttpResponseHandler cancelHanler) {
        String url = getServiceBaseUrl1() + "worker/update/cancel";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("orderId", orderId);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, cancelHanler);
            Utils.mLogError("取消升级：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPetCareHistory(Context mContext, int id, AsyncHttpResponseHandler getPetCareHistoryHanler) {
        String url = getServiceBaseUrl1() + "careHistory/getPetCareHistory";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("id", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, getPetCareHistoryHanler);
            Utils.mLogError("宠物档案：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tagDetail(Context mContext, int id, AsyncHttpResponseHandler tagDetailHandler) {
        String url = getServiceBaseUrl1() + "careHistory/tagDetail";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("id", id);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, tagDetailHandler);
            Utils.mLogError("查看标签：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void workerIntegralInfo(Context mContext, AsyncHttpResponseHandler tagDetailHandler) {
        String url = getServiceBaseUrl1() + "worker/integral/workerIntegralInfo";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, tagDetailHandler);
            Utils.mLogError("查看积分信息：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void workerIntegralList(Context mContext, int page, int limit, AsyncHttpResponseHandler tagDetailHandler) {
        String url = getServiceBaseUrl1() + "worker/integral/workerIntegralList";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("page", page);
            params.put("limit", limit);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, tagDetailHandler);
            Utils.mLogError("查看积分明细：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rankinglList(Context mContext, int scope, AsyncHttpResponseHandler tagDetailHandler) {
        String url = getServiceBaseUrl1() + "ranking/orderRankingList";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("scope", scope);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, tagDetailHandler);
            Utils.mLogError("查看排行榜：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mallRewardlList(Context mContext, int scope, AsyncHttpResponseHandler tagDetailHandler) {
        String url = getServiceBaseUrl1() + "ranking/mallRewardRankingList";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("scope", scope);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, tagDetailHandler);
            Utils.mLogError("查看商品排行榜：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gratuityRankingList(Context mContext, int scope, AsyncHttpResponseHandler tagDetailHandler) {
        String url = getServiceBaseUrl1() + "ranking/gratuityRankingList";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("scope", scope);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, tagDetailHandler);
            Utils.mLogError("查看打赏红包排行榜：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setEatTime(Context mContext, List<EatTimeBean> list, AsyncHttpResponseHandler setEatTimeHanler) {
        String url = getServiceBaseUrl1() + "worker/schedule/update?system=" + getSource() + "_" +
                Global.getCurrentVersion(mContext) + "&cellPhone=" + SharedPreferenceUtil.getInstance(mContext).getString(
                "wcellphone", "")
                + "&imei=" + Global.getIMEI(mContext)
                + "&account=1";
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            JSONArray jSONArray = JSONArray.parseArray(JSON.toJSONString(list));
            ByteArrayEntity entity = new ByteArrayEntity(jSONArray.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, RequestParams.APPLICATION_JSON));
            client.post(mContext, url, entity, RequestParams.APPLICATION_JSON, setEatTimeHanler);
            Utils.mLogError("设置吃饭时间：" + client.getUrlWithQueryString(true, url, null));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "setEatTime2 e = " + e.toString());
        }
    }

    public static void getEatTime(Context mContext, AsyncHttpResponseHandler getEatTimeHanler) {
        String url = getServiceBaseUrl1() + "worker/schedule/lunchConfigs";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, getEatTimeHanler);
            Utils.mLogError("获取吃饭时间：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 月收入新版
     *
     * @param monthIncomeHandler
     */
    public static void getMonthIncome(Context mContext, String month, AsyncHttpResponseHandler monthIncomeHandler) {
        String url = getServiceBaseUrl1() + "income/workerIncomeInfo/getmonthincome";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("month", month);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, monthIncomeHandler);
            Utils.mLogError("查看月收入：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 意见反馈小红点
     */
    public static void havingNewReplay(Context mContext, String lastRequestTimeType0, String lastRequestTimeType1, String lastRequestTimeType2, AsyncHttpResponseHandler monthIncomeHandler) {
        String url = getServiceBaseUrl1() + "feedBack/havingNewReplay";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("lastRequestTimeType0", lastRequestTimeType0);
            params.put("lastRequestTimeType1", lastRequestTimeType1);
            params.put("lastRequestTimeType2", lastRequestTimeType2);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, params, monthIncomeHandler);
            Utils.mLogError("意见反馈小红点：" + client.getUrlWithQueryString(true, url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动页
     *
     * @param context
     * @param startPageHandler
     */
    public static void startPage(Context context,
                                 AsyncHttpResponseHandler startPageHandler) {
        String url = getServiceBaseUrl1() + "home/startPageConfig/startShowImg";
        try {
            RequestParams params = LocalParm(context);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("==-->启动页："
                    + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, startPageHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 弹框
     */
    public static void getActivityPage(Activity mContext, int activityPage, AsyncHttpResponseHandler getActivityPage) {
        String url = getServiceBaseUrl1() + "home/getActivityPage";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("activityPage", activityPage);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("弹框：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, getActivityPage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void healthReport(Activity mContext, AsyncHttpResponseHandler healthReportHandler) {
        String url = getServiceBaseUrl1() + "worker/health/report";
        try {
            RequestParams params = LocalParm(mContext);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("健康上报：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, healthReportHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void serviceOrderDetail(Context mContext,String date, AsyncHttpResponseHandler healthReportHandler) {
        String url = getServiceBaseUrl1() + "income/workerIncomeInfo/queryServiceOrderInfo";
        try {
            RequestParams params = LocalParm(mContext);
            params.put("date",date);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            Utils.mLogError("服务订单详情：" + client.getUrlWithQueryString(true, url, params));
            client.get(url, params, healthReportHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
