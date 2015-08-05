package com.ipinpar.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class ExperienceFlow extends PPBaseActivity{
	private Context mContext;
	private Button btnBack;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experience_flow);
		mContext = this;
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("onClick", "");
				onBackPressed();
			}
		});
	}
	
	
}
