package com.ipinpar.app.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.adapter.StatementListAdapter;
import com.ipinpar.app.entity.AcImageEntity;
import com.ipinpar.app.entity.AcStatementEntity;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.entity.ActivityStatementListEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.ActivityDetailRequest;
import com.ipinpar.app.network.api.StatementListRequest;
import com.ipinpar.app.view.RollViewPager;

public class StatementsListActivity extends PPBaseActivity {

	private Context mContext;
	
	private ProgressDialog wattingDialog;
	
	private StatementListAdapter statementListAdapter;
	
	//活动ID
	private int acid;
	
	//根据uid和acid获取最强宣言列表
	private StatementListRequest statementsListRequest;
	
	private ArrayList<AcStatementEntity> acStatementList = new ArrayList<AcStatementEntity>();
	
	private ImageView btnBack;
	
	private ListView statementListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statements_list);
		mContext = this;
		
		acid = getIntent().getIntExtra("activityID",1);
		
		initView();
		setView();
		
		handlerOngoingAcStatementListRequest.sendEmptyMessage(0);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void initView() {
		
		//等待进度条
		wattingDialog = new ProgressDialog(mContext);
		
		btnBack = (ImageView) findViewById(R.id.ib_left_statements_list);
		
		statementListView = (ListView) findViewById(R.id.statements_list);
		
	}
	
	public void setView(){
		
		statementListAdapter = new StatementListAdapter(mContext,acStatementList,apiQueue);
		
		if(statementListAdapter!=null){
			statementListView.setAdapter(statementListAdapter);
		}
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
	}
	
	Handler handlerOngoingAcStatementListRequest = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				wattingDialog.show();
				statementsListRequest = new StatementListRequest("",acid+"", new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						
						Gson gson = new Gson();
						
						//获取返回的活动
						ActivityStatementListEntity acStatementListEntity = gson.fromJson(response.toString(), ActivityStatementListEntity.class);
						
						if(acStatementListEntity.getResult().equals("1")){
							acStatementList.clear();
							acStatementList.addAll(acStatementListEntity.getDeclarations());
							
							handlerOngoingAcStatementListRequest.sendEmptyMessage(1);
							wattingDialog.dismiss();
						}
					}
					
				});
				statementsListRequest.setTag(TAG);
				apiQueue.add(statementsListRequest);
			break;
			
			case 1:
				
				statementListAdapter.notifyDataSetChanged();
				
				break;
			
			default:
				
				break;
			}
		}
		
	};

}
