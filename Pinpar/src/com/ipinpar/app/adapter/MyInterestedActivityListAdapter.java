package com.ipinpar.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipinpar.app.R;
import com.ipinpar.app.entity.ActivityEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyInterestedActivityListAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<ActivityEntity> aList = new ArrayList<ActivityEntity>();

	
	
	public MyInterestedActivityListAdapter(Context mContext) {
		this.mContext = mContext;
	}
	
	

	public MyInterestedActivityListAdapter(Context mContext, ArrayList<ActivityEntity> aList) {
		this.mContext = mContext;
		this.aList = aList;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return aList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return aList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void addList(ArrayList<ActivityEntity> activityList){
		aList.addAll(activityList);
	}
	
	public void clearList(){
		aList.clear();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ActivityEntity acEntity = aList.get(position);
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater) mContext.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.my_interested_list_item, null);
			viewHolder = new ViewHolder();
			
			viewHolder.rlEssentialStatement = (RelativeLayout) convertView.findViewById(R.id.RL_essential_statement);
			viewHolder.ivBackground = (ImageView) convertView.findViewById(R.id.iv_activity_desc);
			viewHolder.ivBackgroundCover = (ImageView) convertView.findViewById(R.id.iv_activity_desc_cover);
			viewHolder.ivActivityStateInviting = (ImageView) convertView.findViewById(R.id.iv_activity_state_inviting);
			viewHolder.ivActivityStateComplete = (ImageView) convertView.findViewById(R.id.iv_activity_state_complete);
			viewHolder.tvAcShortName =  (TextView) convertView.findViewById(R.id.tv_first_text_desc);
			viewHolder.tvAcName = (TextView) convertView.findViewById(R.id.tv_second_text_desc);
			viewHolder.tvAcDynamicTitle =  (TextView) convertView.findViewById(R.id.tv_dynamic_title);
			viewHolder.ivEssentialStatement = (ImageView) convertView.findViewById(R.id.iv_essential_statement);
			viewHolder.tvAcDynamicDescription = (TextView) convertView.findViewById(R.id.tv_dynamic_description);
			
			viewHolder.rlActivityDetail = (RelativeLayout) convertView.findViewById(R.id.RL_RL_address_and_date);
			viewHolder.tvAcAddress = (TextView) convertView.findViewById(R.id.tv_activity_address);
			viewHolder.tvAcTimeBegin = (TextView) convertView.findViewById(R.id.tv_activity_time_begin);
			viewHolder.tvAcTimeEnd = (TextView) convertView.findViewById(R.id.tv_activity_time_end);
			
			viewHolder.tvAgreecount = (TextView) convertView.findViewById(R.id.tv_praise_num);
			
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ImageLoader.getInstance().displayImage(acEntity.getImgs().get(0).getImg(), viewHolder.ivBackground);
		ImageLoader.getInstance().displayImage("drawable://"+R.drawable.activity_img_cover, viewHolder.ivBackgroundCover);
		
		if(acEntity.getStatus() == 4){
			viewHolder.rlActivityDetail.setVisibility(View.INVISIBLE);
			viewHolder.rlEssentialStatement.setVisibility(View.VISIBLE);
			viewHolder.ivActivityStateInviting.setVisibility(View.INVISIBLE);
			viewHolder.ivActivityStateComplete.setVisibility(View.VISIBLE);
			viewHolder.ivEssentialStatement.setVisibility(View.INVISIBLE);
			
			viewHolder.tvAcShortName.setText(acEntity.getSname());
			viewHolder.tvAcName.setText(acEntity.getAcname());
			viewHolder.tvAcDynamicTitle.setText(acEntity.getDynamic().get(0).getTitle());
			viewHolder.tvAcDynamicDescription.setText(acEntity.getDynamic().get(0).getDescription());
			
		}else if(acEntity.getStatus() == 3){
			viewHolder.rlActivityDetail.setVisibility(View.INVISIBLE);
			viewHolder.rlEssentialStatement.setVisibility(View.VISIBLE);
			viewHolder.ivActivityStateInviting.setVisibility(View.VISIBLE);
			viewHolder.ivActivityStateComplete.setVisibility(View.INVISIBLE);
			viewHolder.ivEssentialStatement.setVisibility(View.VISIBLE);
			viewHolder.ivEssentialStatement.setVisibility(View.VISIBLE);
			
			viewHolder.tvAcShortName.setText(acEntity.getSname());
			viewHolder.tvAcName.setText(acEntity.getAcname());
			viewHolder.tvAcDynamicTitle.setText(acEntity.getDynamic().get(0).getTitle());
			viewHolder.tvAcDynamicDescription.setText(acEntity.getDynamic().get(0).getDescription());
			
		}else{
			viewHolder.rlActivityDetail.setVisibility(View.VISIBLE);
			viewHolder.rlEssentialStatement.setVisibility(View.INVISIBLE);
			viewHolder.ivActivityStateInviting.setVisibility(View.INVISIBLE);
			viewHolder.ivActivityStateComplete.setVisibility(View.INVISIBLE);
			
			viewHolder.tvAcShortName.setText(acEntity.getSname());
			viewHolder.tvAcName.setText(acEntity.getAcname());
			viewHolder.tvAcAddress.setText(acEntity.getAddress2()+"-"+acEntity.getAddress3());
			
			long timeBegin = Long.parseLong(acEntity.getActivebegintime())*1000;
			long timeEnd = Long.parseLong(acEntity.getActiveendtime())*1000;
			viewHolder.tvAcTimeBegin.setText(DateFormat.format("yyyy.MM.dd kk:mm", timeBegin));
			viewHolder.tvAcTimeEnd.setText(DateFormat.format("kk:mm", timeEnd));
		}
		
		viewHolder.tvAgreecount.setText(acEntity.getAgreecount()+"");
		
		return convertView;
	}
	
	public class ViewHolder{
		
		RelativeLayout rlEssentialStatement;
		ImageView ivBackground;
		ImageView ivBackgroundCover;
		ImageView ivActivityStateInviting;
		ImageView ivActivityStateComplete;
		TextView tvAcShortName;
		TextView tvAcName;
		TextView tvAcDynamicTitle;
		ImageView ivEssentialStatement;
		TextView tvAcDynamicDescription;
		
		RelativeLayout rlActivityDetail;
		TextView tvAcAddress;
		TextView tvAcTimeBegin;
		TextView tvAcTimeEnd;
		
		TextView tvAgreecount;
	}

}
