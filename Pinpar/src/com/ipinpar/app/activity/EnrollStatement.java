package com.ipinpar.app.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.db.dao.EnrollInfoDao;
import com.ipinpar.app.entity.EnrollInfoEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.GetEnrollInfoListRequest;

public class EnrollStatement extends PPBaseActivity{

	private Context mContext;
	
	private Button btnNext;
	private EditText ed_enroll_statement_content;
	
	private int acid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_statement);
		acid = getIntent().getIntExtra("acid", 0);
		mContext = this;
		
		findView();
		
		setView();
		GetEnrollInfoListRequest request  = new GetEnrollInfoListRequest(
				UserManager.getInstance().getUserInfo().getUid(), 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response != null && response.getInt("result") == 1) {
								Gson gson = new Gson();
								Type type = new TypeToken<ArrayList<EnrollInfoEntity>>(){}.getType();
								ArrayList<EnrollInfoEntity> entities = gson.fromJson(response.getJSONArray("infos").toString(),
										type);
								EnrollInfoDao.getInstance().insertEnrollInfos(entities);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		apiQueue.add(request);
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
		btnNext = (Button) findViewById(R.id.btn_next);
		ed_enroll_statement_content = (EditText) findViewById(R.id.ed_enroll_statement_content);
	}
	
	public void setView(){
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (ed_enroll_statement_content.getText().toString().trim().length() == 0) {
					Toast.makeText(mContext, "请输入宣言吧~", 1000).show();
					return;
				}
				if (EnrollInfoDao.getInstance().getDefaultInfo() != null) {
					startActivity(EnrollDefaultInfoActivity.getIntent2Me(mContext, acid, ed_enroll_statement_content.getText().toString().trim()));
				}
//				else if (EnrollInfoDao.getInstance().hasInfo()) {
//					startActivity(EnrollInfoListActivity.getIntent2Me(mContext, acid, ed_enroll_statement_content.getText().toString().trim()));
//				}
				else {
					startActivity(new Intent(mContext, EnrollUserinfo.class));
					finish();
				}
			}
		});
	}
	
	public static Intent getIntent2Me(Context context,int acid){
		Intent intent = new Intent(context, EnrollStatement.class);
		intent.putExtra("acid", acid);
		return intent;
	}
}
