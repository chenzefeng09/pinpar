package com.ipinpar.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.Constant;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.view.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PartyHomeVenueActivity extends PPBaseActivity{

	private Context mContext;
	private Button btnBack;
	private GridView gvAllGames;
	
	private CircularImageView userImage;
	private TextView tvUserName;
	private DisplayImageOptions options;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_home_venue);
		mContext=this;
		
		options = new DisplayImageOptions.Builder().
				cacheOnDisk(false).build();
		
		findView();
		setView();
		
	}
	
	public void findView(){
		btnBack = (Button) findViewById(R.id.btn_party_home_venue_back);
		gvAllGames = (GridView) findViewById(R.id.gridview_all_games);
		userImage = (CircularImageView) findViewById(R.id.party_home_venue_userimage);
		tvUserName = (TextView) findViewById(R.id.tv_party_home_user_name);
	}
	
	public void setView(){
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		gvAllGames.setAdapter(new HomeVenueAllGemesAdapter(mContext));
		gvAllGames.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		ImageLoader.getInstance().displayImage(Constant.URL_GET_USERIMAGE
				+UserManager.getInstance().getUserInfo().getUid(), userImage,options);
		tvUserName.setText(UserManager.getInstance().getUserInfo().getUsername());
		
	}
	
	class HomeVenueAllGemesAdapter extends BaseAdapter{
		//上下文对象 
        private Context context; 
        //图片数组 
        private int[] images = new int[]{
   			 R.drawable.party_home_venue_ceramics,R.drawable.party_home_venue_porcelain,
   			 R.drawable.party_home_venue_mud,R.drawable.party_home_venue_teacake,
   			 R.drawable.party_home_venue_teapot,R.drawable.party_home_venue_papercut,
   			 R.drawable.party_home_venue_coffee,R.drawable.party_home_venue_sushi,
   			 R.drawable.party_home_venue_flower,R.drawable.party_home_venue_flying_chess,
   			 R.drawable.party_home_venue_ktv,R.drawable.party_home_venue_table_games};
	   	private String[] roleNames = {
	   			"陶艺","画瓷","彩泥","茶饼","紫砂壶","剪纸",
	   			"咖啡","寿司","插花","飞行棋","K歌","桌游"
	   	};
	   	

		public HomeVenueAllGemesAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.length;
		}

		@Override
		public Object getItem(int item) {
			// TODO Auto-generated method stub
			return item;
		}

		@Override
		public long getItemId(int id) {
			// TODO Auto-generated method stub
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
//			ImageView imageView; 
			final int ivResId = images[position];
			ViewHolder viewHolder;
			
            if (convertView == null) { 
            	LayoutInflater vi = (LayoutInflater) context.
    					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	
            	convertView = vi.inflate(R.layout.list_item_home_venue_game, null);
            	viewHolder = new ViewHolder();
            	viewHolder.ivGameItem = (ImageView) convertView.findViewById(R.id.iv_home_venue_game_item);
            	convertView.setTag(viewHolder);
            }  
            else { 
            	viewHolder = (ViewHolder) convertView.getTag();
            } 
            viewHolder.ivGameItem.setImageResource(ivResId);
            return convertView; 
		}
		
		public class ViewHolder{
			ImageView ivGameItem;
		}
		
	}
}
