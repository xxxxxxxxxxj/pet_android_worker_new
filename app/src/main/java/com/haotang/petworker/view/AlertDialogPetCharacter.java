package com.haotang.petworker.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.CareTag;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogPetCharacter {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView tv_characterdialog_submit;
    private Display display;
    private TagFlowLayout tfl_characterdialog;
    private List<CareTag> characterList = new ArrayList<CareTag>();

    public AlertDialogPetCharacter(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialogPetCharacter builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialogpetcharacter, null);
        // 获取自定义Dialog布局中的控件
        tfl_characterdialog = (TagFlowLayout) view
                .findViewById(R.id.tfl_characterdialog);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        tv_characterdialog_submit = (TextView) view.findViewById(R.id.tv_characterdialog_submit);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
        tv_characterdialog_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(characterList);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogPetCharacter setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public AlertDialogPetCharacter setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public AlertDialogPetCharacter setData(final List<CareTag> localcharacterList) {
        characterList.clear();
        characterList.addAll(localcharacterList);
        TagAdapter<CareTag> tagAdapter = new TagAdapter<CareTag>(characterList) {
            @Override
            public View getView(FlowLayout parent, final int position, CareTag s) {
                View view = (View) View.inflate(context, R.layout.item_petcharacter,
                        null);
                TextView tv_item_workerlist_bq = (TextView) view.findViewById(R.id.tv_item_petcharacter);
                tv_item_workerlist_bq.setText(characterList.get(position).getTag());
                if (characterList.get(position).isSelect()) {
                    tv_item_workerlist_bq.setBackgroundResource(R.drawable.bg_ff3a1e_round);
                    tv_item_workerlist_bq.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    tv_item_workerlist_bq.setBackgroundResource(R.drawable.bg_round_717985order);
                    tv_item_workerlist_bq.setTextColor(context.getResources().getColor(R.color.a717985));
                }
                tv_item_workerlist_bq.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        characterList.get(position).setSelect(!characterList.get(position).isSelect());
                        notifyDataChanged();
                    }
                });
                return view;
            }
        };
        tfl_characterdialog.setAdapter(tagAdapter);
        return this;
    }
}
