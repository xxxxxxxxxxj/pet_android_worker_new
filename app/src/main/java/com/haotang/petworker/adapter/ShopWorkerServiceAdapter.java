package com.haotang.petworker.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.ShopWorkerServiceList;
import com.haotang.petworker.utils.Utils;
import com.haotang.petworker.view.CommonAdapter;
import com.haotang.petworker.view.ViewHolder;

/**
 * <p>
 * Title:ServiceTimeChoosePetAdapter
 * </p>
 * <p>
 * Description:到店服务时长适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-5-16 上午11:31:22
 */
public class ShopWorkerServiceAdapter<T> extends CommonAdapter<T> {

	public ShopWorkerServiceAdapter(Context mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	public OnShopAddListener onShopAddListener = null;

	public interface OnShopAddListener {
		public void OnShopAdd(int position);
	}

	public void setOnShopAddListener(OnShopAddListener onShopAddListener) {
		this.onShopAddListener = onShopAddListener;
	}

	public OnShopMinusListener onShopMinusListener = null;

	public interface OnShopMinusListener {
		public void OnShopMinus(int position);
	}

	public void setOnShopMinusListener(OnShopMinusListener onShopMinusListener) {
		this.onShopMinusListener = onShopMinusListener;
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
		ShopWorkerServiceList shopWorkerServiceList = (ShopWorkerServiceList) mDatas
				.get(position);
		if (shopWorkerServiceList != null) {
			Utils.setText(tv_item_servicetime_adjustment_name,
					shopWorkerServiceList.name, "", View.VISIBLE, View.GONE);
			Utils.setText(tv_item_servicetime_adjustment_ysc,
					shopWorkerServiceList.baseShopDuration + "", "",
					View.VISIBLE, View.GONE);
			Utils.setText(
					tv_item_servicetime_adjustment_sjsc,
					(shopWorkerServiceList.baseShopDuration + shopWorkerServiceList.shopDuration)
							+ "", "", View.VISIBLE, View.GONE);
			Utils.setText(tv_item_servicetime_adjustment_xgsj,
					shopWorkerServiceList.shopDuration + "", "", View.VISIBLE,
					View.GONE);
			iv_item_servicetime_adjustment_jia
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onShopAddListener.OnShopAdd(position);
						}
					});
			iv_item_servicetime_adjustment_jian
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							onShopMinusListener.OnShopMinus(position);
						}
					});
		}
		return viewHolder.getConvertView();
	}
}
