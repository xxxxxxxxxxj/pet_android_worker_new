package com.haotang.petworker;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.petworker.event.RefreshProdictionEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.ImageLoaderUtil;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.SharedPreferenceUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.AlertDialogDefault;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddProductionActivity extends SuperActivity {
    private String TEMPCERTIFICATIONNAME = "pro.jpg";
    private static final int PHOTO_CERTIFICATION = 100;
    private static final int IMAGE_CERTIFICATION = 101;
    private ImageButton ibBack;
    private TextView tvTitle;
    private ImageView ivImage;
    private EditText etName;
    //private EditText etDes;
    private Button btAdd;
    private PopupWindow pWin;
    private LayoutInflater mInflater;
    private RelativeLayout rlLayer;
    private String strimage;
    private MProgressDialog pDialog;
    private SharedPreferenceUtil spUtil;
    private ImageView iv_add;
    private int petkind;
    private int serviceid;
    private int servicetype;
    private int petid;
    private int orderid;
    private String petname;
    private String servicename;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private int pickIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproduction);

        findView();
        setView();
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

    private void findView() {
        mInflater = LayoutInflater.from(this);
        pDialog = new MProgressDialog(this);
        spUtil = SharedPreferenceUtil.getInstance(this);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);

        ivImage = (ImageView) findViewById(R.id.iv_add_image);
        etName = (EditText) findViewById(R.id.et_add_name);
        btAdd = (Button) findViewById(R.id.bt_add_submit);
        rlLayer = (RelativeLayout) findViewById(R.id.rl_add_graylayer);
        iv_add = findViewById(R.id.iv_add_icon);
    }

    private void setView() {
        tvTitle.setText("添加作品");
        rlLayer.setVisibility(View.GONE);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        orderid = getIntent().getIntExtra("orderid", 0);
        petid = getIntent().getIntExtra("petid", 0);
        petkind = getIntent().getIntExtra("petkind", 0);
        serviceid = getIntent().getIntExtra("serviceid", 0);
        servicetype = getIntent().getIntExtra("servicetype", 0);
        petname = getIntent().getStringExtra("petname");
        servicename = getIntent().getStringExtra("servicename");
        ibBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showBack();
            }
        });
        ivImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 选择图片
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
                }
                showPhotoDialog();
            }
        });
        iv_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 选择图片
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
                }
                showPhotoDialog();
            }
        });
        btAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 添加图片到后台
                sendPic();
            }
        });

        etName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.length() + 1 > 10) {
                    ToastUtil.showToastCenterShort(AddProductionActivity.this,
                            "作品名称不能超过10个字！");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        /*etDes.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() + 1 > 140) {
                    ToastUtil.showToastCenterShort(AddProductionActivity.this,
                            "作品详情不能超过140个字！");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });*/

    }

    private void sendPic() {
        if (strimage == null || "".equals(strimage)) {
            ToastUtil.showToastCenterShort(this, "请添加作品图片，填写作品名称");
            return;
        } /*else if (petkind == 0 || serviceid == 0) {
            ToastUtil.showToastCenterShort(this, "请选择作品分类");
            return;
        }*/ else if (etName == null
                || "".equals(etName.getText().toString().trim())) {
            ToastUtil.showToastCenterShort(this, "请填写作品名称");
            return;
        }
        pDialog.showDialog();
        Log.e("TAG", "orderid = " + orderid);
        Log.e("TAG", "petkind = " + petkind);
        Log.e("TAG", "serviceid = " + serviceid);
        Log.e("TAG", "strimage = " + strimage);
        CommUtil.addWork(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this), orderid,
                etName.getText().toString().trim(), "", strimage, petkind, serviceid, addHanler);

    }

    private AsyncHttpResponseHandler addHanler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            Utils.mLogError("美容师添加作品：" + new String(responseBody));
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode) {
                    EventBus.getDefault().post(new RefreshProdictionEvent(true));
                    ToastUtil.showToastCenterShort(AddProductionActivity.this,
                            "作品上传成功！");
                    finishWithAnimation();

                } else {
                    if (jobj.has("msg") && !jobj.isNull("msg")) {
                        String msg = jobj.getString("msg");
                        ToastUtil.showToastCenterShort(
                                AddProductionActivity.this, msg);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ToastUtil.showToastCenterShort(AddProductionActivity.this,
                        "网络不给力,稍后再试");
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            pDialog.closeDialog();
            ToastUtil.showToastCenterShort(AddProductionActivity.this,
                    "网络不给力,稍后再试");

        }

    };
    private InputMethodManager imm;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_CERTIFICATION:
                    // Uri uri = data.getData();
                    // String photoPath =
                    // RealPathUtil.getRealPathFromURI(AddProductionActivity.this,
                    // uri);
                    // setPicToImageView(ivImage,photoPath);
                    if (data == null) {
                        ToastUtil.showToastCenterShort(AddProductionActivity.this,
                                "您选择的照片不存在，请重新选择");
                        return;
                    }
                        ivImage.setVisibility(View.VISIBLE);
                    try {
                        Uri originalUri = data.getData(); // 获得图片的uri
                        if (!TextUtils.isEmpty(originalUri.getAuthority())) {
                            // 这里开始的第二部分，获取图片的路径：
                            String[] proj = {MediaStore.Images.Media.DATA};
                            // Cursor cursor = managedQuery(originalUri, proj, null,
                            // null, null);
                            // 获得用户选择的图片的索引值
                            Cursor cursor = getContentResolver().query(originalUri,
                                    proj, null, null, null);
                            int column_index = cursor
                                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            cursor.moveToFirst();
                            // 最后根据索引值获取图片路径
                            String path = cursor.getString(column_index);
                            cursor.close();
                            // 华为及其他手机系统的图片Url获取
                            setPicToImageView(ivImage, path);
                        } else {
                            // 小米系统走的方法
                            setPicToImageView(ivImage, originalUri.getPath());
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    break;
                case PHOTO_CERTIFICATION:
                    File file = new File(
                            Utils.getOOPath(AddProductionActivity.this),
                            TEMPCERTIFICATIONNAME);

                    if (file != null && file.length() > 0) {
                        ivImage.setVisibility(View.VISIBLE);
                        setPicToImageView(ivImage, file.getAbsolutePath());
                    } else {
                        ToastUtil.showToastCenterShort(AddProductionActivity.this,
                                "您选择的照片不存在，请重新选择");
                    }

                    break;
                case Global.ADDPRODUCTION_TO_SERVICECATEGORY:
                    petkind = data.getIntExtra("petkind", 0);
                    serviceid = data.getIntExtra("id", 0);
                    String servicename = data.getStringExtra("name");
                    String petkindname = data.getStringExtra("petkindname");
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            showBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showBack() {
        if (strimage != null && !"".equals(strimage) || etName != null
                && !"".equals(etName.getText().toString().trim())
                ) {
            new AlertDialogDefault(AddProductionActivity.this).builder()
                    .setTitle("提示").setMsg("您填写的作品信息尚未保存，确认退出？").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishWithAnimation();
                }
            }).show();
        } else {
            finishWithAnimation();
        }
    }

    private void setPicToImageView(ImageView imageView, String path) {
        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();
        BitmapFactory.Options opts = new Options();
        // 设置这个，只得到Bitmap的属性信息放入opts，而不把Bitmap加载到内存中
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;
        // 取最大的比例，保证整个图片的长或者宽必定在该屏幕中可以显示得下
        int scale = Math.max(imageViewWidth / bitmapWidth, imageViewHeight
                / bitmapHeight);
        // 缩放的比例
        opts.inSampleSize = scale;
        opts.inDither = false;// 不进行图片抖动处理
        opts.inPreferredConfig = null;// 设置让解析器以最佳方式解析
        // 内存不足时可被回收
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        // 设置为false,表示不仅Bitmap的属性，也要加载bitmap
        opts.inJustDecodeBounds = false;
        Bitmap bitmaptmp = BitmapFactory.decodeFile(path, opts);
        ImageLoaderUtil.loadImg("file://" + path,
                imageView,
                R.drawable.icon_production_default, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmaptmp.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] byteArray = stream.toByteArray();
        strimage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private void showPhotoDialog() {
        if (pWin == null) {
            rlLayer.setVisibility(View.VISIBLE);
            View view = mInflater.inflate(R.layout.pwin, null);
            Button btPhoto = (Button) view
                    .findViewById(R.id.bt_selectphone_photo);
            Button btImage = (Button) view
                    .findViewById(R.id.bt_selectphone_image);
            Button btCancel = (Button) view
                    .findViewById(R.id.bt_selectphone_cancel);

            pWin = new PopupWindow(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pWin.setFocusable(true);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            pWin.setWidth(dm.widthPixels - 0);
            pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            pWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rlLayer.setVisibility(View.GONE);
                }
            });
            btCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pWin.dismiss();
                    pWin = null;
                    rlLayer.setVisibility(View.GONE);
                }
            });
            btPhoto.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                        pickIntent = 1;
                        if (!checkPermission()) { //没有或没有全部授权
                            requestPermissions(); //请求权限
                        }else {
                            goCamera();
                        }
                    } else {
                        goCamera();
                    }

                }
            });
            btImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
                        pickIntent = 2;
                        if (!checkPermission()) { //没有或没有全部授权
                            requestPermissions(); //请求权限
                        }else {
                            goPick();
                        }
                    } else {
                        goPick();
                    }
                }
            });

        }
    }

    private void goPick() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, IMAGE_CERTIFICATION);
        pWin.dismiss();
        pWin = null;
        rlLayer.setVisibility(View.GONE);
    }

    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        TEMPCERTIFICATIONNAME = System.currentTimeMillis()
                + "_pro.jpg";
        File file = new File(Utils
                .getPetPath(AddProductionActivity.this),
                TEMPCERTIFICATIONNAME);

        Uri mUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(AddProductionActivity.this, "com.haotang.petworker.fileProvider", file);
        }else {
            mUri= Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mUri );
        startActivityForResult(intent, PHOTO_CERTIFICATION);
        pWin.dismiss();
        pWin = null;
        rlLayer.setVisibility(View.GONE);
    }

    //检查权限
    private boolean checkPermission() {
        //是否有权限
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        boolean haveWritePermission = ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return haveCameraPermission && haveWritePermission;

    }

    // 请求所需权限
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);

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
                    if (pickIntent==1){
                        goCamera();
                    }else if (pickIntent==2){
                        goPick();
                    }
                } else {
                    Toast.makeText(mContext, "该功能需要授权方可使用", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    // ///////////////////////////////////////////////////////////////////////

    public enum RealPathUtil {
        INSTANCE;

        public static String getRealPathFromURI(Context context, Uri uri) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                return getRealPathFromURI_BelowAPI11(context, uri);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return getRealPathFromURI_API11to18(context, uri);
            } else {
                return getRealPathFromURI_API19(context, uri);
            }
        }

        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API19(Context context, Uri uri) {
            String filePath = "";
            String wholeID = "";

            try {
                wholeID = DocumentsContract.getDocumentId(uri);
            } catch (Exception ex) {
                ex.printStackTrace(); // Android 4.4.2 can occur this exception.

                return getRealPathFromURI_API11to18(context, uri);
            }

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaColumns.DATA};

            // where id is equal to
            String sel = BaseColumns._ID + "=?";

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                    new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
            return filePath;
        }

        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API11to18(Context context,
                                                          Uri contentUri) {
            String[] proj = {MediaColumns.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(context, contentUri,
                    proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaColumns.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }

        public static String getRealPathFromURI_BelowAPI11(Context context,
                                                           Uri contentUri) {
            String[] proj = {MediaColumns.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri,
                    proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }

}
