package com.ipinpar.app.activity;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.UserEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.LoginRequest;
import com.ipinpar.app.network.api.RegistRequest;
import com.ipinpar.app.util.MD5Util;

public class RegistStep3Activity extends PPBaseActivity implements OnClickListener{
	
	private ImageView iv_icon;
	private ImageView iv_male,iv_female;
	private EditText et_nick_name;
	private Button btn_finish_regit;
	
	private String phone,pwd,email,nickname;
	private int sex;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_complete_uinfo);
		phone = getIntent().getStringExtra("phone");
		pwd = getIntent().getStringExtra("pwd");
		email = getIntent().getStringExtra("email");
		
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_male = (ImageView) findViewById(R.id.iv_male);
		iv_female = (ImageView) findViewById(R.id.iv_female);
		et_nick_name = (EditText) findViewById(R.id.et_nick_name);
		btn_finish_regit = (Button) findViewById(R.id.btn_finish_regit);
		iv_icon.setOnClickListener(this);
		iv_male.setOnClickListener(this);
		iv_female.setOnClickListener(this);
		btn_finish_regit.setOnClickListener(this);
	}
	
	public static Intent getIntent2Me(Context context,String phone,String pwd,String email){
		Intent intent = new Intent(context, RegistStep3Activity.class);
		intent.putExtra("phone", phone);
		intent.putExtra("pwd", pwd);
		intent.putExtra("email", email);
		return intent;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_icon:
			
			break;
		case R.id.iv_male:
			sex = 1;
			iv_male.setBackgroundColor(0xff8763ed);
			iv_male.setImageResource(R.drawable.log_male);
			iv_female.setBackgroundColor(0xffdcdcdc);
			iv_female.setImageResource(R.drawable.log_femailselected);

			break;
		case R.id.iv_female:
			iv_male.setBackgroundColor(0xffdcdcdc);
			iv_male.setImageResource(R.drawable.log_maleselected);
			iv_female.setBackgroundColor(0xff8763ed);
			iv_female.setImageResource(R.drawable.log_female);

			sex = 0;
			break;
		case R.id.btn_finish_regit:
			showProgressDialog();
			nickname = et_nick_name.getText().toString().trim();
			if (TextUtils.isEmpty(nickname)) {
				Toast.makeText(mContext, "昵称不能为空", 1000).show();
				return;
			}
			if (nickname.length() < 2 ||
					et_nick_name.getText().toString().trim().length() >20) {
				Toast.makeText(mContext, "昵称长度为2-20", 1000).show();
				return;
			}
			RegistRequest registRequest;
			try {
				registRequest = new RegistRequest(phone, pwd, nickname, email, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						dissmissProgressDialog();
						Gson gson = new Gson();
						UserEntity userEntity = gson.fromJson(response.toString(), UserEntity.class);
						if (userEntity != null && userEntity.getResult() == 1) {
							Toast.makeText(mContext, "注册成功，欢迎来到品趴！", 1000).show();
						      try {
						         // 调用sdk注册方法
						         EMChatManager.getInstance().createAccountOnServer(userEntity.getUid()+"", MD5Util.MD5(userEntity.getUid()+"pinpa"));
						      } catch (final Exception e) {
									e.printStackTrace();

						      }
							UserManager.getInstance().setUserInfo(userEntity);
							startActivity(new Intent(mContext, MainActivity.class));
							finish();
						}
						else {
							Toast.makeText(mContext, "注册失败，请重试"+userEntity.getResult(), 1000).show();
						}
					}
				});
				apiQueue.add(registRequest);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
}
