package com.ipinpar.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.ipinpar.app.Constant;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.CommentDetailActivity;
import com.ipinpar.app.activity.LoginActivity;
import com.ipinpar.app.activity.NameCardActivity;
import com.ipinpar.app.entity.AcStatementEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.view.CircleImageView;
import com.ipinpar.app.view.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MemberExperiListAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<AcStatementEntity> acMemberExperiList = new ArrayList<AcStatementEntity>();
	private RequestQueue queue;
	private DisplayImageOptions options;

//	public MemberExperiListAdapter(Context mContext) {
//		this.mContext = mContext;
//	}
//
//	
//	
//	public MemberExperiListAdapter(Context mContext,
//			ArrayList<AcStatementEntity> acMemberExperiList) {
//		this.mContext = mContext;
//		this.acMemberExperiList = acMemberExperiList;
//		options = new DisplayImageOptions.Builder().
//				cacheOnDisk(false).build();
//	}
	
	
	
	public MemberExperiListAdapter(Context mContext,
			ArrayList<AcStatementEntity> acMemberExperiList,RequestQueue queue) {
		this.mContext = mContext;
		this.acMemberExperiList = acMemberExperiList;
		this.queue = queue;
		
		options = new DisplayImageOptions.Builder().
				cacheOnDisk(false).build();
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return acMemberExperiList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return acMemberExperiList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void addList(ArrayList<AcStatementEntity> activityList){
		acMemberExperiList.addAll(activityList);
	}
	
	public void clearList(){
		acMemberExperiList.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AcStatementEntity acMemberExperiEntity = acMemberExperiList.get(position);
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater) mContext.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
			convertView = vi.inflate(R.layout.memberexperi_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.userImage =(CircularImageView) convertView.findViewById(R.id.memberexperi_image);
			viewHolder.name = (TextView) convertView.findViewById(R.id.memberexperi_user_name);
			viewHolder.time = (TextView) convertView.findViewById(R.id.memberexperi_time);
			viewHolder.content = (TextView) convertView.findViewById(R.id.memberexperi_content);
			viewHolder.support = (TextView) convertView.findViewById(R.id.tv_memberexperi_support_num);
			viewHolder.comment = (TextView) convertView.findViewById(R.id.tv_memberexperi_comment_num);
			viewHolder.RL_support = convertView.findViewById(R.id.RL_memberexperi_support);
			viewHolder.RL_comment = convertView.findViewById(R.id.RL_memberexperi_comment);
			viewHolder.iv_memberexperi_support = (ImageView) convertView.findViewById(R.id.iv_memberexperi_support);
			viewHolder.reason = (TextView) convertView.findViewById(R.id.tv_luck_reason);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ImageLoader.getInstance().displayImage(Constant.URL_GET_USERIMAGE+acMemberExperiEntity.getUid(), viewHolder.userImage,options);
		
		viewHolder.name.setText(acMemberExperiEntity.getUsername());
		
		long time = Long.parseLong(acMemberExperiEntity.getCreatetime())*1000;
		viewHolder.time.setText(DateFormat.format("yyyy/MM/dd    kk:mm", time));
		
		viewHolder.content.setText(acMemberExperiEntity.getDeclaration());
		viewHolder.support.setText(acMemberExperiEntity.getAgreecount()+"");
		viewHolder.comment.setText(acMemberExperiEntity.getCommentcount()+"");
		viewHolder.reason.setText(acMemberExperiEntity.getReason());
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.startActivity(CommentDetailActivity.getIntent2Me(mContext, acMemberExperiEntity.getEnrollid(),"enrollid"));
			}
		});
		
		viewHolder.RL_support.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getInstance().isLogin()) {
					if (AgreeManager.getInstance().isAgreed(acMemberExperiEntity.getEnrollid(), "enrollid")) {
						AgreeManager.getInstance().agree(
								acMemberExperiEntity.getEnrollid(), 
								"enrollid", new AgreeResultListener() {
									
									@Override
									public void onAgreeResult(boolean agree) {
										if (!agree) {
											acMemberExperiEntity.setAgreecount(acMemberExperiEntity.getAgreecount() - 1);
										}
										notifyDataSetChanged();
									}
								}, queue);
					}
					else {
						AgreeManager.getInstance().agree(
								acMemberExperiEntity.getEnrollid(), 
								"enrollid", new AgreeResultListener() {
									
									@Override
									public void onAgreeResult(boolean agree) {
										if (agree) {
											acMemberExperiEntity.setAgreecount(acMemberExperiEntity.getAgreecount() + 1);
										}
										notifyDataSetChanged();
									}
								}, queue);
					}
				}
				else {
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
				}
			}
		});
		if (AgreeManager.getInstance().isAgreed(acMemberExperiEntity.getEnrollid(), "enrollid")) {
			viewHolder.iv_memberexperi_support.setImageResource(R.drawable.enroll_fist);
		}
		else {
			viewHolder.iv_memberexperi_support.setImageResource(R.drawable.ac_support);
		}
		viewHolder.userImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!UserManager.getInstance().isLogin() || Integer.parseInt(acMemberExperiEntity.getUid()) != UserManager.getInstance().getUserInfo().getUid()) {
					mContext.startActivity(NameCardActivity.getIntent2Me(mContext, Integer.parseInt(acMemberExperiEntity.getUid())));
				}
			}
		});
		
		return convertView;
	}
	
	public class ViewHolder{
		CircularImageView userImage;// 头像图片
		TextView name; // 用户名
		TextView time; // 发布时间
		TextView content; // 评论体
		TextView support; // 支持数
		TextView comment; // 评论数
		View RL_support,RL_comment;
		ImageView iv_memberexperi_support;
		TextView reason;  //幸运理由
	}

}
