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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.widget.PartyHomeVenueDialog;

public class PartyReadyToGetIdentityActivity extends PPBaseActivity{

	private Context mContext;
	private TextView tvPartyInvition;
	private Button btnBack;
	private ImageView ivPartyLaunchRocket;
	private RelativeLayout rlPartyLaunch;
	private RelativeLayout rlPartyLaunchRocketTip;
	private AnimationDrawable AniDraw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_ready_to_get_identity);
		mContext=this;
		
		tvPartyInvition = (TextView) findViewById(R.id.tv_party_ready_to_get_identity);
		btnBack = (Button) findViewById(R.id.btn_party_ready_to_get_identity_back);
		ivPartyLaunchRocket = (ImageView) findViewById(R.id.iv_party_ready_to_get_identity_rocket);
		rlPartyLaunch = (RelativeLayout) findViewById(R.id.RL_ready_to_get_identity);
		rlPartyLaunchRocketTip = (RelativeLayout) findViewById(R.id.RL_ready_to_get_identity_click);
		
		tvPartyInvition.setText(Html.fromHtml(
				PPApplication.getInstance().getFormatString(
						R.string.activity_party_ready_to_get_identity)));
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		ivPartyLaunchRocket.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				handlerStatrActivity.sendEmptyMessage(0);
				
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
				rlPartyLaunchRocketTip.setVisibility(View.INVISIBLE);
				rlPartyLaunch.startAnimation(AnimationUtils.loadAnimation(PartyReadyToGetIdentityActivity.this,R.anim.push_up_out));
				rlPartyLaunch.setVisibility(View.INVISIBLE);
				handlerStatrActivity.sendEmptyMessageDelayed(1, 800);
				
				break;
			case 1:
				
				Intent intent = new Intent();
				intent.setClass(mContext, PartyGetIdentityActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		}
		
	};
	
}
