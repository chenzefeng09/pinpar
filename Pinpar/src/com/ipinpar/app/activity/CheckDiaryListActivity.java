package com.ipinpar.app.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.ExperienceDiaryEntity;
import com.ipinpar.app.network.api.ExperienceDiaryListRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CheckDiaryListActivity extends PPBaseActivity {
	private ListView lv_diary_list;
	private ArrayList<ExperienceDiaryEntity> experiences;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_chek_exp_diary);
		lv_diary_list = (ListView) findViewById(R.id.lv_diary_list);
		lv_diary_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ExperienceDiaryEntity entity  = experiences.get(arg2);
				startActivity(ExperienceDiaryActivity.getIntent2Me(mContext, entity.getAcid(), entity.getUid()));
			}
		});
		experiences = new ArrayList<ExperienceDiaryEntity>();
		int acid = getIntent().getIntExtra("acid", 0);
		if (acid != 0) {
			showProgressDialog();
			ExperienceDiaryListRequest request = new ExperienceDiaryListRequest(acid, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					dissmissProgressDialog();
					try {
						if (response != null && response.getInt("result") == 1) {
							JSONArray jsonArray = response.getJSONArray("summarys");
							for(int i = 0;i<jsonArray.length();i++){
								JSONObject jsonObject = (JSONObject) jsonArray.get(i);
								ExperienceDiaryEntity entity = new ExperienceDiaryEntity();
								entity.setAcid(jsonObject.getInt("acid"));
								entity.setAgreecount(jsonObject.getInt("agreecount"));
								entity.setCommentcount(jsonObject.getInt("commentcount"));
								entity.setCreatetime(jsonObject.getLong("createtime"));
								entity.setDescription(jsonObject.getString("description"));
								entity.setFlag(jsonObject.getInt("flag"));
								entity.setImg(jsonObject.getString("img"));
								entity.setSid(jsonObject.getInt("sid"));
								entity.setTitle(jsonObject.getString("title"));
								entity.setUid(jsonObject.getInt("uid"));
								entity.setUsername(jsonObject.getString("username"));
								experiences.add(entity);
							}
							ExperiencesAdapter adapter = new ExperiencesAdapter(experiences);
							lv_diary_list.setAdapter(adapter);
							
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
	
	private class ExperiencesAdapter extends BaseAdapter{
		private ArrayList<ExperienceDiaryEntity> expEntities;
		private ViewHolder holder;
		
		public ExperiencesAdapter(ArrayList<ExperienceDiaryEntity> experienceDiaryEntities) {
			// TODO Auto-generated constructor stub
			this.expEntities = experienceDiaryEntities;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return expEntities == null? 0 :expEntities.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return expEntities == null ? null:expEntities.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ExperienceDiaryEntity experienceDiaryEntity = expEntities.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.list_item_all_experience_diary, null);
				holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
				holder.tv_like = (TextView) convertView.findViewById(R.id.tv_like);
				holder.iv_activity_desc = (ImageView) convertView.findViewById(R.id.iv_activity_desc);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.RL_activity_description = convertView.findViewById(R.id.RL_activity_description);
				holder.view_like = convertView.findViewById(R.id.view_like);
				holder.view_comment = convertView.findViewById(R.id.view_comment);
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			holder.tv_time.setText(format.format(new Date(experienceDiaryEntity.getCreatetime()*1000)));
			holder.tv_name.setText(PPApplication.getInstance().getFormatString(R.string.experience_list_name,experienceDiaryEntity.getUsername()));
			holder.tv_title.setText(experienceDiaryEntity.getTitle());
			holder.tv_like.setText(PPApplication.getInstance().getFormatString(R.string.experience_list_like, experienceDiaryEntity.getAgreecount()));
			holder.tv_comment.setText(PPApplication.getInstance().getFormatString(R.string.experience_list_comment, experienceDiaryEntity.getCommentcount()));
			ImageLoader.getInstance().displayImage(experienceDiaryEntity.getImg(), holder.iv_activity_desc);;
			ImageLoader.getInstance().displayImage("http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="+experienceDiaryEntity.getUid(), holder.iv_icon);;
			holder.RL_activity_description.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(ExperienceDiaryActivity.getIntent2Me(mContext, experienceDiaryEntity.getAcid(), experienceDiaryEntity.getUid()));
				}
			});;
			holder.view_like.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
			holder.view_comment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
			return convertView;
		}
		
		private class ViewHolder{
			public TextView tv_like,tv_comment,tv_title,tv_name,tv_time;
			public ImageView iv_activity_desc,iv_icon;
			public View RL_activity_description,view_like,view_comment;
		}
		
	}
	
	public static Intent getIntent2Me(Context context,int acid){
		Intent intent = new Intent(context, CheckDiaryListActivity.class);
		intent.putExtra("acid", acid);
		return intent;
	}

}
