package com.ipinpar.app.activity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.CurrDreamShowEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.MyActivityListRequest;
import com.ipinpar.app.network.api.PastedDreamShowLIstRequest;
import com.ipinpar.app.util.BitmapFillet;
import com.ipinpar.app.util.DisplayUtil;
import com.ipinpar.app.widget.PullToRefreshListView;
import com.ipinpar.app.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PastedDreamShowActivity extends PPBaseActivity implements OnScrollListener{

	private Context mContext;

	private RelativeLayout rlMyEnrolledNoTip;
	
	//请求往期的活动
	private MyActivityListRequest myEnrolledAcsRequest;
	
	//保存服务器返回的正在进行中的活动
	private ArrayList<CurrDreamShowEntity> activityList = new ArrayList<CurrDreamShowEntity>();
	
	private PullToRefreshListView myEnrolledActicitiesListView;
	private PastDreamShowListAdapter pastDreamShowAdapter;
	
	private static String MY_ENROLLED_AC_TYPE = "1";
	private static String PAGENUM = "1";
	private static String OFFSET = "40";
	private String maxAcId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pasted_dreamshow);
		mContext = this;
		
		refreshDream();
		
		findView();
		setView();
		
		
	}

	private void refreshDream() {
		PastedDreamShowLIstRequest request = new PastedDreamShowLIstRequest(PAGENUM, OFFSET, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response != null && response.getInt("result") == 1) {
						Gson gson = new Gson();
						Type type = new TypeToken<ArrayList<CurrDreamShowEntity>>() {
						}.getType();
						ArrayList<CurrDreamShowEntity> dreams = gson.fromJson(response.getJSONArray("dreams").toString(),type); 
						activityList.clear();
						activityList.addAll(dreams);
						if (pastDreamShowAdapter == null) {
							pastDreamShowAdapter = new PastDreamShowListAdapter(activityList);
							myEnrolledActicitiesListView.setAdapter(pastDreamShowAdapter);
						}
						else {
							pastDreamShowAdapter.notifyDataSetChanged();
						}
					}
				} catch (JSONException e) {
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
		refreshDream();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void findView(){
		
		rlMyEnrolledNoTip = (RelativeLayout) findViewById(R.id.RL_has_no_tip);
		
		myEnrolledActicitiesListView = (PullToRefreshListView) findViewById(R.id.my_enrolled_activities_list);
		
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
			startActivity(intent);
			
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
			refreshDream();
			
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
	
	private class PastDreamShowListAdapter extends BaseAdapter{
		private ArrayList<CurrDreamShowEntity> dreams;
		private ViewHolder holder;
		
		public PastDreamShowListAdapter(ArrayList<CurrDreamShowEntity> dreams) {
			this.dreams = dreams;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dreams == null ?0:dreams.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dreams == null? null:dreams.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final CurrDreamShowEntity dreamShowEntity = dreams.get(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_item_pasted_dream_show, null);
				holder = new ViewHolder();
				holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
				holder.iv_dream = (ImageView) convertView.findViewById(R.id.iv_dream);
				holder.iv_statement_support = (ImageView) convertView.findViewById(R.id.iv_statement_support);
				holder.iv_statement_comment = (ImageView) convertView.findViewById(R.id.iv_statement_comment);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_curr);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time_curr);
				holder.tv_text_content = (TextView) convertView.findViewById(R.id.tv_text_content);
				holder.tv_dream_state = (TextView) convertView.findViewById(R.id.tv_dream_state);
				holder.tv_statement_support_num = (TextView) convertView.findViewById(R.id.tv_statement_support_num);
				holder.tv_statement_comment_num = (TextView) convertView.findViewById(R.id.tv_statement_comment_num);
				holder.tv_past_dream_order = (TextView) convertView.findViewById(R.id.tv_past_dream_order);
				holder.iv_activity_desc = (ImageView) convertView.findViewById(R.id.iv_activity_desc);
				holder.RL_comment = convertView.findViewById(R.id.RL_comment);
				holder.RL_support = convertView.findViewById(R.id.RL_support);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_past_dream_order.setText("第"+dreamShowEntity.period+"期");
			ImageLoader.getInstance().displayImage(dreamShowEntity.imgsrc, holder.iv_img);
			holder.tv_dream_state.setText(dreamShowEntity.detail);
			holder.tv_name.setText(PPApplication.getInstance().getFormatString(R.string.dream_show_who_says, dreamShowEntity.username));
			holder.tv_text_content.setText(dreamShowEntity.title);
			holder.tv_time.setText(formatTime(dreamShowEntity.createtime*1000));
//			holder.tv_statement_comment_num.setText(dreamShowEntity.commentcount+"");
//			holder.tv_statement_support_num.setText(dreamShowEntity.agreecount+"");
//			if (AgreeManager.getInstance().isAgreed(dreamShowEntity.dreamid, "dreamid")) {
//				holder.iv_statement_support.setImageResource(R.drawable.enroll_fist);
//			}
//			else {
//				holder.iv_statement_support.setImageResource(R.drawable.ac_support);
//			}
			if (TextUtils.isEmpty(dreamShowEntity.mainimg)) {
				holder.iv_dream.setVisibility(View.GONE);
			}
			else {
				holder.iv_dream.setVisibility(View.GONE);
				ImageLoader.getInstance().loadImage(dreamShowEntity.mainimg, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						holder.iv_activity_desc.setImageBitmap(BitmapFillet.fillet(BitmapFillet.TOP, loadedImage, DisplayUtil.dip2px(9)));
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						
					}
				});
//				ImageLoader.getInstance().displayImage(dreamShowEntity.mainimg, holder.iv_activity_desc);
			}
