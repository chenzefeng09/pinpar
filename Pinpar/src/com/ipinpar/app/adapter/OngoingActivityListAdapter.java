package com.ipinpar.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.R;
import com.ipinpar.app.entity.ActivityEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OngoingActivityListAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<ActivityEntity> aList = new ArrayList<ActivityEntity>();

	
	
	public OngoingActivityListAdapter(Context mContext) {
		this.mContext = mContext;
	}
	
	

	public OngoingActivityListAdapter(Context mContext, ArrayList<ActivityEntity> aList) {
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
			convertView = vi.inflate(R.layout.ongoing_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ivBackground = (ImageView) convertView.findViewById(R.id.iv_activity_desc);
			viewHolder.ivBackgroundCover = (ImageView) convertView.findViewById(R.id.iv_activity_desc_cover);
			viewHolder.tvAcShortName =  (TextView) convertView.findViewById(R.id.tv_first_text_desc);
			viewHolder.tvAcName = (TextView) convertView.findViewById(R.id.tv_second_text_desc);
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
		
		viewHolder.tvAcShortName.setText(acEntity.getSname());
		viewHolder.tvAcName.setText(acEntity.getAcname());
		viewHolder.tvAcAddress.setText(acEntity.getAddress2()+"-"+acEntity.getAddress3());
		
		long timeBegin = Long.parseLong(acEntity.getActivebegintime())*1000;
		long timeEnd = Long.parseLong(acEntity.getActiveendtime())*1000;
		viewHolder.tvAcTimeBegin.setText(DateFormat.format("yyyy.MM.dd kk:mm", timeBegin));
		viewHolder.tvAcTimeEnd.setText(DateFormat.format("kk:mm", timeEnd));
		viewHolder.tvAgreecount.setText(acEntity.getAgreecount()+"");
		
		return convertView;
	}
	
	public class ViewHolder{
		ImageView ivBackground;
		ImageView ivBackgroundCover;
		TextView tvAcShortName;
		TextView tvAcName;
		TextView tvAcAddress;
		TextView tvAcTimeBegin;
		TextView tvAcTimeEnd;
		TextView tvAgreecount;
	}

}
