package com.ipinpar.app.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.ExperienceFlow;
import com.ipinpar.app.activity.MainActivity;
import com.ipinpar.app.activity.PastCompleteAcDetail;
import com.ipinpar.app.activity.PastInvitingAcDetail;
import com.ipinpar.app.adapter.PastActivityListAdapter;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.entity.ActivityListEntity;
import com.ipinpar.app.network.api.ActivityListRequest;
import com.ipinpar.app.util.NetWorkState;
import com.ipinpar.app.widget.PullToRefreshListView;
import com.ipinpar.app.widget.PullToRefreshListView.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

public class PastFragment extends PPBaseFragment{

	
	private Context mContext;
	private View backView;
	private View view;
	private LinearLayout speciEnvelope;
	
	private LinearLayout llOngoingActivities;
	
	//请求往期的活动
	private ActivityListRequest pastAcsRequest;
	
	//保存服务器返回的正在进行中的活动
	private ArrayList<ActivityEntity> activityList = new ArrayList<ActivityEntity>();
	
	private PullToRefreshListView pastActicitiesListView;
	private PastActivityListAdapter activityListAdapter;
	
	//分页相关
	private static String PAST_ACLIST_STATUS = "2";
	
	private static String PAGENUM = "1";
	private static String OFFSET = "1";
	private String maxAcId;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if(mContext == null){
			mContext = (Context)activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.past_fragment, null);
		initView(view);
		setView();
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//请求往期的活动
		handlerPastAcsRequest.sendEmptyMessage(0);
	}
	
	public void initView(View view) {
		backView = view.findViewById(R.id.backlayout);
		backView.setBackgroundColor(Color.WHITE);
		
		llOngoingActivities= (LinearLayout) view.findViewById(R.id.LL_title_ongoing);
		
		activityListAdapter = new PastActivityListAdapter(mContext,activityList);
		
		pastActicitiesListView = (PullToRefreshListView) view.findViewById(R.id.past_activities_list);
		
		LayoutInflater inflateor = LayoutInflater.from(mContext);
		speciEnvelope = (LinearLayout) inflateor.inflate(R.layout.header_specification_envelope, 
				pastActicitiesListView,false);
		pastActicitiesListView.addHeaderView(speciEnvelope);
		
		
		
		if(activityListAdapter!=null){
			pastActicitiesListView.setAdapter(activityListAdapter);
		}
	}
	
	public void setView(){
		pastActicitiesListView.setOnScrollListener(onScrollListener);
		pastActicitiesListView.setOnRefreshListener(onRefreshListener);
		pastActicitiesListView.setOnItemClickListener(onItemClickListener);
		
		llOngoingActivities.setOnClickListener(onOngoingActivityClickListener);
	}

	private OnClickListener onOngoingActivityClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			MainActivity activity = (MainActivity) getActivity();
			FragmentManager fm = activity.getSupportFragmentManager();
			activity.container.setCurrentItem(0, false);
		}
	};
	
	private OnScrollListener onScrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
				// 判断滚动到底部
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					handlerPastAcsRequest.sendEmptyMessage(1);
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
			if(position > 1){
				Intent intent = new Intent();
				intent.putExtra("activityID", activityList.get(position-2).getAcid());
				if(activityList.get(position-2).getStatus() == 4){
					intent.setClass(mContext, PastCompleteAcDetail.class);
				}else{
					intent.setClass(mContext, PastInvitingAcDetail.class);
				}
				startActivity(intent);
			}else{
				Intent intent = new Intent();
				intent.setClass(mContext, ExperienceFlow.class);
				startActivity(intent);
			}
			
		}
	};
	
	private OnRefreshListener onRefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			// Do work to refresh the list here.
			
			if(NetWorkState.isConnectingToInternet()){
				handlerPastAcsRequest.sendEmptyMessage(0);
			}
			
			
		}
	};
	
	Handler handlerPastAcsRequest = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				pastAcsRequest = new ActivityListRequest(
						PAST_ACLIST_STATUS,
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
						
						Message msg = new Message();
						msg.obj = acList.getActives();
						msg.what = 2;
						handlerStateChanged.sendMessage(msg);
						
						handlerStateChanged.sendEmptyMessage(0);
						handlerStateChanged.sendEmptyMessage(1);
					}
					
				});
				pastAcsRequest.setTag(TAG);
				apiQueue.add(pastAcsRequest);
			break;
			
			case 1:
				pastAcsRequest = new ActivityListRequest(
						PAST_ACLIST_STATUS,
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
				pastAcsRequest.setTag(TAG);
				apiQueue.add(pastAcsRequest);
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
				pastActicitiesListView.onRefreshComplete();
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
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("PinparPastActivityFragment"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("PinparPastActivityFragment"); 
	}
	
}
