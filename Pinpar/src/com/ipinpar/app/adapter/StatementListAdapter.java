package com.ipinpar.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.CommentDetailActivity;
import com.ipinpar.app.activity.LoginActivity;
import com.ipinpar.app.entity.AcStatementEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.view.CircularImageView;

public class StatementListAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<AcStatementEntity> acStatementList = new ArrayList<AcStatementEntity>();
	private RequestQueue queue;

//	public StatementListAdapter(Context mContext) {
//		this.mContext = mContext;
//	}
//
//	
//	
//	public StatementListAdapter(Context mContext,
//			ArrayList<AcStatementEntity> acStatementList) {
//		this.mContext = mContext;
//		this.acStatementList = acStatementList;
//	}
	
	public StatementListAdapter(Context mContext,
			ArrayList<AcStatementEntity> acStatementList,RequestQueue queue) {
		this.mContext = mContext;
		this.acStatementList = acStatementList;
		this.queue = queue;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return acStatementList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return acStatementList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void addList(ArrayList<AcStatementEntity> activityList){
		acStatementList.addAll(activityList);
	}
	
	public void clearList(){
		acStatementList.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AcStatementEntity acStatementEntity = acStatementList.get(position);
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater) mContext.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
			convertView = vi.inflate(R.layout.statement_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.userImage =(CircularImageView) convertView.findViewById(R.id.statement_image);
			viewHolder.name = (TextView) convertView.findViewById(R.id.statement_user_name);
			viewHolder.time = (TextView) convertView.findViewById(R.id.statement_time);
			viewHolder.content = (TextView) convertView.findViewById(R.id.statement_content);
			viewHolder.support = (TextView) convertView.findViewById(R.id.tv_statement_support_num);
			viewHolder.comment = (TextView) convertView.findViewById(R.id.tv_statement_comment_num);
			viewHolder.RL_support = convertView.findViewById(R.id.RL_support);
			viewHolder.RL_comment = convertView.findViewById(R.id.RL_comment);
			viewHolder.iv_statement_support = (ImageView) convertView.findViewById(R.id.iv_statement_support);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.name.setText(acStatementEntity.getUsername());
		
		long time = Long.parseLong(acStatementEntity.getCreatetime())*1000;
		viewHolder.time.setText(DateFormat.format("yyyy/MM/dd    kk:mm", time));
		
		viewHolder.content.setText(acStatementEntity.getDeclaration());
		viewHolder.support.setText(acStatementEntity.getAgreecount()+"");
		viewHolder.comment.setText(acStatementEntity.getCommentcount()+"");
		viewHolder.RL_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.startActivity(CommentDetailActivity.getIntent2Me(mContext, acStatementEntity));
				
			}
		});
		
		viewHolder.RL_support.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getInstance().isLogin()) {
					if (AgreeManager.getInstance().isAgreed(acStatementEntity.getEnrollid(), "enrollid")) {
						AgreeManager.getInstance().agree(
								UserManager.getInstance().getUserInfo().getUid(), 
								"enrollid", new AgreeResultListener() {
									
									@Override
									public void onAgreeResult(boolean agree) {
										notifyDataSetChanged();
									}
								}, queue);
					}
					else {
						AgreeManager.getInstance().agree(
								UserManager.getInstance().getUserInfo().getUid(), 
								"enrollid", new AgreeResultListener() {
									
									@Override
									public void onAgreeResult(boolean agree) {
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
		if (AgreeManager.getInstance().isAgreed(acStatementEntity.getEnrollid(), "enrollid")) {
			viewHolder.iv_statement_support.setImageResource(R.drawable.ac_support);
		}
		else {
			viewHolder.iv_statement_support.setImageResource(R.drawable.ac_support);
		}
		return convertView;
	}
	
	public class ViewHolder{
		CircularImageView userImage;// 头像图片
		TextView name; // 用户名
		TextView time; // 发布时间
		TextView content; // 评论体
		TextView support; // 评论体
		TextView comment; // 评论体
		View RL_support,RL_comment;
		ImageView iv_statement_support;
	}

}
