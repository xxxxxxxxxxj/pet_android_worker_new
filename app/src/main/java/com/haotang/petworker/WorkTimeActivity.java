package com.haotang.petworker;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.petworker.adapter.WorkTimeAdapter;
import com.haotang.petworker.entity.SetTimeConfig;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.Global;
import com.haotang.petworker.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看上班时间
 */
public class WorkTimeActivity extends SuperActivity {
    @BindView(R.id.ll_workertime_next)
    LinearLayout llWorkertimeNext;
    @BindView(R.id.ll_workertime_pre)
    LinearLayout llWorkertimePre;
    @BindView(R.id.rv_workertime)
    RecyclerView rvWorkertime;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_workertime_jiange)
    TextView tvWorkertimeJiange;
    private List<SetTimeConfig> list = new ArrayList<SetTimeConfig>();
    private WorkTimeAdapter workTimeAdapter;
    private Calendar calendar;
    private int currentweek;
    private String startday;
    private int weeknum = 1;
    private int startdaytotoday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
        getTimes(startday);
    }

    private void initData() {
        calendar = Calendar.getInstance();
        currentweek = calendar.get(Calendar.DAY_OF_WEEK);
        startdaytotoday = getstartday(currentweek);
        calendar.add(Calendar.DATE, -1 * startdaytotoday);
        startday = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
    }

    private void findView() {
        setContentView(R.layout.activity_work_time);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("上线接单时间查询");
        rvWorkertime.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        workTimeAdapter = new WorkTimeAdapter(R.layout.item_workertime, list);
        rvWorkertime.setAdapter(workTimeAdapter);
    }

    private void setLinster() {

    }

    private void getTimes(String startdate) {
        mPDialog.showDialog();
        list.clear();
        CommUtil.getWorkerTime(this, spUtil.getString("wcellphone", ""),
                Global.getIMEI(this), Global.getCurrentVersion(this),
                startdate, 7, timeHandler);
    }

    @OnClick({R.id.ib_titlebar_back, R.id.ll_workertime_next, R.id.ll_workertime_pre})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ll_workertime_next:
                nextWeek();
                break;
            case R.id.ll_workertime_pre:
                preWeek();
                break;
        }
    }

    private void preWeek() {
        weeknum--;
        if (weeknum == 1) {
            llWorkertimePre.setVisibility(View.GONE);
        } else {
            llWorkertimePre.setVisibility(View.VISIBLE);
        }
        llWorkertimeNext.setVisibility(View.VISIBLE);
        calendar.add(Calendar.DATE, -7);

        startday = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
        getTimes(startday);
    }

    private void nextWeek() {
        weeknum++;
        if (weeknum == 3) {
            llWorkertimeNext.setVisibility(View.GONE);
        } else {
            llWorkertimeNext.setVisibility(View.VISIBLE);
        }
        llWorkertimePre.setVisibility(View.VISIBLE);
        calendar.add(Calendar.DATE, 7);
        startday = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
        getTimes(startday);
    }

    private int getstartday(int currentweek) {
        switch (currentweek) {
            case 1:
                return 6;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 4;
            case 7:
                return 5;
            default:
                return 0;
        }
    }

    private AsyncHttpResponseHandler timeHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            try {
                JSONObject jobj = new JSONObject(new String(responseBody));
                int resultCode = jobj.getInt("code");
                if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
                    JSONObject jdata = jobj.getJSONObject("data");
                    if (jdata.has("date") && !jdata.isNull("date")) {
                        tvWorkertimeJiange.setText(jdata.getString("date"));
                    }
                    if (jdata.has("config") && !jdata.isNull("config")) {
                        JSONArray jweek = jdata.getJSONArray("config");
                        if (jweek != null && jweek.length() > 0) {
                            list.clear();
                            for (int i = 0; i < jweek.length(); i++) {
                                list.add(SetTimeConfig.json2Entity(jweek
                                        .getJSONObject(i)));
                            }
                        }
                    }
                    workTimeAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToastCenterShort(mContext,
                            jobj.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
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
}
