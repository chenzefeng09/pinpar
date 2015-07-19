package com.ipinpar.app.db.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.ipinpar.app.db.PPDBService;
import com.ipinpar.app.entity.EnrollInfoEntity;
import com.ipinpar.app.entity.UserEntity;

public class EnrollInfoDao extends PPDBService {
	private static final String TABLE_NAME = "enrollinfo";
	private static final String COLUMN_UID = "uid";
	private static final String COLUMN_INFOID = "infoid";
	private static final String COLUMN_ADDRESS1 = "address1";
	private static final String COLUMN_ADDRESS2 = "address2";
	private static final String COLUMN_ADDRESS3 = "address3";
	private static final String COLUMN_ADDRESSDETAIL = "addressdetail";

	private static final String COLUMN_IDNUMBER = "idnumber";
	private static final String COLUMN_ISDEFAULT = "isdefault";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PHONE = "phone";
	private static final String COLUMN_SEX = "sex";
	private static final String COLUMN_UNIT = "unit";

private static EnrollInfoDao instance;
	
	public static EnrollInfoDao getInstance(){
		if (instance == null) {
			synchronized (EnrollInfoDao.class) {
				if (instance == null) {
					instance = new EnrollInfoDao();
				}
			}
		}
		return instance;
	}
	
	public void clearEnrollInfo(){
		openDB();
		sqLiteDatabase.delete(TABLE_NAME, null, null);
		closeDB();
	}
	
	public EnrollInfoEntity getDefaultInfo(){
		openDB();
		Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, COLUMN_ISDEFAULT+"=1", null, null, null, null);
		if (cursor.moveToFirst()) {
			EnrollInfoEntity enrollInfoEntity = fillCursor(cursor);
			cursor.close();
			return enrollInfoEntity;
		}
		closeDB();
		return null;

	}
	
	public boolean hasInfo(){
		openDB();
		Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
		if (cursor != null && cursor.getCount() != 0) {
			return true;
		}
		closeDB();
		return false;

	}
	
	public boolean removeInfo(int infoid){
		openDB();
		sqLiteDatabase.delete(TABLE_NAME, COLUMN_INFOID+"=?", new String[]{infoid+""});
		closeDB();
		return false;

	}
	
	public void insertEnrollInfo(EnrollInfoEntity user){
		openDB();
		ContentValues values = new ContentValues();
		values.put(COLUMN_UID, user.getUid());
		values.put(COLUMN_ADDRESS1, user.getAddress1());
		values.put(COLUMN_ADDRESS2, user.getAddress2());
		values.put(COLUMN_ADDRESS3, user.getAddress3());
		values.put(COLUMN_ADDRESSDETAIL, user.getAddressdetail());
		values.put(COLUMN_IDNUMBER, user.getIdnumber());
		values.put(COLUMN_INFOID, user.getInfoid());
		values.put(COLUMN_ISDEFAULT, user.getIsdefault());
		values.put(COLUMN_NAME, user.getName());
		values.put(COLUMN_PHONE, user.getPhone());
		values.put(COLUMN_UNIT, user.getUint());
		values.put(COLUMN_SEX, user.getSex());
		sqLiteDatabase.replace(TABLE_NAME, null, values);
		closeDB();
	}
	
	public void insertEnrollInfos(ArrayList<EnrollInfoEntity> infos){
		openDB();
		sqLiteDatabase.beginTransaction();
		for(EnrollInfoEntity user:infos){
			ContentValues values = new ContentValues();
			values.put(COLUMN_UID, user.getUid());
			values.put(COLUMN_ADDRESS1, user.getAddress1());
			values.put(COLUMN_ADDRESS2, user.getAddress2());
			values.put(COLUMN_ADDRESS3, user.getAddress3());
			values.put(COLUMN_ADDRESSDETAIL, user.getAddressdetail());
			values.put(COLUMN_IDNUMBER, user.getIdnumber());
			values.put(COLUMN_INFOID, user.getInfoid());
			values.put(COLUMN_ISDEFAULT, user.getIsdefault());
			values.put(COLUMN_NAME, user.getName());
			values.put(COLUMN_PHONE, user.getPhone());
			values.put(COLUMN_UNIT, user.getUint());
			values.put(COLUMN_SEX, user.getSex());
			sqLiteDatabase.replace(TABLE_NAME, null, values);
		}
		sqLiteDatabase.setTransactionSuccessful();
		sqLiteDatabase.endTransaction();
		closeDB();
	}
	
	
	
	private EnrollInfoEntity fillCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		EnrollInfoEntity entity = new EnrollInfoEntity();
		entity.setUid(cursor.getInt(cursor.getColumnIndex(COLUMN_UID)));
		entity.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
		entity.setSex(cursor.getInt(cursor.getColumnIndex(COLUMN_SEX)));
		entity.setAddress1(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS1)));
		entity.setAddress2(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS2)));
		entity.setAddress3(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS3)));
		entity.setAddressdetail(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESSDETAIL)));
		entity.setIdnumber(cursor.getString(cursor.getColumnIndex(COLUMN_IDNUMBER)));
		entity.setInfoid(cursor.getInt(cursor.getColumnIndex(COLUMN_INFOID)));
		entity.setIsdefault(cursor.getInt(cursor.getColumnIndex(COLUMN_ISDEFAULT)));
		entity.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
		entity.setUint(cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
		return entity;
	}
}
