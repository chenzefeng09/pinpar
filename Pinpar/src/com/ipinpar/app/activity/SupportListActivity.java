package com.ipinpar.app.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.NotificationEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.NotificationRequest;
import com.ipinpar.app.network.api.ReadNotificationRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SupportListActivity extends PPBaseActivity {
	private ListView lv_friends;
	private NewCommentsAdapter adapter;
	private ArrayList<NotificationEntity> comments = new ArrayList<NotificationEntity>();
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_notifacation_list);
		setTitleText("支持");
		lv_friends = (ListView) findViewById(R.id.lv_friends);
		lv_friends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				NotificationEntity entity = comments.get(position);
				if ("comment".equals(entity.getType()) && "enrollid".equals(entity.getFrom_idtype())) {
					startActivity(CommentDetailActivity.getIntent2Me(mContext, entity.getFrom_id()));
				}
				ReadNotificationRequest readNotificationRequest = new ReadNotificationRequest(
						UserManager.getInstance().getUserInfo().getUid(), entity.getId()+"", new Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
							}
						});
				apiQueue.add(readNotificationRequest);
			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (UserManager.getInstance().isLogin()) {
			showProgressDialog();
			NotificationRequest request = new NotificationRequest(
					UserManager.getInstance().getUserInfo().getUid(), 1, 100,
					 new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							dissmissProgressDialog();
							try {
								if (response != null && response.getInt("result") == 1) {
									int new_total = Integer.parseInt(response.getString("newtotal"));
									int total = Integer.parseInt(response.getString("total"));
									JSONArray jsonArray = response.getJSONArray("data");
									comments.clear();
									for(int i=0;i<jsonArray.length();i++){
										JSONObject jsonObject = (JSONObject) jsonArray.get(i);
										NotificationEntity notificationEntity = new NotificationEntity();
										notificationEntity.setAuthor(jsonObject.getString("author"));
										notificationEntity.setAuthorid(jsonObject.getInt("authorid"));
										notificationEntity.setDateline(jsonObject.getLong("dateline"));
										notificationEntity.setFrom_id(jsonObject.getInt("from_id"));
										notificationEntity.setFrom_idtype(jsonObject.getString("from_idtype"));
										notificationEntity.setFrom_num(jsonObject.getInt("from_num"));
										notificationEntity.setId(jsonObject.getInt("id"));
										notificationEntity.setIs_new(jsonObject.getBoolean("new"));
										notificationEntity.setStatus(jsonObject.getInt("status"));
										notificationEntity.setType(jsonObject.getString("type"));
										notificationEntity.setUid(jsonObject.getInt("uid"));
										notificationEntity.setNote(jsonObject.getString("note"));
										if ("agree".equals(notificationEntity.getType())) {
											comments.add(notificationEntity);
										}
									}
									int commentcount = 0;
									int supportcount = 0;
									int notificationcount = 0;
									if (adapter == null) {
										adapter = new NewCommentsAdapter(comments);
										lv_friends.setAdapter(adapter);
									}
									else {
										adapter.notifyDataSetChanged();
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			apiQueue.add(request);
		}
	}
	
	private class NewCommentsAdapter extends BaseAdapter{
		private ArrayList<NotificationEntity> commentEntities;
		private ViewHoler viewHoler;

		public NewCommentsAdapter(ArrayList<NotificationEntity> friends) {
			// TODO Auto-generated constructor stub
			this.commentEntities = friends;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentEntities == null ? 0: commentEntities.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return commentEntities == null ?null:commentEntities.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final NotificationEntity commentEntity = commentEntities.get(position);
			if (convertView == null) {
				viewHoler = new ViewHoler();
				convertView = getLayoutInflater().inflate(R.layout.list_item_comments, null);
				viewHoler.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				viewHoler.tv_comment_action = (TextView) convertView.findViewById(R.id.tv_comment_action);
				viewHoler.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				viewHoler.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				convertView.setTag(viewHoler);
			}
			else {
				viewHoler = (ViewHoler) convertView.getTag();
			}
				viewHoler.tv_comment_action.setText(commentEntity.getNote());
			viewHoler.tv_name.setText(commentEntity.getAuthor());
			ImageLoader.getInstance().displayImage("http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="+commentEntity.getAuthorid(), viewHoler.iv_icon);
			viewHoler.tv_time.setText(formatTime(commentEntity.getDateline()));
			return convertView;
		}
		
		private CharSequence formatTime(long dateline) {
			// TODO Auto-generated method stub
			long deltatime = System.currentTimeMillis()/1000 - dateline;
			if (deltatime < 0) {
				deltatime = 1;
			}
			if (deltatime/(24*60*60)>0) {
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
				
				return date.format(new Date(dateline*1000));
			}
			else if (deltatime/(60*60)>0) {
				return deltatime/(60*60)+"小时之前";
			}
			else if (deltatime/(60)>0) {
				return deltatime/(60)+"分钟之前";
			}
			else {
				return deltatime+"秒之前";
			}
		}
		private class ViewHoler{
			public ImageView iv_icon;
			public TextView tv_name;
			public TextView tv_comment_action;
			public TextView tv_time;
		}
		
	}

}
