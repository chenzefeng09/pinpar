package com.ipinpar.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class EnrollSuccess extends PPBaseActivity{

	private Context mContext;
	
	private RelativeLayout rlEnrollOk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_success);
		mContext = this;
		
		findView();
		
		setView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
	public void findView(){
		rlEnrollOk =  (RelativeLayout) findViewById(R.id.rl_enroll_success_ok);
	}
	
	public void setView(){
		rlEnrollOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});
	}
	
	
}
