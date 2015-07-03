package com.ipinpar.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class UserInfoEditActivity extends PPBaseActivity implements OnClickListener {
	
	private ImageView ib_left,iv_icon,iv_add_intrest;
	private TextView tv_name,tv_sex,tv_qianming;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit_user);
		ib_left = (ImageView) findViewById(R.id.ib_left);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_add_intrest = (ImageView) findViewById(R.id.iv_add_intrest);
		
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_qianming = (TextView) findViewById(R.id.tv_qianming);
		
		ib_left.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		iv_add_intrest.setOnClickListener(this);
		tv_name.setOnClickListener(this);
		tv_sex.setOnClickListener(this);
		tv_qianming.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_left:
			finish();
			break;
		case R.id.iv_icon:
			break;
		case R.id.iv_add_intrest:
			break;
		case R.id.tv_name:
			break;
		case R.id.tv_sex:
			break;
		case R.id.tv_qianming:
			break;

		default:
			break;
		}
	}

}
