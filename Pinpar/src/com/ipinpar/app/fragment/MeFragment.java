package com.ipinpar.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.UserInfoEditActivity;

public class MeFragment extends PPBaseFragment implements OnClickListener{
	
	private Context mContext;
	private View view;
	private TextView tv_header_title,tv_uname,tv_qianming,tv_my_activity,tv_my_invited_activity,tv_my_faverite_activity,tv_my_friend,tv_edit;
	private ImageView ib_right,iv_icon,iv_blur_icon,iv_sex;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if(mContext == null){
			mContext = (Context)activity;
		}
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_me, null);
		
		initView(view);
		
		return view;
	}
	
	public void initView(View view) {
		tv_header_title = (TextView) view.findViewById(R.id.tv_header_title);
		tv_header_title.setText("");
		tv_uname = (TextView) view.findViewById(R.id.tv_uname);
		tv_qianming = (TextView) view.findViewById(R.id.tv_qianming);
		tv_my_activity = (TextView) view.findViewById(R.id.tv_my_activity);
		tv_my_invited_activity = (TextView) view.findViewById(R.id.tv_my_invited_activity);
		tv_my_faverite_activity = (TextView) view.findViewById(R.id.tv_my_faverite_activity);
		tv_my_friend = (TextView) view.findViewById(R.id.tv_my_friend);
		tv_edit = (TextView) view.findViewById(R.id.tv_edit);
		ib_right = (ImageView) view.findViewById(R.id.ib_right);
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		iv_blur_icon = (ImageView) view.findViewById(R.id.iv_blur_icon);
		iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
		tv_edit.setOnClickListener(this);
		tv_my_activity.setOnClickListener(this);
		tv_my_invited_activity.setOnClickListener(this);
		tv_my_faverite_activity.setOnClickListener(this);
		tv_my_friend.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		ib_right.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_my_activity:
			
			break;
		case R.id.tv_my_invited_activity:
			
			break;
		case R.id.tv_my_faverite_activity:
			
			break;
		case R.id.tv_my_friend:
			
			break;
		case R.id.iv_icon:
			
			break;
		case R.id.ib_right:
			
			break;
		case R.id.tv_edit:
			startActivity(new Intent(getActivity(), UserInfoEditActivity.class));
			break;

		default:
			break;
		}
	}

}
