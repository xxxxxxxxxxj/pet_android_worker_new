package com.haotang.petworker.adapter;

import java.util.List;

import com.haotang.petworker.R;
import com.haotang.petworker.entity.Notice;
import com.haotang.petworker.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoticeAdapter extends BaseAdapter {

	private Context context;
	private List<Notice> list;

	public NoticeAdapter(Context context) {
		this.context = context;
	}

	public NoticeAdapter(Context context, List<Notice> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.notice_adapter, null);
			mHolder.item_notice_title = (TextView) convertView
					.findViewById(R.id.item_notice_title);
			mHolder.item_notice_time = (TextView) convertView
					.findViewById(R.id.item_notice_time);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		Utils.mLogError("==-->notice adapter   title   "
				+ list.get(position).title + "  created   "
				+ list.get(position).created);
		mHolder.item_notice_title.setText(list.get(position).title);
		mHolder.item_notice_time.setText(list.get(position).created);
		return convertView;
	}

	class ViewHolder {
		TextView item_notice_title;
		TextView item_notice_time;
	}
}
