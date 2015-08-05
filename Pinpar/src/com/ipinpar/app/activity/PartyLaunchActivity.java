package com.ipinpar.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class PartyLaunchActivity extends PPBaseActivity{

	private Context mContext;
	private TextView tvPartyInvition;
	private Button btnBack,btnLaunch;
	private ImageView ivPartyLaunchRocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_launch);
		mContext=this;
		
		tvPartyInvition = (TextView) findViewById(R.id.tv_party_launch_invition);
		btnBack = (Button) findViewById(R.id.btn_party_launch_back);
		btnLaunch = (Button) findViewById(R.id.btn_party_launch);
		ivPartyLaunchRocket = (ImageView) findViewById(R.id.iv_party_launch_rocket);
		
		tvPartyInvition.setText(Html.fromHtml(
				PPApplication.getInstance().getFormatString(
						R.string.activity_party_invition)));
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		btnLaunch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message message=new Message();  
	            message.what=0;  
	            handlerStatrActivity.sendMessage(message);
			}
		});
		
        /** 
         * 这里设置的是setBackgroundResource，那么你获取的时候通过getBackground 
         */  
        
        ivPartyLaunchRocket.setImageResource(R.drawable.party_launch_rocket_animation);
		AnimationDrawable animationDrawable = (AnimationDrawable) ivPartyLaunchRocket.getDrawable();  
        animationDrawable.start();  
        
	}
	
	
	Handler handlerStatrActivity = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 0:
				
				ivPartyLaunchRocket.startAnimation(AnimationUtils.loadAnimation(PartyLaunchActivity.this,R.anim.push_up_out));
				ivPartyLaunchRocket.setVisibility(View.INVISIBLE);
				handlerStatrActivity.sendEmptyMessageDelayed(1, 800);
				
				break;
			case 1:
				
				Intent intent = new Intent();
				intent.setClass(mContext, PartyReadyToGetIdentityActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		}
		
	};
	
	
	
}
