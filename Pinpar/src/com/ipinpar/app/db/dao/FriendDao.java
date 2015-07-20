package com.ipinpar.app.db.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.ipinpar.app.db.PPDBService;
import com.ipinpar.app.entity.FriendEntity;
import com.ipinpar.app.entity.UserEntity;

public class FriendDao extends PPDBService {
	private static final String TABLE_NAME = "friend";
	private static final String COLUMN_UID = "uid";
	private static final String COLUMN_UNAME = "username";
	private static final String COLUMN_IMGSRC = "imgsrc";
	
private static FriendDao instance;
	
	public static FriendDao getInstance(){
		if (instance == null) {
			synchronized (FriendDao.class) {
				if (instance == null) {
					instance = new FriendDao();
				}
			}
		}
		return instance;
	}
	
	public void clearFriends(){
		openDB();
		sqLiteDatabase.delete(TABLE_NAME, null, null);
		closeDB();
	}
	
	public void insertUser(FriendEntity user){
		openDB();
		ContentValues values = new ContentValues();
		values.put(COLUMN_UID, user.getUid());
		values.put(COLUMN_UNAME, user.getUsername());
		values.put(COLUMN_IMGSRC, user.getImgsrc());
		sqLiteDatabase.replace(TABLE_NAME, null, values);
		closeDB();
	}
	
	public FriendEntity queryUser(int uid){
		openDB();
		Cursor cursor = sqLiteDatabase.query(TABLE_NAME,
				null,
				COLUMN_UID+"=?",
				new String[]{uid+""},
				null, null, null);
		cursor.moveToFirst();
		FriendEntity userEntity = fillCursor(cursor);
		closeDB();
		return userEntity;
	}
	
	private FriendEntity fillCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		FriendEntity entity = new FriendEntity();
		entity.setImgsrc(cursor.getString(cursor.getColumnIndex(COLUMN_IMGSRC)));
		entity.setUid(cursor.getInt(cursor.getColumnIndex(COLUMN_UID)));
		entity.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_UNAME)));
		return entity;
	}

	public void insertUsers(ArrayList<FriendEntity> friends) {
		// TODO Auto-generated method stub
		openDB();
		sqLiteDatabase.beginTransaction();
		for(FriendEntity entity :friends){
			ContentValues values = new ContentValues();
			values.put(COLUMN_UID, entity.getUid());
			values.put(COLUMN_UNAME, entity.getUsername());
			values.put(COLUMN_IMGSRC, entity.getImgsrc());
			sqLiteDatabase.replace(TABLE_NAME, null, values);
		}
		sqLiteDatabase.setTransactionSuccessful();
		sqLiteDatabase.endTransaction();
		closeDB();
	}

}
