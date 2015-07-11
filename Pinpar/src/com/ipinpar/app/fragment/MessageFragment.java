package com.ipinpar.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ipinpar.app.PPBaseFragment;
import com.ipinpar.app.R;

public class MessageFragment extends PPBaseFragment {
	private View rl_notification,rl_support,rl_comment;
	private ImageView ci_comment,ci_suppport,ci_notification;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_message, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		
	}
}
