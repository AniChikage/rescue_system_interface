package com.chen.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "myuser.db";
	public static final String TB_NAME = "u_user";

	// 构造方法
	public SqliteHelper(Context context) {

		super(context, DB_NAME, null, 1);
	}
	/**
	 * 创建新数据库表
	 */
	@Override
	/**
	 * 注意：似乎一定要用_id,而不能用如_usid
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists "+TB_NAME+"(_id integer primary key,uname varchar(8),upassword varchar(20))");

	}

	/**
	 * 当检测与前一次创建数据库版本不一样时，先删除表再创建新表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	public void updateColumn(SQLiteDatabase db, String oldColumn,
			String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + TB_NAME + " CHANGE " + oldColumn + " "
					+ newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
