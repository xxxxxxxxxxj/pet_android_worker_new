package com.haotang.petworker.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.petworker.AgreementActivity;
import com.haotang.petworker.IncomeActivity;
import com.haotang.petworker.LoginNewActivity;
import com.haotang.petworker.MainAcPage;
import com.haotang.petworker.NoticeActivity;
import com.haotang.petworker.OrderActivity;
import com.haotang.petworker.R;
import com.haotang.petworker.entity.ActivityPage;
import com.haotang.petworker.entity.StyleAndColorBean;
import com.loveplusplus.demo.image.ImagePagerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isLog = false;
    private static String log_tag = "petw";

    public static void mLogError(String msg) {
        if (isLog)
            Log.e(log_tag, msg);
    }

    public static void mLog(String msg) {
        if (isLog)
            Log.i(log_tag, msg);
    }

    public static void mLog_d(String msg) {
        if (isLog)
            Log.d(log_tag, msg);
    }

    public static void mLog_v(String msg) {
        if (isLog)
            Log.v(log_tag, msg);
    }

    public static void mLog_w(String msg) {
        if (isLog)
            Log.w(log_tag, msg);
    }

    /**
     * 是否WIFI联网
     *
     * @param context
     * @return
     */
    public static boolean checkWIFI(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (null != networkInfo) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType())
                return true;
        }
        return false;
        // String type = networkInfo.getTypeName();
        // return "WIFI".equalsIgnoreCase(type);
    }

    /**
     * 是否网络可用
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.isAvailable()) {
            return true;
        }
        return false;
        // String type = networkInfo.getTypeName();
        // return "WIFI".equalsIgnoreCase(type);
    }

    /**
     * 获取手机屏幕的宽高
     *
     * @param activity
     * @return
     */
    public static int[] getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 把毫秒为单位的时间格式化为0000-00-00 00:00:00
     *
     * @param time
     * @return
     */
    public static String formatDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(time));
        if (date == null || "".equalsIgnoreCase(date))
            date = "0000-00-00 00:00:00";
        return date;
    }

    /**
     * 把以毫秒为单位的时间格式化为0000-00-00
     *
     * @param time
     * @return
     */
    public static String formatDateShort(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(time));
        if (date == null || "".equalsIgnoreCase(date))
            date = "0000-00-00";
        return date;
    }

    /**
     * 生成一个小于Max的随机数
     *
     * @param max
     * @return
     */
    public static int getRandom(int max) {
        Random rd = new Random();
        return rd.nextInt(max);
    }

    /**
     * 使double类型保留points位小数
     *
     * @param num
     * @return
     */
    public static double formatDouble(double num, int points) {
        BigDecimal bigDecimal = new BigDecimal(num);
        return bigDecimal.setScale(points, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * double类型转换为int类型
     *
     * @param num
     * @return
     */
    public static int formatDouble(double num) {
        return Integer.parseInt(new java.text.DecimalFormat("0").format(num));
    }

    /**
     * 把数据写入sd卡
     *
     * @param log      数据
     * @param filename 文件名
     * @param time     时间，系统 时间，单位毫秒
     */
    public static void writeToSDCard(String log, String filename, long time) {

        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        File dir = new File(path, "haotang");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File guiji = new File(dir, filename);
        try {
            FileWriter fw = new FileWriter(guiji, true);
            fw.append(formatDate(time) + " ::");
            fw.append(log + "\r\n");
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 获取应用的文件夹
     *
     * @return
     */
    public static String getOOPath(Context context) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), ".pet");
        } else {
            file = context.getCacheDir();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 格式化double类型
     *
     * @param pattern 如#0.00为保留两位小数
     * @param decimal
     * @return
     */
    // 格式化里程和费用
    public static String formatDecimal(String pattern, double decimal) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(pattern);
        return df.format(decimal);

    }

    /**
     * 把毫秒为单位的时间格式化为时分秒
     *
     * @param time
     * @return
     */
    // 格式化时间格式
    public static String formatTime(long time) {
        long totalss = time / 1000;
        long timess = totalss % 60;
        long totalmm = totalss / 60;
        long timemm = totalmm % 60;
        long totalhh = totalmm / 60;
        long timehh = totalhh % 60;
        StringBuffer sb = new StringBuffer();
        if (timehh < 10) {
            sb.append("0");
        }
        sb.append(timehh);
        sb.append(":");
        if (timemm < 10) {
            sb.append("0");
        }
        sb.append(timemm);
        sb.append(":");
        if (timess < 10) {
            sb.append("0");
        }
        sb.append(timess);
        return sb.toString();
    }

    /**
     * 把毫秒为单位的时间格式化为时分秒
     *
     * @param time
     * @return
     */
    // 格式化时间格式
    public static String formatTimeFM(long time) {
        long totalss = time / 1000;
        long timess = totalss % 60;
        long totalmm = totalss / 60;
        long timemm = totalmm % 60;
        long totalhh = totalmm / 60;
        long timehh = totalhh % 60;
        StringBuffer sb = new StringBuffer();
        if (timehh < 10) {
            sb.append("0");
        }
        sb.append(timehh);
        sb.append(":");
        if (timemm < 10) {
            sb.append("0");
        }
        sb.append(timemm);
        sb.append(":");
        if (timess < 10) {
            sb.append("0");
        }
        sb.append(timess);
        return sb.toString();
    }

    /**
     * 格式化以分钟为单位的时间，转化为小时：分钟
     *
     * @param minutestr
     * @return
     */
    public static String formatMinutesToHour(String minutestr) {
        int minutes = Integer.parseInt(minutestr);
        int minute = minutes % 60;
        int hour = minutes / 60;
        if (0 == hour) {
            return minute + "分钟";
        } else {
            return hour + "小时" + minute + "分钟";
        }
    }

    /**
     * 创建并获取文件夹的路径
     *
     * @param context
     * @return
     */
    public static String getDir(Context context) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile(), "pet");
        } else {
            file = context.getCacheDir();
        }
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    /**
     * 创建精度条对话框
     *
     * @param ctx
     * @param info
     * @return
     */
    public static ProgressDialog createProcessDialog(Context ctx, String info) {
        ProgressDialog processDialog = new ProgressDialog(ctx);
        processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processDialog.setTitle("");
        processDialog.setMessage(info);
        processDialog.setIcon(android.R.drawable.ic_dialog_map);
        processDialog.setIndeterminate(false);
        processDialog.setCancelable(true);
        processDialog.setCanceledOnTouchOutside(false);
        processDialog.show();
        return processDialog;

    }

    /**
     * 电话验证
     *
     * @param
     * @return
     */
    public static boolean checkPhone(Context context, String phone) {
        boolean isAvail = isNumeric(phone);
        if ("".equals(phone.trim())) {
            return false;
        }
        if (!isAvail) {
            return false;
        }
        return true;
    }

    /**
     * 邮箱验证
     *
     * @param context
     * @param email_et
     * @return
     */
    public static boolean checkEmail(Context context, EditText email_et) {
        String emailPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        String email = email_et.getText().toString().trim();
        boolean isAvail = startCheck(emailPattern, email);

        if (!isAvail) {
            ToastUtil.showToastCenterShort(context, "邮箱格式不正确，请重新输入");
            email_et.requestFocus();
            email_et.setFocusableInTouchMode(true);
            return false;
        }
        if ("".equals(email.trim())) {
            ToastUtil.showToastCenterShort(context, "邮箱为空，请重新输入");
            email_et.requestFocus();
            email_et.setFocusableInTouchMode(true);
            return false;
        }
        if (email.length() > 50) {
            ToastUtil.showToastCenterShort(context, "邮箱长度不能大于50，请重新输入");
            email_et.requestFocus();
            email_et.setFocusableInTouchMode(true);
            return false;
        }
        return true;
    }

    /**
     * 正则规则验证
     *
     * @param reg
     * @param string
     * @return
     */
    public static boolean startCheck(String reg, String string) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * app是否正在运行
     *
     * @param context
     * @return
     */
    public static boolean isAppAvailable(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskinfos = am.getRunningTasks(20);
        boolean result = false;
        for (int i = 0; i < taskinfos.size(); i++) {
            if (context.getPackageName().equals(
                    taskinfos.get(i).topActivity.getPackageName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static void telePhoneBroadcast(Activity activity, String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }

    public static void telePhoneBroadcast(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    // 改变时间样式
    public static String ChangeTime(String dateOld) {
        String n = dateOld.replace("年", "-");
        String y = n.replace("月", "-");
        String r = y.replace("日", " ");
        String s = r.replace("时", ":");
        String f = s.replace("分", "");
        return f;
    }

    // 改变时间样式
    public static String ChangeTime2(String dateOld) {
        String lastStr = dateOld.substring(0, dateOld.indexOf("日") + 1);
        String n = lastStr.replace("年", "-");
        String y = n.replace("月", "-");
        String r = y.replace("日", " ");
        String s = r.replace("时", ":");
        String f = s.replace("分", "");
        return f;
    }

    // 设置圆角背景
    public static Drawable getDW(String locColor) {
        int strokeWidth = 1; // 3dp 边框宽度
        int roundRadius = 6; // 8dp 圆角半径
        int strokeColor = Color.parseColor("#" + locColor);// 边框颜色
        int fillColor = Color.parseColor("#" + locColor);// 内部填充颜色
        GradientDrawable gd = new GradientDrawable();// 创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    @SuppressLint({"NewApi", "InlinedApi"})
    public static Bitmap getxtsldraw(Context c, String file) {
        File f = new File(file);
        Bitmap drawable = null;
        if (f.length() / 1024 < 100) {
            drawable = BitmapFactory.decodeFile(file);
        } else {
            Cursor cursor = c.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + " like ?",
                    new String[]{"%" + file}, null);
            if (cursor == null || cursor.getCount() == 0) {
                drawable = getbitmap(file, 720 * 1280);
            } else {
                if (cursor.moveToFirst()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    String videoId = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media._ID));
                    long videoIdLong = Long.parseLong(videoId);
                    drawable = MediaStore.Images.Thumbnails.getThumbnail(
                            c.getContentResolver(), videoIdLong,
                            Thumbnails.MINI_KIND, options);
                } else {
                    // drawable = BitmapFactory.decodeResource(c.getResources(),
                    // R.drawable.ic_doctor);
                }
            }
        }
        int degree = 0;
        ExifInterface exifInterface;
        try {
            exifInterface = new android.media.ExifInterface(file);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
            if (degree != 0 && drawable != null) {
                Matrix m = new Matrix();
                m.setRotate(degree, (float) drawable.getWidth() / 2,
                        (float) drawable.getHeight() / 2);
                drawable = Bitmap.createBitmap(drawable, 0, 0,
                        drawable.getWidth(), drawable.getHeight(), m, true);
            }
            Log.e("TAG", "getxtsldraw degree = " + degree);
        } catch (java.lang.OutOfMemoryError e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static Bitmap getbitmap(String imageFile, int length) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile, opts);
        int ins = computeSampleSize(opts, -1, length);
        opts.inSampleSize = ins;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inJustDecodeBounds = false;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
            return bmp;
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static String creatfile(Context pContext, Bitmap bm, String filename) {
        if (bm == null) {
            return "";
        }
        File f = new File(getSDCardPath(pContext) + "/" + filename + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(f);
            if (bm.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static String getSDCardPath(Context pContext) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.jiuyi160_c";
            File PathDir = new File(path);
            if (!PathDir.exists()) {
                PathDir.mkdirs();
            }
            return path;
        } else {
            return pContext.getCacheDir().getAbsolutePath();
        }
    }

    public static void setImage(ImageView iv, String path,
                                final int defaultimgid) {
        if (path != null && !"".equals(path.trim())) {
            ImageLoaderUtil.loadImg(path, iv,
                    R.drawable.icon_production_default, null);
        }
    }

    public static void setImageWithTag(ImageView iv, String path,
                                       final int defaultimgid) {
        if (path != null && !"".equals(path.trim())) {

            ImageLoaderUtil.loadImg(path, iv,
                    R.drawable.icon_production_default, null);

        }
    }

    public static void setStringText(TextView tv, String str) {
        if (str != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.INVISIBLE);
        }
    }

    public static JSONObject getNetData(Context context, byte[] responseBody,
                                        String codeStr) {
        JSONObject objectData = null;
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            int code = object.getInt(codeStr);
            if (code == 0) {
                if (object.has("data") && !object.isNull("data")) {
                    objectData = object.getJSONObject("data");
                }
            } else {
                if (object.has("msg") && !object.isNull("msg")) {
                    String msg = object.getString("msg");
                    ToastUtil.showToastBottomShort(context, msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objectData;
    }

    public static void setText(TextView tv, String str, String defaultStr,
                               int visibilt, int defaultVisibilt) {
        if (str != null && !TextUtils.isEmpty(str)) {
            tv.setText(str);
            tv.setVisibility(visibilt);
        } else {
            tv.setText(defaultStr);
            tv.setVisibility(defaultVisibilt);
        }
    }

    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     * <p>
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     * <p>
     * B.本地路径:url="file://mnt/sdcard/photo/image.png";
     * <p>
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @return
     */
    public static Bitmap GetLocalOrNetBitmap(String imageUrl) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(imageUrl).openStream(),
                    IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (Exception e) {
            Log.e("TAG", "e.toString() = " + e.toString());
            Log.e("TAG", "e = " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public final static int IO_BUFFER_SIZE = 1024;

    public static void imageBrower(Context context, int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    // 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Context context,
                                             final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static void goneJP(Context context) {
        try {
            ((InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(((Activity) context)
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap toturn(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(+degree); /* 翻转90度 */
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    public static void playSound(Context context) {
        // 第一个参数为允许soundPool同时播放几个音效,这里只播放1个音效
        // 第二个参数为播放的流的类型
        // 第三个参数为播放的音效的质量,目前该值设为0即可
        AudioManager manager = (AudioManager) context
                .getSystemService(context.AUDIO_SERVICE);
        int maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final float Volume = currentVolume / (float) maxVolume;
        SoundPool pool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        pool.load(context, R.raw.neworder, 1);
        Log.e("TAG", "pool = " + pool);
        pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                Log.e("TAG", "soundPool = " + soundPool + ",,,sampleId = "
                        + sampleId + ",,,status = " + status + ",,,Volume = "
                        + Volume);
                soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1);
            }
        });
    }

    private static void initBeepSound(Context context) {
        // ((Activity)
        // context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(beepListener);
        AssetFileDescriptor file = context.getResources().openRawResourceFd(
                R.raw.ceshi);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.prepare();
        } catch (IOException e) {
            mediaPlayer = null;
        }
        playBeepSoundAndVibrate(context, mediaPlayer);
    }

    private static void playBeepSoundAndVibrate(Context context,
                                                MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        Vibrator vibrator = (Vibrator) context
                .getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(200L);
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final static OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 计算时间差
     *
     * @param oldTime
     * @param newTime
     * @return
     */
    public static int TimeCha(String oldTime, String newTime) {
        int days = 0;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(newTime);
            Date d2 = df.parse(oldTime);
            long diff = d1.getTime() - d2.getTime();
            days = (int) (diff / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    @SuppressLint("NewApi")
    public static void hideBottomUIMenu(Activity context) {
        // 隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower
            // api
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            // for new api versions.
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static boolean isStrNull(String str) {
        boolean bool = false;
        if (str != null && !TextUtils.isEmpty(str)) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    public static void setTextAndStyle(Activity mActivity, String str, int startLength, int endLength, int style, TextView textView) {
        if (isStrNull(str)) {
            SpannableString ss = new SpannableString(str);
            ss.setSpan(new TextAppearanceSpan(mActivity, style),
                    startLength, endLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(ss);
        }
    }

    public static void setTextAndColor(Context mActivity, String str, int startLength, int endLength, int color, TextView textView) {
        if (isStrNull(str)) {
            SpannableString ss = new SpannableString(str);
            ss.setSpan(
                    new ForegroundColorSpan(mActivity.getResources().getColor(color)), startLength, endLength,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(ss);
        }
    }

    public static void setTextStyleAndColor(Context mActivity, String str, int color, int style, TextView textView) {
        if (isStrNull(str)) {
            SpannableString ss = new SpannableString(removeIndexStr(removeIndexStr(str, "#"), "&"));
            String[] split = removeIndexStr(str, "&").split("#");
            if (split.length > 0) {//改变颜色
                StringBuffer sb = new StringBuffer();
                sb.setLength(0);
                List<Integer> xList = new ArrayList<Integer>();
                xList.clear();
                List<StyleAndColorBean> colorList = new ArrayList<StyleAndColorBean>();
                colorList.clear();
                for (int i = 0; i < split.length; i++) {
                    sb.append(split[i]);
                    int index = sb.toString().length();
                    Log.e("TAG", "index = " + index);
                    Log.e("TAG", "sb.toString() = " + sb.toString());
                    xList.add(index);
                }
                Log.e("TAG", "xList.size() = " + xList.size());
                if (xList.size() % 2 != 0) {
                    xList.remove(xList.size() - 1);
                }
                if (xList.size() > 0 && xList.size() % 2 == 0) {
                    for (int i = 0; i < xList.size(); i++) {
                        if (i % 2 != 0) {
                            colorList.add(new StyleAndColorBean(xList.get(i - 1), xList.get(i)));
                        }
                    }
                    for (int i = 0; i < colorList.size(); i++) {
                        Log.e("TAG", "#" + i + " = " + colorList.get(i).getStartIndex());
                        Log.e("TAG", "#" + i + " = " + colorList.get(i).getEndIndex());
                        ss.setSpan(
                                new ForegroundColorSpan(mActivity.getResources().getColor(color)),
                                colorList.get(i).getStartIndex(), colorList.get(i).getEndIndex(),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
            String[] split1 = removeIndexStr(str, "#").split("&");
            if (split1.length > 0) {//改变大小和颜色
                StringBuffer sb = new StringBuffer();
                sb.setLength(0);
                List<Integer> aList = new ArrayList<Integer>();
                aList.clear();
                List<StyleAndColorBean> styleAndColorList = new ArrayList<StyleAndColorBean>();
                styleAndColorList.clear();
                for (int i = 0; i < split1.length; i++) {
                    sb.append(split1[i]);
                    int index = sb.toString().length();
                    aList.add(index);
                }
                if (aList.size() % 2 != 0) {
                    aList.remove(aList.size() - 1);
                }
                if (aList.size() > 0 && aList.size() % 2 == 0) {
                    for (int i = 0; i < aList.size(); i++) {
                        if (i % 2 != 0) {
                            styleAndColorList.add(new StyleAndColorBean(aList.get(i - 1), aList.get(i)));
                        }
                    }
                    for (int i = 0; i < styleAndColorList.size(); i++) {
                        Log.e("TAG", "&" + i + " = " + styleAndColorList.get(i).getStartIndex());
                        Log.e("TAG", "&" + i + " = " + styleAndColorList.get(i).getEndIndex());
                        ss.setSpan(new TextAppearanceSpan(mActivity, style),
                                styleAndColorList.get(i).getStartIndex(), styleAndColorList.get(i).getEndIndex(),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        ss.setSpan(
                                new ForegroundColorSpan(mActivity.getResources().getColor(color)),
                                styleAndColorList.get(i).getStartIndex(), styleAndColorList.get(i).getEndIndex(),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
            textView.setText(ss);
        } else {
            textView.setText("");
        }
    }

    public static int getStartIndex(String str, String indexStr) {
        int startIndex = 0;
        if (isStrNull(str) && str.contains(indexStr)) {
            startIndex = str.indexOf(indexStr);
        }
        return startIndex;
    }

    public static int getLastIndex(String str, String indexStr) {
        int lastIndex = 0;
        if (isStrNull(str) && str.contains(indexStr)) {
            lastIndex = str.lastIndexOf(indexStr) - 1;
        }
        return lastIndex;
    }

    public static String removeIndexStr(String str, String indexStr) {
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        if (isStrNull(str)) {
            String[] split = str.split(indexStr);
            if (split != null && split.length > 0) {
                for (int j = 0; j < split.length; j++) {
                    sb.append(split[j]);
                }
            } else {
                sb.append(str);
            }
        } else {
            sb.append("");
        }
        return sb.toString();
    }

    public static boolean isNumeric(String str) {
        str.trim().replace(" ", "");
        if (str.length() != 11) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPhone1(Context context, EditText phone_et) {
        String phone = phone_et.getText().toString().trim().replace(" ", "");
        boolean isAvail = isNumeric(phone);
        if ("".equals(phone.trim())) {
            phone_et.requestFocus();
            phone_et.setFocusableInTouchMode(true);
            return false;
        }
        if (!isAvail) {
            phone_et.requestFocus();
            phone_et.setFocusableInTouchMode(true);
            return false;
        }
        return true;
    }

    /**
     * 获取应用的文件夹
     *
     * @return
     */
    public static String getPetPath(Context context) {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), ".pet");
        } else {
            file = context.getCacheDir();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static boolean checkLogin(Context mContext) {
        boolean bool;
        String wcellphone = SharedPreferenceUtil.getInstance(mContext)
                .getString("wcellphone", "");
        if (wcellphone != null && !wcellphone.isEmpty()) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    public static void goService(Activity mActivity, int point, String backup) {
        if(checkLogin(mActivity)){
            switch (point) {
                case 31://订单列表页
                    mActivity.startActivity(new Intent(mActivity, OrderActivity.class));
                    break;
                case 32:// 订单排名页
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.MainActivity");
                    intent.putExtra("index", 1);
                    mActivity.sendBroadcast(intent);
                    break;
                case 33://我的
                    Intent intent1 = new Intent();
                    intent1.setAction("android.intent.action.MainActivity");
                    intent1.putExtra("index", 2);
                    mActivity.sendBroadcast(intent1);
                    break;
                case 34://学习园地页
                    mActivity.startActivity(new Intent(mActivity, AgreementActivity.class).putExtra("url", backup));
                    break;
                case 35://收入明细页
                    mActivity.startActivity(new Intent(mActivity, IncomeActivity.class));
                    break;
                case 36://公告通知页
                    mActivity.startActivity(new Intent(mActivity, NoticeActivity.class));
                    break;
            }
        }else{
            Intent intent = new Intent(mActivity, LoginNewActivity.class);
            mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    //向指定的文件中写入指定的数据
    public static void writeFileData(Context context, String filename, String content) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND);//获得FileOutputStream
            //将要写入的字符串转换为byte数组
            byte[] bytes = content.getBytes();
            fos.write(bytes);//将byte数组写入文件
            fos.close();//关闭文件输出流
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "writeFileData e = " + e.toString());
        }
    }

    //打开指定文件，读取其数据，返回字符串对象
    public static String readFileData(Context context, String fileName) {
        String result = "";
        try {
            FileInputStream fis = context.openFileInput(fileName);
            //获取文件长度
            int lenght = fis.available();
            byte[] buffer = new byte[lenght];
            fis.read(buffer);
            //将byte数组转换成指定格式的字符串
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "readFileData e = " + e.toString());
        }
        return result;
    }

    public static void ActivityPage(Activity mActivity, List<ActivityPage> bannerList) {
        Intent intent = new Intent(mActivity, MainAcPage.class);
        intent.putExtra("bannerList", (Serializable) bannerList);
        mActivity.startActivity(intent);
    }

    public static String getCityName(int type){
        return type == 1 ? "北京" : "深圳";
    }
}
