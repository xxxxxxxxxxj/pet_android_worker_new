package com.haotang.petworker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 解决scrollview和其他滑动控件有冲突时的问题，主要用于嵌套webview
 * 
 * @author Administrator
 * 
 */
public class MScrollView extends ScrollView {
	private GestureDetector mGestureDetector;

	public MScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
		setFadingEdgeLength(0);
	}

	public MScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
		setFadingEdgeLength(0);
	}

	public MScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
		setFadingEdgeLength(0);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	// Return false if we're scrolling in the x direction
	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceY) > Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}

}
