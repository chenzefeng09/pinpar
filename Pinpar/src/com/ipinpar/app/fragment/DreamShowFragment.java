package com.ipinpar.app.fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.CommentDetailActivity;
import com.ipinpar.app.activity.DreamShowPublishActivity;
import com.ipinpar.app.activity.LoginActivity;
import com.ipinpar.app.activity.NameCardActivity;
import com.ipinpar.app.activity.PastedDreamShowActivity;
import com.ipinpar.app.activity.ShowBigImage;
import com.ipinpar.app.entity.CurrDreamShowEntity;
import com.ipinpar.app.entity.DreamShowEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.CurrDreamShowRequest;
import com.ipinpar.app.network.api.DreamShowListRequest;
import com.ipinpar.app.widget.PullToRefreshWhiteHeaderListView;
import com.ipinpar.app.widget.PullToRefreshWhiteHeaderListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DreamShowFragment extends PPBaseFragment implements OnClickListener{
	private View view;

	private ImageView iv_write_dream,iv_img,iv_dream;
	private TextView tv_name_curr,tv_time_curr,tv_text_content,tv_dream_state,tv_see_more;
	private PullToRefreshWhiteHeaderListView el_other_dream;
	
	private CurrDreamShowEntity currDream;
	private ArrayList<DreamShowEntity> dreamShows = new ArrayList<DreamShowEntity>();

	private View rl_curr_dream;
	private DreamShowListAdapter adapter;
	private View listHeader;
	
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dream_show, null);
		listHeader = getActivity().getLayoutInflater().inflate(R.layout.view_dream_show_header, null);
		initView();
		refreshDreamList();

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
		}

	private void initView() {
		el_other_dream = (PullToRefreshWhiteHeaderListView) view.findViewById(R.id.el_other_dream);
		el_other_dream.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				refreshDreamList();
			}
		});
		iv_write_dream = (ImageView) view.findViewById(R.id.iv_write_dream);
		iv_img = (ImageView) listHeader.findViewById(R.id.iv_img);
		iv_dream = (ImageView) listHeader.findViewById(R.id.iv_dream);
		tv_name_curr = (TextView) listHeader.findViewById(R.id.tv_name_curr);
		tv_time_curr = (TextView) listHeader.findViewById(R.id.tv_time_curr);
		tv_text_content = (TextView) listHeader.findViewById(R.id.tv_text_content);
		tv_dream_state = (TextView) listHeader.findViewById(R.id.tv_dream_state);
		tv_see_more = (TextView) listHeader.findViewById(R.id.tv_see_more);
		
		rl_curr_dream = listHeader.findViewById(R.id.rl_curr_dream);
		tv_see_more.setOnClickListener(this);
		iv_write_dream.setOnClickListener(this);
		iv_img.setOnClickListener(this);
		rl_curr_dream.setOnClickListener(this);
		el_other_dream.addHeaderView(listHeader);
	}
	
	@Override
	public void onResume() {
		super.onResume();

	}
	
	private void setUpView(){
		ImageLoader.getInstance().displayImage(currDream.imgsrc, iv_img);
		tv_name_curr.setText(PPApplication.getInstance().getFormatString(R.string.dream_show_who_says, currDream.username));
		tv_time_curr.setText(formatTime(currDream.createtime*1000));
		tv_text_content.setText(currDream.title);
		tv_dream_state.setText(currDream.detail);
		if (TextUtils.isEmpty(currDream.detail)) {
			tv_dream_state.setVisibility(View.GONE);
		}
		else {
			tv_dream_state.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(currDream.mainimg)) {
			iv_dream.setVisibility(View.GONE);
		}
		else {
//			iv_dream.setVisibility(View.VISIBLE);
//			ImageLoader.getInstance().displayImage(currDream.mainimg, iv_dream);
			iv_dream.setVisibility(View.GONE);
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
	
	private void refreshDreamList(){
		CurrDreamShowRequest request = new CurrDreamShowRequest( new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					if (response!= null && response.getInt("result") == 1) {
						Gson gson = new Gson();
						Type type = new TypeToken<ArrayList<CurrDreamShowEntity>>() {
						}.getType();
						ArrayList<CurrDreamShowEntity> dreams = gson.fromJson(response.getJSONArray("dreams").toString(),type);
						if (dreams != null && dreams.size()!=0) {
							currDream = dreams.get(0);
						}
						setUpView();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("dream list json", response.toString());
			}
		});
		apiQueue.add(request);
		
		DreamShowListRequest request2 = new DreamShowListRequest(new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				if (el_other_dream!=null) {
					el_other_dream.onRefreshComplete();
				}
				try {
					if (response!= null && response.getInt("result") == 1) {
						
						Gson gson = new Gson();
						Type type = new TypeToken<ArrayList<DreamShowEntity>>() {
						}.getType();
						ArrayList<DreamShowEntity> dreams = gson.fromJson(response.getJSONArray("dreams").toString(),type);
						if (dreams != null && dreams.size()!=0) {
								adapter = new DreamShowListAdapter(dreams);
								el_other_dream.setAdapter(adapter);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("dream list json 2", response.toString());

			}
		});
		apiQueue.add(request2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_curr_dream:
		case R.id.tv_see_more:
			startActivity(new Intent(mContext, PastedDreamShowActivity.class));
			break;
			
		case R.id.iv_write_dream:
			if (UserManager.getInstance().isLogin()) {
				startActivity(new Intent(mContext, DreamShowPublishActivity.class));
			}
			else {
				startActivity(new Intent(mContext, LoginActivity.class));
			}
			break;
			
		case R.id.iv_img:
			
			break;

		default:
			break;
		}
		
	}
	
	private class DreamShowListAdapter extends BaseAdapter{
		private ArrayList<DreamShowEntity> dreams;
		private ViewHolder holder;
		
		public DreamShowListAdapter(ArrayList<DreamShowEntity> dreams) {
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
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_normal_dream, null);
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
			if (!TextUtils.isEmpty(dreamShowEntity.detail)) {
				holder.tv_dream_state.setText(dreamShowEntity.detail);
				holder.tv_dream_state.setVisibility(View.VISIBLE);
			}
			else {
				holder.tv_dream_state.setVisibility(View.GONE);

			}
			
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
				holder.iv_dream.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, ShowBigImage.class);
						intent.putExtra("remotepath", dreamShowEntity.author_img);
						mContext.startActivity(intent);
					}
				});
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
					startActivity(CommentDetailActivity.getIntent2Me(getActivity(), dreamShowEntity.dreamid, "dreamid","详情"));
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
}
