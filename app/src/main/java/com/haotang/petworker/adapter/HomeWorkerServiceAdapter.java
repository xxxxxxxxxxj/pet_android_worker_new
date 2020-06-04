package com.haotang.petworker.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.HomeWorkerServiceList;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.CommonAdapter;
import com.haotang.petworker.view.ViewHolder;

/**
 * <p>
 * Title:ServiceTimeChoosePetAdapter
 * </p>
 * <p>
 * Description:上门服务时长适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-5-16 上午11:31:22
 */
public class HomeWorkerServiceAdapter<T> extends CommonAdapter<T> {
	public HomeWorkerServiceAdapter(Context mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	public OnHomeAddListener onHomeAddListener = null;

	public interface OnHomeAddListener {
		public void OnHomeAdd(int position);
	}

	public void setOnHomeAddListener(OnHomeAddListener onHomeAddListener) {
		this.onHomeAddListener = onHomeAddListener;
	}

	public OnHomeMinusListener onHomeMinusListener = null;

	public interface OnHomeMinusListener {
		public void OnHomeMinus(int position);
	}

	public void setOnHomeMinusListener(OnHomeMinusListener onHomeMinusListener) {
		this.onHomeMinusListener = onHomeMinusListener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_servicetime_adjustment, position);
		TextView tv_item_servicetime_adjustment_name = viewHolder
				.getView(R.id.tv_item_servicetime_adjustment_name);
		TextView tv_item_servicetime_adjustment_ysc = viewHolder
				.getView(R.id.tv_item_servicetime_adjustment_ysc);
		ImageView iv_item_servicetime_adjustment_jian = viewHolder
				.getView(R.id.iv_item_servicetime_adjustment_jian);
		TextView tv_item_servicetime_adjustment_xgsj = viewHolder
				.getView(R.id.tv_item_servicetime_adjustment_xgsj);
		ImageView iv_item_servicetime_adjustment_jia = viewHolder
				.getView(R.id.iv_item_servicetime_adjustment_jia);
		TextView tv_item_servicetime_adjustment_sjsc = viewHolder
				.getView(R.id.tv_item_servicetime_adjustment_sjsc);
		HomeWorkerServiceList homeWorkerServiceList = (HomeWorkerServiceList) mDatas
				.get(position);
		if (homeWorkerServiceList != null) {
			Utils.setText(tv_item_servicetime_adjustment_name,
					homeWorkerServiceList.name, "", View.VISIBLE, View.GONE);
			Utils.setText(tv_item_servicetime_adjustment_ysc,
					homeWorkerServiceList.baseHomeDuration + "", "",
					View.VISIBLE, View.GONE);
			Utils.setText(
					tv_item_servicetime_adjustment_sjsc,
					(homeWorkerServiceList.baseHomeDuration + homeWorkerServiceList.homeDuration)
							+ "", "", View.VISIBLE, View.GONE);
			Utils.setText(tv_item_servicetime_adjustment_xgsj,
					homeWorkerServiceList.homeDuration + "", "", View.VISIBLE,
					View.GONE);
			iv_item_servicetime_adjustment_jia
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onHomeAddListener.OnHomeAdd(position);
						}
					});
			iv_item_servicetime_adjustment_jian
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onHomeMinusListener.OnHomeMinus(position);
						}
					});
		}
		return viewHolder.getConvertView();
	}
}
