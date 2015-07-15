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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.ExperienceFlow;
import com.ipinpar.app.activity.MainActivity;
import com.ipinpar.app.activity.OngoingAcDetail;
import com.ipinpar.app.activity.PastCompleteAcDetail;
import com.ipinpar.app.activity.PastInvitingAcDetail;
import com.ipinpar.app.adapter.OngoingActivityListAdapter;
import com.ipinpar.app.adapter.PastActivityListAdapter;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.entity.ActivityListEntity;
import com.ipinpar.app.network.api.ActivityListRequest;
import com.ipinpar.app.util.NetWorkState;
import com.ipinpar.app.widget.PullToRefreshListView;
import com.ipinpar.app.widget.PullToRefreshListView.OnRefreshListener;

public class PastFragment extends PPBaseFragment implements OnScrollListener{

	
	private Context mContext;
	private View backView;
	private View view;
	private LinearLayout speciEnvelope;
	
	private LinearLayout llOngoingActivities;
	private ProgressDialog wattingDialog;
	
	//请求往期的活动
	private ActivityListRequest pastAcsRequest;
	
	//保存服务器返回的正在进行中的活动
	private ArrayList<ActivityEntity> activityList = new ArrayList<ActivityEntity>();
	
	private PullToRefreshListView pastActicitiesListView;
	private PastActivityListAdapter activityListAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d("PastFragment:", "onAttach");
		if(mContext == null){
			mContext = (Context)activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.past_fragment, null);
		Log.d("PastFragment:", "onCreateView");
		initView(view);
		setView();
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("PastFragment:", "onCreate");
		//请求进行中的活动
		handlerPastAcsRequest.sendEmptyMessage(0);
	}
	
	public void initView(View view) {
		backView = view.findViewById(R.id.backlayout);
		backView.setBackgroundColor(Color.WHITE);
		
		wattingDialog = new ProgressDialog(mContext, SCROLL_STATE_TOUCH_SCROLL);
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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

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
			
//			if (NetWorkState.isConnectingToInternet()) {// 开始刷新
//							
//				handler.sendEmptyMessage(1);
//				
//				GetDataTask();
//			} else {// 刷新失败
//				handlerRefreshList.sendEmptyMessage(4);
//				handlerRefreshList.sendEmptyMessage(9);
//				handlerRefreshList.sendEmptyMessage(13);
//			}
			
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
				wattingDialog.show();
				pastAcsRequest = new ActivityListRequest("2","","100","1","5", new Listener<JSONObject>() {
					
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
				pastAcsRequest.setTag(TAG);
				apiQueue.add(pastAcsRequest);
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
				pastActicitiesListView.onRefreshComplete();
				break;
			default:
				break;
			}
		}
	};
	
}
