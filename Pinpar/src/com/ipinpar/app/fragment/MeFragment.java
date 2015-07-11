package com.ipinpar.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;
import com.ipinpar.app.activity.LoginActivity;
import com.ipinpar.app.activity.SettingActivity;
import com.ipinpar.app.activity.UserInfoEditActivity;
import com.ipinpar.app.manager.UserManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MeFragment extends PPBaseFragment implements OnClickListener{
	
	private Context mContext;
	private View view;
	private TextView tv_header_title,tv_uname,tv_qianming,tv_my_activity,tv_my_invited_activity,tv_my_faverite_activity,tv_my_friend,tv_edit;
	private ImageView ib_right,iv_icon,iv_blur_icon,iv_sex;
	private DisplayImageOptions options;
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
		options = new DisplayImageOptions.Builder().
				cacheOnDisk(false).build();
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!UserManager.getInstance().isLogin()) {
			iv_icon.setImageResource(R.drawable.defaultavatarmale);
			iv_sex.setImageResource(R.drawable.log_maleselected);
			tv_qianming.setText("没有留下任何文字");
			tv_edit.setText("登录");
		}
		else {
			ImageLoader.getInstance().displayImage(UserManager.getInstance().getUserInfo().getImgsrc(), iv_icon,options);
			if (UserManager.getInstance().getUserInfo().getSex() == 1) {
				iv_sex.setImageResource(R.drawable.log_maleselected);
				if (TextUtils.isEmpty(UserManager.getInstance().getUserInfo().getImgsrc())) {
					iv_icon.setImageResource(R.drawable.defaultavatarmale);
				}
			}
			else {
				iv_sex.setImageResource(R.drawable.log_femailselected);
				if (TextUtils.isEmpty(UserManager.getInstance().getUserInfo().getImgsrc())) {
					iv_icon.setImageResource(R.drawable.defaultavatarfemail);
				}
			}
			if (TextUtils.isEmpty(UserManager.getInstance().getUserInfo().getSignature())) {
				tv_qianming.setText("没有留下任何文字");
			}
			else {
				tv_qianming.setText(UserManager.getInstance().getUserInfo().getSignature());
			}
			tv_uname.setText(UserManager.getInstance().getUserInfo().getUsername());
			tv_edit.setText("编辑");
		}

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
			if (UserManager.getInstance().isLogin()) {
				startActivity(new Intent(getActivity(), UserInfoEditActivity.class));
			}
			else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
		case R.id.ib_right:
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_edit:
			if (UserManager.getInstance().isLogin()) {
				startActivity(new Intent(getActivity(), UserInfoEditActivity.class));
			}
			else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;

		default:
			break;
		}
	}

}
