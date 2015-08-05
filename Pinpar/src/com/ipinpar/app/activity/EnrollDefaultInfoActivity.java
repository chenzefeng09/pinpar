package com.ipinpar.app.activity;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.db.dao.EnrollInfoDao;
import com.ipinpar.app.entity.EnrollInfoEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.EnrollActivityRequest;

public class EnrollDefaultInfoActivity extends PPBaseActivity {
	
	public static final int REQUEST_CODE_SELECT_ENROLL_INFO = 1;

	private TextView tv_enroll_userinfo_name,tv_enroll_enroll_phone,tv_enroll_address,tv_other_infos;
	private CheckBox cb_enroll_userinfo_agree;
	private Button btn_submit;
	private EnrollInfoEntity currEntity; 
	private int acid;
	private String declaration;
	private TextView tv_enroll_userinfo_protocol;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		declaration = getIntent().getStringExtra("declaration");
		acid = getIntent().getIntExtra("acid", 0);
		setContentView(R.layout.activity_default_enroll_info);
		tv_enroll_userinfo_name = (TextView) findViewById(R.id.tv_enroll_userinfo_name);
		tv_enroll_enroll_phone = (TextView) findViewById(R.id.tv_enroll_enroll_phone);
		tv_enroll_address = (TextView) findViewById(R.id.tv_enroll_address);
		tv_other_infos = (TextView) findViewById(R.id.tv_other_infos);
		cb_enroll_userinfo_agree = (CheckBox) findViewById(R.id.cb_enroll_userinfo_agree);
		tv_enroll_userinfo_protocol = (TextView) findViewById(R.id.tv_enroll_userinfo_protocol);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		currEntity = EnrollInfoDao.getInstance().getDefaultInfo();
		if (currEntity == null) {
			
		}
		else {
			tv_enroll_userinfo_name.setText(currEntity.getName());
			tv_enroll_enroll_phone.setText(currEntity.getPhone());
			tv_enroll_address.setText(currEntity.getAddress1()+currEntity.getAddress2()+currEntity.getAddress3()+currEntity.getAddressdetail());
		}
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!cb_enroll_userinfo_agree.isChecked()) {
					Toast.makeText(mContext, "请阅读并同意《用户协议》", 1000).show();
					return;
				}
				EnrollActivityRequest request;
				try {
					request = new EnrollActivityRequest(acid,
							UserManager.getInstance().getUserInfo().getUid(),
							declaration, currEntity.getInfoid(), new Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									try {
										if (response != null && response.getInt("result") == 1) {
											Toast.makeText(mContext, "报名成功~", 1000).show();
											setResult(RESULT_OK);
											finish();
										}
										else if (response.getInt("result") == 102) {
											Toast.makeText(mContext, "已报名过该活动，请等待审核~", 1000).show();
										}
										else {
											Toast.makeText(mContext, "报名失败，请重试~", 1000).show();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
					apiQueue.add(request);

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		tv_other_infos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(mContext, EnrollInfoListActivity.class),
						REQUEST_CODE_SELECT_ENROLL_INFO);
			}
		});
		tv_enroll_userinfo_protocol.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(PPWebView.getIntent2Me(mContext,
						"http://api.ipinpar.com/pinpaV2/articleEnroll.jsp", "报名协议"));
			}
		});
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case REQUEST_CODE_SELECT_ENROLL_INFO:
			if (arg1 != RESULT_CANCELED) {
				EnrollInfoEntity sEnrollInfoEntity = (EnrollInfoEntity) arg2.getSerializableExtra("selected_info");
				currEntity = sEnrollInfoEntity;
				tv_enroll_userinfo_name.setText(currEntity.getName());
				tv_enroll_enroll_phone.setText(currEntity.getPhone());
				tv_enroll_address.setText(currEntity.getAddress());
			}
			break;

		default:
			break;
		}
	}
	
	public static Intent getIntent2Me(Context context,int acid,String declaration){
		Intent intent = new Intent(context, EnrollDefaultInfoActivity.class);
		intent.putExtra("acid", acid);
		intent.putExtra("declaration", declaration);
		return intent;
	}
}
