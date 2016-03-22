package com.chen.sqlite.dao;

import com.chen.modl.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {
	 private SqliteHelper helper;
	 //写入 ，不然会是出错，是空指针
	 public UserDAO(Context context){
		 helper=new SqliteHelper(context);
	 }
	 /**
	  * 添加用户信息
	  */
	 
	 public void add(User user){
		 SQLiteDatabase db=helper.getWritableDatabase(); 
		 String sql="Insert into u_user(_id,uname,upassword) values(?,?,?)";
		 db.execSQL(sql, new Object[]
		                            {
				 user.getUid(),user.getUname(),user.getUpassword()
		                            }                           
		 );
		 db.close();
	 }
	 /**
	  * 删除用户信息
	  */
	  public void delete(Integer...uid){
		  if(uid.length>0){
			 StringBuffer sb=new StringBuffer();
			 for(int i=0;i<uid.length;i++){
				 sb.append("?").append(",");
			 }
			 sb.deleteCharAt(sb.length()-1);
			 SQLiteDatabase database=helper.getWritableDatabase();
			 String sql="delete from u_user where _id in ("+sb+")";
			 database.execSQL(sql, (Object[])uid);
		  }
	  }


	  /**
	   * 用户修改
	   */
	  public void update(User user){
		  SQLiteDatabase db=helper.getWritableDatabase();//写入数据库中注意！！！！不能放在外面
	 	 String sql="update u_user set uname=?,upassword=? where _id=?";
	 	 db.execSQL(sql, new Object[]{
	 			user.getUname(),user.getUpassword(),user.getUid()
	 	 });
	  }
	  /**
	   * 查找用户信息
	   */
	  public User find(int uid){
		  SQLiteDatabase db=helper.getWritableDatabase();//写入数据库中注意！！！！不能放在外面
	 	 String sql="select _id,uname,upassword from u_user where _id=?";
	 	 Cursor cursor=db.rawQuery(sql, new String[]{
	 			 String.valueOf(uid)
	 	 });
	 	 if(cursor.moveToNext()){
	 		 return new User(
	 				 cursor.getInt(cursor.getColumnIndex("_id")),
	 				 cursor.getString(cursor.getColumnIndex("uname")), 
	 		         cursor.getString(cursor.getColumnIndex("upassword"))
	 				);
	 	 }
	 		 return null;
	 }
	  
	/**
	 * 显示用户
	 */
		public Cursor select() {
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.query("u_user",
					null, null, null, null,
					null, "_id desc");
			return cursor;
		}
}
