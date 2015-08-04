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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.ipinpar.app.Constant;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.adapter.PartyExperiencesListAdapter;
import com.ipinpar.app.entity.PartyExperienceEntity;
import com.ipinpar.app.entity.PartyExperiencesListEntity;
import com.ipinpar.app.entity.PartyUserInfoEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.PartyExperiencesListRequest;
import com.ipinpar.app.network.api.PartyGetUserInfoRequest;
import com.ipinpar.app.network.api.SetTeamRequest;
import com.ipinpar.app.util.NetWorkState;
import com.ipinpar.app.view.CircularImageView;
import com.ipinpar.app.widget.PartyAgreeDialog;
import com.ipinpar.app.widget.PartyHomeVenueDialog;
import com.ipinpar.app.widget.PullToRefreshListView;
import com.ipinpar.app.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PartyExperienceActivity extends PPBaseActivity{

	private Context mContext;
	private Button btnBack;
	
	
	private TextView tvExperienceName;
	private RelativeLayout rlPartyExperiencesUserInfo;
	private TextView tvGoldCoins,tvCreativeValue,tvFunnyValue,tvPracticalValue,tvConfusedValue;
	private CircularImageView userImage,dialogUserImage;
	private TextView tvUserName,tvDialogUserName;
	
	private int uid;
	private PartyHomeVenueDialog partyUserInfoDialog;
	private ImageView ivDialogUserInfoClose;
	
	private PartyGetUserInfoRequest partyGetUserInfoRequest;
	
	private DisplayImageOptions options;
	
	private int experienceId;
	
	private TextView tvGameName;
	
	private TextView tvDialogGoldCoins,tvDialogCreativeValue,tvDialogFunnyValue,
				tvDialogPracticalValue,tvDialogConfusedValue;
	
	private SetTeamRequest setTeamRequest;
	private TextView tvDialogTeamName;
	private RelativeLayout rlTomato,rlYellow,rlLemon,rlBlueberry;
	private ImageView ivTomato,ivYellow,ivLemon,ivBlueberry;
	private TextView tvTomato,tvYellow,tvLemon,tvBlueberry;
	private Button btnTeamSelectOk;
	private int selectedTeamId = 0;
	
	private ImageView ivDialogCeramics,ivDialogPorcelain,ivDialogMud,ivDialogTeaCake,
	ivDialogTeaPot,ivDialogPaperCut,ivDialogCoffee,ivDialogSushi,
	ivDialogFlower,ivDialogFlyingChess,ivDialogKTV,ivDialogTableGames;

	private String[] roleNames = {
   			"陶艺","画瓷","彩泥","茶饼","紫砂壶","剪纸",
   			"咖啡","寿司","插花","飞行棋","K歌","桌游"
   	};
	
	private ImageView ivExperiEdit;
	
	//party体验列表相关
	private PullToRefreshListView partyExperiencesListView;
	private RelativeLayout rlPartyExperiencesNoTip;
	
	private PartyExperiencesListRequest partyExperiencesListRequest;
	private PartyExperiencesListAdapter partyExperiencesListAdapter;
	private ArrayList<PartyExperienceEntity> partyExperiencesList = new ArrayList<PartyExperienceEntity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_experiences);
		mContext=this;
		
		uid = UserManager.getInstance().getUserInfo().getUid();
		
		options = new DisplayImageOptions.Builder().
				cacheOnDisk(false).build();
		
		experienceId = getIntent().getIntExtra("ExperienceId",1);
		
		partyUserInfoDialog = new PartyHomeVenueDialog(mContext,  
                R.layout.dialog_party_home_venue_userinfo, R.style.PartyDialogTheme);  
		
		
		findView();
		findUserInfoView();
		
		
		setView();
		setUserInfoView();
		
		handlerPartyUserInfoRequest.sendEmptyMessage(0);
		handlerPartyExperiencesRequest.sendEmptyMessage(0);
		
		
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		handlerPartyExperiencesRequest.sendEmptyMessage(0);
	}



	public void findView(){
		btnBack = (Button) findViewById(R.id.btn_party_experiences_back);
		tvExperienceName = (TextView) findViewById(R.id.tv_party_experience_name);
		rlPartyExperiencesUserInfo = (RelativeLayout) findViewById(R.id.RL_party_experiences_personal);
		tvGoldCoins = (TextView) findViewById(R.id.tv_party_experiences_gold_coin);
		tvCreativeValue = (TextView) findViewById(R.id.tv_party_experiences_creative);
		tvFunnyValue = (TextView) findViewById(R.id.tv_party_experiences_funny);
		tvPracticalValue = (TextView) findViewById(R.id.tv_party_experiences_practical);
		tvConfusedValue = (TextView) findViewById(R.id.tv_party_experiences_confused);
		
		userImage = (CircularImageView) findViewById(R.id.party_experiences_userimage);
		tvUserName = (TextView) findViewById(R.id.tv_party_experiences_user_name);
		
		ivExperiEdit = (ImageView) findViewById(R.id.iv_party_experiences_edit);
		
		partyExperiencesListAdapter = new PartyExperiencesListAdapter(mContext,partyExperiencesList,apiQueue);
		partyExperiencesListView = (PullToRefreshListView) findViewById(R.id.party_experiences_list);
		rlPartyExperiencesNoTip = (RelativeLayout) findViewById(R.id.RL_has_no_experiences_tip);
		
		if(partyExperiencesListAdapter!=null){
			partyExperiencesListView.setAdapter(partyExperiencesListAdapter);
		}
		partyExperiencesListView.setOnRefreshListener(onRefreshListener);
		
	}
	
	public void findUserInfoView(){
		ivDialogUserInfoClose = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_dialog_userinfo_close);
		dialogUserImage = (CircularImageView) partyUserInfoDialog.findViewById(R.id.party_home_venue_dialog_userimage);
		tvDialogUserName = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_dialog_user_name);
		tvGameName = (TextView) partyUserInfoDialog.findViewById(R.id.party_home_venue_dialog_game_name);
		tvDialogGoldCoins = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_dialog_gold_coin);
		tvDialogCreativeValue = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_venue_dialog_creative);
		tvDialogFunnyValue = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_venue_dialog_funny);
		tvDialogPracticalValue = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_venue_dialog_practical);
		tvDialogConfusedValue = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_venue_dialog_confused);
		
		ivDialogCeramics = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_ceramics);
		ivDialogPorcelain = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_porcelain);
		ivDialogMud = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_mud);
		ivDialogTeaCake = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_teacake);
		ivDialogTeaPot = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_teapot);
		ivDialogPaperCut = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_papercut);
		ivDialogCoffee = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_coffee);
		ivDialogSushi = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_sushi);
		ivDialogFlower = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_flower);
		ivDialogFlyingChess = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_flying_chess);
		ivDialogKTV = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_ktv);
		ivDialogTableGames = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_participate_ac_table_games);
		
		tvDialogTeamName = (TextView) partyUserInfoDialog.findViewById(R.id.tvTeameName);
		rlTomato = (RelativeLayout) partyUserInfoDialog.findViewById(R.id.RL_party_home_venue_team_tomato);
		rlYellow = (RelativeLayout) partyUserInfoDialog.findViewById(R.id.RL_party_home_venue_team_yellow);
		rlLemon = (RelativeLayout) partyUserInfoDialog.findViewById(R.id.RL_party_home_venue_team_lemon);
		rlBlueberry = (RelativeLayout) partyUserInfoDialog.findViewById(R.id.RL_party_home_venue_team_blueberry);
		btnTeamSelectOk = (Button) partyUserInfoDialog.findViewById(R.id.btn_team_ok);
		
		ivTomato = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_tomato);
		ivYellow = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_yellow);
		ivLemon = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_lemon);
		ivBlueberry = (ImageView) partyUserInfoDialog.findViewById(R.id.iv_party_home_venue_blueberry);
	
		tvTomato = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_venue_team_tomato);
		tvYellow = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_venue_team_yellow);
		tvLemon = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_venue_team_lemon);
		tvBlueberry = (TextView) partyUserInfoDialog.findViewById(R.id.tv_party_home_venue_team_blueberry);
	
	}
	
	public void setView(){
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		tvExperienceName.setText(roleNames[experienceId-1]);
		
		ImageLoader.getInstance().displayImage(Constant.URL_GET_USERIMAGE
				+UserManager.getInstance().getUserInfo().getUid(), userImage,options);
		tvUserName.setText(UserManager.getInstance().getUserInfo().getUsername());
		rlPartyExperiencesUserInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				partyUserInfoDialog.show();
			}
		});
		
		ivExperiEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//这里的experienceId是从0-11，所以传roleid的时候要加个1
				Intent intent = new Intent();
				intent.putExtra("RoleId",experienceId);
				intent.setClass(mContext, PartyExperienceEditActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	public void SetUserInfoValue(PartyUserInfoEntity partyUserInfo){
		tvGoldCoins.setText(partyUserInfo.getScore()+"");
		tvCreativeValue.setText(partyUserInfo.getAgreeweight1()+"");
		tvFunnyValue.setText(partyUserInfo.getAgreeweight2()+"");
		tvPracticalValue.setText(partyUserInfo.getAgreeweight3()+"");
		tvConfusedValue.setText(partyUserInfo.getAgreeweight4()+"");
		
	}
	
	public void setUserInfoView(){
		ivDialogUserInfoClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				partyUserInfoDialog.dismiss();
			}
		});
		rlTomato.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedTeamId = 1;
				tvDialogTeamName.setText("番茄派");
				tvDialogTeamName.setTextColor(mContext.getResources().getColor(R.color.dialog_team_tomato));
				rlTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_purple));
				rlYellow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_yellow));
				rlLemon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_lemon));
				rlBlueberry.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_blueberry));
				
				
			}
		});
		rlYellow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedTeamId = 2;
				tvDialogTeamName.setText("蛋黄派");
				tvDialogTeamName.setTextColor(mContext.getResources().getColor(R.color.dialog_team_yellow));
				rlTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_tomato));
				rlYellow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_purple));
				rlLemon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_lemon));
				rlBlueberry.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_blueberry));
				
				
			}
		});
		rlLemon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedTeamId = 3;
				tvDialogTeamName.setText("青柠派");
				tvDialogTeamName.setTextColor(mContext.getResources().getColor(R.color.dialog_team_lemon));
				rlTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_tomato));
				rlYellow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_yellow));
				rlLemon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_purple));
				rlBlueberry.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_blueberry));
				
				
			}
		});
		rlBlueberry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedTeamId = 4;
				tvDialogTeamName.setText("蓝莓派");
				tvDialogTeamName.setTextColor(mContext.getResources().getColor(R.color.dialog_team_blueberry));
				rlTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_tomato));
				rlYellow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_yellow));
				rlLemon.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_lemon));
				rlBlueberry.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_purple));
				
				
			}
		});
		btnTeamSelectOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(selectedTeamId == 0){
					Toast.makeText(mContext, "请选择分组!", 1000).show();
				}else{
					Message msg = new Message();
					msg.arg1 = selectedTeamId;
					msg.what = 0;
					handlerSetTeamRequest.sendMessage(msg);
					
					handlerPartyUserInfoRequest.sendEmptyMessage(0);
				}
			}
		});
		
	}
	
	public void SetDialogUserInfoTeamInfo(PartyUserInfoEntity partyUserInfo){
		if(partyUserInfo.getTeamid() != -1){
			tvDialogTeamName.setVisibility(View.INVISIBLE);
			rlYellow.setVisibility(View.INVISIBLE);
			rlLemon.setVisibility(View.INVISIBLE);
			rlBlueberry.setVisibility(View.INVISIBLE);
			btnTeamSelectOk.setVisibility(View.INVISIBLE);
			rlTomato.setClickable(false);
		}
		if(partyUserInfo.getTeamid() == 1){
			tvTomato.setText("番茄派");
			ivTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_tomato));
			rlTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_tomato));
		}else if(partyUserInfo.getTeamid() == 2){
			tvTomato.setText("蛋黄派");
			ivTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_yellow));
			rlTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_yellow));
		}else if(partyUserInfo.getTeamid() == 3){
			tvTomato.setText("青柠派");
			ivTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_lemon));
			rlTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_lemon));
		}else if(partyUserInfo.getTeamid() == 4){
			tvTomato.setText("蓝莓派");
			ivTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_blueberry));
			rlTomato.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedbutton_blueberry));
		}
	}
	
	public void SetDialogUserInfoValue(PartyUserInfoEntity partyUserInfo){
		tvDialogGoldCoins.setText(partyUserInfo.getScore()+"");
		tvDialogCreativeValue.setText(partyUserInfo.getAgreeweight1()+"");
		tvDialogFunnyValue.setText(partyUserInfo.getAgreeweight2()+"");
		tvDialogPracticalValue.setText(partyUserInfo.getAgreeweight3()+"");
		tvDialogConfusedValue.setText(partyUserInfo.getAgreeweight4()+"");
		
		ImageLoader.getInstance().displayImage(Constant.URL_GET_USERIMAGE
				+UserManager.getInstance().getUserInfo().getUid(), dialogUserImage,options);
		tvDialogUserName.setText(UserManager.getInstance().getUserInfo().getUsername());
		
		if(partyUserInfo.getRoleid() <= 0){
			tvGameName.setText("");
		}else{
			tvGameName.setText(roleNames[partyUserInfo.getRoleid()-1]);
		}
		
		for(int i=0;i<partyUserInfo.getRoleids().size();i++){
			if(partyUserInfo.getRoleids().get(i).getRoleid() == 1){
				ivDialogCeramics.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_ceramics_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 2){
				ivDialogPorcelain.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_porcelain_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 3){
				ivDialogMud.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_mud_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 4){
				ivDialogTeaCake.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_teacake_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 5){
				ivDialogTeaPot.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_teapot_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 6){
				ivDialogPaperCut.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_papercut_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 7){
				ivDialogCoffee.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_coffee_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 8){
				ivDialogSushi.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_sushi_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 9){
				ivDialogFlower.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_flower_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 10){
				ivDialogFlyingChess.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_flying_chess_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 11){
				ivDialogKTV.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_ktv_light));
			}else if(partyUserInfo.getRoleids().get(i).getRoleid() == 12){
				ivDialogTableGames.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.party_home_venue_dialog_table_games_light));
			}
		}
		
	}
	
	private OnRefreshListener onRefreshListener = new OnRefreshListener() {
		public void onRefresh() {
			// Do work to refresh the list here.
			
			if(NetWorkState.isConnectingToInternet()){
				handlerPartyExperiencesRequest.sendEmptyMessage(0);
			}
			
		}
	};
	
	Handler handlerPartyUserInfoRequest = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:

				partyGetUserInfoRequest = new PartyGetUserInfoRequest(
						UserManager.getInstance().getUserInfo().getUid()+"",
						new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						
						Gson gson = new Gson();
						
						PartyUserInfoEntity partyUserInfoEntity = gson.fromJson(response.toString(), PartyUserInfoEntity.class);
						
						if(partyUserInfoEntity.getResult() == 1){
							SetUserInfoValue(partyUserInfoEntity);
							SetDialogUserInfoValue(partyUserInfoEntity);
							SetDialogUserInfoTeamInfo(partyUserInfoEntity);
						}
					}
					
				});
				partyGetUserInfoRequest.setTag(TAG);
				apiQueue.add(partyGetUserInfoRequest);
			break;
			
			default:
				
				break;
			}
		}
		
	};
	
	Handler handlerSetTeamRequest = new Handler(){

		int teamid;
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			teamid = msg.arg1;
			
			switch(msg.what){
			case 0:
				setTeamRequest = new SetTeamRequest(
						uid+"",teamid+"", new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						
						try {
							if (response !=null && response.getInt("result") == 1) {
								Toast.makeText(mContext, "成功选定团队!", 1000).show();
								Intent intent = new Intent();
								intent.setClass(mContext, PartyHomeVenueActivity.class);
								startActivity(intent);
								finish();
							}else if (response !=null && response.getInt("result") == 0) {
								Toast.makeText(mContext, "已经选定团队!", 1000).show();
							}else {
								Toast.makeText(mContext, "团队选择异常,请稍后再试!", 1000).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				});
				setTeamRequest.setTag(TAG);
				apiQueue.add(setTeamRequest);
			break;
			
			default:
				
				break;
			}
		}
		
	};
	
	
	Handler handlerPartyExperiencesRequest = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				partyExperiencesListRequest = new PartyExperiencesListRequest(
						experienceId+"",
						new Listener<JSONObject>() {
					
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						
						Gson gson = new Gson();
						
						PartyExperiencesListEntity experiencesListEntity = gson.fromJson(response.toString(), PartyExperiencesListEntity.class);
						
						partyExperiencesList.clear();
						partyExperiencesList.addAll(experiencesListEntity.getExperiencing());
						
						handlerStateChanged.sendEmptyMessage(0);
						handlerStateChanged.sendEmptyMessage(1);
						
						if(partyExperiencesList.size() == 0){
							rlPartyExperiencesNoTip.setVisibility(View.VISIBLE);
						}
					}
					
				});
				partyExperiencesListRequest.setTag(TAG);
				apiQueue.add(partyExperiencesListRequest);
			break;
			
			
			default:
				
				break;
			}
		}
		
	};
	
	Handler handlerStateChanged = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				
				if(partyExperiencesListAdapter == null){
					partyExperiencesListAdapter = new PartyExperiencesListAdapter(mContext,partyExperiencesList,apiQueue);
					partyExperiencesListView.setAdapter(partyExperiencesListAdapter);
					
				}else{
					partyExperiencesListAdapter.notifyDataSetChanged();
				}
				
				break;
			case 1:
				partyExperiencesListView.onRefreshComplete();
				break;
			default:
				break;
			}
		}
	};
	
}
