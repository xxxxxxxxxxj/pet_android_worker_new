package com.haotang.petworker;

import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.petworker.adapter.CommonIncomeAdapter;

import butterknife.OnClick;

/**
 * 作者：灼星
 * 时间：2020-04-20
 * 基本收入
 */
public class PrimaryIncomeActivity extends IncomeDetailNewActivity {
    private CommonIncomeAdapter commonIncomeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView(){
        //是基本收入才显示头布局
        contentTop.setVisibility(View.VISIBLE);
        tvGradeName.setText(data.getRole());
        tvYearNumber.setText(TextUtils.concat(String.valueOf(data.getWorkyear()),"年"));
    }
    private void initData(){
        if (data.getWorkerScheduleList() != null && data.getWorkerScheduleList().getScheduleDay() != null){
//            data.getWorkerScheduleList().getScheduleDay().add(new ScheduleDay("30",2,"事假"));
            commonIncomeAdapter = new CommonIncomeAdapter(R.layout.item_common_income,
                    data.getWorkerScheduleList().getScheduleDay());
            recyclerViewTop.setLayoutManager(new GridLayoutManager(this,2));
            recyclerViewTop.setAdapter(commonIncomeAdapter);
            if (data.getWorkerScheduleList().getScheduleDay().size() > 2){
                recyclerViewTop.post(new Runnable() {
                    @Override
                    public void run() {
                        downUpImageView.setVisibility(View.VISIBLE);
                        recyclerViewTop.setNestedScrollingEnabled(false);
                        showUp(true);
                    }
                });
            }
        }

    }

    private void showUp(boolean flag){
        ViewGroup.LayoutParams layoutParams = recyclerViewTop.getLayoutParams();
        if (flag){
            //收起状态
            layoutParams.height = commonIncomeAdapter.getChildHeight();
            downUpImageView.setImageResource(R.drawable.down_up_icon);
            downUpImageView.setTag(false);
            scrollTop();
        }else{
            //展开状态
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            downUpImageView.setImageResource(R.drawable.pack_up_icon);
            downUpImageView.setTag(true);
        }
        recyclerViewTop.setLayoutParams(layoutParams);
    }

    private void scrollTop(){
        CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
            if (topAndBottomOffset != 0) {
                appBarLayoutBehavior.setTopAndBottomOffset(0);
            }
        }
    }

    @OnClick({R.id.down_up_image_view})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.down_up_image_view:
                showUp((Boolean) downUpImageView.getTag());
                break;
            default:
                break;
        }
    }
}
