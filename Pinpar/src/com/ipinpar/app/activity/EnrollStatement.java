package com.ipinpar.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class EnrollStatement extends PPBaseActivity{

	private Context mContext;
	
	private Button btnNext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_statement);
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
		btnNext = (Button) findViewById(R.id.btn_next);
	}
	
	public void setView(){
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, EnrollUserinfo.class);
				startActivity(intent);
			}
		});
	}
}
