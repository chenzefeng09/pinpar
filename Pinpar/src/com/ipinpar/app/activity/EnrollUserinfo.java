package com.ipinpar.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class EnrollUserinfo extends PPBaseActivity{

	private Context mContext;
	
	private Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_userinfo);
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
		btnSubmit = (Button) findViewById(R.id.btn_submit);
	}
	
	public void setView(){
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, EnrollSuccess.class);
				startActivity(intent);
			}
		});
	}
	
}
