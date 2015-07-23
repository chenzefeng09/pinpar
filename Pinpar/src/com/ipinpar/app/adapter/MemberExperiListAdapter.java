package com.ipinpar.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipinpar.app.Constant;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.AcStatementEntity;
import com.ipinpar.app.view.CircleImageView;
import com.ipinpar.app.view.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MemberExperiListAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<AcStatementEntity> acMemberExperiList = new ArrayList<AcStatementEntity>();
	private DisplayImageOptions options;

	public MemberExperiListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	
	
	public MemberExperiListAdapter(Context mContext,
			ArrayList<AcStatementEntity> acMemberExperiList) {
		this.mContext = mContext;
		this.acMemberExperiList = acMemberExperiList;
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
		return convertView;
	}
	
	public class ViewHolder{
		CircularImageView userImage;// 头像图片
		TextView name; // 用户名
		TextView time; // 发布时间
		TextView content; // 评论体
		TextView support; // 支持数
		TextView comment; // 评论数
		TextView reason;  //幸运理由
	}

}
