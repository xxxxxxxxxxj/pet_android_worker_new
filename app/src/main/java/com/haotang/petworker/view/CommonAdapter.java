package com.haotang.petworker.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected int index;
	public void clearDeviceList() {
		if (mDatas != null) {
			mDatas.clear();
		}
		notifyDataSetChanged();
	}
	public CommonAdapter(Context mContext, List<T> mDatas) {
		this.mContext = mContext;
		this.mDatas = mDatas;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setData(List<T> mDatas) {
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setData(List<T> mDatas, int index) {
		this.mDatas = mDatas;
		this.index = index;
	}

}
