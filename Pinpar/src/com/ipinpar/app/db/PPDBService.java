package com.ipinpar.app.db;

import android.database.sqlite.SQLiteDatabase;


public class PPDBService {
	public PPDBHelper helper;
	public SQLiteDatabase sqLiteDatabase;
	
	public PPDBService(){
		helper = new PPDBHelper();
	}
	
	public void openDB(){
		sqLiteDatabase = helper.getWritableDatabase();
	}
	
	public void closeDB(){
		if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
			sqLiteDatabase.close();
		}
	}

}
