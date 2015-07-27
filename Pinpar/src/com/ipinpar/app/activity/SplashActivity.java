package com.ipinpar.app.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.sharesdk.framework.ShareSDK;

import com.android.volley.Response.Listener;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.ipinpar.app.PPBaseActivity;
import com.ipinpar.app.R;
import com.ipinpar.app.db.dao.UserDao;
import com.ipinpar.app.entity.UserEntity;
import com.ipinpar.app.manager.UserManager;
import com.ipinpar.app.network.api.LoginRequest;
import com.ipinpar.app.util.MD5Util;
import com.ipinpar.app.util.PreferenceUtils;
import com.ipinpar.app.util.VersionUtils;

public class SplashActivity extends PPBaseActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_splash);
		int lastversion = PreferenceUtils.getPrefInt(mContext, "curr_version_code", 0);
		if (lastversion < VersionUtils.getCurrentVersionCode(mContext)) {
			PreferenceUtils.setPrefBoolean(mContext, "show_intro", true);
		}
				
		final UserEntity entity = UserDao.getInstance().getLogedUser();
		ShareSDK.initSDK(mContext);

		if (entity != null) {
			UserManager.getInstance().setUserInfo(entity);
			LoginRequest request = new LoginRequest(entity.getMobile(), entity.getPassword(), new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					UserEntity userEntity = gson.fromJson(response.toString(), UserEntity.class);
					if (userEntity != null && userEntity.getResult() == 1) {
						userEntity.setPassword(entity.getPassword());
						UserDao.getInstance().insertUser(userEntity);
						UserManager.getInstance().setUserInfo(userEntity);
					}
					else {
						UserManager.getInstance().logOut();
					}

				}
			});
			apiQueue.add(request);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
				         // 调用sdk注册方法
				         EMChatManager.getInstance().createAccountOnServer(entity.getUid()+"", MD5Util.MD5(entity.getUid()+"pinpa"));
				      } catch (final Exception e) {
							e.printStackTrace();

				      }
				}
			}).start();;
			 
			EMChatManager.getInstance().login(entity.getUid()+"",
					MD5Util.MD5(entity.getUid()+"pinpa"),
					new EMCallBack() {//回调
				@Override
				public void onSuccess() {
					EMChatManager.getInstance().updateCurrentUserNick(entity.getUsername());
					runOnUiThread(new Runnable() {
						public void run() {
//							EMGroupManager.getInstance().loadAllGroups();
							EMChatManager.getInstance().loadAllConversations();
							Log.d("main", "登陆聊天服务器成功！");		
						}
					});
				}

				@Override
				public void onProgress(int progress, String status) {

				}

				@Override
				public void onError(int code, String message) {
					Log.d("main", "登陆聊天服务器失败！");
				}
			});
		}
		     
		final Timer timer = new Timer();

		TimerTask timerTask = new TimerTask() {	
			int time = 3;
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (time > 0 ) {
					time--;
				}
				else {
					runOnUiThread(new Runnable() {
						public void run() {
							timer.cancel();
							if (PreferenceUtils.getPrefBoolean(mContext, "show_intro", true)) {
								startActivity(new Intent(mContext, IntroActivity.class));
							}
							else{
								startActivity(new Intent(mContext,MainActivity.class));
							}
							finish();
						}
					});
				}
			}
		};
		timer.schedule(timerTask, 1000, 1000);
	}

}
