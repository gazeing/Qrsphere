package com.qrsphere.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{

	private static String NAME = "qrcode_store.db";
	private static int VERSION = 1;
	public DBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS qrcode (id integer primary key autoincrement, rawdata varchar(3999), time long, hashcode integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS favor (id integer primary key autoincrement, rawdata varchar(3999), time long, hashcode integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS userinfo (id integer primary key autoincrement, infodata varchar(3999))");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS qrcode");
		onCreate(db);
		
	}
	
}
