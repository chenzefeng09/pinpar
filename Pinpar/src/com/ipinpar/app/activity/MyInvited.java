package com.ipinpar.app.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.adapter.MyInvitedActivityListAdapter;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.entity.ActivityListEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.MyActivityListRequest;
import com.ipinpar.app.util.NetWorkState;
import com.ipinpar.app.widget.PullToRefreshListView;
import com.ipinpar.app.widget.PullToRefreshListView.OnRefreshListener;

public class MyInvited extends PPBaseActivity implements OnScrollListener{

	private Context mContext;
	
	private RelativeLayout rlMyEnrolledNoTip;

	//请求往期的活动
	private MyActivityListRequest myInvitedAcsRequest;
	
	//保存服务器返回的正在进行中的活动
	private ArrayList<ActivityEntity> activityList = new ArrayList<ActivityEntity>();
	
	private PullToRefreshListView myInvitedActicitiesListView;
	private MyInvitedActivityListAdapter activityListAdapter;
	
	private static String MY_INVITED_AC_TYPE = "2";
	private static String PAGENUM = "1";
	private static String OFFSET = "40";
	private String maxAcId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_invited);
		mContext = this;
		
		findView();
		setView();
		
		handlerMyInvitedAcsRequest.sendEmptyMessage(0);
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
		
		activityListAdapter = new MyInvitedActivityListAdapter(mContext,activityList);
		
		myInvitedActicitiesListView = (PullToRefreshListView) findViewById(R.id.my_invited_activities_list);
		
		if(activityListAdapter!=null){
			myInvitedActicitiesListView.setAdapter(activityListAdapter);
		}
	}
	
	public void setView(){
		myInvitedActicitiesListView.setOnScrollListener(onScrollListener);
		myInvitedActicitiesListView.setOnRefreshListener(onRefreshListener);
		myInvitedActicitiesListView.setOnItemClickListener(onItemClickListener);
		
	}
	
	
	private OnScrollListener onScrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
				// 判断滚动到底部
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					
//					if(reqPackId.equals("-2")){
//					
//						//分页数先向上取整
//						Log.d("当前本地总的记录数为：", DataManager.Instance().courseList.size()+"");
//						reqPageNum = (int)Math.ceil((double)DataManager.Instance().courseList.size()/20) + 1;
//						curPageNum = reqPageNum;
//						Message msg = new Message();
//						msg.arg1 = reqPageNum;
//						msg.what = 5;
//						handler.sendMessage(msg);
//					}else{
//						Toast.makeText(mContext, "已经加载全部内容", Toast.LENGTH_SHORT).show();
//					}
					
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
			if(activityList.get(position-1).getStatus2() == 2){
				intent.putExtra("activity", activityList.get(position-1));
				intent.setClass(mContext, InviteLetterActivity.class);
				startActivity(intent);
			}else if(activityList.get(position-1).getStatus2() == 3){
				
				startActivity(ExperienceDiaryEditActivity.getIntent2Me(
						mContext, 
						activityList.get(position-1).getAcid(),
						UserManager.getInstance().getUserInfo().getUid()));
				
			}else{
				startActivity(ExperienceDiaryActivity.getIntent2Me(
						mContext, 
						activityList.get(position-1).getAcid(),
						UserManager.getInstance().getUserInfo().getUid()));
			}
		}
	};
	
	private OnRefreshListener onRefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			// Do work to refresh the list here.
			
			if(NetWorkState.isConnectingToInternet()){
				handlerMyInvitedAcsRequest.sendEmptyMessage(0);
			}
			
			
		}
	};
	
	Handler handlerMyInvitedAcsRequest = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				myInvitedAcsRequest = new MyActivityListRequest(
						UserManager.getInstance().getUserInfo().getUid()+"",
						MY_INVITED_AC_TYPE,
						PAGENUM,
						OFFSET, new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						Log.d("onResponse:","进入onResponse！");
						
						Gson gson = new Gson();
						
						ActivityListEntity acList = gson.fromJson(response.toString(), ActivityListEntity.class);
						
						activityList.clear();
						activityList.addAll(acList.getActives());
						
						handlerStateChanged.sendEmptyMessage(0);
						handlerStateChanged.sendEmptyMessage(1);
						
						if(activityList.size() == 0){
							rlMyEnrolledNoTip.setVisibility(View.VISIBLE);
						}
					}
					
				});
				myInvitedAcsRequest.setTag(TAG);
				apiQueue.add(myInvitedAcsRequest);
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
				myInvitedActicitiesListView.onRefreshComplete();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
}
