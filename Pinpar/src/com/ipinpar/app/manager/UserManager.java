package com.ipinpar.app.manager;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.ipinpar.app.db.dao.EnrollInfoDao;
import com.ipinpar.app.db.dao.UserDao;
import com.ipinpar.app.entity.UserEntity;

public class UserManager {

	private  static UserManager instance;
	
	private UserManager(){};
	
	public static UserManager getInstance(){
		if (instance == null) {
			synchronized (UserManager.class) {
				if (instance == null) {
					instance = new UserManager();
				}
			}
		}
		return instance;
	}
	
	public UserEntity getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserEntity userInfo) {
		this.userInfo = userInfo;
		if (userInfo != null && userInfo.getUid() != 0) {
			isLogin = true;
		}
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	private UserEntity userInfo;
	private boolean isLogin;
	
	public void logOut(){
		userInfo = null;
		isLogin = false;
		UserDao.getInstance().clearUsers();
		EnrollInfoDao.getInstance().clearEnrollInfo();
		//此方法为异步方法
		EMChatManager.getInstance().logout(new EMCallBack() {
		            
			@Override
			public void onSuccess() {
			    
			}
			
			@Override
			public void onProgress(int progress, String status) {
			    // TODO Auto-generated method stub
			    
			}
			
			@Override
			public void onError(int code, String message) {
			    // TODO Auto-generated method stub
			    
			}
		});
	}
}
