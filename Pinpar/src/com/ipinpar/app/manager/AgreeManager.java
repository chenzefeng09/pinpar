package com.ipinpar.app.manager;

import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.ipinpar.app.db.dao.AgreeDao;
import com.ipinpar.app.entity.AgreeEntity;
import com.ipinpar.app.network.api.AgreeRequest;

public class AgreeManager {
	
	private HashSet<AgreeEntity> userAgrees;
	private static AgreeManager instance;
	private AgreeManager() {
		if (UserManager.getInstance().isLogin()) {
			userAgrees = AgreeDao.getInstance().listAgree(
					UserManager.getInstance().getUserInfo().getUid());
		}
	}
	
	public static AgreeManager getInstance(){
		if (instance == null) {
			synchronized (AgreeManager.class) {
				if (instance == null) {
					instance = new AgreeManager();
				}
			}
		}
		return instance;
	}
	
	public void agree(final int fromid,final String fromidtype,RequestQueue queue){
		agree(fromid, fromidtype, null, queue);
	}
	
	public void agree(final int fromid,final String fromidtype,final AgreeResultListener listener,RequestQueue queue){
		final AgreeEntity entity = new AgreeEntity(fromid,fromidtype);
		if (UserManager.getInstance().isLogin()) {

		if (userAgrees.contains(entity)) {
			AgreeRequest request = new AgreeRequest(UserManager.getInstance().getUserInfo().getUid(),
					fromidtype, fromid, "disagree", new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response !=null && response.getInt("result") == 1) {
									userAgrees.remove(entity);
									AgreeDao.getInstance().disagree(
											UserManager.getInstance().getUserInfo().getUid(), 
											fromid, fromidtype);
									if (listener != null) {
										listener.onAgreeResult(false);
									}
								}
								else if (response !=null && response.getInt("result") == 0) {
									userAgrees.remove(entity);
									AgreeDao.getInstance().disagree(
											UserManager.getInstance().getUserInfo().getUid(), 
											fromid, fromidtype);
									if (listener != null) {
										listener.onAgreeResult(true);
									}
								}
								else if (response !=null && response.getInt("result") == 102) {
									userAgrees.remove(entity);
									AgreeDao.getInstance().disagree(
											UserManager.getInstance().getUserInfo().getUid(), 
											fromid, fromidtype);
									if (listener != null) {
										listener.onAgreeResult(false);
									}
								}
								else {
									if (listener != null) {
										listener.onAgreeResult(false);
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			queue.add(request);
		}
		else {
			AgreeRequest request = new AgreeRequest(UserManager.getInstance().getUserInfo().getUid(),
					fromidtype, fromid, "agree", new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response !=null && response.getInt("result") == 1) {
									userAgrees.add(entity);
									AgreeDao.getInstance().agree(
											UserManager.getInstance().getUserInfo().getUid(), 
											fromid, fromidtype);
									if (listener != null) {
										listener.onAgreeResult(true);
									}
								}
								else if (response !=null && response.getInt("result") == 0) {
									userAgrees.add(entity);
									AgreeDao.getInstance().agree(
											UserManager.getInstance().getUserInfo().getUid(), 
											fromid, fromidtype);
									if (listener != null) {
										listener.onAgreeResult(false);
									}
								}
								else if (response !=null && response.getInt("result") == 101) {
									userAgrees.add(entity);
									AgreeDao.getInstance().agree(
											UserManager.getInstance().getUserInfo().getUid(), 
											fromid, fromidtype);
									if (listener != null) {
										listener.onAgreeResult(true);
									}
								}
								else {
									if (listener != null) {
										listener.onAgreeResult(true);
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
			queue.add(request);
		}
		}
	}
	
	public boolean isAgreed(int fromid,String fromidtype){
		AgreeEntity agreeEntity = new AgreeEntity(fromid, fromidtype);
		return userAgrees.contains(agreeEntity);
	}
	
	public static interface AgreeResultListener{
		public void onAgreeResult(boolean agree);
	}
	

}
