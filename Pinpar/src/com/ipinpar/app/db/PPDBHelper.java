package com.ipinpar.app.db;

import com.ipinpar.app.PPApplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PPDBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "ppdb";
	private static final int DB_VERSION = 1;

	public PPDBHelper() {
		super(PPApplication.getContext(), DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT exists user(uid integer PRIMARY KEY ,"
				+ "username text,"
				+ "mobile text,"
				+ "email text,"
				+ "imgsrc text,"
				+ "lastloginip text,"
				+ "latitude text,"
				+ "longitude text,"
				+ "password text,"
				+ "platform text,"
				+ "qq text,"
				+ "regdate long,"
				+ "regip salt,"
				+ "sex INTEGER,"
				+ "salt text,"
				+ "signature text,"
				+ "weixin text"
				+ ")");
		db.execSQL("CREATE TABLE IF NOT exists friend(uid integer PRIMARY KEY ,"
				+ "imgsrc text,"
				+ "username text"
				+ ")");
		db.execSQL("CREATE TABLE IF NOT exists enrollinfo(infoid integer PRIMARY KEY ,"
				+ "uid integer,"
				+ "address1 text,"
				+ "address2 text,"
				+ "address3 text,"
				+ "addressdetail text,"
				+ "idnumber text,"
				+ "isdefault integer,"
				+ "name text,"
				+ "phone text,"
				+ "sex integer,"
				+ "unit text"
				+ ")");
		db.execSQL("CREATE TABLE IF NOT exists agree(uid integer,"
				+ "fromidtype text,"
				+ "fromid integer, PRIMARY KEY(uid,fromidtype,fromid)"
				+ ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
