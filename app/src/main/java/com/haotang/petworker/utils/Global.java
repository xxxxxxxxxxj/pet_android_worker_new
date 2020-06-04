package com.haotang.petworker.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Global {
    public static final int EXITE_SYSTEM = 100;
    public static final int EXITE_SYSTEM_DELAYEDTIME = 2000;
    public static final int FLASH_DELAYEDTIME = 3000;
    public static final int ADDPRODUCTION_TO_SERVICECATEGORY = 3001;
    public static final int NURSINGRECORD_TO_ADDRECORD = 3002;
    public static final int UPGRADESERVICE_TO_PAYCODE = 3003;
    public static final int ORDERDETAIL_TO_NURSINGRECORD = 3004;
    public static final int LOGIN_STARTTIME_VERIFICATION = 101;
    public static final int BEAUTICIANINFO_TO_SIGN = 102;
    public static final int APK_DOWNLOAD_OVER = 103;
    public static final int APK_DOWNLOAD_FAIL = 104;
    public static final int APK_DOWNLOADING = 105;
    public static final int ADDPET_TO_PETLIST = 3005;
    public static final int RESULT_OK = 1000;
    public static final int ADDRECORD_TO_IMGSHOW = 106;
    public static final int UPGRADESERVICE_CHOOSEPET = 3006;
    public static final int AREAANDEVATOPULLSERVER = 3100;
    public static final int ORDERADAPTER_TO_ORDERDETAIL = 3101;
    public static final String MD5_STR = "haotang_jishubu01";
    public static final int ORDERDETAIL_TO_SERVICE = 3102;
    public static final int ORDERDETAIL_TO_ITEM = 3103;
    public static final int CODE_EXIT = 100003;

    /**
     * 个推
     * <p>
     * APPID:nnrvbw5LomAgQKnSHnlZn8 APPKEY:NgHYvDn71i8V8OlrdBaWU4
     * APPSECRET:HoN3421Je479ZFVDXHi8Q7 MASTERSECRET:F2jxbvMhrC7IOjRcncqzx8
     */

    public static int ANIM_COVER_FROM_LEFT() {
        return 0;
    }

    public static int ANIM_COVER_FROM_RIGHT() {
        return 1;
    }

    public static int ANIM_NONE() {
        return -1;
    }

    public static String ANIM_DIRECTION() {
        return "anim_direction";
    }

    /**
     * 获取IMEI码
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {

        return GetDeviceId.readDeviceID(context);
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public static String getCurrentVersion(Context context) {
        String curVersion = "0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);// getPackageName()是你当前类的包名，0代表是获取版本信息
            curVersion = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curVersion;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phone
     */
    public static void cellPhone(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * @param urlpath  下载链接URL
     * @param savepath 保存的路径
     * @param filename 保存的文件名
     * @return 保存的文件
     */
    public static File downloadApk(String urlpath, String savepath,
                                   String filename, Handler handler) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlpath);
            HttpResponse response = client.execute(httpGet);
            InputStream is = response.getEntity().getContent();
            File file = new File(savepath, filename);
            long total = response.getEntity().getContentLength();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buff = new byte[2 * 1024];
            int len = 0;
            int count = 0;
            long current = 0;
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
                count++;
                current += len;
                if (count % 13 == 0 || current == total) {
                    Message msg = handler.obtainMessage();
                    if (current == total) {
                        msg.what = APK_DOWNLOAD_OVER;
                    } else {
                        msg.what = APK_DOWNLOADING;
                    }
                    msg.obj = current * 100.0 / total;
                    handler.sendMessage(msg);
                }
            }
            is.close();
            fos.close();
            return file;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Message msg = handler.obtainMessage();
            msg.what = APK_DOWNLOAD_FAIL;
            handler.sendMessage(msg);
            return null;
        }
    }

    public static void savePushID(Context context, String cid) {
        SharedPreferenceUtil.getInstance(context).saveString("wcid", cid);
    }

    public static String getPushID(Context context) {
        return SharedPreferenceUtil.getInstance(context).getString("wcid", "");
    }

    public static String encodeWithBase64(Bitmap bitmap) {
        if (bitmap == null)
            return "";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);// 在保证图片尽量不失真的情况下，减少图片上传,下载所需要的流量
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }
}
