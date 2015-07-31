package com.ipinpar.app.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.network.api.GetVerifyCodeRequest;
import com.ipinpar.app.network.api.VerifyCodeRequest;

public class RegistActivityStep2 extends PPBaseActivity {
	
//	private EditText et_user_phone,et_code,et_email;
	private EditText et_user_phone,et_code;
	private TextView tv_get_code;
	private Button btn_next;
//	private String phone,pwd,verify_code,identifier,email_address;
	private String phone,verify_code,identifier;
	
	private TextView tv_read_protocol;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_regist_phone_code);
//		phone = getIntent().getStringExtra("phone");
//		pwd = getIntent().getStringExtra("pwd");
		et_user_phone = (EditText) findViewById(R.id.et_user_phone);
//		et_user_phone.setEnabled(false);
		et_user_phone.setText(phone);
		et_code = (EditText) findViewById(R.id.et_code);
		
		tv_read_protocol = (TextView) findViewById(R.id.tv_read_protocol);

//		et_email = (EditText) findViewById(R.id.et_email);
		tv_get_code = (TextView) findViewById(R.id.tv_get_code);
		btn_next = (Button) findViewById(R.id.btn_next);
		tv_get_code.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgressDialog();
				phone = et_user_phone.getText().toString().trim();
				GetVerifyCodeRequest request = new GetVerifyCodeRequest(phone, 0, null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						dissmissProgressDialog();
						try {
							if (response.getInt("result")  == 1) {
								identifier = response.getString("identifier");
								Toast.makeText(mContext, "验证码正在路上，请稍等", 1000).show();
								tv_get_code.setEnabled(false);
								final Timer timer = new Timer();
								TimerTask timerTask = new TimerTask() {
									int time = 120;
									@Override
									public void run() {
										// TODO Auto-generated method stub
										if (time > 0 ) {
											time--;
											tv_get_code.post(new Runnable() {
												
												@Override
												public void run() {
													// TODO Auto-generated method stub
													tv_get_code.setText(time+"秒");
												}
											});
										}
										else {
											timer.cancel();
											tv_get_code.post(new Runnable() {
												
												@Override
												public void run() {
													// TODO Auto-generated method stub
													tv_get_code.setText("获取验证码");
													tv_get_code.setEnabled(true);												}
											});
											
										}
										
									}
								};
								timer.schedule(timerTask, 1000, 1000);
							}else if (response.getInt("result")  == 101){
								Toast.makeText(mContext, "手机号码格式错误，请重试"+response.getInt("result") , 1000).show();
								
							}else if (response.getInt("result")  == -1){
								Toast.makeText(mContext, "手机号码已注册，请直接登录"+response.getInt("result") , 1000).show();
							}else {
								Toast.makeText(mContext, "注册失败，请重试"+response.getInt("result") , 1000).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				apiQueue.add(request);
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProgressDialog();
				phone = et_user_phone.getText().toString().trim();
				verify_code = et_code.getText().toString().trim();
				if (TextUtils.isEmpty(verify_code)) {
					Toast.makeText(mContext, "验证码不能为空", 1000).show();
					dissmissProgressDialog();
					return;
				}
				VerifyCodeRequest request = new VerifyCodeRequest(phone, identifier, verify_code, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						dissmissProgressDialog();
						try {
							if (response.getInt("result") == 1) {
								startActivity(RegistStep3Activity.getIntent2Me(mContext, phone));
							}
							else {
								Toast.makeText(mContext, "验证失败，请重试", 1000).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				apiQueue.add(request);
			}
		});
		
		tv_read_protocol.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(PPWebView.getIntent2Me(mContext, "http://api.ipinpar.com/pinpaV2/article.jsp","用户协议"));
			}
		});

	}
	
	public static Intent getIntent2Me(Context context,String phone,String pwd){
		Intent intent = new Intent(context, RegistActivityStep2.class);
		intent.putExtra("phone", phone);
		intent.putExtra("pwd", pwd);
		return intent;
	}

}
