package com.ipinpar.app.activity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.DreamShowEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.network.api.MyDreamListRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyDreamsActivity extends PPBaseActivity {
	
	private ListView iv_my_dreams;
	private MyDreamShowListAdapter adapter;
	private ArrayList<DreamShowEntity> my_dreams;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_my_dream_show);
		my_dreams = new ArrayList<DreamShowEntity>();
		iv_my_dreams = (ListView) findViewById(R.id.iv_my_dreams);
	}
	
	private void refreshMyDreams(){
		showProgressDialog();
		MyDreamListRequest request = new MyDreamListRequest(
				UserManager.getInstance().getUserInfo().getUid(), 
				0, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						dissmissProgressDialog();
						try {
							if (response != null && response.getInt("result") == 1) {
								Gson gson = new Gson();
								Type type = new TypeToken<ArrayList<DreamShowEntity>>() {
								}.getType();
								ArrayList<DreamShowEntity> dreams = gson.fromJson(response.getJSONArray("dreams").toString(),type);
								my_dreams.clear();
								my_dreams.addAll(dreams);
								if (adapter == null) {
									adapter = new MyDreamShowListAdapter(my_dreams);
									iv_my_dreams.setAdapter(adapter);
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshMyDreams();
	}

	
	private class MyDreamShowListAdapter extends BaseAdapter{
		private ArrayList<DreamShowEntity> dreams;
		private ViewHolder holder;
		
		public MyDreamShowListAdapter(ArrayList<DreamShowEntity> dreams) {
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
			final DreamShowEntity dreamShowEntity = dreams.get(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_item_normal_dream, null);
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
				holder.RL_comment = convertView.findViewById(R.id.RL_comment);
				holder.RL_support = convertView.findViewById(R.id.RL_support);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(dreamShowEntity.imgsrc, holder.iv_img);
			holder.tv_dream_state.setText(dreamShowEntity.detail);
			holder.tv_name.setText(PPApplication.getInstance().getFormatString(R.string.dream_show_who_says, dreamShowEntity.username));
			holder.tv_text_content.setText(dreamShowEntity.title);
			holder.tv_time.setText(formatTime(dreamShowEntity.createtime*1000));
			holder.tv_statement_comment_num.setText(dreamShowEntity.commentcount+"");
			holder.tv_statement_support_num.setText(dreamShowEntity.agreecount+"");
			if (AgreeManager.getInstance().isAgreed(dreamShowEntity.dreamid, "dreamid")) {
				holder.iv_statement_support.setImageResource(R.drawable.enroll_fist);
			}
			else {
				holder.iv_statement_support.setImageResource(R.drawable.ac_support);
			}
			if (TextUtils.isEmpty(dreamShowEntity.author_img)) {
				holder.iv_dream.setVisibility(View.GONE);
			}
			else {
				holder.iv_dream.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(dreamShowEntity.author_img, holder.iv_dream);
			}
			holder.RL_support.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (UserManager.getInstance().isLogin()) {
						AgreeManager.getInstance().agree(dreamShowEntity.dreamid, "dreamid", new AgreeResultListener() {
							
							@Override
							public void onAgreeResult(boolean agree) {
								if (agree) {
									dreamShowEntity.agreecount++;
								}
								else {
									dreamShowEntity.agreecount--;
								}
								notifyDataSetChanged();
							}
						}, apiQueue);
					}
					else {
						startActivity(new Intent(mContext, LoginActivity.class));
					}
					
				}
			});
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
			public ImageView iv_img,iv_dream,iv_statement_support,iv_statement_comment;
			public TextView tv_name,tv_time,tv_text_content,tv_dream_state,tv_statement_support_num,tv_statement_comment_num;
			public View RL_support,RL_comment;
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
