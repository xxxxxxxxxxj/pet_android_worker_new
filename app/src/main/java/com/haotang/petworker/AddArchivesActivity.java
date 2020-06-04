package com.haotang.petworker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.contrarywind.view.WheelView;
import com.flyco.roundview.RoundTextView;
import com.haotang.petworker.adapter.ArchivesAfterImgAdapter;
import com.haotang.petworker.adapter.ArchivesAlrightTagAdapter;
import com.haotang.petworker.adapter.ArchivesBeforeImgAdapter;
import com.haotang.petworker.adapter.ArchivesCareTagAdapter;
import com.haotang.petworker.adapter.ArchivesProblemTagAdapter;
import com.haotang.petworker.entity.CareTag;
import com.haotang.petworker.entity.PetTag;
import com.haotang.petworker.event.AddArchivesEvent;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.FileSizeUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.AlertDialogDefault;
import com.haotang.petworker.view.AlertDialogPetCharacter;
import com.haotang.petworker.view.FluidLayout;
import com.haotang.petworker.view.GridSpacingItemDecoration;
import com.haotang.petworker.view.NoScollFullGridLayoutManager;
import com.umeng.analytics.MobclickAgent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * 添加宠物档案界面
 */
public class AddArchivesActivity extends SuperActivity {
    public static SuperActivity act;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_addpetarchives_img)
    ImageView ivAddpetarchivesImg;
    @BindView(R.id.tv_addpetarchives_nickname)
    TextView tvAddpetarchivesNickname;
    @BindView(R.id.iv_addpetarchives_sex)
    ImageView ivAddpetarchivesSex;
    @BindView(R.id.tv_addpetarchives_month)
    TextView tvAddpetarchivesMonth;
    @BindView(R.id.tv_addpetarchives_petname)
    TextView tvAddpetarchivesPetname;
    @BindView(R.id.rv_addpetarchives_beforeimg)
    RecyclerView rvAddpetarchivesBeforeimg;
    @BindView(R.id.rv_addpetarchives_img)
    RecyclerView rvAddpetarchivesImg;
    @BindView(R.id.iv_addpetarchives_nopro)
    ImageView ivAddpetarchivesNopro;
    @BindView(R.id.rv_addpetarchives_nopro_tag)
    RecyclerView rvAddpetarchivesNoproTag;
    @BindView(R.id.et_addpetarchives_nopro_content)
    EditText etAddpetarchivesNoproContent;
    @BindView(R.id.ll_addpetarchives_nopro_tag)
    LinearLayout llAddpetarchivesNoproTag;
    @BindView(R.id.iv_addpetarchives_yespro)
    ImageView ivAddpetarchivesYespro;
    @BindView(R.id.rv_addpetarchives_yespro_tag)
    RecyclerView rvAddpetarchivesYesproTag;
    @BindView(R.id.et_addpetarchives_yespro_content)
    EditText etAddpetarchivesYesproContent;
    @BindView(R.id.ll_addpetarchives_yespro_tag)
    LinearLayout llAddpetarchivesYesproTag;
    @BindView(R.id.rv_addpetarchives_hljl_tag)
    RecyclerView rvAddpetarchivesHljlTag;
    @BindView(R.id.et_addpetarchives_hljl_content)
    EditText etAddpetarchivesHljlContent;
    @BindView(R.id.fluidNoProTag)
    FluidLayout fluidNoProTag;
    @BindView(R.id.fluidYesProTag)
    FluidLayout fluidYesProTag;
    @BindView(R.id.fluidHljlTag)
    FluidLayout fluidHljlTag;
    @BindView(R.id.RelatNoPro)
    RelativeLayout RelatNoPro;
    @BindView(R.id.RelatHasPro)
    RelativeLayout RelatHasPro;
    @BindView(R.id.btn_addpetarchives_submit)
    Button btnAddpetarchivesSubmit;
    @BindView(R.id.tv_addarchives_nlfw)
    TextView tvAddarchivesNlfw;
    @BindView(R.id.tv_addarchives_nljlr)
    TextView tvAddarchivesNljlr;
    @BindView(R.id.tv_addarchives_tt)
    TextView tvAddarchivesTt;
    @BindView(R.id.tv_addarchives_ttjlr)
    TextView tvAddarchivesTtjlr;
    @BindView(R.id.tv_addpetarchives_xg_jlr)
    TextView tv_addpetarchives_xg_jlr;
    @BindView(R.id.tv_addpetarchives_xg_str)
    TextView tv_addpetarchives_xg_str;
    @BindView(R.id.nsv_addpetarchives)
    NestedScrollView nsv_addpetarchives;
    private int orderId;
    private String petimg;
    private int petsex;
    private String petmonth;
    private String petname;
    private String nickname;
    private List<PetTag> ageGroupList = new ArrayList<PetTag>();
    private List<PetTag> petPostureList = new ArrayList<PetTag>();
    private List<CareTag> problemTagList = new ArrayList<CareTag>();
    private List<CareTag> alrightTagList = new ArrayList<CareTag>();
    private List<CareTag> careHistoryTagList = new ArrayList<CareTag>();
    private List<String> beforeImgList = new ArrayList<String>();
    private List<String> afterImgList = new ArrayList<String>();
    private List<File> beforeFileList = new ArrayList<File>();
    private List<File> afterFileList = new ArrayList<File>();
    private ArchivesProblemTagAdapter archivesProblemTagAdapter;
    private ArchivesAlrightTagAdapter archivesAlrightTagAdapter;
    private ArchivesCareTagAdapter archivesCareTagAdapter;
    private int flag;
    private ArchivesBeforeImgAdapter archivesBeforeImgAdapter;
    private ArchivesAfterImgAdapter archivesAfterImgAdapter;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_CAMER = 2;
    private String TEMPCERTIFICATIONNAME;
    private int imgFlag;
    private int customerid;
    private int gravity = Gravity.TOP;
    private int ageGroup;
    private int petPosture;
    private int ageGroupIndex = -1;
    private int petPostureIndex = -1;
    private int index = 1;
    private String[] ageGroupArray;
    private String[] petPostureArray;
    private List<CareTag> characterList = new ArrayList<CareTag>();
    private StringBuffer sbCharacter = new StringBuffer();
    private StringBuffer sbCharacterIds = new StringBuffer();
    private String[] petCharacterIds;
    private int pickIntent;
    private static final int REQUEST_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getData();
    }

    private void initData() {
        act = this;
        mPDialog = new MProgressDialog(this);
        EventBus.getDefault().register(this);
        mPDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        MApplication.listAppoint.add(this);
        orderId = getIntent().getIntExtra("orderId", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<CareTag> event) {
        Log.e("TAG", "event = " + event);
        characterList.clear();
        characterList.addAll(event);
        sbCharacter.setLength(0);
        for (int i = 0; i < characterList.size(); i++) {
            if (characterList.get(i).isSelect()) {
                sbCharacter.append(characterList.get(i).getTag() + ",");
            }
        }
        if (sbCharacter.length() > 0) {
            Utils.setText(tv_addpetarchives_xg_str, sbCharacter.substring(0, sbCharacter.length() - 1), "未填写", View.VISIBLE, View.VISIBLE);
        } else {
            tv_addpetarchives_xg_str.setText("");
        }
    }

    private void findView() {
        setContentView(R.layout.activity_add_archives);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("新增记录");

        rvAddpetarchivesBeforeimg.setHasFixedSize(true);
        rvAddpetarchivesBeforeimg.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager3 = new
                NoScollFullGridLayoutManager(rvAddpetarchivesBeforeimg, this, 4, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager3.setScrollEnabled(false);
        rvAddpetarchivesBeforeimg.setLayoutManager(noScollFullGridLayoutManager3);
        rvAddpetarchivesBeforeimg.addItemDecoration(new GridSpacingItemDecoration(4,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                false));
        beforeImgList.add("drawable://"
                + R.drawable.icon_img_add);
        archivesBeforeImgAdapter = new ArchivesBeforeImgAdapter(R.layout.item_addpetarchives_img
                , beforeImgList);
        rvAddpetarchivesBeforeimg.setAdapter(archivesBeforeImgAdapter);

        rvAddpetarchivesImg.setHasFixedSize(true);
        rvAddpetarchivesImg.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager4 = new
                NoScollFullGridLayoutManager(rvAddpetarchivesImg, this, 4, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager4.setScrollEnabled(false);
        rvAddpetarchivesImg.setLayoutManager(noScollFullGridLayoutManager4);
        rvAddpetarchivesImg.addItemDecoration(new GridSpacingItemDecoration(4,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                false));
        afterImgList.add("drawable://"
                + R.drawable.icon_img_add);
        archivesAfterImgAdapter = new ArchivesAfterImgAdapter(R.layout.item_addpetarchives_img
                , afterImgList);
        rvAddpetarchivesImg.setAdapter(archivesAfterImgAdapter);

        rvAddpetarchivesNoproTag.setHasFixedSize(true);
        rvAddpetarchivesNoproTag.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                NoScollFullGridLayoutManager(rvAddpetarchivesNoproTag, this, 4, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager.setScrollEnabled(false);
        rvAddpetarchivesNoproTag.setLayoutManager(noScollFullGridLayoutManager);
        rvAddpetarchivesNoproTag.addItemDecoration(new GridSpacingItemDecoration(4,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                false));
        archivesAlrightTagAdapter = new ArchivesAlrightTagAdapter(R.layout.item_addpetarchives_tag
                , alrightTagList);
        rvAddpetarchivesNoproTag.setAdapter(archivesAlrightTagAdapter);

        rvAddpetarchivesYesproTag.setHasFixedSize(true);
        rvAddpetarchivesYesproTag.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager1 = new
                NoScollFullGridLayoutManager(rvAddpetarchivesYesproTag, this, 4, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager1.setScrollEnabled(false);
        rvAddpetarchivesYesproTag.setLayoutManager(noScollFullGridLayoutManager1);
        rvAddpetarchivesYesproTag.addItemDecoration(new GridSpacingItemDecoration(4,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                false));
        archivesProblemTagAdapter = new ArchivesProblemTagAdapter(R.layout.item_addpetarchives_tag
                , problemTagList);
        rvAddpetarchivesYesproTag.setAdapter(archivesProblemTagAdapter);

        rvAddpetarchivesHljlTag.setHasFixedSize(true);
        rvAddpetarchivesHljlTag.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager2 = new
                NoScollFullGridLayoutManager(rvAddpetarchivesHljlTag, this, 4, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager2.setScrollEnabled(false);
        rvAddpetarchivesHljlTag.setLayoutManager(noScollFullGridLayoutManager2);
        rvAddpetarchivesHljlTag.addItemDecoration(new GridSpacingItemDecoration(4,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                false));
        archivesCareTagAdapter = new ArchivesCareTagAdapter
                (R.layout.item_addpetarchives_tag
                        , careHistoryTagList);
        rvAddpetarchivesHljlTag.setAdapter(archivesCareTagAdapter);
    }

    private void setLinster() {
        archivesBeforeImgAdapter.setOnImgClickListener(new ArchivesBeforeImgAdapter.OnImgClickListener() {

            @Override
            public void OnImgClick(int position) {
                if (beforeImgList.size() > position) {
                    if (beforeImgList.get(position).startsWith("drawable://")) {
                        imgFlag = 1;
                        Utils.goneJP(AddArchivesActivity.this);
                        popPhoto();
                    }
                }
            }
        });
        archivesBeforeImgAdapter.setOnImgDelListener(new ArchivesBeforeImgAdapter.OnImgDelListener() {
            @Override
            public void OnImgDel(int position) {
                if (beforeImgList.size() > position) {
                    beforeImgList.remove(position);
                    beforeFileList.remove(position);
                    archivesBeforeImgAdapter.notifyDataSetChanged();
                    boolean isHave = false;
                    for (int i = 0; i < beforeImgList.size(); i++) {
                        if (beforeImgList.get(i).startsWith("drawable://")) {
                            isHave = true;
                            break;
                        }
                    }
                    if (!isHave) {
                        beforeImgList.add("drawable://"
                                + R.drawable.icon_img_add);
                    }
                }
            }
        });
        archivesAfterImgAdapter.setOnImgClickListener(new ArchivesAfterImgAdapter.OnImgClickListener() {
            @Override
            public void OnImgClick(int position) {
                if (afterImgList.size() > position) {
                    if (afterImgList.get(position).startsWith("drawable://")) {
                        imgFlag = 2;
                        Utils.goneJP(AddArchivesActivity.this);
                        popPhoto();
                    }
                }
            }
        });
        archivesAfterImgAdapter.setOnImgDelListener(new ArchivesAfterImgAdapter.OnImgDelListener() {
            @Override
            public void OnImgDel(int position) {
                if (afterImgList.size() > position) {
                    afterImgList.remove(position);
                    afterFileList.remove(position);
                    archivesAfterImgAdapter.notifyDataSetChanged();
                    boolean isHave = false;
                    for (int i = 0; i < afterImgList.size(); i++) {
                        if (afterImgList.get(i).startsWith("drawable://")) {
                            isHave = true;
                            break;
                        }
                    }
                    if (!isHave) {
                        afterImgList.add("drawable://"
                                + R.drawable.icon_img_add);
                    }
                }
            }
        });
        archivesProblemTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (problemTagList.size() > position) {
                    problemTagList.get(position).setSelect(!problemTagList.get(position).isSelect());
                    archivesProblemTagAdapter.notifyDataSetChanged();
                }
            }
        });
        archivesAlrightTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (alrightTagList.size() > position) {
                    alrightTagList.get(position).setSelect(!alrightTagList.get(position).isSelect());
                    archivesAlrightTagAdapter.notifyDataSetChanged();
                }
            }
        });
        archivesCareTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (careHistoryTagList.size() > position) {
                    careHistoryTagList.get(position).setSelect(!careHistoryTagList.get(position).isSelect());
                    archivesCareTagAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void popPhoto() {
        ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.photo_bottom_dialog, null);
        TextView tv_photo_bottomdia_camer = (TextView) customView.findViewById(R.id.tv_photo_bottomdia_camer);
        TextView tv_photo_bottomdia_picture = (TextView) customView.findViewById(R.id.tv_photo_bottomdia_picture);
        RoundTextView tv_photo_bottomdia_cancel = (RoundTextView) customView.findViewById(R.id.tv_photo_bottomdia_cancel);
        ImageView iv_photobottom_bg = (ImageView) customView.findViewById(R.id.iv_photobottom_bg);
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        iv_photobottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        tv_photo_bottomdia_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        tv_photo_bottomdia_camer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                pWinBottomDialog.dismiss();
            }
        });
        tv_photo_bottomdia_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
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
    }

    private void goPick() {
        int maxSelectable = 0;
        if (imgFlag == 1) {
            maxSelectable = 4 - (beforeImgList.size() - 1);
        } else if (imgFlag == 2) {
            maxSelectable = 4 - (afterImgList.size() - 1);
        }
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .maxSelectable(maxSelectable)
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .forResult(SELECT_PICTURE);
    }

    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        TEMPCERTIFICATIONNAME = System.currentTimeMillis()
                + "_pro.jpg";
        File file = new File(Utils
                .getPetPath(AddArchivesActivity.this),
                TEMPCERTIFICATIONNAME);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            uri = FileProvider.getUriForFile(AddArchivesActivity.this, "com.haotang.petworker.fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, SELECT_CAMER);
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

    private void compressWithRx(final String path) {
        File file = new File(path);
        Log.e("TAG", "--------------压缩前file.length() = " + FileSizeUtil.formatFileSize(
                file.length(), false) + "---path = " + file.getAbsolutePath());
        Luban.with(this).load(path).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(File file) {
                mPDialog.closeDialog();
                Log.e("TAG", "--------------压缩后file.length() = " + FileSizeUtil.formatFileSize(
                        file.length(), false) + "---path = " + file.getAbsolutePath());
                if (imgFlag == 1) {
                    beforeFileList.add(file);
                } else if (imgFlag == 2) {
                    afterFileList.add(file);
                }
            }

            @Override
            public void onError(Throwable e) {
                mPDialog.closeDialog();
                Log.e("TAG", "压缩失败e = " + e.toString());
                ToastUtil.showToastBottomShort(mContext, "压缩失败");
                if (imgFlag == 1) {
                    beforeFileList.add(new File(path));
                } else if (imgFlag == 2) {
                    afterFileList.add(new File(path));
                }
            }
        }).launch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || resultCode == Global.RESULT_OK) {
            switch (requestCode) {
                // 从相册选择
                case SELECT_PICTURE:
                    if (data == null) {
                        ToastUtil.showToastBottomShort(AddArchivesActivity.this,
                                "您选择的照片不存在，请重新选择");
                        return;
                    }
                    try {
                        List<String> arrayList = Matisse.obtainPathResult(data);
                        if (arrayList != null && arrayList.size() > 0) {
                            if (imgFlag == 1) {
                                for (int i = 0; i < beforeImgList.size(); i++) {
                                    if (beforeImgList.get(i).startsWith("drawable://")) {
                                        beforeImgList.remove(i);
                                        break;
                                    }
                                }
                                for (int i = 0; i < arrayList.size(); i++) {
                                    beforeImgList.add(arrayList.get(i));
                                    mPDialog.showDialog("压缩图片中,请稍后...");
                                    compressWithRx(arrayList.get(i));
                                }
                                if (beforeImgList.size() < 4) {
                                    beforeImgList.add("drawable://"
                                            + R.drawable.icon_img_add);
                                }
                                archivesBeforeImgAdapter.notifyDataSetChanged();
                            } else if (imgFlag == 2) {
                                for (int i = 0; i < afterImgList.size(); i++) {
                                    if (afterImgList.get(i).startsWith("drawable://")) {
                                        afterImgList.remove(i);
                                        break;
                                    }
                                }
                                for (int i = 0; i < arrayList.size(); i++) {
                                    afterImgList.add(arrayList.get(i));
                                    mPDialog.showDialog("压缩图片中,请稍后...");
                                    compressWithRx(arrayList.get(i));
                                }
                                if (afterImgList.size() < 4) {
                                    afterImgList.add("drawable://"
                                            + R.drawable.icon_img_add);
                                }
                                archivesAfterImgAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                // 拍照添加图片
                case SELECT_CAMER:
                    File file = new File(Utils.getPetPath(AddArchivesActivity.this),
                            TEMPCERTIFICATIONNAME);
                    if (file != null && file.length() > 0) {
                        if (imgFlag == 1) {
                            for (int i = 0; i < beforeImgList.size(); i++) {
                                if (beforeImgList.get(i).startsWith("drawable://")) {
                                    beforeImgList.remove(i);
                                    break;
                                }
                            }
                            beforeImgList.add(file.getAbsolutePath());
                            if (beforeImgList.size() < 4) {
                                beforeImgList.add("drawable://"
                                        + R.drawable.icon_img_add);
                            }
                            archivesBeforeImgAdapter.notifyDataSetChanged();
                        } else if (imgFlag == 2) {
                            for (int i = 0; i < afterImgList.size(); i++) {
                                if (afterImgList.get(i).startsWith("drawable://")) {
                                    afterImgList.remove(i);
                                    break;
                                }
                            }
                            afterImgList.add(file.getAbsolutePath());
                            if (afterImgList.size() < 4) {
                                afterImgList.add("drawable://"
                                        + R.drawable.icon_img_add);
                            }
                            archivesAfterImgAdapter.notifyDataSetChanged();
                        }
                        mPDialog.showDialog("压缩图片中,请稍后...");
                        compressWithRx(file.getAbsolutePath());
                    } else {
                        Toast.makeText(AddArchivesActivity.this, "您选择的照片不存在，请重新选择",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void getData() {
        mPDialog.showDialog();
        problemTagList.clear();
        alrightTagList.clear();
        careHistoryTagList.clear();
        ageGroupList.clear();
        petPostureList.clear();
        characterList.clear();
        CommUtil.toAddCareHistory(this, orderId, toAddCareHistory);
    }

    private AsyncHttpResponseHandler toAddCareHistory = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                String msg = jobj.getString("msg");
                if (0 == resultCode) {
                    if (jobj.has("data") && !jobj.isNull("data")) {
                        JSONObject jdata = jobj.getJSONObject("data");
                        if (jdata.has("ageGroup") && !jdata.isNull("ageGroup")
                                && jdata.getJSONArray("ageGroup").length() > 0) {
                            JSONArray jsonArray = jdata.getJSONArray("ageGroup");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ageGroupList.add(PetTag
                                        .json2Entity(jsonArray
                                                .getJSONObject(i)));
                            }
                        }
                        if (jdata.has("petPosture") && !jdata.isNull("petPosture")
                                && jdata.getJSONArray("petPosture").length() > 0) {
                            JSONArray jsonArray = jdata.getJSONArray("petPosture");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                petPostureList.add(PetTag
                                        .json2Entity(jsonArray
                                                .getJSONObject(i)));
                            }
                        }
                        if (jdata.has("customerPet") && !jdata.isNull("customerPet")) {
                            JSONObject jcustomerPet = jdata.getJSONObject("customerPet");
                            if (jcustomerPet.has("ageGroup") && !jcustomerPet.isNull("ageGroup")) {
                                ageGroup = jcustomerPet.getInt("ageGroup");
                            }
                            if (jcustomerPet.has("petPosture") && !jcustomerPet.isNull("petPosture")) {
                                petPosture = jcustomerPet.getInt("petPosture");
                            }
                            if (jcustomerPet.has("recorder") && !jcustomerPet.isNull("recorder")) {
                                Utils.setText(tvAddarchivesNljlr, "记录人：" + jcustomerPet.getString("recorder"), "记录人：", View.VISIBLE, View.VISIBLE);
                                Utils.setText(tvAddarchivesTtjlr, "记录人：" + jcustomerPet.getString("recorder"), "记录人：", View.VISIBLE, View.VISIBLE);
                                Utils.setText(tv_addpetarchives_xg_jlr, "记录人：" + jcustomerPet.getString("recorder"), "记录人：", View.VISIBLE, View.VISIBLE);
                            }
                            if (jcustomerPet.has("avatar") && !jcustomerPet.isNull("avatar")) {
                                petimg = jcustomerPet.getString("avatar");
                            }
                            if (jcustomerPet.has("nickName") && !jcustomerPet.isNull("nickName")) {
                                nickname = jcustomerPet.getString("nickName");
                            }
                            if (jcustomerPet.has("petName") && !jcustomerPet.isNull("petName")) {
                                petname = jcustomerPet.getString("petName");
                            }
                            if (jcustomerPet.has("sex") && !jcustomerPet.isNull("sex")) {
                                petsex = jcustomerPet.getInt("sex");
                            }
                            if (jcustomerPet.has("extendParam") && !jcustomerPet.isNull("extendParam")) {
                                JSONObject jextendParam = jcustomerPet.getJSONObject("extendParam");
                                if (jextendParam.has("month") && !jextendParam.isNull("month")) {
                                    petmonth = jextendParam.getString("month");
                                }
                            }
                            if (jcustomerPet.has("id") && !jcustomerPet.isNull("id")) {
                                customerid = jcustomerPet.getInt("id");
                            }
                            if (jcustomerPet.has("petCharacter") && !jcustomerPet.isNull("petCharacter")) {
                                String petCharacter = jcustomerPet.getString("petCharacter");
                                if (Utils.isStrNull(petCharacter)) {
                                    petCharacterIds = petCharacter.split(",");
                                }
                            }
                        }
                        if (jdata.has("items") && !jdata.isNull("items")) {
                            JSONObject jitems = jdata.getJSONObject("items");
                            if (jitems.has("problem") && !jitems.isNull("problem")
                                    && jitems.getJSONArray("problem").length() > 0) {
                                JSONArray jsonArray = jitems.getJSONArray("problem");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    problemTagList.add(CareTag
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                }
                            }
                            if (jitems.has("alright") && !jitems.isNull("alright")
                                    && jitems.getJSONArray("alright").length() > 0) {
                                JSONArray jsonArray = jitems.getJSONArray("alright");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    alrightTagList.add(CareTag
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                }
                            }
                            if (jitems.has("careHistory") && !jitems.isNull("careHistory")
                                    && jitems.getJSONArray("careHistory").length() > 0) {
                                JSONArray jsonArray = jitems.getJSONArray("careHistory");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    careHistoryTagList.add(CareTag
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                }
                            }
                            if (jitems.has("petCharacter") && !jitems.isNull("petCharacter")
                                    && jitems.getJSONArray("petCharacter").length() > 0) {
                                JSONArray jsonArray = jitems.getJSONArray("petCharacter");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    characterList.add(CareTag
                                            .json2Entity(jsonArray
                                                    .getJSONObject(i)));
                                }
                            }
                            if (careHistoryTagList.size() > 0) {
                                setTagHljl(fluidHljlTag, careHistoryTagList);
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastCenterShort(AddArchivesActivity.this, msg);
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                ToastUtil.showToastCenterShort(AddArchivesActivity.this,
                        "数据异常");
            }
            GlideUtil.loadCircleImg(AddArchivesActivity.this, petimg, ivAddpetarchivesImg, R.drawable.user_icon_unnet_circle);
            Utils.setText(tvAddpetarchivesNickname, nickname, "", View.VISIBLE, View.VISIBLE);
            Utils.setText(tvAddpetarchivesMonth, petmonth, "", View.VISIBLE, View.GONE);
            Utils.setText(tvAddpetarchivesPetname, petname, "", View.VISIBLE, View.VISIBLE);
            if (petsex == 0) {
                ivAddpetarchivesSex.setImageResource(R.drawable.pet_archives_nv);
            } else if (petsex == 1) {
                ivAddpetarchivesSex.setImageResource(R.drawable.pet_archives_nan);
            }
            if (problemTagList.size() > 0) {
                archivesProblemTagAdapter.notifyDataSetChanged();
            }
            if (alrightTagList.size() > 0) {
                archivesAlrightTagAdapter.notifyDataSetChanged();
            }
            if (careHistoryTagList.size() > 0) {
                archivesCareTagAdapter.notifyDataSetChanged();
            }
            if (petCharacterIds != null && petCharacterIds.length > 0 && characterList.size() > 0) {
                for (int i = 0; i < petCharacterIds.length; i++) {
                    for (int j = 0; j < characterList.size(); j++) {
                        if (Utils.isStrNull(petCharacterIds[i]) && characterList.get(j).getId() == Integer.parseInt(petCharacterIds[i])) {
                            characterList.get(j).setSelect(true);
                        }
                    }
                }
            }
            if (characterList.size() > 0) {
                sbCharacter.setLength(0);
                for (int i = 0; i < characterList.size(); i++) {
                    if (characterList.get(i).isSelect()) {
                        sbCharacter.append(characterList.get(i).getTag() + ",");
                    }
                }
                if (sbCharacter.length() > 0) {
                    Utils.setText(tv_addpetarchives_xg_str, sbCharacter.substring(0, sbCharacter.length() - 1), "未填写", View.VISIBLE, View.VISIBLE);
                } else {
                    tv_addpetarchives_xg_str.setText("");
                }
            }
            if (ageGroupList != null && ageGroupList.size() > 0) {
                ageGroupArray = new String[ageGroupList.size()];
                for (int i = 0; i < ageGroupList.size(); i++) {
                    ageGroupArray[i] = ageGroupList.get(i).getKey();
                }
                for (int i = 0; i < ageGroupList.size(); i++) {
                    if (ageGroupList.get(i).getValue() == ageGroup) {
                        ageGroupIndex = i;
                        Utils.setText(tvAddarchivesNlfw, ageGroupList.get(i).getKey(), "", View.VISIBLE, View.VISIBLE);
                        break;
                    }
                }
            }
            if (petPostureList != null && petPostureList.size() > 0) {
                petPostureArray = new String[petPostureList.size()];
                for (int i = 0; i < petPostureList.size(); i++) {
                    petPostureArray[i] = petPostureList.get(i).getKey();
                }
                for (int i = 0; i < petPostureList.size(); i++) {
                    if (petPostureList.get(i).getValue() == petPosture) {
                        petPostureIndex = i;
                        Utils.setText(tvAddarchivesTt, petPostureList.get(i).getKey(), "", View.VISIBLE, View.VISIBLE);
                        break;
                    }
                }
            }
            nsv_addpetarchives.fullScroll(ScrollView.FOCUS_UP);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(AddArchivesActivity.this,
                    "请求失败");
        }
    };

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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void exit() {
        if (isChange()) {
            new AlertDialogDefault(mActivity).builder()
                    .setTitle("提示").setMsg("是否退出编辑").isOneBtn(false).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).setPositiveButton("退出编辑", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();
        } else {
            finish();
        }
    }

    @OnClick({R.id.ib_titlebar_back,
            R.id.iv_addpetarchives_nopro,
            R.id.iv_addpetarchives_yespro,
            R.id.RelatHasPro,
            R.id.RelatNoPro,
            R.id.btn_addpetarchives_submit, R.id.rl_addpetarchives_xg, R.id.rl_addarchives_nlfw, R.id.rl_addarchives_tt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_addpetarchives_xg:
                if (characterList.size() > 0) {
                    new AlertDialogPetCharacter(mContext).builder().setCanceledOnTouchOutside(false)
                            .setData(characterList).show();
                }
                break;
            case R.id.ib_titlebar_back:
                exit();
                break;
            case R.id.RelatNoPro:
            case R.id.iv_addpetarchives_nopro:
                flag = 1;
                setTag();
                setTagHasNoPro(fluidNoProTag, alrightTagList);
                break;
            case R.id.RelatHasPro:
            case R.id.iv_addpetarchives_yespro:
                flag = 2;
                setTag();
                setTagYesPro(fluidYesProTag, problemTagList);
                break;
            case R.id.btn_addpetarchives_submit:
                postData();
                break;
            case R.id.rl_addarchives_nlfw:
                if (ageGroupList != null && ageGroupList.size() > 0) {
                    index = 1;
                    showSrDialog();
                }
                break;
            case R.id.rl_addarchives_tt:
                if (petPostureList != null && petPostureList.size() > 0) {
                    index = 2;
                    showSrDialog();
                }
                break;
        }
    }

    private void showSrDialog() {
        ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.ageortt_bottom_dialog, null);
        ImageView iv_ageortt_bottom_close = (ImageView) customView.findViewById(R.id.iv_ageortt_bottom_close);
        final WheelView wv_ageortt_bottom = (WheelView) customView.findViewById(R.id.wv_ageortt_bottom);
        ImageView iv_ageortt_bottom_bg = (ImageView) customView.findViewById(R.id.iv_ageortt_bottom_bg);
        Button btn_ageortt_bottom = (Button) customView.findViewById(R.id.btn_ageortt_bottom);
        TextView tv_ageortt_bottom_title = (TextView) customView.findViewById(R.id.tv_ageortt_bottom_title);
        if (index == 1) {
            tv_ageortt_bottom_title.setText("请选择宠物的年龄范围");
        } else if (index == 2) {
            tv_ageortt_bottom_title.setText("请选择宠物的体态");
        }
        wv_ageortt_bottom.setLineSpacingMultiplier(2.0f);
        wv_ageortt_bottom.setTextSize(18 * getResources().getDisplayMetrics().density / 3);
        wv_ageortt_bottom.setTextColorCenter(getResources().getColor(R.color.a333333));
        wv_ageortt_bottom.setTextColorOut(getResources().getColor(R.color.a999999));
        wv_ageortt_bottom.setCyclic(false);//循环滚动
        wv_ageortt_bottom.setDividerColor(getResources().getColor(R.color.a979797));
        wv_ageortt_bottom.smoothScroll(WheelView.ACTION.FLING);
        if (index == 1) {
            wv_ageortt_bottom.setCurrentItem(ageGroupIndex);
            wv_ageortt_bottom.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(ageGroupArray)));
        } else {
            wv_ageortt_bottom.setCurrentItem(petPostureIndex);
            wv_ageortt_bottom.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(petPostureArray)));
        }
        final PopupWindow pWinBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        btn_ageortt_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 1) {
                    ageGroupIndex = wv_ageortt_bottom.getCurrentItem();
                    PetTag petTag = ageGroupList.get(ageGroupIndex);
                    ageGroup = petTag.getValue();
                    Utils.setText(tvAddarchivesNlfw, petTag.getKey(), "", View.VISIBLE, View.VISIBLE);
                } else {
                    petPostureIndex = wv_ageortt_bottom.getCurrentItem();
                    PetTag petTag = petPostureList.get(petPostureIndex);
                    petPosture = petTag.getValue();
                    Utils.setText(tvAddarchivesTt, petTag.getKey(), "", View.VISIBLE, View.VISIBLE);
                }
                pWinBottomDialog.dismiss();
            }
        });
        iv_ageortt_bottom_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
        iv_ageortt_bottom_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWinBottomDialog.dismiss();
            }
        });
    }

    private boolean isChange() {
        boolean isChange = false;
        String babyContent = "";
        String itemIds = "";
        String careItemIds = "";
        String ids = "";
        if (flag == 2) {
            babyContent = etAddpetarchivesYesproContent.getText().toString().trim();
            if (problemTagList.size() > 0) {
                for (int i = 0; i < problemTagList.size(); i++) {
                    if (problemTagList.get(i).isSelect()) {
                        Log.e("TAG", "problemTagList i = " + i);
                        itemIds = itemIds + problemTagList.get(i).getId() + ",";
                    }
                }
            }
        } else if (flag == 1) {
            babyContent = etAddpetarchivesNoproContent.getText().toString().trim();
            if (alrightTagList.size() > 0) {
                for (int i = 0; i < alrightTagList.size(); i++) {
                    if (alrightTagList.get(i).isSelect()) {
                        Log.e("TAG", "alrightTagList i = " + i);
                        itemIds = itemIds + alrightTagList.get(i).getId() + ",";
                    }
                }
            }
        }
        if (careHistoryTagList.size() > 0) {
            for (int i = 0; i < careHistoryTagList.size(); i++) {
                if (careHistoryTagList.get(i).isSelect()) {
                    careItemIds = careItemIds + careHistoryTagList.get(i).getId() + ",";
                }
            }
        }
        if (Utils.isStrNull(itemIds)) {
            ids = itemIds.substring(0, itemIds.length() - 1);
        } else {
            ids = "0";
        }
        if (Utils.isStrNull(careItemIds)) {
            ids = ids + ";" + careItemIds.substring(0, careItemIds.length() - 1);
        } else {
            ids = ids + ";" + "0";
        }
        sbCharacterIds.setLength(0);
        for (int i = 0; i < characterList.size(); i++) {
            if (characterList.get(i).isSelect()) {
                if (i == characterList.size() - 1) {
                    sbCharacterIds.append(characterList.get(i).getId());
                } else {
                    sbCharacterIds.append(characterList.get(i).getId() + ",");
                }
            }
        }
        if (Utils.isStrNull(etAddpetarchivesHljlContent.getText().toString().trim()) ||
                Utils.isStrNull(babyContent) || !ids.equals("0;0") ||
                Utils.isStrNull(sbCharacterIds.toString()) || beforeFileList.size() > 0 || afterFileList.size() > 0 || ageGroupIndex >= 0 || petPostureIndex >= 0) {
            isChange = true;
        } else {
            isChange = false;
        }
        return isChange;
    }

    private void postData() {
        String babyContent = "";
        String itemIds = "";
        String careItemIds = "";
        String ids = "";
        Log.e("TAG", "flag = " + flag);
        Log.e("TAG", "problemTagList = " + problemTagList.toString());
        Log.e("TAG", "alrightTagList = " + alrightTagList.toString());
        Log.e("TAG", "flag = " + flag);
        if (flag == 2) {
            babyContent = etAddpetarchivesYesproContent.getText().toString().trim();
            if (problemTagList.size() > 0) {
                for (int i = 0; i < problemTagList.size(); i++) {
                    if (problemTagList.get(i).isSelect()) {
                        Log.e("TAG", "problemTagList i = " + i);
                        itemIds = itemIds + problemTagList.get(i).getId() + ",";
                    }
                }
            }
        } else if (flag == 1) {
            babyContent = etAddpetarchivesNoproContent.getText().toString().trim();
            if (alrightTagList.size() > 0) {
                for (int i = 0; i < alrightTagList.size(); i++) {
                    if (alrightTagList.get(i).isSelect()) {
                        Log.e("TAG", "alrightTagList i = " + i);
                        itemIds = itemIds + alrightTagList.get(i).getId() + ",";
                    }
                }
            }
        }
        if (careHistoryTagList.size() > 0) {
            for (int i = 0; i < careHistoryTagList.size(); i++) {
                if (careHistoryTagList.get(i).isSelect()) {
                    careItemIds = careItemIds + careHistoryTagList.get(i).getId() + ",";
                }
            }
        }
        if (TextUtils.isEmpty(babyContent)) {
            ToastUtil.showToastCenterShort(mContext, "请填写宝贝情况");
            mPDialog.closeDialog();
            return;
        } else {
            if (babyContent.length() < 4) {
                ToastUtil.showToastCenterShort(mContext, "宝贝情况请输入至少四个字");
                mPDialog.closeDialog();
                return;
            }
        }
        if (!TextUtils.isEmpty(etAddpetarchivesHljlContent.getText())) {
            if (etAddpetarchivesHljlContent.getText().length() < 4) {
                ToastUtil.showToastCenterShort(mContext, "护理记录请输入至少四个字");
                mPDialog.closeDialog();
                return;
            }
        } else {
            ToastUtil.showToastCenterShort(mContext, "请填写护理记录");
            mPDialog.closeDialog();
            return;
        }
        Log.e("TAG", "itemIds = " + itemIds);
        Log.e("TAG", "careItemIds = " + careItemIds);
        if (Utils.isStrNull(itemIds)) {
            ids = itemIds.substring(0, itemIds.length() - 1);
        } else {
            ids = "0";
        }
        if (Utils.isStrNull(careItemIds)) {
            ids = ids + ";" + careItemIds.substring(0, careItemIds.length() - 1);
        } else {
            ids = ids + ";" + "0";
        }
        Log.e("TAG", "ids = " + ids);
        File[] array = new File[beforeFileList.size()];
        beforeFileList.toArray(array);

        File[] array1 = new File[afterFileList.size()];
        afterFileList.toArray(array1);

        sbCharacterIds.setLength(0);
        for (int i = 0; i < characterList.size(); i++) {
            if (characterList.get(i).isSelect()) {
                if (i == characterList.size() - 1) {
                    sbCharacterIds.append(characterList.get(i).getId());
                } else {
                    sbCharacterIds.append(characterList.get(i).getId() + ",");
                }
            }
        }
        mPDialog.showDialog();
        CommUtil.addCareHistory(mContext, orderId, petsex,
                petmonth, etAddpetarchivesHljlContent.getText()
                        .toString().trim(), petname,
                babyContent, ids, array, array1, flag, ageGroup, petPosture, sbCharacterIds.toString(),
                addCareHistory);
    }

    private AsyncHttpResponseHandler addCareHistory = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jsonObject = new JSONObject(new String(responseBody));
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    ToastUtil.showToastBottomShort(AddArchivesActivity.this, "上传成功");
                    setResult(RESULT_OK);
                    startActivity(new Intent(AddArchivesActivity.this,
                            PetArchivesNewActivity.class)
                            .putExtra("id", customerid).putExtra("petmonth", petmonth));
                    finishWithAnimation();
                    EventBus.getDefault().post(new AddArchivesEvent(orderId));
                } else {
                    if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
                        ToastUtil.showToastCenterShort(AddArchivesActivity.this,
                                jsonObject.getString("msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
        }
    };

    private void setTagHasNoPro(FluidLayout fluid_layout, final List<CareTag> TagList) {
        fluid_layout.removeAllViews();
        fluid_layout.setGravity(gravity);
        for (int i = 0; i < TagList.size(); i++) {
            TextView tv = new TextView(this);
            if (TagList.get(i).isSelect()) {
                tv.setBackgroundResource(R.drawable.bg_ff3a1e_round);
                tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                tv.setBackgroundResource(R.drawable.bg_round_717985order);
                tv.setTextColor(getResources().getColor(R.color.a717985));
            }

            tv.setText(TagList.get(i).getTag());
            tv.setTextSize(12);
            tv.setPadding(60, 15, 60, 15);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 20, 20, 20);
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
                    if (TagList.get(finalI).isSelect()) {
                        TagList.get(finalI).setSelect(false);
                        view.setBackgroundResource(R.drawable.bg_round_717985order);
                        view.setTextColor(getResources().getColor(R.color.a717985));
                    } else {
                        TagList.get(finalI).setSelect(true);
                        view.setBackgroundResource(R.drawable.bg_ff3a1e_round);
                        view.setTextColor(getResources().getColor(R.color.white));
                    }
                }
            });
            fluid_layout.addView(tv, params);
        }
    }

    private void setTagYesPro(FluidLayout fluid_layout, final List<CareTag> TagList) {
        fluid_layout.removeAllViews();
        fluid_layout.setGravity(gravity);
        for (int i = 0; i < TagList.size(); i++) {
            TextView tv = new TextView(this);
            if (TagList.get(i).isSelect()) {
                tv.setBackgroundResource(R.drawable.bg_ff3a1e_round);
                tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                tv.setBackgroundResource(R.drawable.bg_round_717985order);
                tv.setTextColor(getResources().getColor(R.color.a717985));
            }

            tv.setText(TagList.get(i).getTag());
            tv.setTextSize(12);
            tv.setPadding(60, 15, 60, 15);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 20, 20, 20);
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
                    if (TagList.get(finalI).isSelect()) {
                        TagList.get(finalI).setSelect(false);
                        view.setBackgroundResource(R.drawable.bg_round_717985order);
                        view.setTextColor(getResources().getColor(R.color.a717985));
                    } else {
                        TagList.get(finalI).setSelect(true);
                        view.setBackgroundResource(R.drawable.bg_ff3a1e_round);
                        view.setTextColor(getResources().getColor(R.color.white));
                    }
                }
            });
            fluid_layout.addView(tv, params);
        }
    }

    private void setTagHljl(FluidLayout fluid_layout, final List<CareTag> TagList) {
        fluid_layout.removeAllViews();
        fluid_layout.setGravity(gravity);
        for (int i = 0; i < TagList.size(); i++) {
            TextView tv = new TextView(this);
            if (TagList.get(i).isSelect()) {
                tv.setBackgroundResource(R.drawable.bg_ff3a1e_round);
                tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                tv.setBackgroundResource(R.drawable.bg_round_717985order);
                tv.setTextColor(getResources().getColor(R.color.a717985));
            }

            tv.setText(TagList.get(i).getTag());
            tv.setTextSize(12);
            tv.setPadding(60, 15, 60, 15);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 20, 10, 20);
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
                    if (TagList.get(finalI).isSelect()) {
                        TagList.get(finalI).setSelect(false);
                        view.setBackgroundResource(R.drawable.bg_round_717985order);
                        view.setTextColor(getResources().getColor(R.color.a717985));
                    } else {
                        TagList.get(finalI).setSelect(true);
                        view.setBackgroundResource(R.drawable.bg_ff3a1e_round);
                        view.setTextColor(getResources().getColor(R.color.white));
                    }
                }
            });
            fluid_layout.addView(tv, params);
        }
    }

    private void setTag() {
        if (flag == 1) {
            etAddpetarchivesYesproContent.setText("");
            ivAddpetarchivesNopro.setImageResource(R.drawable.icon_tag_select);
            ivAddpetarchivesYespro.setImageResource(R.drawable.icon_tag_unselect);
            llAddpetarchivesYesproTag.setVisibility(View.GONE);
            llAddpetarchivesNoproTag.setVisibility(View.VISIBLE);
            if (problemTagList.size() > 0) {
                for (int i = 0; i < problemTagList.size(); i++) {
                    problemTagList.get(i).setSelect(false);
                }
                archivesProblemTagAdapter.notifyDataSetChanged();
            }
        } else if (flag == 2) {
            etAddpetarchivesNoproContent.setText("");
            ivAddpetarchivesNopro.setImageResource(R.drawable.icon_tag_unselect);
            ivAddpetarchivesYespro.setImageResource(R.drawable.icon_tag_select);
            llAddpetarchivesYesproTag.setVisibility(View.VISIBLE);
            llAddpetarchivesNoproTag.setVisibility(View.GONE);
            if (alrightTagList.size() > 0) {
                for (int i = 0; i < alrightTagList.size(); i++) {
                    alrightTagList.get(i).setSelect(false);
                }
                archivesAlrightTagAdapter.notifyDataSetChanged();
            }
        }
    }
}
