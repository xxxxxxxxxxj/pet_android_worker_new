package com.haotang.petworker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.haotang.petworker.adapter.IncomeAdapter;
import com.haotang.petworker.entity.NewIncome;
import com.haotang.petworker.net.AsyncHttpResponseHandler;
import com.haotang.petworker.utils.CommUtil;
import com.haotang.petworker.utils.ToastUtil;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.NoScollFullLinearLayoutManager;
import com.haotang.petworker.view.ObservableScrollView;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收入主界面
 */
public class IncomeActivity extends SuperActivity implements ObservableScrollView.ScrollViewListener {
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.rl_income_title)
    RelativeLayout rlIncomeTitle;
    @BindView(R.id.tv_income_month)
    TextView tvIncomeMonth;
    @BindView(R.id.tv_income_jzrq)
    TextView tvIncomeJzrq;
    @BindView(R.id.tv_income_monthdesc)
    TextView tvIncomeMonthdesc;
    @BindView(R.id.osv_income)
    ObservableScrollView osvIncome;
    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.grv_income)
    RecyclerView grvIncome;
    @BindView(R.id.ll_income_top)
    LinearLayout llIncomeTop;
    @BindView(R.id.tv_emptyview_desc)
    TextView tvEmptyviewDesc;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    private int selectedMonthIndex;
    private String selectMonth;
    private String incomeJzrq;
    private List<String> monthList = new ArrayList<String>();
    private List<String> monthShowList = new ArrayList<>();
    private List<NewIncome.DataBean.ListBeanXX> incomeList = new ArrayList<>();
    private IncomeAdapter incomeAdapter;
    private int imageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        initData();
        findView();
        setView();
        initListener();
        getData();
    }

    private void initData() {
    }

    private void findView() {
        setContentView(R.layout.activity_incom);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("本月收入");
        ibTitlebarOther.setVisibility(View.VISIBLE);
        ibTitlebarOther.setBackgroundResource(R.drawable.icon_rili);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ibTitlebarOther.getLayoutParams();
        layoutParams.width = 80;
        layoutParams.height = 80;
        ibTitlebarOther.setLayoutParams(layoutParams);
        rlIncomeTitle.bringToFront();
        grvIncome.setHasFixedSize(true);
        grvIncome.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullGridLayoutManager = new
                NoScollFullLinearLayoutManager(IncomeActivity.this);
        noScollFullGridLayoutManager.setScrollEnabled(false);
        grvIncome.setLayoutManager(noScollFullGridLayoutManager);
        incomeAdapter = new IncomeAdapter(mContext);
        grvIncome.setAdapter(incomeAdapter);
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = llIncomeTop.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llIncomeTop.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = llIncomeTop.getHeight();
                osvIncome.setScrollViewListener(IncomeActivity.this);
            }
        });
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            rlIncomeTitle.setBackgroundColor(Color.argb((int) 0, 0, 34, 65));
        } else if (y > 0 && y <= imageHeight) {
            float scale = (float) y / imageHeight;
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            rlIncomeTitle.setBackgroundColor(Color.argb((int) alpha, 0, 34, 65));
        } else {
            rlIncomeTitle.setBackgroundColor(Color.argb((int) 255, 0, 34, 65));
        }
    }

    private void initListener() {
        incomeAdapter.setListener((position, data) -> {
            Intent intent = new Intent();
            intent.putExtra("position", position);
            intent.putExtra("selectMonth", selectMonth);
            intent.putExtra("data", data);
            intent.putExtra("incomeJzrq",incomeJzrq);
            if (data.getType() == 0) {
                intent.setClass(IncomeActivity.this,PrimaryIncomeActivity.class);
            }
            else {
                intent.setClass(IncomeActivity.this,IncomeDetailNewActivity.class);
            }
            startActivity(intent);
        });
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
        monthShowList.clear();
        monthList.clear();
        incomeList.clear();
        CommUtil.getMonthIncome(this, selectMonth, handler);
    }

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              byte[] responseBody) {
            mPDialog.closeDialog();
            Gson gson = new Gson();
            NewIncome income = gson.fromJson(new String(responseBody), NewIncome.class);
            if (income != null) {
                int code = income.getCode();
                String msg = income.getMsg();
                if (code == 0) {
                    if (income.getData() != null) {
                        rlEmpty.setVisibility(View.GONE);
                        grvIncome.setVisibility(View.VISIBLE);
                        NewIncome.DataBean incomeData = income.getData();
                        tvIncomeMonth.setText("¥" + incomeData.getTotalIncome());
                        tvIncomeJzrq.setText(incomeData.getMonthDayInfo());
                        incomeJzrq = incomeData.getMonthDayInfo();
                        tvIncomeMonthdesc.setText(incomeData.getIncomeExplain());
                        incomeList.addAll(incomeData.getList());
                        incomeAdapter.setList(incomeList);
                        //月份
                        if (incomeData.getMonths() != null && incomeData.getMonths().size() > 0) {
                            List<NewIncome.DataBean.MonthsBean> monthsBeans = incomeData.getMonths();
                            for (int i = 0; i < monthsBeans.size(); i++) {
                                String month = monthsBeans.get(i).getMonth();
                                monthList.add(month);
                                monthShowList.add(month.substring(0,month.length()-3));
                                //判断选中的月份
                                if (monthsBeans.get(i).getIsactive() == 1) {
                                    selectedMonthIndex = i;
                                    selectMonth = monthsBeans.get(i).getMonth();
                                }
                            }
                        }
                    }
                } else {
                    rlEmpty.setVisibility(View.VISIBLE);
                    tvEmptyviewDesc.setText(msg);
                    grvIncome.setVisibility(View.GONE);
                    ToastUtil.showToastBottomShort(mActivity, msg);
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              byte[] responseBody, Throwable error) {
            mPDialog.closeDialog();
            ToastUtil.showToastCenterShort(mActivity, "请求失败 ");
        }
    };

    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.ib_titlebar_other:
                showMonthDialog();
                break;
        }
    }

    private void showMonthDialog() {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.income_month_bottom_dialog, null);
        RelativeLayout rl_income_month_close = (RelativeLayout) customView.findViewById(R.id.rl_income_month_close);
        TextView tv_income_month_wc = (TextView) customView.findViewById(R.id.tv_income_month_wc);
        final WheelView wlv_income_month = (WheelView) customView.findViewById(R.id.wlv_income_month);
        wlv_income_month.setCurrentItem(selectedMonthIndex);
        wlv_income_month.setLineSpacingMultiplier(2.0f);
        wlv_income_month.setTextSize(18 * getResources().getDisplayMetrics().density / 3);
        wlv_income_month.setTextColorCenter(getResources().getColor(R.color.a002241));
        wlv_income_month.setTextColorOut(getResources().getColor(R.color.a9FA7B9));
        wlv_income_month.setAdapter(new ArrayWheelAdapter<String>(monthShowList));
        wlv_income_month.setCyclic(false);//循环滚动
        wlv_income_month.setDividerColor(getResources().getColor(R.color.a979797));
        final PopupWindow monthBottomDialog = new PopupWindow(customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        monthBottomDialog.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        monthBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        monthBottomDialog.setOutsideTouchable(true);
        //设置可以点击
        monthBottomDialog.setTouchable(true);
        //进入退出的动画
        monthBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        monthBottomDialog.setWidth(Utils.getDisplayMetrics(this)[0]);
        monthBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        tv_income_month_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMonthIndex = wlv_income_month.getCurrentItem();
                selectMonth = monthList.get(selectedMonthIndex);
                //判断是否是当前月份改变标题
                if (selectedMonthIndex==0){
                    tvTitlebarTitle.setText("本月收入");
                }else {
                    tvTitlebarTitle.setText(monthShowList.get(selectedMonthIndex));
                }
                monthBottomDialog.dismiss();
                getData();
            }
        });
        rl_income_month_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthBottomDialog.dismiss();
            }
        });
        monthBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
    }
}
