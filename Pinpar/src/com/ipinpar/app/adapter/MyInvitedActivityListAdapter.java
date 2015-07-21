package com.ipinpar.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.ipinpar.app.util.BitmapFillet;
import com.ipinpar.app.util.DisplayUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MyInvitedActivityListAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<ActivityEntity> aList = new ArrayList<ActivityEntity>();

	
	
	public MyInvitedActivityListAdapter(Context mContext) {
		this.mContext = mContext;
	}
	
	

	public MyInvitedActivityListAdapter(Context mContext, ArrayList<ActivityEntity> aList) {
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
			convertView = vi.inflate(R.layout.my_invited_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.rlInvitedEnvelope = (RelativeLayout) convertView.findViewById(R.id.RL_myinvited_activity_status_invitedenvelope);
			viewHolder.rlInvitedEditExperi = (RelativeLayout) convertView.findViewById(R.id.RL_myinvited_activity_status_edit_experi);
			viewHolder.rlInvitedViewExperi = (RelativeLayout) convertView.findViewById(R.id.RL_myinvited_activity_status_view_experi);
			
			viewHolder.ivBackground = (ImageView) convertView.findViewById(R.id.iv_activity_desc);
			viewHolder.ivBackgroundCover = (ImageView) convertView.findViewById(R.id.iv_activity_desc_cover);
			viewHolder.tvAcShortName =  (TextView) convertView.findViewById(R.id.tv_activity_shop);
			viewHolder.tvAcName = (TextView) convertView.findViewById(R.id.tv_activity_name);
			
			viewHolder.tvAcTimeBegin = (TextView) convertView.findViewById(R.id.tv_activity_time_begin);
			viewHolder.tvAcTimeEnd = (TextView) convertView.findViewById(R.id.tv_activity_time_end);
			
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ImageLoader.getInstance().displayImage(acEntity.getImgs().get(0).getImg(),
				viewHolder.ivBackground, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				final ImageView imageView = (ImageView) view;
				imageView.setImageBitmap(BitmapFillet.fillet(BitmapFillet.TOP, loadedImage,
						DisplayUtil.dip2px(9)));
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
//		ImageLoader.getInstance().displayImage(acEntity.getImgs().get(0).getImg(), viewHolder.ivBackground,options);
		
		ImageLoader.getInstance().displayImage("drawable://"+R.drawable.activity_img_cover, viewHolder.ivBackgroundCover,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				final ImageView imageView = (ImageView) view;
				imageView.setImageBitmap(BitmapFillet.fillet(BitmapFillet.TOP, loadedImage,
						DisplayUtil.dip2px(9)));
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
		if(acEntity.getStatus2() == 2){
			viewHolder.rlInvitedEnvelope.setVisibility(View.VISIBLE);
			viewHolder.rlInvitedEditExperi.setVisibility(View.INVISIBLE);
			viewHolder.rlInvitedViewExperi.setVisibility(View.INVISIBLE);
		}else if(acEntity.getStatus2() == 3){
			viewHolder.rlInvitedEnvelope.setVisibility(View.INVISIBLE);
			viewHolder.rlInvitedEditExperi.setVisibility(View.VISIBLE);
			viewHolder.rlInvitedViewExperi.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.rlInvitedEnvelope.setVisibility(View.INVISIBLE);
			viewHolder.rlInvitedEditExperi.setVisibility(View.INVISIBLE);
			viewHolder.rlInvitedViewExperi.setVisibility(View.VISIBLE);
			
		}
		
		viewHolder.tvAcShortName.setText(acEntity.getSname());
		viewHolder.tvAcName.setText(acEntity.getAcname());
		
		long timeBegin = Long.parseLong(acEntity.getActivebegintime())*1000;
		long timeEnd = Long.parseLong(acEntity.getActiveendtime())*1000;
		viewHolder.tvAcTimeBegin.setText(DateFormat.format("yyyy.MM.dd kk:mm", timeBegin));
		viewHolder.tvAcTimeEnd.setText(DateFormat.format("kk:mm", timeEnd));
		
		return convertView;
	}
	
	public class ViewHolder{
		
		RelativeLayout rlInvitedEnvelope;
		RelativeLayout rlInvitedEditExperi;
		RelativeLayout rlInvitedViewExperi;
		
		ImageView ivBackground;
		ImageView ivBackgroundCover;
		TextView tvAcShortName;
		TextView tvAcName;
		
		TextView tvAcTimeBegin;
		TextView tvAcTimeEnd;
		
	}

}
