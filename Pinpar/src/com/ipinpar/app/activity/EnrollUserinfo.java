package com.ipinpar.app.activity;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.db.dao.EnrollInfoDao;
import com.ipinpar.app.entity.EnrollInfoEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.AddEnrollInfoRequest;
import com.ipinpar.app.view.AddressSelectVIew;
import com.ipinpar.app.view.AddressSelectVIew.onSelectListener;

public class EnrollUserinfo extends PPBaseActivity{

	private Context mContext;
	
	private Button btnSubmit;
	private EditText et_enroll_userinfo_name,et_enroll_enroll_phone
	,et_enroll_enroll_school_company,et_enroll_enroll_id,et_enroll_address;
	private CheckBox cb_enroll_userinfo_agree;
	private TextView et_enroll_address_city_area;
	private RadioGroup rg_userinfo_sex;
	
	private String username,phone,unit,idnumber,adress,address1,address2,address3;
	private int sex = 1;
	
	private int acid;
	private String declaration;
	private AddressSelectVIew addressView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_userinfo);
		declaration = getIntent().getStringExtra("declaration");
		acid = getIntent().getIntExtra("acid", 0);
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
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		et_enroll_userinfo_name = (EditText) findViewById(R.id.et_enroll_userinfo_name);
		et_enroll_enroll_phone = (EditText) findViewById(R.id.et_enroll_enroll_phone);
		et_enroll_enroll_school_company = (EditText) findViewById(R.id.et_enroll_enroll_school_company);
		et_enroll_enroll_id = (EditText) findViewById(R.id.et_enroll_enroll_id);
		et_enroll_address = (EditText) findViewById(R.id.et_enroll_address);
		rg_userinfo_sex = (RadioGroup) findViewById(R.id.rg_userinfo_sex);
		cb_enroll_userinfo_agree = (CheckBox) findViewById(R.id.cb_enroll_userinfo_agree);
		et_enroll_address_city_area = (TextView) findViewById(R.id.et_enroll_address_city_area);
		rg_userinfo_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rb_male) {
					sex = 1;
				}
				else if (checkedId == R.id.rb_female) {
					sex = 2;
				}
			}
		});
		et_enroll_address_city_area.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (addressView == null) {
					addressView = new AddressSelectVIew(mContext, new onSelectListener() {
						@Override
						public void onSelect(String address1, String address2, String address3) {
							EnrollUserinfo.this.address1 = address1;
							EnrollUserinfo.this.address2 = address2;
							EnrollUserinfo.this.address3 = address3;
							et_enroll_address_city_area.setText(address1+address2+address3);
						}
					});
					int[] anchorCenter = new int[2];
					// 读取位置anchor座标
					et_enroll_address_city_area.getLocationInWindow(anchorCenter);
					addressView.showAtLocation(et_enroll_address_city_area, Gravity.TOP | Gravity.LEFT,
							anchorCenter[0], anchorCenter[1]);
					addressView.update();
				}
				else {
					int[] anchorCenter = new int[2];
					// 读取位置anchor座标
					et_enroll_address_city_area.getLocationInWindow(anchorCenter);
					addressView.showAtLocation(et_enroll_address_city_area, Gravity.TOP | Gravity.LEFT,
							anchorCenter[0], anchorCenter[1]);
					addressView.update();
				}
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if (addressView != null && addressView.isShowing()) {
			addressView.dismiss();
		}
		else {
			super.onBackPressed();
		}
	}
	
	public void setView(){
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (checkInputsOk()) {
					showProgressDialog();
					AddEnrollInfoRequest request;
					try {
						request = new AddEnrollInfoRequest(
								UserManager.getInstance().getUserInfo().getUid(), 
								username, 
								address1,
								address2,
								address3,
								idnumber, 
								phone, 
								sex,
								unit,
								adress,
								new Response.Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										dissmissProgressDialog();
										try {
											if (response != null && response.getInt("result") == 1) {
												Toast.makeText(mContext, "新增报名信息成功", 1000).show();
												if (!EnrollInfoDao.getInstance().hasInfo()) {
													EnrollInfoEntity enrollInfoEntity = new EnrollInfoEntity();
													enrollInfoEntity.setAddressdetail(adress);
													enrollInfoEntity.setIdnumber(idnumber);
													enrollInfoEntity.setIsdefault(1);
													enrollInfoEntity.setName(username);
													enrollInfoEntity.setPhone(phone);
													enrollInfoEntity.setSex(sex);
													enrollInfoEntity.setUid(UserManager.getInstance().getUserInfo().getUid());
													enrollInfoEntity.setUint(unit);
													EnrollInfoDao.getInstance().insertEnrollInfo(enrollInfoEntity);
													startActivity(EnrollDefaultInfoActivity.getIntent2Me(mContext, acid, declaration));
													finish();
												}
												finish();
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
			}

			
		});
	}
	
	private boolean checkInputsOk() {
		// TODO Auto-generated method stub
		username = et_enroll_userinfo_name.getText().toString().trim();
		phone = et_enroll_enroll_phone.getText().toString().trim();
		unit = et_enroll_enroll_school_company.getText().toString().trim();
		idnumber = et_enroll_enroll_id.getText().toString().trim();
		adress = et_enroll_address.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(mContext, "姓名不能为空", 1000).show();
			return false;
		}
		else if (TextUtils.isEmpty(phone)) {
			Toast.makeText(mContext, "手机号不能为空", 1000).show();
			return false;
		}
		else if (phone.length() < 11) {
			Toast.makeText(mContext, "请输入正确手机号", 1000).show();
			return false;
		}
		else if (TextUtils.isEmpty(unit)) {
			Toast.makeText(mContext, "单位不能为空", 1000).show();
			return false;
		}
		else if (TextUtils.isEmpty(adress)) {
			Toast.makeText(mContext, "地址不能为空", 1000).show();
			return false;
		}
		else if (!cb_enroll_userinfo_agree.isChecked()) {
			Toast.makeText(mContext, "请阅读并同意《用户协议》", 1000).show();
			return false;
		}
		
		
		return true;
	}
	
	public static Intent getIntent2Me(Context context,int acid,String declaration){
		Intent intent = new Intent(context, EnrollUserinfo.class);
		intent.putExtra("acid", acid);
		intent.putExtra("declaration", declaration);
		return intent;
	}
	
}
