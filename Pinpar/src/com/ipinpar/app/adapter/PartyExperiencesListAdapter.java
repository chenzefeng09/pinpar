package com.ipinpar.app.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.Constant;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.CommentDetailActivity;
import com.ipinpar.app.activity.ExperienceDiaryActivity;
import com.ipinpar.app.activity.LoginActivity;
import com.ipinpar.app.activity.NameCardActivity;
import com.ipinpar.app.activity.PartyCommentDetailActivity;
import com.ipinpar.app.entity.AcStatementEntity;
import com.ipinpar.app.entity.PartyExperienceEntity;
import com.ipinpar.app.entity.PartyExperiencesListEntity;
import com.ipinpar.app.entity.PartyUserInfoEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.PartyAgreeRequest;
import com.ipinpar.app.network.api.PartyGetUserInfoRequest;
import com.ipinpar.app.view.CircularImageView;
import com.ipinpar.app.widget.PartyAgreeDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PartyExperiencesListAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<PartyExperienceEntity> partyExperiencesList = new ArrayList<PartyExperienceEntity>();
	private RequestQueue queue;
	private DisplayImageOptions options;
	
	private PartyAgreeDialog partyAgreeDialog;
	private PartyAgreeRequest partyAgreeRequest;

	public PartyExperiencesListAdapter(Context mContext,
			ArrayList<PartyExperienceEntity> partyExperiencesList,RequestQueue queue) {
		this.mContext = mContext;
		this.partyExperiencesList = partyExperiencesList;
		this.queue = queue;
		
		options = new DisplayImageOptions.Builder().
				cacheOnDisk(false).build();
		
		partyAgreeDialog = new PartyAgreeDialog(mContext,  
                R.layout.dialog_party_agree, R.style.PartyDialogTheme);  
		
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partyExperiencesList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return partyExperiencesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void addList(ArrayList<PartyExperienceEntity> experiencesList){
		partyExperiencesList.addAll(experiencesList);
	}
	
	public void clearList(){
		partyExperiencesList.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final PartyExperienceEntity partyExperienceEntity = partyExperiencesList.get(position);
		final ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater) mContext.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
			convertView = vi.inflate(R.layout.party_experiences_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.userImage =(CircularImageView) convertView.findViewById(R.id.party_experiences_image);
			viewHolder.name = (TextView) convertView.findViewById(R.id.party_experiences_user_name);
			viewHolder.time = (TextView) convertView.findViewById(R.id.party_experiences_time);
			viewHolder.content = (TextView) convertView.findViewById(R.id.tv_party_experiences_content);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_party_experiences_content_image);
			viewHolder.support = (TextView) convertView.findViewById(R.id.tv_party_experiences_support_num);
			viewHolder.comment = (TextView) convertView.findViewById(R.id.tv_party_experiences_comment_num);
			viewHolder.RL_support = convertView.findViewById(R.id.RL_party_experiences_support);
			viewHolder.RL_comment = convertView.findViewById(R.id.RL_party_experiences_comment);
			viewHolder.iv_party_experiences_support = (ImageView) convertView.findViewById(R.id.iv_party_experiences_support);
			
//			viewHolder.tvPartyAgreeTip = (TextView) convertView.findViewById(R.id.tv_party_agree_tip);
			viewHolder.RL_creative = partyAgreeDialog.findViewById(R.id.RL_dialog_party_agree_creative);
			viewHolder.RL_funny = partyAgreeDialog.findViewById(R.id.RL_dialog_party_agree_funny);
			viewHolder.RL_practical = partyAgreeDialog.findViewById(R.id.RL_dialog_party_agree_practical);
			viewHolder.RL_confused = partyAgreeDialog.findViewById(R.id.RL_dialog_party_agree_confused);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ImageLoader.getInstance().displayImage(Constant.URL_GET_USERIMAGE+partyExperienceEntity.getAuthorid(), viewHolder.userImage,options);
		
		viewHolder.name.setText(partyExperienceEntity.getUsername());
		
		long time = Long.parseLong(partyExperienceEntity.getCreatetime());
		
		viewHolder.time.setText(formatTime(time));
		
//		if(partyExperienceEntity.getContent() == ""){
//			viewHolder.content.setVisibility(View.GONE);
//		}else{
			viewHolder.content.setText(partyExperienceEntity.getContent());
//		}
		
//		if(partyExperienceEntity.getImg() == ""){
//			viewHolder.image.setVisibility(View.GONE);
//		}else{
			ImageLoader.getInstance().displayImage(partyExperienceEntity.getImg(),viewHolder.image,options);
//		}
		
		viewHolder.support.setText(partyExperienceEntity.getAgreecount()+"");
		viewHolder.comment.setText(partyExperienceEntity.getCommentcount()+"");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.startActivity(PartyCommentDetailActivity.getIntent2Me(mContext, partyExperienceEntity.getExperiencingid(), "experiencingid"));
			}
		});
		
		viewHolder.RL_support.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getInstance().isLogin()) {
					//已经点过赞
					if (AgreeManager.getInstance().isAgreed(partyExperienceEntity.getExperiencingid(), "experiencingid")) {
//						AgreeManager.getInstance().agree(
//								partyExperienceEntity.getExperiencingid(), 
//								"experiencingid", new AgreeResultListener() {
//									
//									@Override
//									public void onAgreeResult(boolean agree) {
//										if (!agree) {
//											partyExperienceEntity.setAgreecount(partyExperienceEntity.getAgreecount() - 1);
//										}
//										notifyDataSetChanged();
//									}
//								}, queue);
						Toast.makeText(mContext, "请不要重复点赞哦～", 1000).show();
					}
					else {
//						AgreeManager.getInstance().partyAgree(
//								partyExperienceEntity.getExperiencingid(), 
//								"experiencingid", new AgreeResultListener() {
//									
//									@Override
//									public void onAgreeResult(boolean agree) {
//										if (agree) {
//											partyExperienceEntity.setAgreecount(partyExperienceEntity.getAgreecount() + 1);
//										}
//										notifyDataSetChanged();
//									}
//								}, queue);
						partyAgreeDialog.show();
					}
				}
				else {
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
				}
			}
		});
		viewHolder.RL_creative.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				partyAgreeDialog.dismiss();
