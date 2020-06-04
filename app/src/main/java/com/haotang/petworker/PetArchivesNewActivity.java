package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.petworker.adapter.ArchivesHistoryAdapter;
import com.haotang.petworker.entity.CareHistoryNew;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.GlideUtil;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PetArchivesNewActivity extends SuperActivity {

    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_archives_head)
    ImageView ivArchivesHead;
    @BindView(R.id.tv_archives_nickname)
    TextView tvArchivesNickname;
    @BindView(R.id.iv_archives_sex)
    ImageView ivArchivesSex;
    @BindView(R.id.tv_archives_type)
    TextView tvArchivesType;
    @BindView(R.id.rv_archives_list)
    RecyclerView rvArchivesList;
    @BindView(R.id.tv_archives_age)
    TextView tvArchivesAge;
    private int id;
    private List<CareHistoryNew> list = new ArrayList<CareHistoryNew>();
    public MProgressDialog mPDialog;
    private ArchivesHistoryAdapter historyAdapter;
    private String petmonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        getData();
        setLinstener();
    }

    private void setLinstener() {
        historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CareHistoryNew careHistory = list.get(position);
                Intent intent = new Intent(PetArchivesNewActivity.this, PetArchivesDetailActivity.class);
                intent.putExtra("careHistory", careHistory);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        mPDialog.showDialog();
        CommUtil.getPetCareHistory(PetArchivesNewActivity.this, id, getPetCareHistoryHanler);
    }

    private AsyncHttpResponseHandler getPetCareHistoryHanler = new AsyncHttpResponseHandler() {

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
                        if (jdata.has("careHistory") && !jdata.isNull("careHistory")
                                && jdata.getJSONArray("careHistory").length() > 0) {
                            JSONArray jsonArray = jdata.getJSONArray("careHistory");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(CareHistoryNew
                                        .json2Entity(jsonArray
                                                .getJSONObject(i)));
                                historyAdapter.notifyDataSetChanged();
                            }
                        }
                        if (list.size()==0){
                            historyAdapter.setEmptyView(setEmptyViewBase(2,"暂时没有宠物档案~",R.drawable.icon_noproduction,null));
                        }
                        if (jdata.has("customerPet") && !jdata.isNull("customerPet")) {
                            JSONObject cdata = jdata.getJSONObject("customerPet");
                            if (cdata.has("avatar") && !cdata.isNull("avatar")) {
                                GlideUtil.loadImg(PetArchivesNewActivity.this, cdata.getString("avatar"), ivArchivesHead, R.drawable.icon_beautician_default);
                            }
                            if (cdata.has("nickName") && !cdata.isNull("nickName")) {
                                tvArchivesNickname.setText(cdata.getString("nickName"));
                            }
                            if (cdata.has("sex") && !cdata.isNull("sex")) {
                                int sex = cdata.getInt("sex");
                                if (sex == 0) {
                                    ivArchivesSex.setImageResource(R.drawable.pet_archives_nv);
                                } else if (sex == 1) {
                                    ivArchivesSex.setImageResource(R.drawable.pet_archives_nan);
                                }
                            }
                            if (cdata.has("typeName") && !cdata.isNull("typeName")) {
                                tvArchivesType.setText(cdata.getString("typeName"));
                            }
                        }
                    }
                } else {
                    ToastUtil.showToastCenterShort(PetArchivesNewActivity.this,
                            msg);
                }
            } catch (JSONException e) {
                Log.e("TAG", "e = " + e.toString());
                ToastUtil.showToastCenterShort(PetArchivesNewActivity.this,
                        "数据异常");
            }
            if (list.size() > 0) {

            } else {

            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(PetArchivesNewActivity.this,
                    "请求失败");
        }
    };

    private void initData() {
        id = getIntent().getIntExtra("id", 0);
        petmonth = getIntent().getStringExtra("petmonth");
    }

    private void setView() {
        setContentView(R.layout.activity_pet_archives_new);
        ButterKnife.bind(this);
        mPDialog = new MProgressDialog(this);
        Utils.setText(tvArchivesAge, petmonth, "", View.VISIBLE, View.GONE);
        historyAdapter = new ArchivesHistoryAdapter(R.layout.item_archives_history, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PetArchivesNewActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvArchivesList.setLayoutManager(layoutManager);
        tvTitlebarTitle.setText("宠物档案");
        rvArchivesList.setAdapter(historyAdapter);
    }

    @OnClick({R.id.ib_titlebar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
        }
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
}
