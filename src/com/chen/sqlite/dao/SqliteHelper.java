package com.chen.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "myuser.db";
	public static final String TB_NAME = "u_user";

	// ���췽��
	public SqliteHelper(Context context) {

		super(context, DB_NAME, null, 1);
	}
	/**
	 * ���������ݿ��
	 */
	@Override
	/**
	 * ע�⣺�ƺ�һ��Ҫ��_id,����������_usid
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists "+TB_NAME+"(_id integer primary key,uname varchar(8),upassword varchar(20))");

	}

	/**
	 * �������ǰһ�δ������ݿ�汾��һ��ʱ����ɾ�����ٴ����±�
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
