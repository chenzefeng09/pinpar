package com.ipinpar.app.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class MainViewPager extends ViewPager{

	private boolean enabled;
	
	public MainViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MainViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.enabled = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		 if (this.enabled) {
			 return super.onTouchEvent(arg0);
	     }
	  
	     return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (this.enabled) {
            return super.onInterceptTouchEvent(arg0);
        }
        return false;
	}

	 public void setPagingEnabled(boolean enabled) {
	    this.enabled = enabled;
	 }
	
}
