package com.chen.provider;

import com.chen.sqlite.dao.SqliteHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class UserContentProvider extends ContentProvider
{
	private SqliteHelper dbOpenHelper;
	private final static int USERS = 1;
	private final static int USER = 2;
	private final static String AUTHORITY = "com.chen.provider.userprovider";
	private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	//添加两种URI
	static
	{
		//content://com.chen.provider.userprovider/user
		sMatcher.addURI(AUTHORITY, "user", USERS);
		//content://com.chen.provider.userprovider/user/#
		sMatcher.addURI(AUTHORITY, "user/#", USER);
	}
	@Override
	public boolean onCreate()
	{
		dbOpenHelper = new SqliteHelper(this.getContext());
		return true;
	}

	/*
	 * 
	 * 删除
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int count = 0;
		switch (sMatcher.match(uri))
		{
		//uri="content://com.chen.provider.userprovider/person"
		case USERS:
			count = db.delete("u_user", selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
		return count;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (sMatcher.match(uri))
		{
		case USERS:
			return "vnd.android.cursor.dir/personprovider.person";
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
	}
	/*
	 * 添加
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		long pid = 0;
		switch (sMatcher.match(uri))
		{
		case USERS:
			pid = db.insert("u_user", "uname", values);
//			pid = db.insert("u_user", "upassword", values);
			//content://com.chen.provider.userprovider/user/pid
			return ContentUris.withAppendedId(uri, pid);
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
	}


	@Override
	/*
	 * 查询
	 */
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		switch (sMatcher.match(uri))
		{
		case USERS:
			return db.query("u_user", projection, selection, selectionArgs, null, null, sortOrder);
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
	}

	@Override
	/*
	 * 更新
	 */
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int count = 0;
		switch (sMatcher.match(uri))
		{
		case USERS:
			//content://com.chen.provider.userprovider/user
			count = db.update("u_user", values, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
		return count;
	}
}
