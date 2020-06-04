package com.haotang.petworker;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.haotang.petworker.adapter.IncomeMoreAdapter;
import com.haotang.petworker.adapter.IncomeTipAdapter;
import com.haotang.petworker.entity.NewIncome;
import com.haotang.petworker.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author:姜谷蓄
 * @Description:薪资详情页
 */
public class IncomeDetailNewActivity extends SuperActivity {


    @BindView(R.id.ib_titlebar_other)
    ImageButton ibTitlebarOther;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.ib_titlebar_back)
    ImageButton ibTitlebarBack;
    @BindView(R.id.rv_income_detail)
    RecyclerView rvIncomeDetail;
    @BindView(R.id.tv_income_detail_money)
    TextView tvIncomeDetailMoney;
    @BindView(R.id.tv_income_detail_data)
    TextView tvIncomeDetailData;
    @BindView(R.id.tv_income_include)
    TextView tvIncomeInclude;
    @BindView(R.id.bt_titlebar_other)
    Button btTitlebarOther;
    @BindView(R.id.rl_commodity_black)
    RelativeLayout rl_commodity_black;
    @BindView(R.id.vw_titlebar_top)
    View vwTitlebarTop;
    @BindView(R.id.rl_income_detail_money)
    RelativeLayout rlIncomeDetailMoney;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_grade_name)
    TextView tvGradeName;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_year_number)
    TextView tvYearNumber;
    @BindView(R.id.recycler_view_top)
    RecyclerView recyclerViewTop;
    @BindView(R.id.content_top)
    LinearLayout contentTop;
    @BindView(R.id.head_root)
    RelativeLayout headRoot;
    protected IncomeTipAdapter tipAdapter;
    protected int position;
    protected String selectMonth;
    protected String incomeJzrq;
    protected Integer bgColor;
    protected NewIncome.DataBean.ListBeanXX data = new NewIncome.DataBean.ListBeanXX();
    protected List<NewIncome.DataBean.ListBeanXX.ListBeanX.ListBean> moreIncome = new ArrayList<>();
    @BindView(R.id.down_up_image_view)
    ImageView downUpImageView;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setView();
        initWindows();
        setListener();
    }


    protected void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(android.R.color.transparent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "1");
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("TAG", "2");
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    private void initData() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        data = (NewIncome.DataBean.ListBeanXX) intent.getSerializableExtra("data");
        incomeJzrq = intent.getStringExtra("incomeJzrq");
        selectMonth = intent.getStringExtra("selectMonth");
        //判断设置颜色
        switch (position) {
            case 1:
                bgColor = R.drawable.bg_round1;
                break;
            case 2:
                bgColor = R.drawable.bg_round2;
                break;
            case 3:
                bgColor = R.drawable.bg_round3;
                break;
            case 0:
                bgColor = R.drawable.bg_round4;
                break;
        }

    }


    private void setView() {
        setContentView(R.layout.activity_income_detail_new);
        ButterKnife.bind(this);
        tipAdapter = new IncomeTipAdapter(IncomeDetailNewActivity.this, bgColor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(IncomeDetailNewActivity.this);
        rvIncomeDetail.setLayoutManager(linearLayoutManager);
        rvIncomeDetail.setAdapter(tipAdapter);
        tvTitlebarTitle.setText(data.getName());
        tvIncomeDetailMoney.setText(data.getAmount());
        tvIncomeDetailData.setText(incomeJzrq);
        if (data.getList() != null && data.getList().size() > 0) {
            tvIncomeInclude.setText("包含" + data.getList().size() + "项");
            tipAdapter.setList(data.getList());
        }
        if (data.getCalculateExplain() != null && !("").equals(data.getCalculateExplain())) {
            btTitlebarOther.setVisibility(View.VISIBLE);
            btTitlebarOther.setText("说明");
        }
        //是基本收入才显示头布局
        if (position == 1) {
            contentTop.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {
        tipAdapter.setListener(new IncomeTipAdapter.ItemClickListener() {
            @Override
            public void onItemClick(NewIncome.DataBean.ListBeanXX.ListBeanX data) {
                showIncomePop(data);
            }

            @Override
            public void onDetailClick(NewIncome.DataBean.ListBeanXX.ListBeanX data) {
                Intent intent = new Intent(IncomeDetailNewActivity.this,ServiceOrderDetailActivity.class);
                intent.putExtra("data",selectMonth);
                startActivity(intent);
            }
        });
    }

    private void showIncomePop(NewIncome.DataBean.ListBeanXX.ListBeanX data) {
        rl_commodity_black.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.commodity_detail_show));//开始动画
        rl_commodity_black.setVisibility(View.VISIBLE);
        rl_commodity_black.bringToFront();
        moreIncome.clear();
        View customView = View.inflate(mContext, R.layout.pop_income_detail, null);
        TextView tvPopColor = customView.findViewById(R.id.tv_pop_income_color);
        TextView tvPopTitle = customView.findViewById(R.id.tv_pop_income_title);
        TextView tvPopIncome = customView.findViewById(R.id.tv_income_popmoney);
        RecyclerView rvPopRecy = customView.findViewById(R.id.rv_pop_income);
        TextView tvPopKnow = customView.findViewById(R.id.tv_pop_income_know);
        RelativeLayout rlDismiss = customView.findViewById(R.id.rl_pop_dissmiss);

        //动态设置高度
        ViewGroup.LayoutParams layoutParams = rlDismiss.getLayoutParams();
        int[] location = new int[2];
        headRoot.getLocationOnScreen(location);
        layoutParams.height = headRoot.getHeight()+location[1] - getStatusBarHeight(this);
        rlDismiss.setLayoutParams(layoutParams);
        tvPopColor.setBackgroundResource(bgColor);
        tvPopTitle.setText(data.getName());
        tvPopIncome.setText(data.getAmount());
        LinearLayoutManager layoutManager = new LinearLayoutManager(IncomeDetailNewActivity.this);
        IncomeMoreAdapter incomeMoreAdapter = new IncomeMoreAdapter(mActivity);
        rvPopRecy.setLayoutManager(layoutManager);
        rvPopRecy.setAdapter(incomeMoreAdapter);
        rvPopRecy.setLayoutManager(layoutManager);
        if (data.getList() != null && data.getList().size() > 0) {
            moreIncome.addAll(data.getList());
            incomeMoreAdapter.setList(moreIncome);
        }
        PopupWindow pWinBottomDialog = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pWinBottomDialog.setFocusable(true);// 取得焦点 
        pWinBottomDialog.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失 
        pWinBottomDialog.setOutsideTouchable(true);
        //设置可以点击 
        pWinBottomDialog.setTouchable(true);
        //进入退出的动画 
        pWinBottomDialog.setAnimationStyle(R.style.mypopwindow_anim_style);
        pWinBottomDialog.setWidth(Utils.getDisplayMetrics(IncomeDetailNewActivity.this)[0]);
        pWinBottomDialog.showAtLocation(customView, Gravity.BOTTOM, 0, 0);
        pWinBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rl_commodity_black.setVisibility(View.GONE);
            }
        });
        tvPopKnow.setOnClickListener(view -> {
            pWinBottomDialog.dismiss();
        });
        rlDismiss.setOnClickListener(view -> pWinBottomDialog.dismiss());
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


    @OnClick({R.id.ib_titlebar_back, R.id.ib_titlebar_other, R.id.bt_titlebar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.bt_titlebar_other:
                Intent intent = new Intent(IncomeDetailNewActivity.this, CommissionExplainActivity.class);
                intent.putExtra("explain", data.getCalculateExplain());
                intent.putExtra("icon", data.getRoyaltyPic());
                startActivity(intent);
                break;
        }
    }
}
