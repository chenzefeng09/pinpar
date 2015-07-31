package com.ipinpar.app.activity;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ipinpar.app.network.api.RegistRequest;
import com.ipinpar.app.util.MD5Util;

public class RegistStep3Activity extends PPBaseActivity implements OnClickListener{
	
	private EditText et_nick_name,et_email,et_pwd,et_pwd_confirm;
	
	private Button btn_finish_regit;
	
	private String phone,pwd1,pwd2,email,nickname;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_complete_uinfo);
		phone = getIntent().getStringExtra("phone");

		et_nick_name = (EditText) findViewById(R.id.et_nick_name);
		et_pwd = (EditText) findViewById(R.id.et_complete_pwd);
		et_pwd_confirm = (EditText) findViewById(R.id.et_complete_pwd_confirm);
		et_email = (EditText) findViewById(R.id.et_complete_email);
		btn_finish_regit = (Button) findViewById(R.id.btn_submit);
		
		btn_finish_regit.setOnClickListener(this);
	}
	
	public static Intent getIntent2Me(Context context,String phone){
		Intent intent = new Intent(context, RegistStep3Activity.class);
		intent.putExtra("phone", phone);
		return intent;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			showProgressDialog();
			
			if(checkOk()){
				if(TextUtils.isEmpty(email)){
					new AlertDialog.Builder(this).setTitle("提示")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("邮箱是我们与您取得联系的重要方式，强烈建议您填写")
					.setPositiveButton("好吧，我填", new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).setNegativeButton("就不填", new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							RegistRequest registRequest;
							try {
								registRequest = new RegistRequest(phone, pwd1, nickname, new Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										// TODO Auto-generated method stub
										dissmissProgressDialog();
										Gson gson = new Gson();
										final UserEntity userEntity = gson.fromJson(response.toString(), UserEntity.class);
										if (userEntity != null && userEntity.getResult() == 1) {
											Toast.makeText(mContext, "注册成功，欢迎来到品趴！", 1000).show();
											new Thread(new Runnable() {
												
												@Override
												public void run() {
													// TODO Auto-generated method stub
													try {
												         // 调用sdk注册方法
												         EMChatManager.getInstance().createAccountOnServer(userEntity.getUid()+"", MD5Util.MD5(userEntity.getUid()+"pinpa"));
												      } catch (final Exception e) {
															e.printStackTrace();

												      }
												}
											}).start();;
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
						}
					}).show();
				}else{
					RegistRequest registRequest2;
					try {
						registRequest2 = new RegistRequest(phone, pwd1, nickname, email, new Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								dissmissProgressDialog();
								Gson gson = new Gson();
								final UserEntity userEntity = gson.fromJson(response.toString(), UserEntity.class);
								if (userEntity != null && userEntity.getResult() == 1) {
									Toast.makeText(mContext, "注册成功，欢迎来到品趴！", 1000).show();
									new Thread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											try {
										         // 调用sdk注册方法
										         EMChatManager.getInstance().createAccountOnServer(userEntity.getUid()+"", MD5Util.MD5(userEntity.getUid()+"pinpa"));
										      } catch (final Exception e) {
													e.printStackTrace();

										      }
										}
									}).start();;
									UserManager.getInstance().setUserInfo(userEntity);
									startActivity(new Intent(mContext, MainActivity.class));
									finish();
								}
								else {
									Toast.makeText(mContext, "注册失败，请重试"+userEntity.getResult(), 1000).show();
								}
							}
						});
						apiQueue.add(registRequest2);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}else{
				dissmissProgressDialog();
			}
			
			break;

		default:
			break;
		}
	}
	
	private boolean checkOk() {
		// TODO Auto-generated method stub
		
		nickname = et_nick_name.getText().toString().trim();
		pwd1 = et_pwd.getText().toString().trim();
		pwd2 = et_pwd_confirm.getText().toString().trim();
		email = et_email.getText().toString().trim();
		if (TextUtils.isEmpty(nickname)) {
			Toast.makeText(mContext, "昵称不能为空", 1000).show();
		}
		else if (nickname.length() < 2 ||
				et_nick_name.getText().toString().trim().length() >20) {
			Toast.makeText(mContext, "昵称长度为2-20", 1000).show();
		}
		else if (TextUtils.isEmpty(pwd1)) {
			Toast.makeText(mContext, "请输入密码", 1000).show();
		}
		else if (pwd1.length() < 8) {
			Toast.makeText(mContext, "密码过短，请输入8位密码", 1000).show();
		}
		else if (TextUtils.isEmpty(pwd2)) {
			Toast.makeText(mContext, "请输入确认密码", 1000).show();
		}
		else if (!pwd2.equals(pwd1)) {
			Toast.makeText(mContext, "密码不匹配，请重新输入", 1000).show();
		}
		else{
			return true;
		}
		return false;
	}
}
