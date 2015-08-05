package com.ipinpar.app.activity;

import java.util.ArrayList;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.entity.AvailableRolesListEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.RolesAvailableRequest;
import com.ipinpar.app.network.api.SetRoleRequest;
import com.ipinpar.app.widget.PartyHomeVenueDialog;

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
	
	//获取可选的角色（三个）
	private RolesAvailableRequest rolesAvailableRequest;
	
	private SetRoleRequest setRoleRequest;
	
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
	
	private PartyHomeVenueDialog partySelectRolesDialog;
	private ImageView ivOut,ivIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_get_identity);
		mContext=this;
		
		uid = UserManager.getInstance().getUserInfo().getUid();
		
		startAnimaition();
		
		handlerRolesAvailableRequest.sendEmptyMessage(0);
		
		findView();
		setView();
		
	}
	
	public void startAnimaition(){
		//初始化等待进度条
		partySelectRolesDialog = new PartyHomeVenueDialog(mContext,  
                R.layout.dialog_party_select_random_roles_waitting, R.style.PartyDialogTheme); 
		
		ivOut = (ImageView) partySelectRolesDialog.findViewById(R.id.iv_party_select_random_roles_waitting_out);
		ivIn = (ImageView) partySelectRolesDialog.findViewById(R.id.iv_party_select_random_roles_waitting_in);
		
		ivOut.startAnimation(AnimationUtils.loadAnimation(PartyGetIdentityActivity.this,R.anim.anti_clock_wise_out));
		ivIn.startAnimation(AnimationUtils.loadAnimation(PartyGetIdentityActivity.this,R.anim.clock_wise_in));
		
		partySelectRolesDialog.show();
		handlerStatrAnimation.sendEmptyMessageDelayed(1, 5000);
				
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
					tvRoleSelectedTip.setText("请选择角色！");
				}else{
					if(bRole1){
						selectedRole = availableRoles.get(0);
						
						Message msg = new Message();
						msg.what = 0;
						msg.arg1 = selectedRole;
						handlerSetRoleRequest.sendMessage(msg);
						
					}else if(bRole2){
						selectedRole = availableRoles.get(1);
						
						Message msg = new Message();
						msg.what = 0;
						msg.arg1 = selectedRole;
						handlerSetRoleRequest.sendMessage(msg);
					}else if(bRole3){
						selectedRole = availableRoles.get(2);
						
						Message msg = new Message();
						msg.what = 0;
						msg.arg1 = selectedRole;
						handlerSetRoleRequest.sendMessage(msg);
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
				
				if(availableRoles!=null && availableRoles.size()>=3){
					tvRoleSelectedTip.setText("已选择"+roleNames[availableRoles.get(0)-1]
							+"，将获得"+roleNames[availableRoles.get(0)-1]+"游戏的双倍积分技能");
				}
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
				
				if(availableRoles!=null && availableRoles.size()>=3){
					tvRoleSelectedTip.setText("已选择"+roleNames[availableRoles.get(1)-1]
							+"，将获得"+roleNames[availableRoles.get(1)-1]+"游戏的双倍积分技能");
				}
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
				
				if(availableRoles!=null && availableRoles.size()>=3){
					tvRoleSelectedTip.setText("已选择"+roleNames[availableRoles.get(2)-1]
							+"，将获得"+roleNames[availableRoles.get(2)-1]+"游戏的双倍积分技能");
				}
			}
		});
		
	}
	
	public void setRoleCards(){
		if(availableRoles!=null && availableRoles.size()>=3){
			ivRole1.setImageResource(images[availableRoles.get(0)-1]);
			ivRole2.setImageResource(images[availableRoles.get(1)-1]);
			ivRole3.setImageResource(images[availableRoles.get(2)-1]);
			
			tvRole1.setText(roleNames[availableRoles.get(0)-1]);
			tvRole2.setText(roleNames[availableRoles.get(1)-1]);
			tvRole3.setText(roleNames[availableRoles.get(2)-1]);
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
	
	Handler handlerSetRoleRequest = new Handler(){

		int roleid;
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			roleid = msg.arg1;
			
			switch(msg.what){
			case 0:
				setRoleRequest = new SetRoleRequest(
						uid+"",roleid+"", new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						
						try {
							if (response !=null && response.getInt("result") == 1) {
								Toast.makeText(mContext, "成功选定角色!", 1000).show();
								Intent intent = new Intent();
								intent.setClass(mContext, PartyHomeVenueActivity.class);
								startActivity(intent);
								finish();
							}else if (response !=null && response.getInt("result") == 0) {
								Toast.makeText(mContext, "已经选定角色!", 1000).show();
							}else {
								Toast.makeText(mContext, "角色设定异常,请稍后再试!", 1000).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				});
				setRoleRequest.setTag(TAG);
				apiQueue.add(setRoleRequest);
			break;
			
			default:
				
				break;
			}
		}
		
	};
	
	Handler handlerStatrAnimation = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 0:
				
				break;
			case 1:
				partySelectRolesDialog.dismiss();
				break;
			default:
				break;
			}
		}
		
	};
	
}
