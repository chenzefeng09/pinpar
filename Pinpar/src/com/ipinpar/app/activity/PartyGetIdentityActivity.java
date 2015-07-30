package com.ipinpar.app.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPApplication;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.ActivityListEntity;
import com.ipinpar.app.entity.AvailableRolesListEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.ActivityListRequest;
import com.ipinpar.app.network.api.RolesAvailableRequest;

public class PartyGetIdentityActivity extends PPBaseActivity{

	private Context mContext;
	private Button btnBack,btnOk;
	private RelativeLayout rlRole1,rlRole2,rlRole3;
	private ImageView ivRole1,ivRole2,ivRole3;
	private TextView tvRole1,tvRole2,tvRole3;
	private TextView tvRoleSelectedTip;
	private boolean bRole1 = false;
	private boolean bRole2 = false;
	private boolean bRole3 = false;
	private int selectedRole;
	private int uid;
	
	private ArrayList<Integer> availableRoles = new ArrayList<Integer>();
	
	//请求进行中的活动
	private RolesAvailableRequest rolesAvailableRequest;
	
	private int[] images = new int[]{
			 R.drawable.party_identity_ceramics,R.drawable.party_identity_porcelain,
			 R.drawable.party_identity_mud,R.drawable.party_identity_teacake,
			 R.drawable.party_identity_teapot,R.drawable.party_identity_papercut,
			 R.drawable.party_identity_coffee,R.drawable.party_identity_sushi,
			 R.drawable.party_identity_flower,R.drawable.party_identity_flying_chess,
			 R.drawable.party_identity_ktv,R.drawable.party_identity_table_games};
	private String[] roleNames = {
			"陶艺","画瓷","彩泥","茶饼","紫砂壶","剪纸",
			"咖啡","寿司","插花","飞行棋","K歌","桌游"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_get_identity);
		mContext=this;
		
		uid = UserManager.getInstance().getUserInfo().getUid();
		
		handlerRolesAvailableRequest.sendEmptyMessage(0);
		
		findView();
		setView();
		
	}
	
	public void findView(){
		btnBack = (Button) findViewById(R.id.btn_party_get_identity_back);
		btnOk = (Button) findViewById(R.id.btn_get_identity_ok);
		rlRole1 = (RelativeLayout) findViewById(R.id.RL_party_get_identity_role1);
		rlRole2 = (RelativeLayout) findViewById(R.id.RL_party_get_identity_role2);
		rlRole3 = (RelativeLayout) findViewById(R.id.RL_party_get_identity_role3);
		
		ivRole1 = (ImageView) findViewById(R.id.iv_party_get_identity_role1);
		ivRole2 = (ImageView) findViewById(R.id.iv_party_get_identity_role2);
		ivRole3 = (ImageView) findViewById(R.id.iv_party_get_identity_role3);
		
		tvRole1 = (TextView) findViewById(R.id.tv_party_get_identity_role1);
		tvRole2 = (TextView) findViewById(R.id.tv_party_get_identity_role2);
		tvRole3 = (TextView) findViewById(R.id.tv_party_get_identity_role3);
		
		tvRoleSelectedTip = (TextView) findViewById(R.id.tv_get_identity_role_tip);
		
	}
	
	public void setView(){
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(bRole1==false && bRole2 == false && bRole3 ==false){
//					Toast.makeText(mContext, "请选择角色！", 1000).show();
					tvRoleSelectedTip.setText("请选择角色！");
				}else{
					if(bRole1){
						selectedRole = availableRoles.get(0);
						
						Intent intent = new Intent();
						intent.setClass(mContext, PartyHomeVenueActivity.class);
						startActivity(intent);
					}else if(bRole2){
						selectedRole = availableRoles.get(1);
						
						Intent intent = new Intent();
						intent.setClass(mContext, PartyHomeVenueActivity.class);
						startActivity(intent);
					}else if(bRole3){
						selectedRole = availableRoles.get(2);
						
						Intent intent = new Intent();
						intent.setClass(mContext, PartyHomeVenueActivity.class);
						startActivity(intent);
					}else{
						tvRoleSelectedTip.setText("请选择角色！");
					}
				}
				
			}
		});
		rlRole1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bRole1 = true;
				bRole2 = false;
				bRole3 = false;
				rlRole1.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_pressed));
				rlRole2.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_normal));
				rlRole3.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_normal));
				
				tvRoleSelectedTip.setText("已选择"+roleNames[availableRoles.get(0)]
						+"，将获得"+roleNames[availableRoles.get(0)]+"游戏的双倍积分技能");
			}
		});
		rlRole2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bRole1 = false;
				bRole2 = true;
				bRole3 = false;
				rlRole1.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_normal));
				rlRole2.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_pressed));
				rlRole3.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_normal));
				
				tvRoleSelectedTip.setText("已选择"+roleNames[availableRoles.get(1)]
						+"，将获得"+roleNames[availableRoles.get(1)]+"游戏的双倍积分技能");
			}
		});
		rlRole3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bRole1 = false;
				bRole2 = false;
				bRole3 = true;
				rlRole1.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_normal));
				rlRole2.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_normal));
				rlRole3.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.party_get_identity_pressed));
				
				tvRoleSelectedTip.setText("已选择"+roleNames[availableRoles.get(2)]
						+"，将获得"+roleNames[availableRoles.get(2)]+"游戏的双倍积分技能");
			}
		});
		
	}
	
	public void setRoleCards(){
		if(availableRoles!=null && availableRoles.size()>=3){
			ivRole1.setImageResource(images[availableRoles.get(0)]);
			ivRole2.setImageResource(images[availableRoles.get(1)]);
			ivRole3.setImageResource(images[availableRoles.get(2)]);
			
			tvRole1.setText(roleNames[availableRoles.get(0)]);
			tvRole2.setText(roleNames[availableRoles.get(1)]);
			tvRole3.setText(roleNames[availableRoles.get(2)]);
		}
	}
	
	Handler handlerRolesAvailableRequest = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				rolesAvailableRequest = new RolesAvailableRequest(
						uid+"", new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						
						Gson gson = new Gson();
						
						AvailableRolesListEntity availableRolesList = gson.fromJson(response.toString(), AvailableRolesListEntity.class);
						
						availableRoles.clear();
						availableRoles.addAll(availableRolesList.getRoles());
						
						setRoleCards();
						
					}
					
				});
				rolesAvailableRequest.setTag(TAG);
				apiQueue.add(rolesAvailableRequest);
			break;
			
			
			default:
				
				break;
			}
		}
		
	};
	
}
