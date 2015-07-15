package com.ipinpar.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;

public class ExperienceDiaryActivity extends PPBaseActivity {
	
	private ListView lv_diary;
	private ImageView iv_icon,iv_title_bkg;
	private TextView tv_name;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_experience_diary);
		int uid = getIntent().getIntExtra("uid", 0);
		if (uid == 0) {
			Toast.makeText(mContext, "获取体验日记失败，请重试", 1000).show();;
			finish();
		}
		else {
			
		}
		lv_diary = (ListView) findViewById(R.id.lv_diary);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_title_bkg = (ImageView) findViewById(R.id.iv_title_bkg);
		tv_name = (TextView) findViewById(R.id.tv_name);
	}
	
	
	public static Intent getIntent2Me(Context context,int uid){
		Intent intent = new Intent(context, ExperienceDiaryActivity.class);
		intent.putExtra("uid", uid);
		return intent;
	}

}
