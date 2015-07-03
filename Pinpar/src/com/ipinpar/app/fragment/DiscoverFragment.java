package com.ipinpar.app.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.network.api.OngoingActivitiesRequest;
import com.ipinpar.app.widget.pulltorefresh.PullToRefreshListView;

public class DiscoverFragment extends PPBaseFragment implements OnScrollListener{

	private Context mContext;
	private View backView;
	private View view;
	
	private OngoingActivitiesRequest ongoingAcsRequest;
	
	private PullToRefreshListView ongoingActicitiesListView;
	
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
		view = inflater.inflate(R.layout.discover_fragment, null);
		
		initView(view);
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.d("Ongoing Request：","执行进行中活动的请求！");
		handlerOngoingAcs.sendEmptyMessage(0);
	}
	
	public void initView(View view) {
		backView = view.findViewById(R.id.backlayout);
		backView.setBackgroundColor(Color.WHITE);
		
		ongoingActicitiesListView = (PullToRefreshListView) view.findViewById(R.id.ongoing_activities_list);
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
	
	Handler handlerOngoingAcs = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				ongoingAcsRequest = new OngoingActivitiesRequest("4", new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						Log.d("onResponse:","进入onResponse！");
						
						Gson gson = new Gson();
						
						ArrayList<ActivityEntity> activityLists = new ArrayList<ActivityEntity>();
						
//						Type type = (Type) new TypeToken<ArrayList<ActivityEntity>>(){}.getType();
//						
//						Log.e("ActicityEntity response:",response.toString());
						
//						activityLists = gson.fromJson(response.toString(), ActivityEntity.class);
//						
						for(ActivityEntity ae:activityLists){
							Log.d("ActicityEntity content:",ae.getAcname());
						}
						
					}
					
				});
				ongoingAcsRequest.setTag(TAG);
				apiQueue.add(ongoingAcsRequest);
			break;
			
			default:
				
				break;
			}
		}
		
	};

}
