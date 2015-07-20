package com.ipinpar.app.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.db.dao.FriendDao;
import com.ipinpar.app.entity.FriendEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.FriendsListRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FriendActivity extends PPBaseActivity {
	
	private ListView lv_friends;
	private MyFriendsAdapter adapter;
	private ArrayList<FriendEntity> friends;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_my_friend);
		lv_friends = (ListView) findViewById(R.id.lv_friends);
		lv_friends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FriendEntity frEntity = friends.get(arg2);
				startActivity(NameCardActivity.getIntent2Me(mContext, frEntity.getUid()));
				
			}
		});
		friends = new ArrayList<FriendEntity>();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		FriendsListRequest request = new FriendsListRequest(
				UserManager.getInstance().getUserInfo().getUid()+"",new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							if (response != null && response.getInt("result") == 1) {
								friends.clear();
								int size = response.getInt("total");
								if (size != 0) {
									JSONArray jsonObject = response.getJSONArray("data");
									Gson gson = new Gson();
									Type listType=new TypeToken<ArrayList<FriendEntity>>(){}.getType();
									friends.addAll((ArrayList<FriendEntity>)gson.fromJson(jsonObject.toString(), listType));
									if (adapter == null) {
										adapter = new MyFriendsAdapter(friends);
										lv_friends.setAdapter(adapter);
									}
									else {
										adapter.notifyDataSetChanged();
									}
									FriendDao.getInstance().insertUsers(friends);
								}
							}
							else {
								Toast.makeText(mContext, "获取好友列表失败，请重试", 1000).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} );
		apiQueue.add(request);
	}
	
	private class MyFriendsAdapter extends BaseAdapter{
		private ArrayList<FriendEntity> friendENtities;
		private ViewHolder holder;
		private DisplayImageOptions options;
		public MyFriendsAdapter(ArrayList<FriendEntity> friendENtities) {
			// TODO Auto-generated constructor stub
			this.friendENtities = friendENtities;
			options = new DisplayImageOptions.Builder().cacheOnDisk(false).
					build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return friendENtities == null?0:friendENtities.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return friendENtities == null ?null:friendENtities.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final FriendEntity friendENtity = friendENtities.get(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_item_friend, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_chat = (TextView) convertView.findViewById(R.id.tv_chat);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_chat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext, ChatActivity.class);
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
					intent.putExtra("userId", friendENtity.getUid()+"");
					startActivity(intent);
				}
			});
			holder.tv_name.setText(friendENtity.getUsername());
			ImageLoader.getInstance().displayImage(friendENtity.getImgsrc(), holder.iv_icon,options);
			if (TextUtils.isEmpty(friendENtity.getImgsrc())) {
				holder.iv_icon.setImageResource(R.drawable.defaultavatarmale);
			}
			return convertView;
		}
		
		private  class ViewHolder{
			public ImageView iv_icon;
			public TextView tv_name;
			public TextView tv_chat;
		}
		
	}

}
