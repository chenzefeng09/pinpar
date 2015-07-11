package com.ipinpar.app.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.db.dao.UserDao;
import com.ipinpar.app.entity.UserEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.LoginRequest;
import com.ipinpar.app.util.MD5Util;

public class LoginActivity extends PPBaseActivity implements OnClickListener{
	
	public static final String TAG = "LoginActivity";
	
	private EditText et_user_phone,et_pwd;
	private Button btn_regist,btn_login;
	private TextView tv_read_protocol;
	private ImageView ib_left;
	private LoginRequest request;
	
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		et_user_phone = (EditText) findViewById(R.id.et_user_phone);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_read_protocol = (TextView) findViewById(R.id.tv_read_protocol);
		ib_left = (ImageView) findViewById(R.id.ib_left);
		ib_left.setOnClickListener(this);
		btn_regist.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		tv_read_protocol.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_read_protocol:
			
			break;
		case R.id.ib_left:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btn_regist:
			//
			startActivity(new Intent(mContext, RegistActivity.class));
			break;
		case R.id.btn_login:
			showProgressDialog();
			String phone = et_user_phone.getText().toString().trim();
			final String pwd = et_pwd.getText().toString().trim();
			if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd)) {
				request = new LoginRequest(phone, MD5Util.MD5(pwd), new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									dissmissProgressDialog();
									// TODO Auto-generated method stub
									Gson gson = new Gson();
									UserEntity user = gson.fromJson(response.toString(), UserEntity.class);
									if (user != null && user.getResult() == 1) {
										//登录成功
										user.setPassword(MD5Util.MD5(pwd));
										UserDao.getInstance().insertUser(user);
										UserManager.getInstance().setUserInfo(user);
										Toast.makeText(mContext, "登录成功", 1000).show();
										setResult(RESULT_OK);
										finish();
									}
									else {
										//登录失败
										Toast.makeText(mContext, "登录失败", 1000).show();

									}
								}
							});
							request.setTag(TAG);
							apiQueue.add(request);
			}
			else {
				Toast.makeText(mContext, "手机号或密码不能为空", 1000).show();

			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(RESULT_CANCELED);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		apiQueue.cancelAll(TAG);
	}

}
