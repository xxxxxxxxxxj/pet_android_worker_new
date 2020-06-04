package com.haotang.petworker;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.AlertDialogDefault;
import com.haotang.petworker.view.MyGridView;
import com.umeng.analytics.MobclickAgent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2018/1/3 0003.
 */

public class AreaAndEvaFeedBackActivity extends SuperActivity {
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_feedback_data)
    TextView tvFeedbackData;
    private ImageButton ib_titlebar_back;
    private TextView tv_titlebar_title;
    List<Bitmap> imgList = new ArrayList<Bitmap>();
    MyAdapter ImgAdapter;
    private MyGridView gridView_get_dog_phone;
    private PopupWindow pWin;
    private LinearLayout ll_addImg;
    private RelativeLayout rvPopRoot;
    private File out;
    private static final int SELECT_CAMER = 2;
    private List<File> listFile = new ArrayList<File>();
    ArrayList<String> list = new ArrayList<String>();
    private String path = "";
    private static final int IMAGE_CERTIFICATION = 101;
    private MyReceiver receiver;
    private int type;
    private String title;
    private TextView textview_top_show;
    private EditText edit_content;
    private TextView textview_length;
    private ImageView is_anonymous;
    private Button buton_pull_server;
    private boolean isAnonymous = false;
    private int pickIntent;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private String year;
    private String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_eva_feed_back_activity);
        ButterKnife.bind(this);

        getLocalTime();
        initView();
        initGridView();
        setView();
        initListener();
        initReceiver();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.editFeedBack(mContext, type, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        JSONObject obj = object.getJSONObject("data");
                        if (obj.has("integralText") && !obj.isNull("integralText")) {
                            textview_top_show.setText(obj.getString("integralText"));
                        }
                        if (obj.has("backGroundText") && !obj.isNull("backGroundText")) {
                            textview_top_show.setHint(obj.getString("backGroundText"));
                        }
                        if (obj.has("anonymousText") && !obj.isNull("anonymousText")) {

                        }
                        if (obj.has("buttonText") && !obj.isNull("buttonText")) {

                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void getLocalTime() {
        Calendar calendar=Calendar.getInstance();
        year = calendar.get(Calendar.YEAR)+"";
        if (calendar.get(Calendar.MONTH)+1>=10){
            month = calendar.get(Calendar.MONTH)+1+"";
        }else {
            int i = calendar.get(Calendar.MONTH) + 1;
            month = "0"+i;
        }
    }

    private void initReceiver() {
        // 广播事件**********************************************************************
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.AreaAndEvaFeedBackActivity");
        // 注册广播接收器
        registerReceiver(receiver, filter);
    }

    private void initListener() {
        btTitlebarOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AreaAndEvaFeedBackActivity.this, AreaFeedbackHistoryActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        gridView_get_dog_phone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= 5) {
                    if (position == imgList.size() && position != 5/* &&position<=2 */) {
                        showSelectDialog();
                    } else if (position == 5) {
                        Toast.makeText(mContext, "当前最多支持五张图片", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        edit_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textview_length.setText(s.length() + "/1000");
            }
        });
        ib_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithAnimation();
            }
        });
        is_anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnonymous) {
                    isAnonymous = true;
                    is_anonymous.setBackgroundResource(R.drawable.icon_tag_select);
                } else {
                    isAnonymous = false;
                    is_anonymous.setBackgroundResource(R.drawable.icon_pay_normal);
                }
            }
        });
        buton_pull_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_content.getText())) {
                    ToastUtil.showToastCenterShort(mContext, "请填写内容");
                    return;
                }
                showBackDialog();
            }
        });
    }

    private void showBackDialog() {
        new AlertDialogDefault(mActivity).builder()
                .setTitle("提示").setMsg("本月仅能填写1次且提交后不可修改，确认提交吗？").isOneBtn(false).setNegativeButton("再想想", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setPositiveButton("确认提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPDialog.showDialog();
                final File[] imgs = new File[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    imgs[i] = new File(list.get(i));
                }
                String[] fileNames = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    fileNames[i] = list.get(i);
                }
                if (list.size() > 0) {
                    CommUtil.upLoadPicForFeedBack(mContext, imgs, null, saveImgHandler);
                } else {
                    saveData(null);
                }
            }
        }).show();
    }

    private void saveData(String workerImg) {
        mPDialog.showDialog();
        int isAnon = 0;
        if (isAnonymous) {
            isAnon = 1;
        } else {
            isAnon = 0;
        }
        String editStr = null;
        if (!TextUtils.isEmpty(edit_content.getText())) {
            editStr = edit_content.getText().toString();
        }
        CommUtil.addFeedBack(mContext, type, isAnon, editStr, workerImg, saveHandler);
    }

    private AsyncHttpResponseHandler saveImgHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Utils.mLogError("==-->imgs " + new String(responseBody));
            try {
                mPDialog.closeDialog();
                JSONObject object = new JSONObject(new String(responseBody));
                int code = object.getInt("code");
                if (code == 0) {
                    if (object.has("data") && !object.isNull("data")) {
                        String dataStr = object.getString("data");
                        if (dataStr.contains(";")) {
                            String[] array = dataStr.split(";");
                            if (array.length > 0) {
                                StringBuilder str = new StringBuilder();
                                for (int i = 0; i < array.length; i++) {
                                    str.append(array[i] + ",");
                                }
                                saveData("[" + str.substring(0, str.length() - 1) + "]");
                            }
                        } else {
                            saveData("[" + dataStr + "]");
                        }
                    } else {
                        saveData(null);
                    }
                } else {
                    saveData(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };
    private AsyncHttpResponseHandler saveHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {
                mPDialog.closeDialog();
                JSONObject obj = new JSONObject(new String(responseBody));
                int code = obj.getInt("code");
                if (code == 0) {
                    ToastUtil.showToastCenterShort(mContext, "提交成功");
                    setResult(Global.RESULT_OK);
                    finishWithAnimation();
                } else {
                    ToastUtil.showToastCenterShort(mContext, "保存失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mPDialog.closeDialog();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void setView() {
        tvTitlebarTitle.setText(title);
    }

    private void showSelectDialog() {
        pWin = null;
        if (pWin == null) {
            rvPopRoot.setVisibility(View.VISIBLE);
            View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_choose_icon, null);
            TextView pop_getIcon_action = (TextView) view.findViewById(R.id.pop_getIcon_action);
            TextView pop_getIcon_local = (TextView) view
                    .findViewById(R.id.pop_getIcon_local);
            LinearLayout pop_getIcon_cancle = (LinearLayout) view
                    .findViewById(R.id.pop_getIcon_cancle);
            pWin = new PopupWindow(view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pWin.setFocusable(true);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            pWin.setWidth(dm.widthPixels/* - 40 */);
            pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            // 拍照
            pop_getIcon_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                        pickIntent = 1;
                        if (!checkPermission()) { //没有或没有全部授权
                            requestPermissions(); //请求权限
                        } else {
                            goCamera();
                        }
                    } else {
                        goCamera();
                    }

                }
            });
            // 本地获取图片
            pop_getIcon_local.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                        pickIntent = 2;
                        if (!checkPermission()) { //没有或没有全部授权
                            requestPermissions(); //请求权限
                        } else {
                            goPick();
                        }
                    } else {
                        goPick();
                    }
                }
            });
            pop_getIcon_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    pWin.dismiss();
                    pWin = null;
                }
            });
            pWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rvPopRoot.setVisibility(View.GONE);
                }
            });
        }
    }

    private void goPick() {
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .maxSelectable(5 - imgList.size())
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .forResult(100214);
        pWin.dismiss();
        pWin = null;
    }

    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        String photoname = getCurrentTime() + "a.jpg";
        out = new File(getSDPath(), photoname);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            uri = FileProvider.getUriForFile(AreaAndEvaFeedBackActivity.this, "com.haotang.petworker.fileProvider", out);
        } else {
            uri = Uri.fromFile(out);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, SELECT_CAMER);
        pWin.dismiss();
        pWin = null;
    }

    public String getCurrentTime() {// 避免特殊字符产生无法调起拍照后无法保存返回
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        String currentTime = df.format(new Date());// new Date()为获取当前系统时间
        return currentTime;
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    private File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            // 这里可以修改为你的路径
            sdDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
        }
        return sdDir;
    }

    //检查权限
    private boolean checkPermission() {
        //是否有权限
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        boolean haveWritePermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return haveCameraPermission && haveWritePermission;

    }

    // 请求所需权限
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);

    }

    // 请求权限后会在这里回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:

                boolean allowAllPermission = false;

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//被拒绝授权
                        allowAllPermission = false;
                        break;
                    }
                    allowAllPermission = true;
                }

                if (allowAllPermission) {
                    if (pickIntent == 1) {
                        goCamera();
                    } else if (pickIntent == 2) {
                        goPick();
                    }
                } else {
                    Toast.makeText(mContext, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        title = getIntent().getStringExtra("title");
        ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        textview_top_show = (TextView) findViewById(R.id.textview_top_show);
        edit_content = (EditText) findViewById(R.id.edit_content);
        textview_length = (TextView) findViewById(R.id.textview_length);
        is_anonymous = (ImageView) findViewById(R.id.is_anonymous);
        rvPopRoot = findViewById(R.id.rl_poproot_bg);
        gridView_get_dog_phone = (MyGridView) findViewById(R.id.gridView_get_dog_phone);
        buton_pull_server = (Button) findViewById(R.id.buton_pull_server);
        ll_addImg = findViewById(R.id.ll_eva_addimage);
        btTitlebarOther.setVisibility(View.VISIBLE);
        btTitlebarOther.setText("历史记录");
        tvFeedbackData.setText(year+"-"+month);
        if (type == 2) {
            ll_addImg.setVisibility(View.INVISIBLE);
        }
        ButterKnife.bind(this);

        ImgAdapter = new MyAdapter();
        ImgAdapter.setIsShowDelete(true);
        gridView_get_dog_phone.setAdapter(ImgAdapter);
    }

    private void initGridView() {

    }

    @OnClick({R.id.ib_titlebar_back, R.id.bt_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                break;
            case R.id.bt_titlebar_other:
                break;
        }
    }


    /**
     * 用于gridview显示多张照片
     *
     * @author wlc
     * @date 2015-4-16
     */
    public class MyAdapter extends BaseAdapter {

        private boolean isDelete;  //用于删除图标的显隐
        private LayoutInflater inflater = LayoutInflater.from(mContext);

        @Override
        public int getCount() {
            //需要额外多出一个用于添加图片
            return imgList.size() >= 5 ? imgList.size() : imgList.size() + 1;
        }

        @Override
        public Object getItem(int arg0) {
            return imgList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {

            //初始化页面和相关控件
            convertView = inflater.inflate(R.layout.item_imgview, null);
            ImageView img_pic = (ImageView) convertView
                    .findViewById(R.id.img_pic);
            LinearLayout ly = (LinearLayout) convertView
                    .findViewById(R.id.layout);
            LinearLayout ll_picparent = (LinearLayout) convertView
                    .findViewById(R.id.ll_picparent);
            ImageView delete = (ImageView) convertView
                    .findViewById(R.id.img_delete);

            //默认的添加图片的那个item是不需要显示删除图片的
            if (imgList.size() >= 1) {
                if (position <= imgList.size() - 1) {
                    ll_picparent.setVisibility(View.GONE);
                    img_pic.setVisibility(View.VISIBLE);
                    img_pic.setImageBitmap(imgList.get(position));
                    // 设置删除按钮是否显示
                    delete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
                }
            }

            //当处于删除状态时，删除事件可用
            //注意：必须放到getView这个方法中，放到onitemClick中是不起作用的。
            if (isDelete) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imgList.remove(position);
                        ImgAdapter.notifyDataSetChanged();

                    }
                });
            }

            return convertView;
        }

        /**
         * 设置是否显示删除图片
         *
         * @param isShowDelete
         */
        public void setIsShowDelete(boolean isShowDelete) {
            this.isDelete = isShowDelete;
            notifyDataSetChanged();
        }

    }

    private void getLuban(String path) {
        Luban.with(this).load(new File(path))
                .setCompressListener(new OnCompressListener() {

                    @Override
                    public void onSuccess(File file) {
                        // TODO Auto-generated method stub
                        mPDialog.closeDialog();
//							Bitmap bm = Utils.getxtsldraw(evaluateActivity, file.getAbsolutePath());
//							if (null != bm && !"".equals(bm)) {
//								imgList.add(bm);
//							}
                        listFile.add(file);
//							ImgAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        mPDialog.showDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int index = bundle.getInt("index");
            if (index == 0) {
                Utils.mLogError("==-->login 接收广播");
                int position = bundle.getInt("position");
                list.remove(position);
                imgList.remove(position);
                listFile.remove(position);
                Utils.mLogError("==-->listev  1  --> " + list.size());
                Utils.mLogError("==-->listev 2 --->  " + imgList.size());
                ImgAdapter.notifyDataSetChanged();

            } else if (index == 1) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100214:
                    List<String> arrayList = Matisse.obtainPathResult(data);
                    for (int i = 0; i < arrayList.size(); i++) {
                        Bitmap bm = Utils.getxtsldraw(mContext, arrayList.get(i));
                        list.add(arrayList.get(i));
                        getLuban(arrayList.get(i));
                        if (null != bm && !"".equals(bm)) {
                            imgList.add(bm);
                        }
                    }
                    ImgAdapter.notifyDataSetChanged();
                    break;
                case SELECT_CAMER:
                    // 拍照添加图片
                    Bitmap bm1 = Utils.getxtsldraw(mContext, out.getAbsolutePath());
                    path = Utils.creatfile(mContext, bm1, "android_addrecord" + getCurrentTime());
//                    setImg(path, bm1);
                    list.add(path);
                    getLuban(path);
                    if (null != bm1 && !"".equals(bm1)) {
                        imgList.add(bm1);
                    }
                    ImgAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
