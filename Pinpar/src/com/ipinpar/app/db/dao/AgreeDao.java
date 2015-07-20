package com.ipinpar.app.db.dao;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.Cursor;

import com.ipinpar.app.db.PPDBService;
import com.ipinpar.app.entity.AgreeEntity;

public class AgreeDao extends PPDBService {
	private static final String TABLE_NAME = "agree";
	private static final String COLUMN_UID = "uid";
	private static final String COLUMN_FROMID= "fromid";
	private static final String COLUMN_FROMIDTYPE = "fromidtype";
	
private static AgreeDao instance;
	
	public static AgreeDao getInstance(){
		if (instance == null) {
			synchronized (AgreeDao.class) {
				if (instance == null) {
					instance = new AgreeDao();
				}
			}
		}
		return instance;
	}
	
	public void agree(int uid,int fromid,String fromidtype){
		openDB();
		ContentValues contentValues = new ContentValues();
		contentValues.put("uid", uid);
		contentValues.put("fromid", fromid);
		contentValues.put("fromidtype", fromidtype);
		sqLiteDatabase.replace(TABLE_NAME, null, contentValues);
		closeDB();
	}
	
	public void disagree(int uid,int fromid,String fromidtype){
		openDB();
		ContentValues contentValues = new ContentValues();
		contentValues.put("uid", uid);
		contentValues.put("fromid", fromid);
		contentValues.put("fromidtype", fromidtype);
		sqLiteDatabase.delete(TABLE_NAME, COLUMN_FROMID+"=? and "+COLUMN_FROMIDTYPE+"=? and "+COLUMN_UID+"=?",
				new String[]{fromid+"",fromidtype,uid+""});
		closeDB();
	}
	
	public HashSet<AgreeEntity> listAgree(int uid){
		openDB();
		HashSet<AgreeEntity> agreeEntities = new HashSet<AgreeEntity>();
		Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, COLUMN_UID+"=?", 
				new String[]{uid+""}, null, null, null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			AgreeEntity entity = fillCursor(cursor);
			agreeEntities.add(entity);
		}
		closeDB();
		cursor.close();
		return agreeEntities;
	}

	private AgreeEntity fillCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		AgreeEntity entity = new AgreeEntity();
		entity.setFromid(cursor.getInt(cursor.getColumnIndex(COLUMN_FROMID)));
		entity.setFromidtype(cursor.getString(cursor.getColumnIndex(COLUMN_FROMIDTYPE)));
		return entity;
	}
}