//				viewHolder.tvPartyAgreeTip.setText("创意值 ＋1");
				Message msg = new Message();
				msg.arg1 = 1;
				msg.arg2 = partyExperienceEntity.getAuthorid();
				msg.what = 0;
				handlerAgreeRequest.sendMessage(msg);
				
			}
		});
		viewHolder.RL_funny.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				partyAgreeDialog.dismiss();
				
//				viewHolder.tvPartyAgreeTip.setText("搞笑值 ＋1");
				Message msg = new Message();
				msg.arg1 = 2;
				msg.arg2 = partyExperienceEntity.getAuthorid();
				msg.what = 0;
				handlerAgreeRequest.sendMessage(msg);
				
			}
		});
		viewHolder.RL_practical.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				partyAgreeDialog.dismiss();
				
//				viewHolder.tvPartyAgreeTip.setText("实用值 ＋1");
				Message msg = new Message();
				msg.arg1 = 3;
				msg.arg2 = partyExperienceEntity.getAuthorid();
				msg.what = 0;
				handlerAgreeRequest.sendMessage(msg);
			}
		});
		viewHolder.RL_confused.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				partyAgreeDialog.dismiss();
				
//				viewHolder.tvPartyAgreeTip.setText("太囧值 ＋1");
				Message msg = new Message();
				msg.arg1 = 4;
				msg.arg2 = partyExperienceEntity.getAuthorid();
				msg.what = 0;
				handlerAgreeRequest.sendMessage(msg);
			}
		});

		if (AgreeManager.getInstance().isAgreed(partyExperienceEntity.getExperiencingid(), "experiencingid")) {
			viewHolder.iv_party_experiences_support.setImageResource(R.drawable.enroll_fist);
		}
		else {
			viewHolder.iv_party_experiences_support.setImageResource(R.drawable.ac_support);
		}
		viewHolder.userImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!UserManager.getInstance().isLogin() || partyExperienceEntity.getAuthorid() != UserManager.getInstance().getUserInfo().getUid()) {
					mContext.startActivity(NameCardActivity.getIntent2Me(mContext, partyExperienceEntity.getAuthorid()));
				}
			}
		});
		return convertView;
	}
	
	private CharSequence formatTime(long dateline) {
		// TODO Auto-generated method stub
		long deltatime = System.currentTimeMillis()/1000 - dateline;
		if (deltatime < 0) {
			deltatime = 1;
		}
		if (deltatime/(24*60*60)>0) {
			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd    kk:mm");
			
			return date.format(new Date(dateline*1000));
		}
		else if (deltatime/(60*60)>0) {
			return deltatime/(60*60)+"小时前";
		}
		else if (deltatime/(60)>0) {
			return deltatime/(60)+"分钟前";
		}
		else {
			return deltatime+"秒之前";
		}
	}
	
	Handler handlerAgreeRequest = new Handler(){
		int agreeWeight;
		int fromId;
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			
			agreeWeight = msg.arg1;
			fromId = msg.arg2;
			
			switch(msg.what){
			
			case 0:
				partyAgreeRequest = new PartyAgreeRequest(
						UserManager.getInstance().getUserInfo().getUid()+"",
						"experiencingid",
						fromId+"",
						agreeWeight+"",
						new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							if (response != null && response.getInt("result") == 1) {
								Toast.makeText(mContext, "点赞成功！", 1000).show();;
								
							}
							else if(response != null && response.getInt("result") == 101){
								Toast.makeText(mContext, "请不要重复点赞哦～", 1000).show();

							}else{
								Toast.makeText(mContext, "点赞失败，请重试", 1000).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
				});
				queue.add(partyAgreeRequest);
				break;
			default:
				break;
			}
		}
		
	};
	
	public class ViewHolder{
		CircularImageView userImage;// 头像图片
		TextView name; // 用户名
		TextView time; // 发布时间
		TextView content; // 评论体
		ImageView image;
		TextView support; 
		TextView comment; 
		View RL_support,RL_comment;
		ImageView iv_party_experiences_support;
		
//		TextView tvPartyAgreeTip;
		View RL_creative,RL_funny,RL_practical,RL_confused;
	}

}
