package com.ipinpar.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.Constant;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.PartyUserInfoEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.PartyGetUserInfoRequest;
import com.ipinpar.app.network.api.SetTeamRequest;
import com.ipinpar.app.view.CircularImageView;
import com.ipinpar.app.widget.PartyHomeVenueDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PartyExperienceEditActivity extends PPBaseActivity{

	private Context mContext;
	private Button btnBack;
	private EditText etPartyExperiText;
	private ImageView ivPartyExperiImage;
	private TextView tvPartyExperiCommit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_experiences_edit);
		mContext=this;
		
		
		findView();
		
		
		setView();
		
	}
	
	public void findView(){
		btnBack = (Button) findViewById(R.id.btn_party_experiences_edit_back);
		tvPartyExperiCommit = (TextView) findViewById(R.id.tv_party_experience_commit);
		etPartyExperiText = (EditText) findViewById(R.id.et_party_experiences_text);
		ivPartyExperiImage = (ImageView) findViewById(R.id.iv_party_experiences_image);
		
	}
	
	
	public void setView(){
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	
}
