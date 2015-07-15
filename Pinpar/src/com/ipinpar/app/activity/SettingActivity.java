package com.ipinpar.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.manager.UserManager;

public class SettingActivity extends PPBaseActivity implements OnClickListener{
	private TextView tv_change_pwd,et_sugest,tv_invite,tv_update;
	private Button btn_logout;
	private ImageView ib_left;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting);
		tv_change_pwd = (TextView) findViewById(R.id.tv_change_pwd);
		et_sugest = (TextView) findViewById(R.id.et_sugest);
		tv_invite = (TextView) findViewById(R.id.tv_invite);
		tv_update = (TextView) findViewById(R.id.tv_update);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		ib_left = (ImageView) findViewById(R.id.ib_left);
		tv_change_pwd.setOnClickListener(this);
		et_sugest.setOnClickListener(this);
		tv_invite.setOnClickListener(this);
		tv_update.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		ib_left.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!UserManager.getInstance().isLogin()) {
			btn_logout.setText("登录");
			tv_invite.setVisibility(View.GONE);
			tv_change_pwd.setVisibility(View.GONE);
		}
		else {
			btn_logout.setText("退出当前帐号");
			tv_invite.setVisibility(View.VISIBLE);
			tv_change_pwd.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_left:
			finish();
			break;
		case R.id.tv_change_pwd:
			
			break;
		case R.id.et_sugest:
			
			break;
		case R.id.tv_invite:
			
			break;
		case R.id.tv_update:
			
			break;
		case R.id.btn_logout:
			if (UserManager.getInstance().isLogin()) {
				UserManager.getInstance().logOut();
				btn_logout.setText("登录");
				tv_invite.setVisibility(View.GONE);
				tv_change_pwd.setVisibility(View.GONE);
				startActivity(new Intent(mContext, MainActivity.class));
			}
			else {
				startActivity(new Intent(mContext, LoginActivity.class));
			}
			break;

		default:
			break;
		}
	}

}
