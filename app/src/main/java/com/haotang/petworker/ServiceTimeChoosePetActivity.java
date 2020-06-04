package com.haotang.petworker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.petworker.adapter.ServiceTimeChoosePetAdapter;
import com.haotang.petworker.entity.NewPetBean;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CharacterParser;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.MProgressDialog;
import com.haotang.petworker.utils.PetPinyinComparator;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.view.SideBar;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Title:ServiceTimeChoosePetActivity
 * </p>
 * <p>
 * Description:调整服务时长宠物列表
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2017-5-16 上午10:51:02
 */
public class ServiceTimeChoosePetActivity extends SuperActivity implements
        OnClickListener {
    private MProgressDialog pDialog;
    private ListView lv_servicetimecp_list;
    private ImageButton ibBack;
    private TextView tvTitle;
    private CharacterParser characterParser;
    private PetPinyinComparator pinyinComparator;
    private List<NewPetBean> listNewPetList = new ArrayList<NewPetBean>();
    private SideBar sbBar;
    private TextView tvBarHint;
    private ServiceTimeChoosePetAdapter<NewPetBean> newPetBeanServiceTimeChoosePetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        intiView();
        setView();
        initLinsetr();
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
        pDialog.showDialog();
        CommUtil.petlistNew(this, petlistNewHandler);
    }

    private AsyncHttpResponseHandler petlistNewHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            pDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                if (jobj.has("code") && !jobj.isNull("code")) {
                    int code = jobj.getInt("code");
                    if (code == 0) {
                        if (jobj.has("data") && !jobj.isNull("data")) {
                            JSONArray jdata = jobj.getJSONArray("data");
                            if (jdata != null && jdata.length() > 0) {
                                listNewPetList.clear();
                                for (int i = 0; i < jdata.length(); i++) {
                                    listNewPetList
                                            .add(NewPetBean.json2Entity(jdata
                                                    .getJSONObject(i)));
                                }
                                // 根据a-z进行排序源数据
                                Collections.sort(listNewPetList, pinyinComparator);
                                newPetBeanServiceTimeChoosePetAdapter.setData(listNewPetList);
                                lv_servicetimecp_list.setSelection(0);
                            }
                        }
                    } else {
                        if (jobj.has("msg") && !jobj.isNull("msg")) {
                            ToastUtil.showToastBottomShort(
                                    ServiceTimeChoosePetActivity.this,
                                    jobj.getString("msg"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastCenterShort(
                        ServiceTimeChoosePetActivity.this, "数据解析异常");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            ToastUtil.showToastCenterShort(ServiceTimeChoosePetActivity.this,
                    "请求失败");
            pDialog.closeDialog();
        }
    };

    private void initData() {
        pDialog = new MProgressDialog(this);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PetPinyinComparator();
    }

    private void intiView() {
        setContentView(R.layout.activity_service_time_choose_pet);
        lv_servicetimecp_list = (ListView) findViewById(R.id.lv_servicetimecp_list);
        ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
        tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        sbBar = (SideBar) findViewById(R.id.sb_choosepet_sidebar);
        tvBarHint = (TextView) findViewById(R.id.tv_choosepet_hint);
    }

    private void setView() {
        tvTitle.setText("服务时长调整");
        listNewPetList.clear();
        newPetBeanServiceTimeChoosePetAdapter = new ServiceTimeChoosePetAdapter<NewPetBean>(
                this,
                listNewPetList);
        lv_servicetimecp_list
                .setAdapter(newPetBeanServiceTimeChoosePetAdapter);
        sbBar.setTextView(tvBarHint);
    }

    private void initLinsetr() {
        ibBack.setOnClickListener(this);
        lv_servicetimecp_list
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (listNewPetList.size() > 0
                                && listNewPetList.size() > position) {
                            NewPetBean newPetBean = listNewPetList
                                    .get(position);
                            if (newPetBean != null) {
                                Intent intent = new Intent(
                                        getApplicationContext(),
                                        AdjustmentServiceTimeActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("newPetBean", newPetBean);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    }
                });
        sbBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                try {
                    int position = newPetBeanServiceTimeChoosePetAdapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        lv_servicetimecp_list.setSelection(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
