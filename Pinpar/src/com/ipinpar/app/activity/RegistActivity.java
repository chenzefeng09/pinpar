package com.ipinpar.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class RegistActivity extends PPBaseActivity {
	
	private EditText et_user_phone,et_pwd,et_pwd_confirm;
	private Button btn_login;
	private String phone,pwd1,pwd2;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_regist);
		et_user_phone = (EditText) findViewById(R.id.et_user_phone);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_pwd_confirm = (EditText) findViewById(R.id.et_pwd_confirm);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkOk()){
					startActivity(RegistActivityStep2.getIntent2Me(mContext, phone, pwd1));
				}
			}
		});
	}
	
	private boolean checkOk() {
		// TODO Auto-generated method stub
		phone = et_user_phone.getText().toString().trim();
		pwd1 = et_pwd.getText().toString().trim();
		pwd2 = et_pwd_confirm.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(mContext, "手机号不能为空", 1000).show();
		}
		else if (!TextUtils.isDigitsOnly(phone) && phone.length() < 11) {
			Toast.makeText(mContext, "手机号格式错误", 1000).show();
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
		else {
			return true;
		}
		return false;
	}


}
