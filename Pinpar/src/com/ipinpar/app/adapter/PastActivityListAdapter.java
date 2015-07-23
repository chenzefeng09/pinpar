package com.ipinpar.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.Constant;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.ActivityEntity;
import com.ipinpar.app.util.BitmapFillet;
import com.ipinpar.app.util.DisplayUtil;
import com.ipinpar.app.view.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PastActivityListAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<ActivityEntity> aList = new ArrayList<ActivityEntity>();
	private DisplayImageOptions options;
	
	
	public PastActivityListAdapter(Context mContext) {
		this.mContext = mContext;
	}
	
	

	public PastActivityListAdapter(Context mContext, ArrayList<ActivityEntity> aList) {
		this.mContext = mContext;
		this.aList = aList;
		options = new DisplayImageOptions.Builder().
				cacheOnDisk(false).build();
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
			convertView = vi.inflate(R.layout.past_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.userImage = (CircularImageView) convertView.findViewById(R.id.statement_image);
			viewHolder.ivBackground = (ImageView) convertView.findViewById(R.id.iv_activity_desc);
			viewHolder.ivBackgroundCover = (ImageView) convertView.findViewById(R.id.iv_activity_desc_cover);
			viewHolder.ivActivityStateInviting = (ImageView) convertView.findViewById(R.id.iv_activity_state_inviting);
			viewHolder.ivActivityStateComplete = (ImageView) convertView.findViewById(R.id.iv_activity_state_complete);
			viewHolder.tvAcShortName =  (TextView) convertView.findViewById(R.id.tv_first_text_desc);
			viewHolder.tvAcName = (TextView) convertView.findViewById(R.id.tv_second_text_desc);
			viewHolder.tvViewCount = (TextView) convertView.findViewById(R.id.tv_viewcount);
			viewHolder.tvAcDynamicTitle =  (TextView) convertView.findViewById(R.id.tv_dynamic_title);
			viewHolder.ivEssentialStatement = (ImageView) convertView.findViewById(R.id.iv_essential_statement);
			viewHolder.tvAcDynamicDescription = (TextView) convertView.findViewById(R.id.tv_dynamic_description);
			
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
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
		
		if(acEntity.getStatus() == 4){
			viewHolder.ivActivityStateInviting.setVisibility(View.INVISIBLE);
			viewHolder.ivActivityStateComplete.setVisibility(View.VISIBLE);
			viewHolder.ivEssentialStatement.setVisibility(View.INVISIBLE);
		}else if(acEntity.getStatus() == 3 || acEntity.getStatus() == 2){
			viewHolder.ivActivityStateInviting.setVisibility(View.VISIBLE);
			viewHolder.ivActivityStateComplete.setVisibility(View.INVISIBLE);
			viewHolder.ivEssentialStatement.setVisibility(View.VISIBLE);
		}else{
			viewHolder.ivActivityStateInviting.setVisibility(View.INVISIBLE);
			viewHolder.ivActivityStateComplete.setVisibility(View.INVISIBLE);
			viewHolder.ivEssentialStatement.setVisibility(View.INVISIBLE);
		}
		
		ImageLoader.getInstance().displayImage(Constant.URL_GET_USERIMAGE+acEntity.getDynamic().get(0).getUid(), viewHolder.userImage,options);
		
		viewHolder.tvAcShortName.setText(acEntity.getSname());
		viewHolder.tvAcName.setText(acEntity.getAcname());
		viewHolder.tvViewCount.setText(acEntity.getReadcount()+"");
		viewHolder.tvAcDynamicTitle.setText(acEntity.getDynamic().get(0).getTitle());
		viewHolder.tvAcDynamicDescription.setText(acEntity.getDynamic().get(0).getDescription());
		
		return convertView;
	}
	
	public class ViewHolder{
		CircularImageView userImage;// 头像图片
		ImageView ivBackground;
		ImageView ivBackgroundCover;
		ImageView ivActivityStateInviting;
		ImageView ivActivityStateComplete;
		TextView tvAcShortName;
		TextView tvAcName;
		TextView tvViewCount;
		TextView tvAcDynamicTitle;
		ImageView ivEssentialStatement;
		TextView tvAcDynamicDescription;
	}

}
