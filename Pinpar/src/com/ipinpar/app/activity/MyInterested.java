package com.ipinpar.app.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.adapter.MyInterestedActivityListAdapter;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.entity.ActivityListEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.MyActivityListRequest;
import com.ipinpar.app.util.NetWorkState;
import com.ipinpar.app.widget.PullToRefreshListView;
import com.ipinpar.app.widget.PullToRefreshListView.OnRefreshListener;

public class MyInterested extends PPBaseActivity implements OnScrollListener{

	private Context mContext;

	private ProgressDialog wattingDialog;
	
	//请求往期的活动
	private MyActivityListRequest myInterestedAcsRequest;
	
	//保存服务器返回的正在进行中的活动
	private ArrayList<ActivityEntity> activityList = new ArrayList<ActivityEntity>();
	
	private PullToRefreshListView myInterestedActicitiesListView;
	private MyInterestedActivityListAdapter activityListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_interested);
		mContext = this;
		
		findView();
		setView();
		
		handlerMyInterestedAcsRequest.sendEmptyMessage(0);
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
		
		wattingDialog = new ProgressDialog(mContext,SCROLL_STATE_TOUCH_SCROLL);
		
		activityListAdapter = new MyInterestedActivityListAdapter(mContext,activityList);
		
		myInterestedActicitiesListView = (PullToRefreshListView) findViewById(R.id.my_interested_activities_list);
		
		if(activityListAdapter!=null){
			myInterestedActicitiesListView.setAdapter(activityListAdapter);
		}
	}
	
	public void setView(){
		myInterestedActicitiesListView.setOnScrollListener(onScrollListener);
		myInterestedActicitiesListView.setOnRefreshListener(onRefreshListener);
		myInterestedActicitiesListView.setOnItemClickListener(onItemClickListener);
		
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
				handlerMyInterestedAcsRequest.sendEmptyMessage(0);
			}
			
			
		}
	};
	
	Handler handlerMyInterestedAcsRequest = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				wattingDialog.show();
				myInterestedAcsRequest = new MyActivityListRequest(
						UserManager.getInstance().getUserInfo().getUid()+"",
						"3","1","10", new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						Log.d("onResponse:","进入onResponse！");
						
						Gson gson = new Gson();
						
						ActivityListEntity acList = gson.fromJson(response.toString(), ActivityListEntity.class);
						
						activityList.clear();
						activityList.addAll(acList.getActives());
						
						wattingDialog.dismiss();
						handlerStateChanged.sendEmptyMessage(0);
						handlerStateChanged.sendEmptyMessage(1);
					}
					
				});
				myInterestedAcsRequest.setTag(TAG);
				apiQueue.add(myInterestedAcsRequest);
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
				myInterestedActicitiesListView.onRefreshComplete();
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
