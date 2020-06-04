package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.petworker.adapter.ArchivesIconAdapter;
import com.haotang.petworker.entity.CareHistoryNew;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PetArchivesDetailActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_archives_pethead)
    ImageView ivArchivesPethead;
    @BindView(R.id.tv_archives_petname)
    TextView tvArchivesPetname;
    @BindView(R.id.tv_upload_time)
    TextView tvUploadTime;
    @BindView(R.id.tv_archives_content)
    TextView tvArchivesContent;
    @BindView(R.id.rv_archives_before)
    RecyclerView rvArchivesBefore;
    @BindView(R.id.rv_archives_after)
    RecyclerView rvArchivesAfter;
    @BindView(R.id.iv_archives_beautyhead)
    ImageView ivArchivesBeautyhead;
    @BindView(R.id.archives_beautyname)
    TextView archivesBeautyname;
    @BindView(R.id.tv_archives_beautyinfo)
    TextView tvArchivesBeautyinfo;
    @BindView(R.id.tv_archives_baby)
    TextView tvArchivesBaby;
    @BindView(R.id.tv_archives_service)
    TextView tvArchivesService;
    @BindView(R.id.tv_archives_servicetype)
    TextView tvArchivesServicetype;
    @BindView(R.id.rl_archives_bottom)
    RelativeLayout rlArchivesBottom;
    private int id;
    private MProgressDialog pDialog;
    private CareHistoryNew careHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
    }

    private void setView() {
        setContentView(R.layout.activity_pet_archives_detail);
        ButterKnife.bind(this);
        pDialog = new MProgressDialog(this);
        tvTitlebarTitle.setText("宠物档案");
        GlideUtil.loadImg(this, careHistory.getAvatar(), ivArchivesPethead, R.drawable.icon_beautician_default);
        GlideUtil.loadImg(this,careHistory.getWorkerAvatar(),ivArchivesBeautyhead, R.drawable.icon_beautician_default);
        archivesBeautyname.setText(careHistory.getWorkerName());
        tvUploadTime.setText("上传时间:" + careHistory.getCreateTimes());
        tvArchivesBeautyinfo.setText(careHistory.getLevelName() + "美容师|" + careHistory.getShopName());
        tvArchivesPetname.setText(careHistory.getNickName());
        tvArchivesContent.setText(careHistory.getContent());
        tvArchivesBaby.setText(careHistory.getBabyContent());
        tvArchivesService.setText(careHistory.getServiceName());
        tvArchivesServicetype.setText(careHistory.getServiceLoc());
        GridLayoutManager layoutManager = new GridLayoutManager(PetArchivesDetailActivity.this, 2);
        GridLayoutManager layoutManagerTwo = new GridLayoutManager(PetArchivesDetailActivity.this, 2);
        rvArchivesBefore.setLayoutManager(layoutManager);
        rvArchivesAfter.setLayoutManager(layoutManagerTwo);
        ArchivesIconAdapter iconAdapterOne = new ArchivesIconAdapter(this, careHistory.getBeforePicsList());
        rvArchivesBefore.setAdapter(iconAdapterOne);
        ArchivesIconAdapter iconAdapterTwo = new ArchivesIconAdapter(this, careHistory.getPicsList());
        rvArchivesAfter.setAdapter(iconAdapterTwo);
        if (careHistory.getBeforePicsList().size()==0&&careHistory.getPicsList().size()==0){
            rlArchivesBottom.setVisibility(View.GONE);
        }
        iconAdapterOne.setListener(new ArchivesIconAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.imageBrower(PetArchivesDetailActivity.this, position, careHistory.getBeforePicsList().toArray(new String[] {}));
            }
        });
        iconAdapterTwo.setListener(new ArchivesIconAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Utils.imageBrower(PetArchivesDetailActivity.this, position, careHistory.getPicsList().toArray(new String[] {}));
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        careHistory = (CareHistoryNew) intent.getSerializableExtra("careHistory");
    }

    private void getData() {
        pDialog.showDialog();

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

    @OnClick(R.id.ib_titlebar_back)
    public void onViewClicked() {
        finish();
    }
}
