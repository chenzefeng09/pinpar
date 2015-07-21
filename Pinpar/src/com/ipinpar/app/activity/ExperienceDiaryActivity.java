package com.ipinpar.app.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.ExperienceDiaryDetailEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.network.api.ExperienceDiaryRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ExperienceDiaryActivity extends PPBaseActivity {
	
	private ListView lv_diary;
	private ImageView iv_icon,iv_title_bkg,iv_agree,iv_comment;
	private TextView tv_name;
	private Button btn_agree,btn_comment;
	private View ll_comment,ll_agree;
	
	private ImageView shareView;
	private String shareTitle;
	private String shareImageUrl;
	
	private int shareSid;
	
	private ArrayList<ExperienceDiaryDetailEntity> experienceDetials;
	private int expid;
	private int agreecount,commentcount;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_experience_diary);
		final int sid = getIntent().getIntExtra("sid", 0);
		int uid = getIntent().getIntExtra("uid", 0);
		int activityid = getIntent().getIntExtra("activityid", 0);
		btn_agree = (Button) findViewById(R.id.btn_agree);
		btn_comment = (Button) findViewById(R.id.btn_comment);
		lv_diary = (ListView) findViewById(R.id.lv_diary);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_title_bkg = (ImageView) findViewById(R.id.iv_title_bkg);
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_agree = (ImageView) findViewById(R.id.iv_agree);
		iv_comment = (ImageView) findViewById(R.id.iv_comment);
		ll_comment = findViewById(R.id.ll_comment);
		ll_agree = findViewById(R.id.ll_agree);
		
		ll_agree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getInstance().isLogin()) {
					showProgressDialog();
					if (AgreeManager.getInstance().isAgreed(expid, "sid")) {
						AgreeManager.getInstance().agree(
								expid, 
								"sid", new AgreeResultListener() {
									
									@Override
									public void onAgreeResult(boolean agree) {
										dissmissProgressDialog();
										if (!agree) {
											iv_agree.setImageResource(R.drawable.experience_diary_like);
											btn_agree.setText((--agreecount)+"");
										}
									}
								}, apiQueue);
					}
					else {
						AgreeManager.getInstance().agree(
								expid, 
								"sid", new AgreeResultListener() {
									
									@Override
									public void onAgreeResult(boolean agree) {
										dissmissProgressDialog();
										if (agree) {
											iv_agree.setImageResource(R.drawable.experience_diary_like_click);
											btn_agree.setText((++agreecount)+"");										}
									}
								}, apiQueue);
					}
				}
				else {
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
				}
			}
		});
		ll_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.startActivity(CommentDetailActivity.getIntent2Me(mContext, expid,"sid"));
			}
		});
		showProgressDialog();
		if (sid != 0){
			ExperienceDiaryRequest request = new ExperienceDiaryRequest(sid, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					dissmissProgressDialog();
					try {
						if (response != null && response.getInt("result") == 1) {
							ImageLoader.getInstance().displayImage(response.getString("img"), iv_title_bkg);
							ImageLoader.getInstance().displayImage("http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="+response.getInt("uid"), iv_icon);
							tv_name.setText(response.getString("title"));
							agreecount = response.getInt("agreecount");
							commentcount = response.getInt("commentcount");
							expid = response.getInt("sid");
							if (AgreeManager.getInstance().isAgreed(expid, "sid")) {
								iv_agree.setImageResource(R.drawable.experience_diary_like_click);
							}
							btn_agree.setText(agreecount+"");
							btn_comment.setText(commentcount+"");
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<ExperienceDiaryDetailEntity>>(){}.getType();
							experienceDetials = gson.fromJson(response.getJSONArray("details").toString(), type);
							
							//分享中用到到字段
							shareTitle = response.getString("title");
							shareImageUrl = changeShareImageUrl(response.getString("img"));
							shareSid = sid;
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			apiQueue.add(request);
		}
		else if (uid != 0 && activityid != 0) {
			ExperienceDiaryRequest request = new ExperienceDiaryRequest(uid,activityid, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					dissmissProgressDialog();
					try {
						if (response != null && response.getInt("result") == 1) {
							ImageLoader.getInstance().displayImage(response.getString("img"), iv_title_bkg);
							ImageLoader.getInstance().displayImage("http://api.ipinpar.com/pinpaV2/api.pinpa?protocol=10008&a="+response.getInt("uid"), iv_icon);
							tv_name.setText(response.getString("title"));
							expid = response.getInt("sid");
							if (AgreeManager.getInstance().isAgreed(expid, "sid")) {
								iv_agree.setImageResource(R.drawable.experience_diary_like_click);
							}
							agreecount = response.getInt("agreecount");
							commentcount = response.getInt("commentcount");
							btn_agree.setText(agreecount+"");
							btn_comment.setText(commentcount+"");
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<ExperienceDiaryDetailEntity>>(){}.getType();
							experienceDetials = gson.fromJson(response.getJSONArray("details").toString(), type);
							
							//分享中用到到字段
							shareTitle = response.getString("title");
							shareImageUrl = changeShareImageUrl(response.getString("img"));
							shareSid = sid;
							
							DiaryDetailAdapter adapter = new DiaryDetailAdapter(experienceDetials);
							lv_diary.setAdapter(adapter);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			apiQueue.add(request);
		}
		lv_diary = (ListView) findViewById(R.id.lv_diary);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_title_bkg = (ImageView) findViewById(R.id.iv_title_bkg);
		tv_name = (TextView) findViewById(R.id.tv_name);
		shareView = (ImageView) findViewById(R.id.ib_right);
		
		shareView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showShare();
			}
		});
	}
	
	/*
	 * 将ImageURL中的“_bcut”或者“_b”字符串替换成“_s”
	 * */
	public String changeShareImageUrl(String source){
		if((source.indexOf("_bcut") != -1)){
			return source.replace("_bcut", "_s");
		}else{
			return source.replace("_b", "_s");
		}
	}
	
	private void showShare() {
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(shareTitle);
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://www.ipinpar.com");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("小伙伴们来看看这篇体验心得吧～");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://m.ipinpar.com/summary.jsp?sid="+shareSid);
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("品趴——给生活加点糖！");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://m.ipinpar.com/summary.jsp?sid="+shareSid);
		 //图片的网络路径，新浪微博、人人、QQ空间和Linked-in
		 oks.setImageUrl(shareImageUrl);
		// 启动分享GUI
		 oks.show(this);
	}
	
	
	
	
	private class DiaryDetailAdapter extends BaseAdapter{
		private ArrayList<ExperienceDiaryDetailEntity> details;
		private ViewHolder holder;
		public DiaryDetailAdapter(ArrayList<ExperienceDiaryDetailEntity> detailEntities) {
			// TODO Auto-generated constructor stub
			this.details = detailEntities;
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return details == null ? 0 :details.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return details == null ?null : details.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ExperienceDiaryDetailEntity detailEntity = details.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.list_item_experience_diary, null);
				holder.iv_diary_img = (ImageView) convertView.findViewById(R.id.iv_diary_img);
				holder.tv_diary_txt = (TextView) convertView.findViewById(R.id.tv_diary_txt);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (TextUtils.isEmpty(detailEntity.getImg())) {
				holder.iv_diary_img.setVisibility(View.GONE);
			}
			else {
				holder.iv_diary_img.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(detailEntity.getImg(), holder.iv_diary_img);
			}
			holder.tv_diary_txt.setText(detailEntity.getContent());
			return convertView;
		}
		
		private class ViewHolder{
			public ImageView iv_diary_img;
			public TextView tv_diary_txt;
		}
		
	}
	
	public static Intent getIntent2Me(Context context,int sid){
		Intent intent = new Intent(context, ExperienceDiaryActivity.class);
		intent.putExtra("sid", sid);
		return intent;
	}
	
	public static Intent getIntent2Me(Context context,int activityid,int uid){
		Intent intent = new Intent(context, ExperienceDiaryActivity.class);
		intent.putExtra("uid", uid);
		intent.putExtra("activityid", activityid);
		return intent;
	}

}
