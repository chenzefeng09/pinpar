package com.ipinpar.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.ipinpar.app.activity.LoginActivity;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.manager.AgreeManager;
import com.ipinpar.app.manager.AgreeManager.AgreeResultListener;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.util.BitmapFillet;
import com.ipinpar.app.util.DisplayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class OngoingActivityListAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<ActivityEntity> aList = new ArrayList<ActivityEntity>();
	private RequestQueue queue;
	private DisplayImageOptions options;
	private Bitmap roundcoverbitmap;
//	public OngoingActivityListAdapter(Context mContext) {
//		this.mContext = mContext;
//	}
//	
//	
//
//	public OngoingActivityListAdapter(Context mContext, ArrayList<ActivityEntity> aList) {
//		this.mContext = mContext;
//		this.aList = aList;
//	}
	
	public OngoingActivityListAdapter(Context mContext, ArrayList<ActivityEntity> aList,RequestQueue queue) {
		this.mContext = mContext;
		this.aList = aList;
		this.queue = queue;
		options = new DisplayImageOptions.Builder().build();
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
		final ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater) mContext.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.ongoing_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ivBackground = (ImageView) convertView.findViewById(R.id.iv_activity_desc);
			viewHolder.ivBackgroundCover = (ImageView) convertView.findViewById(R.id.iv_activity_desc_cover);
			viewHolder.tvAcShortName =  (TextView) convertView.findViewById(R.id.tv_first_text_desc);
			viewHolder.tvAcName = (TextView) convertView.findViewById(R.id.tv_second_text_desc);
			viewHolder.tvViewCount = (TextView) convertView.findViewById(R.id.tv_viewcount);
			viewHolder.tvAcAddress = (TextView) convertView.findViewById(R.id.tv_activity_address);
			viewHolder.tvAcTimeBegin = (TextView) convertView.findViewById(R.id.tv_activity_time_begin);
			viewHolder.tvAcTimeEnd = (TextView) convertView.findViewById(R.id.tv_activity_time_end);
			viewHolder.tvAgreecount = (TextView) convertView.findViewById(R.id.tv_praise_num);
			viewHolder.RL_acticity_praise  = convertView.findViewById(R.id.RL_acticity_praise);
			viewHolder.iv_acticity_praise = (ImageView) convertView.findViewById(R.id.iv_acticity_praise);

			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (UserManager.getInstance().isLogin()) {
			if (AgreeManager.getInstance().isAgreed(acEntity.getAcid(), "acid")) {
				viewHolder.iv_acticity_praise.setImageResource(R.drawable.activity_praise);
			}
			else {
				viewHolder.iv_acticity_praise.setImageResource(R.drawable.ac_ongoing_list_interested);
			}
		}
		else {
			viewHolder.iv_acticity_praise.setImageResource(R.drawable.ac_ongoing_list_interested);
		}
		
		ImageLoader.getInstance().displayImage(acEntity.getImgs().get(0).getImg(),
				viewHolder.ivBackground, options, new ImageLoadingListener() {
			
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
		
		ImageLoader.getInstance().displayImage("drawable://"+R.drawable.activity_img_cover, viewHolder.ivBackgroundCover,options,new ImageLoadingListener() {
			
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
		
		viewHolder.tvAcShortName.setText(acEntity.getSname());
		viewHolder.tvAcName.setText(acEntity.getAcname());
		viewHolder.tvViewCount.setText(acEntity.getReadcount()+"");
		viewHolder.tvAcAddress.setText(acEntity.getAddress2()+"-"+acEntity.getAddress3());
		
		long timeBegin = Long.parseLong(acEntity.getActivebegintime())*1000;
		long timeEnd = Long.parseLong(acEntity.getActiveendtime())*1000;
		viewHolder.tvAcTimeBegin.setText(DateFormat.format("yyyy.MM.dd kk:mm", timeBegin));
		viewHolder.tvAcTimeEnd.setText(DateFormat.format("kk:mm", timeEnd));
		viewHolder.tvAgreecount.setText(acEntity.getAgreecount()+"");
		viewHolder.RL_acticity_praise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (UserManager.getInstance().isLogin()) {
					if (AgreeManager.getInstance().isAgreed(acEntity.getAcid(), "acid")) {
						AgreeManager.getInstance().agree(
								acEntity.getAcid(), 
								"acid", new AgreeResultListener() {
									
									@Override
									public void onAgreeResult(boolean agree) {
										if (!agree) {
											acEntity.setAgreecount(acEntity.getAgreecount() - 1);
											notifyDataSetChanged();
//											viewHolder.tv_praise_num.setText(acEntity.getAgreecount()+"");
//											viewHolder.iv_acticity_praise.setImageResource(R.drawable.ac_detail_interested);
										}
									}
								}, queue);
					}
					else {
						AgreeManager.getInstance().agree(
								acEntity.getAcid(), 
								"acid", new AgreeResultListener() {
									
									@Override
									public void onAgreeResult(boolean agree) {
										if (agree) {
											acEntity.setAgreecount(acEntity.getAgreecount() + 1);
											notifyDataSetChanged();
//											viewHolder.tv_praise_num.setText(acEntity.getAgreecount()+"");
//											viewHolder.iv_acticity_praise.setImageResource(R.drawable.activity);
										}
									}
								}, queue);
					}
				}
				else {
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
				}
			}
		});
		return convertView;
	}
	
	public class ViewHolder{
		ImageView ivBackground;
		ImageView ivBackgroundCover;
		TextView tvAcShortName;
		TextView tvAcName;
		TextView tvViewCount;
		TextView tvAcAddress;
		TextView tvAcTimeBegin;
		TextView tvAcTimeEnd;
		TextView tvAgreecount;
		View RL_acticity_praise;
		ImageView iv_acticity_praise;
	}

}