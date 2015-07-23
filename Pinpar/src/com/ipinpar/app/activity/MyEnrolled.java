package com.ipinpar.app.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.adapter.MyEnrolledActivityListAdapter;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.entity.ActivityListEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.MyActivityListRequest;
import com.ipinpar.app.util.NetWorkState;
import com.ipinpar.app.widget.PullToRefreshListView;
import com.ipinpar.app.widget.PullToRefreshListView.OnRefreshListener;

public class MyEnrolled extends PPBaseActivity{

	private Context mContext;

	private RelativeLayout rlMyEnrolledNoTip;
	
	//请我报名的活动
	private MyActivityListRequest myEnrolledAcsRequest;
	
	//保存服务器返回的正在进行中的活动
	private ArrayList<ActivityEntity> activityList = new ArrayList<ActivityEntity>();
	
	private PullToRefreshListView myEnrolledActicitiesListView;
	private MyEnrolledActivityListAdapter activityListAdapter;
	
	//分页相关
	private static String MY_ENROLLED_ACTIVITY_TYPE = "1";
	
	private static String PAGENUM = "1";
	private static String OFFSET = "20";
	private String maxAcId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_enrolled);
		mContext = this;
		
		findView();
		setView();
		
		handlerMyEnrolledAcsRequest.sendEmptyMessage(0);
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
		
		rlMyEnrolledNoTip = (RelativeLayout) findViewById(R.id.RL_has_no_tip);
		
		activityListAdapter = new MyEnrolledActivityListAdapter(mContext,activityList);
		
		myEnrolledActicitiesListView = (PullToRefreshListView) findViewById(R.id.my_enrolled_activities_list);
		
		if(activityListAdapter!=null){
			myEnrolledActicitiesListView.setAdapter(activityListAdapter);
		}
	}
	
	public void setView(){
		myEnrolledActicitiesListView.setOnScrollListener(onScrollListener);
		myEnrolledActicitiesListView.setOnRefreshListener(onRefreshListener);
		myEnrolledActicitiesListView.setOnItemClickListener(onItemClickListener);
		
	}
	
	private OnScrollListener onScrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
				// 判断滚动到底部
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					
					handlerMyEnrolledAcsRequest.sendEmptyMessage(1);
					
				}
				break;
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("activityID", activityList.get(position-1).getAcid());
			if(activityList.get(position-1).getStatus() == 4){
				intent.setClass(mContext, PastCompleteAcDetail.class);
			}else if(activityList.get(position-1).getStatus() == 3){
				intent.setClass(mContext, PastInvitingAcDetail.class);
			}else{
				intent.setClass(mContext, OngoingAcDetail.class);
			}
			startActivity(intent);
			
		}
	};
	
	private OnRefreshListener onRefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			// Do work to refresh the list here.
			
			if(NetWorkState.isConnectingToInternet()){
				handlerMyEnrolledAcsRequest.sendEmptyMessage(0);
			}
			
			
		}
	};
	
	Handler handlerMyEnrolledAcsRequest = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				//1、uid 2、type(报名的、受邀的、感兴趣的) 3、pagenum 4、pagecount
				myEnrolledAcsRequest = new MyActivityListRequest(
						UserManager.getInstance().getUserInfo().getUid()+"",
						MY_ENROLLED_ACTIVITY_TYPE,
						PAGENUM,
						OFFSET, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						
						Gson gson = new Gson();
						
						ActivityListEntity acList = gson.fromJson(response.toString(), ActivityListEntity.class);
						
						if(activityList.size()>0){
							maxAcId = activityList.get(activityList.size()-1).getAcid()+"";
						}else{
							maxAcId = "0";
						}
						
						if(activityList.size() == 0){
							rlMyEnrolledNoTip.setVisibility(View.VISIBLE);
						}
						
						Message msg = new Message();
						msg.obj = acList.getActives();
						msg.what = 2;
						handlerStateChanged.sendMessage(msg);
						
						handlerStateChanged.sendEmptyMessage(0);
						handlerStateChanged.sendEmptyMessage(1);
					
					}
					
				});
				myEnrolledAcsRequest.setTag(TAG);
				apiQueue.add(myEnrolledAcsRequest);
			break;
			case 1:
				//1、uid 2、type(报名的、受邀的、感兴趣的) 3、pagenum 4、pagecount
				myEnrolledAcsRequest = new MyActivityListRequest(
						UserManager.getInstance().getUserInfo().getUid()+"",
						MY_ENROLLED_ACTIVITY_TYPE,
						maxAcId,
						PAGENUM,
						OFFSET, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						
						Gson gson = new Gson();
						
						ActivityListEntity acList = gson.fromJson(response.toString(), ActivityListEntity.class);
						
						activityList.addAll(acList.getActives());
						
						if(activityList.size()>0){
							maxAcId = activityList.get(activityList.size()-1).getAcid()+"";
						}else{
							maxAcId = "0";
						}
						
						handlerStateChanged.sendEmptyMessage(0);
						handlerStateChanged.sendEmptyMessage(1);
						
					}
					
				});
				myEnrolledAcsRequest.setTag(TAG);
				apiQueue.add(myEnrolledAcsRequest);
				break;
			
			default:
				
				break;
			}
		}
		
	};
	
	Handler handlerStateChanged = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				activityListAdapter.notifyDataSetChanged();
				break;
			case 1:
				myEnrolledActicitiesListView.onRefreshComplete();
				break;
			case 2:
				activityList.clear();
				activityList.addAll((ArrayList<ActivityEntity>)msg.obj);
				break;
			default:
				break;
			}
		}
	};
}
