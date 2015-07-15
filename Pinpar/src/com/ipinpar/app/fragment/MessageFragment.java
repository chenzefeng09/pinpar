package com.ipinpar.app.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.CommentsListActivity;
import com.ipinpar.app.activity.NotificationListActivity;
import com.ipinpar.app.activity.SupportListActivity;
import com.ipinpar.app.entity.NotificationEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.NotificationRequest;

public class MessageFragment extends PPBaseFragment implements OnClickListener{
	private View rl_notification,rl_support,rl_comment;
	private ImageView ci_comment,ci_suppport,ci_notification;
	private TextView tv_newcomment,tv_newsupport,tv_newsup;
	private ArrayList<NotificationEntity> notifications = new ArrayList<NotificationEntity>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_message, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		rl_notification = view.findViewById(R.id.rl_notification);
		rl_support = view.findViewById(R.id.rl_support);
		rl_comment = view.findViewById(R.id.rl_comment);
		tv_newcomment = (TextView) view.findViewById(R.id.tv_newcomment);
		tv_newsupport = (TextView) view.findViewById(R.id.tv_newsupport);
		tv_newsup = (TextView) view.findViewById(R.id.tv_newsup);
		rl_comment.setOnClickListener(this);
		rl_notification.setOnClickListener(this);
		rl_support.setOnClickListener(this);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (UserManager.getInstance().isLogin()) {
				NotificationRequest request = new NotificationRequest(
						UserManager.getInstance().getUserInfo().getUid(), 1, 100,
						 new Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								try {
									if (response != null && response.getInt("result") == 1) {
										notifications.clear();
										int new_total = Integer.parseInt(response.getString("newtotal"));
										int total = Integer.parseInt(response.getString("total"));
										JSONArray jsonArray = response.getJSONArray("data");
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
											notifications.add(notificationEntity);
										}
										int commentcount = 0;
										int supportcount = 0;
										int notificationcount = 0;
										for(NotificationEntity notificationEntity:notifications){
											if ("agree".equals(notificationEntity.getType())) {
												if (notificationEntity.getIs_new()) {
													supportcount++;
												}
											}
											else if ("comment".equals(notificationEntity.getType())) {
												if (notificationEntity.getIs_new()) {
													commentcount++;
												}
											}
											else if ("friend".equals(notificationEntity.getType())) {
												if (notificationEntity.getIs_new()) {
													notificationcount++;
												}
												
											}
											else if ("invite".equals(notificationEntity.getType())) {
												if (notificationEntity.getIs_new()) {
													notificationcount++;
												}											}
										}
										
										tv_newcomment.setText(commentcount+"个新评论");
										tv_newsupport.setText(supportcount+"个新支持");
										if (supportcount == 0) {
											tv_newsupport.setVisibility(View.GONE);
										}
										if (commentcount == 0) {
											tv_newcomment.setVisibility(View.GONE);
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_notification:
			startActivity(new Intent(mContext, NotificationListActivity.class));
			break;
		case R.id.rl_support:
			startActivity(new Intent(mContext, SupportListActivity.class));
			break;
		case R.id.rl_comment:
			startActivity(new Intent(mContext, CommentsListActivity.class));
			break;

		default:
			break;
		}
	}
}
