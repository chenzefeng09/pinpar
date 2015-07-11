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
	private static final String COLUMN_IMGSRC = "imgSrc";


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
	
	public void insertUser(UserEntity user){
		openDB();
		ContentValues values = new ContentValues();
		values.put(COLUMN_UID, user.getUid());
		values.put(COLUMN_EMAIL, user.getEmail());
		values.put(COLUMN_UNAME, user.getUsername());
		values.put(COLUMN_MOBILE, user.getMobile());
		values.put(COLUMN_UID, user.getUid());
		values.put(COLUMN_IMGSRC, user.getImgsrc());
		sqLiteDatabase.insert(TABLE_NAME, null, values);
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
		return entity;
	}
}
