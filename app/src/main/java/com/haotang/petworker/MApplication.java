package com.haotang.petworker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.haotang.petworker.utils.ExampleUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.TimeUtils;
import com.haotang.petworker.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.taobao.sophix.SophixManager;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * <p>
 * Title:MApplication
 * </p>
 * <p>
 * Description:主干
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2016-8-15 下午9:15:33
 */
@SuppressLint("NewApi")
public class MApplication extends Application {
    public static ArrayList<Activity> listAppoint;
    private static final int MSG_SET_ALIASANDTAGS = 1001;
    Set<String> tagSet = new LinkedHashSet<String>();
    private SharedPreferenceUtil spUtil;
    private static MApplication instance;
    public static Typeface typeFace;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //查询补丁
        if ((TimeUtils.getHotfixTime(this)) && isMainProcess()){
            SophixManager.getInstance().queryAndLoadNewPatch();
        }

        setTypeface();
        spUtil = SharedPreferenceUtil.getInstance(this);
        instance = this;
        SDKInitializer.initialize(getApplicationContext());
        listAppoint = new ArrayList<Activity>();
        JPushInterface.setDebugMode(Utils.isLog); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPush
        setaliasAndTags();
        String cid = JPushInterface.getRegistrationID(getApplicationContext());
        Utils.mLogError("cid = " + cid);
        if (!cid.isEmpty()) {
            Utils.mLogError("cid = " + cid);
            Global.savePushID(getApplicationContext(), cid);
        }
        initImageLoader(getApplicationContext());
        //友盟设置
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //设置是否对日志信息进行加密, 默认false(不加密).
        UMConfigure.setEncryptEnabled(true);
        UMConfigure.init(getApplicationContext(), "5ab9e7368f4a9d3e0e000036", "pet_worker", UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    /**
     * 通过反射方法设置app全局字体
     */
    public void setTypeface() {
        typeFace = Typeface.createFromAsset(getAssets(), "DIN_Alternate_Bold.ttf");
        try {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, typeFace);
        } catch (NoSuchFieldException e) {
            Log.e("TAG", "e1111111111111111 = " + e.toString());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e("TAG", "e2222222222222222 = " + e.toString());
            e.printStackTrace();
        }
    }

    public static MApplication getInstance() {
        return instance;
    }

    // 初始化ImageLoader
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCacheExtraOptions(720, 1280)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .discCacheExtraOptions(720, 1280, null)
                // Can slow ImageLoader, use it carefully (Better don't use
                // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100)
                // 缓存的文件数量
                // .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
                // (5
                // s),
                // readTimeout
                // (30
                // s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
        ImageLoader.getInstance().init(config);
    }

    private void setaliasAndTags() {
        // 获取电话号码
        String cellphone = spUtil.getString("cellphone", "");
        Utils.mLogError("cellphone = " + cellphone);
        if (cellphone != null && !TextUtils.isEmpty(cellphone)) {
            // 检查 tag 的有效性
            if (!ExampleUtil.isValidTagAndAlias(cellphone)) {
                Utils.mLogError("Invalid format");
            } else {
                tagSet.add(cellphone);
            }
            // 设置别名和tag
            JPushInterface.setAliasAndTags(getApplicationContext(), cellphone,
                    tagSet, mAliasCallback);
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIASANDTAGS:
                    Utils.mLogError("Set aliasandtags in handler.");
                    TagAndAlias tagAndAlias = (TagAndAlias) msg.obj;
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            tagAndAlias.getAlias(), tagAndAlias.getTags(),
                            mAliasCallback);
                    break;
                default:
                    Utils.mLogError("Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Utils.mLogError("设置别名:" + logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Utils.mLogError("设置别名:" + logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(MSG_SET_ALIASANDTAGS,
                                        new TagAndAlias(alias, tags)), 1000 * 60);
                    } else {
                        Utils.mLogError("设置别名:" + "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Utils.mLogError("设置别名:" + logs);
            }
        }
    };

    class TagAndAlias {
        private String alias;
        private Set<String> tags;

        public TagAndAlias(String alias, Set<String> tags) {
            super();
            this.alias = alias;
            this.tags = tags;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public Set<String> getTags() {
            return tags;
        }

        public void setTags(Set<String> tags) {
            this.tags = tags;
        }
    }
    /**
     * 获取当前进程名
     */
    private String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }
    /**
     * 包名判断是否为主进程
     *
     * @param
     * @return
     */
    public boolean isMainProcess() {
        return getApplicationContext().getPackageName().equals(getCurrentProcessName());
    }
}
