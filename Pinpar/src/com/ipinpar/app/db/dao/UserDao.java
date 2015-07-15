package com.ipinpar.app.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.ipinpar.app.db.PPDBService;
import com.ipinpar.app.entity.UserEntity;

public class UserDao extends PPDBService {
	
	
	private static final String TABLE_NAME = "user";
	private static final String COLUMN_UID = "uid";
	private static final String COLUMN_UNAME = "username";
	private static final String COLUMN_MOBILE = "mobile";
	private static final String COLUMN_EMAIL = "email";
	private static final String COLUMN_IMGSRC = "imgsrc";

	private static final String COLUMN_LLONGIN_IP = "lastloginip";
	private static final String COLUMN_LATITUDE = "latitude";
	private static final String COLUMN_LONGITUDE = "longitude";
	private static final String COLUMN_PASSWORD = "password";
	private static final String COLUMN_PLATFORM = "platform";
	private static final String COLUMN_QQ = "qq";
	private static final String COLUMN_REGDATE = "regdate";
	private static final String COLUMN_REGIP = "regip";
	private static final String COLUMN_SALT = "salt";
	private static final String COLUMN_SEX = "sex";
	private static final String COLUMN_SIGNATURE = "signature";
	private static final String COLUMN_WEIXIN = "weixin";


	private static UserDao instance;
	
	public static UserDao getInstance(){
		if (instance == null) {
			synchronized (UserDao.class) {
				if (instance == null) {
					instance = new UserDao();
				}
			}
		}
		return instance;
	}
	
	public boolean hasUserInfo(){
		openDB();
		Cursor cursor =sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null && cursor.getCount() != 0) {
			closeDB();
			return true;
		}
		closeDB();
		return false;
	}
	
	public UserEntity getLogedUser(){
		openDB();
		Cursor cursor =sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			UserEntity entity = fillCursor(cursor);
			closeDB();
			return entity;
		}
		closeDB();
		return null;
	}
	
	public void clearUsers(){
		openDB();
		sqLiteDatabase.delete(TABLE_NAME, null, null);
		closeDB();
	}
	
	public void insertUser(UserEntity user){
		openDB();
		ContentValues values = new ContentValues();
		values.put(COLUMN_UID, user.getUid());
		values.put(COLUMN_EMAIL, user.getEmail());
		values.put(COLUMN_UNAME, user.getUsername());
		values.put(COLUMN_MOBILE, user.getMobile());
<<<<<<< HEAD
		values.put(COLUMN_IMGSRC, user.getImgsrc());
		values.put(COLUMN_LLONGIN_IP, user.getLastloginip());
		values.put(COLUMN_LATITUDE, user.getLatitude());
		values.put(COLUMN_LONGITUDE, user.getLongitude());
		values.put(COLUMN_PASSWORD, user.getPassword());
		values.put(COLUMN_PLATFORM, user.getPlatform());
		values.put(COLUMN_QQ, user.getQq());
		values.put(COLUMN_REGDATE, user.getRegdate());
		values.put(COLUMN_REGIP, user.getRegip());
		values.put(COLUMN_SALT, user.getSalt());
		values.put(COLUMN_SEX, user.getSex());
		values.put(COLUMN_SIGNATURE, user.getSignature());
		values.put(COLUMN_WEIXIN, user.getWeixin());
		sqLiteDatabase.replace(TABLE_NAME, null, values);
=======
		values.put(COLUMN_UID, user.getUid());
		values.put(COLUMN_IMGSRC, user.getImgsrc());
		sqLiteDatabase.insert(TABLE_NAME, null, values);
>>>>>>> 0a84fae454259b4198485d9f31bee1a8429a1e66
		closeDB();
	}
	
	public UserEntity queryUser(int uid){
		openDB();
		Cursor cursor = sqLiteDatabase.query(TABLE_NAME,
				null,
				COLUMN_UID+"=?",
				new String[]{uid+""},
				null, null, null);
		UserEntity userEntity = fillCursor(cursor);
		closeDB();
		return userEntity;
	}

	private UserEntity fillCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		UserEntity entity = new UserEntity();
		entity.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
		entity.setImgsrc(cursor.getString(cursor.getColumnIndex(COLUMN_IMGSRC)));
		entity.setMobile(cursor.getString(cursor.getColumnIndex(COLUMN_MOBILE)));
		entity.setUid(cursor.getInt(cursor.getColumnIndex(COLUMN_UID)));
		entity.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_UNAME)));
		entity.setLastloginip(cursor.getString(cursor.getColumnIndex(COLUMN_LLONGIN_IP)));
		entity.setLatitude(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE)));
		entity.setLongitude(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE)));
		entity.setPlatform(cursor.getString(cursor.getColumnIndex(COLUMN_PLATFORM)));
		entity.setQq((cursor.getString(cursor.getColumnIndex(COLUMN_QQ))));
		entity.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
		entity.setRegdate(cursor.getLong(cursor.getColumnIndex(COLUMN_REGDATE)));
		entity.setRegip(cursor.getString(cursor.getColumnIndex(COLUMN_REGIP)));
		entity.setSalt(cursor.getString(cursor.getColumnIndex(COLUMN_SALT)));
		entity.setSignature(cursor.getString(cursor.getColumnIndex(COLUMN_SIGNATURE)));
		entity.setWeixin(cursor.getString(cursor.getColumnIndex(COLUMN_WEIXIN)));
		entity.setSex(cursor.getInt(cursor.getColumnIndex(COLUMN_SEX)));
		return entity;
	}
}
