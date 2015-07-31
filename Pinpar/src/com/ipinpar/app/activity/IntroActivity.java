package com.ipinpar.app.activity;

import github.chenupt.springindicator.SpringIndicator;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.util.DisplayUtil;
import com.ipinpar.app.util.PreferenceUtils;
import com.ipinpar.app.util.VersionUtils;

public class IntroActivity extends PPBaseActivity implements OnPageChangeListener {
	private ViewPager viewpager;
    private List<View> views;  
    private IntroAdapter vpAdapter;  
    private SpringIndicator indicator;
	 //引导图片资源  
    private static final int[] pics = { R.drawable.intro_1,  
            R.drawable.intro_2, R.drawable.intro_3 };  
      
    //底部小店图片  
    private ImageView[] dots ;  
      
    //记录当前选中位置  
    private int currentIndex;  
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_intro);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		indicator = (SpringIndicator) findViewById(R.id.indicator);
		views = new ArrayList<View>();  
        
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT);  
          
        //初始化引导图片列表  
        for(int i=0; i<pics.length; i++) {  
            ImageView iv = new ImageView(this);  
            iv.setLayoutParams(mParams);  
            iv.setImageResource(pics[i]);  
            iv.setScaleType(ScaleType.CENTER_CROP);
            views.add(iv);  
        }  
        //初始化Adapter  
        vpAdapter = new IntroAdapter(views);  
        viewpager.setAdapter(vpAdapter);  
        //绑定回调  
        viewpager.setOnPageChangeListener(this);  
         
        viewpager.setOnTouchListener(new OnTouchListener() {
			float downx,downy;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
				
				case MotionEvent.ACTION_DOWN:
					downx = event.getRawX();
					downy = event.getRawY();
					break;
					
				case MotionEvent.ACTION_MOVE:
					if (viewpager.getCurrentItem() == 2 && downx - event.getRawX() > DisplayUtil.dip2px(80)) {
						PreferenceUtils.setPrefBoolean(mContext, "show_intro", false);
						PreferenceUtils.setPrefInt(mContext, "curr_version_code", VersionUtils.getCurrentVersionCode(mContext));
						startActivity(new Intent(mContext, MainActivity.class));
//						overridePendingTransition(0, R.anim.intro_fade_out);
						finish();

					}
					break;
				}
				
				return false;
			}
		});
		indicator.setViewPager(viewpager);

        //初始化底部小点  
//        initDots();  
		
	}

	private class IntroAdapter extends PagerAdapter{
		
		 //界面列表  
	    private List<View> views;  
	      
	    public IntroAdapter (List<View> views){  
	        this.views = views;  
	    }  
	  
	    //销毁arg1位置的界面  
	    @Override  
	    public void destroyItem(View arg0, int arg1, Object arg2) {  
	        ((ViewPager) arg0).removeView(views.get(arg1));       
	    }  
	  
	    @Override  
	    public void finishUpdate(View arg0) {  
	        // TODO Auto-generated method stub  
	          
	    }  
	  
	    //获得当前界面数  
	    @Override  
	    public int getCount() {  
	        if (views != null)  
	        {  
	            return views.size();  
	        }  
	          
	        return 0;  
	    }  
	      
	  
	    //初始化arg1位置的界面  
	    @Override  
	    public Object instantiateItem(View arg0, int arg1) {  
	          
	        ((ViewPager) arg0).addView(views.get(arg1), 0);  
	          
	        return views.get(arg1);  
	    }  
	  
	    //判断是否由对象生成界面  
	    @Override  
	    public boolean isViewFromObject(View arg0, Object arg1) {  
	        return (arg0 == arg1);  
	    }  
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		
	}
	
	
}
