package com.ipinpar.app.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Scroller;
import android.widget.Toast;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.adapter.MainPagerAdapter;
import com.ipinpar.app.fragment.DiscoverFragment;
import com.ipinpar.app.fragment.MeFragment;
import com.ipinpar.app.util.NetWorkState;

public class MainActivity extends PPBaseActivity {
	
	// 一个网络请求的例子
//	private LoginRequest request ;
	private Context mContext;
	private ViewPager container = null;
	private RadioGroup tabRadioGroup;
	private RadioButton  rdBtnDiscover,rdBtnMessage,rdBtnMe;
	
	private DiscoverFragment discoverFragment;
	private MeFragment meFragment;
	
	private List<PPBaseFragment> fragments;
	private View backView;
	
	private MainPagerAdapter mPagerAdapter;
	private FragmentManager fm; 
	
	private static boolean isExit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		
		checkNetWork();
		initWidgets();
		
		tabRadioGroup.check(R.id.btn_discover);
		
		
		
		
		
		
		
		backView = findViewById(R.id.backlayout);
		backView.setBackgroundColor(Color.WHITE);
		
		
		//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
		//一个网络请求例子的实现
		
//		request = new LoginRequest("15001225725", "chzf7229", new Listener<JSONObject>() {
//
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
//				Gson gson = new Gson();
//				UserEntity user = gson.fromJson(response.toString(), UserEntity.class);
//				if (user != null && user.getResult() == 1) {
//					//登录成功
//				}
//				else {
//					//登录失败
//				}
//			}
//		});
//		request.setTag(TAG);
//		apiQueue.add(request);
//		
		
		//一个网络请求例子的实现
		//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	private void initWidgets(){
		
		container = (ViewPager) findViewById(R.id.containerBody);
		try {
			 Field mScroller;
			    mScroller = ViewPager.class.getDeclaredField("mScroller");
			    mScroller.setAccessible(true); 
			    FixedSpeedScroller scroller = new FixedSpeedScroller(mContext, new DecelerateInterpolator());
			     scroller.setFixedDuration(400);
			    mScroller.set(container, scroller);
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (NoSuchFieldException e) {

			e.printStackTrace();
		}
		
		tabRadioGroup = (RadioGroup)findViewById(R.id.tab_radiogroup);
		
		rdBtnDiscover = (RadioButton) findViewById(R.id.btn_discover);
		rdBtnMessage = (RadioButton) findViewById(R.id.btn_message);
		rdBtnMe = (RadioButton) findViewById(R.id.btn_me);
		
		tabRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.btn_discover) {
					container.setCurrentItem(0, true);
				}
				else if (checkedId == R.id.btn_message) {
					container.setCurrentItem(1, true);
				} 
				else if (checkedId == R.id.btn_me) {
					container.setCurrentItem(2, true);
				} 
			}
		});
		
		fragments = new ArrayList<PPBaseFragment>();
		
		discoverFragment = new DiscoverFragment();
//		
//		
		meFragment = new MeFragment();
		fragments.add(discoverFragment);
		fragments.add(meFragment);
		
		fm = getSupportFragmentManager();
		mPagerAdapter = new MainPagerAdapter(fm, fragments);
		container.setAdapter(mPagerAdapter);
		container.setCurrentItem(0);
		container.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					tabRadioGroup.check(R.id.btn_discover);
					break;
				case 1:
					tabRadioGroup.check(R.id.btn_message);
					break;
				case 2:
					tabRadioGroup.check(R.id.btn_me);
					break;

				default:
					break;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void checkNetWork() {
		// TODO Auto-generated method stub
		int netState = NetWorkState.getAPNType();
		if (netState == 0) {
			AlertDialog alert = new AlertDialog.Builder(mContext).create();
			alert.setTitle(R.string.alert_title);
			alert.setMessage(getResources().getString(
					R.string.alert_net_content));
			alert.setIcon(android.R.drawable.ic_dialog_alert);
			alert.setButton(AlertDialog.BUTTON_POSITIVE, getResources()
					.getString(R.string.alert_btn_set),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setAction(android.provider.Settings.ACTION_SETTINGS);
							startActivityForResult(intent, 1);
						}
					});
			alert.setButton(AlertDialog.BUTTON_NEGATIVE, getResources()
					.getString(R.string.alert_btn_cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
						}
					});
			alert.show();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			exit();
			return false;
		}	else	{
		return super.onKeyDown(keyCode, event);
		}
	}
	
	public void exit(){
		if (!isExit){
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0,2000);
		} else {
			finish();
		}
	}
	
	Handler mHandler = new Handler()	{
		public void handleMessage(Message msg)	{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}
	};
	
	private void showAlertAndCancel(String msg,
			DialogInterface.OnClickListener ocl) {
		AlertDialog alert = new AlertDialog.Builder(mContext).create();
		alert.setTitle(R.string.alert_title);
		alert.setMessage(msg);
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setButton(AlertDialog.BUTTON_POSITIVE,
				getResources().getString(R.string.alert_btn_ok), ocl);
		alert.setButton(AlertDialog.BUTTON_NEGATIVE,
				getResources().getString(R.string.alert_btn_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alert.show();
	}
	
	public class FixedSpeedScroller extends Scroller {

	    private int mDuration = 800;

	    public FixedSpeedScroller(Context context) {
	        super(context);
	    }

	    public void setFixedDuration(int i) {
	    	mDuration = i;
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }
	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }
	}

}