//			holder.RL_support.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					if (UserManager.getInstance().isLogin()) {
//						AgreeManager.getInstance().agree(dreamShowEntity.dreamid, "dreamid", new AgreeResultListener() {
//							
//							@Override
//							public void onAgreeResult(boolean agree) {
//								if (agree) {
//									dreamShowEntity.agreecount++;
//								}
//								else {
//									dreamShowEntity.agreecount--;
//								}
//								notifyDataSetChanged();
//							}
//						}, apiQueue);
//					}
//					else {
//						startActivity(new Intent(mContext, LoginActivity.class));
//					}
//					
//				}
//			});
			holder.iv_img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (UserManager.getInstance().isLogin() && dreamShowEntity.uid == UserManager.getInstance().getUserInfo().getUid()) {
						return ;
					}
					else {
						startActivity(NameCardActivity.getIntent2Me(mContext, dreamShowEntity.uid));
					}
					
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(CommentDetailActivity.getIntent2Me(mContext, dreamShowEntity.dreamid, "dreamid","详情"));
				}
			});
			return convertView;
		}
		public class ViewHolder{
			public ImageView iv_img,iv_dream,iv_statement_support,iv_statement_comment,iv_activity_desc;
			public TextView tv_name,tv_time,tv_text_content,tv_dream_state,tv_statement_support_num,tv_statement_comment_num;
			public View RL_support,RL_comment;
			public TextView tv_past_dream_order;
		}
	}
	
	private String formatTime(long time){
		Date date = new Date(time);
		Date date2 = new Date(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();

		calendar.setTime(date);
		calendar2.setTime(date2);
		
		if (calendar.get(Calendar.DAY_OF_YEAR) -calendar.get(Calendar.DAY_OF_YEAR) >0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(date);
		}
		else if (time - System.currentTimeMillis() >60*60*1000) {
			return (time - System.currentTimeMillis())/(60*60*1000)+"小时之前";
		}
		else if (time - System.currentTimeMillis() > 60*1000) {
			return (time - System.currentTimeMillis())/(60*1000)+"分钟之前";
		}
		else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(date);
		}
		
	}
	
}
