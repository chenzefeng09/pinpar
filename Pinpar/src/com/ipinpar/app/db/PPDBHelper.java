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
		db.execSQL("CREATE TABLE IF NOT EXIT user(uid integer,uname text,uicon text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
