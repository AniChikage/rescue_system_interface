package com.chen.provider;


import com.chen.modl.User;
import com.chen.sqlite.dao.SqliteHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QuesOperate {
	 private SqliteHelper helper;
	 //д�� ����Ȼ���ǳ����ǿ�ָ��
	 public QuesOperate(Context context){
		 helper=new SqliteHelper(context);}
		 

	 /**
	  * ����û���Ϣ
	  */
	 
	 public void add(User user){
		 SQLiteDatabase db=helper.getWritableDatabase(); 
		 String sql="Insert into u_user(uname,upassword) values(?,?)";
		 db.execSQL(sql, new Object[]
		                            {
				 user.getUname(),user.getUpassword()
		                            }                           
		 );
		 db.close();
	 }
	 
	
	  public void delete(String...user_name){
			 SQLiteDatabase database=helper.getWritableDatabase();
		//	 String sql="delete from u_user where uname=?";
			 database.delete("u_user","uname=?",user_name);
	  }
	  public void delete_ques(Integer...uid){
		  if(uid.length>0){
			 StringBuffer sb=new StringBuffer();
			 for(int i=0;i<uid.length;i++){
				 sb.append("?").append(",");
			 }
			 sb.deleteCharAt(sb.length()-1);
			 SQLiteDatabase database=helper.getWritableDatabase();
			 String sql="delete from q_ques where _id in ("+sb+")";
			 database.execSQL(sql, (Object[])uid);
		  }
	  }


	  /**
	   * �û��޸�
	   */
	  public void update(User user){
		  SQLiteDatabase db=helper.getWritableDatabase();//д�����ݿ���ע�⣡���������ܷ�������
	 	 String sql="update u_user set upassword=? where uname=?";
	 	 db.execSQL(sql, new Object[]{
	 			user.getUpassword(),user.getUname()
	 	 });
	  }
	  
	  public void update_learn(String user_name, String cate, int point){
		  SQLiteDatabase db=helper.getWritableDatabase();//д�����ݿ���ע�⣡���������ܷ�������
	 	 String sql="update user_learn_table set " + cate + " =? where user_name=?";
	 	 db.execSQL(sql, new Object[]{
	 			point,user_name
	 	 });
	  }

	  /**
	   * �����û���Ϣ
	   */
	 
	  public SQLiteDatabase find_account(){
		  SQLiteDatabase db=helper.getWritableDatabase();//д�����ݿ���ע�⣡���������ܷ�������
	   
	 	 return db;
	 }
	  
	  
	/**
	 * ��ʾ�û�
	 */
		public Cursor select() {
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.query("u_user",
					null, null, null, null,
					null, "_id desc");
			return cursor;
		}
		
		public Cursor select_ques() {
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.query("q_ques",
					null, null, null, null,
					null, "_id desc");
			return cursor;
		}

		public Cursor select_learn_user() {
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.query("user_learn_table",
					null, null, null, null,
					null, "_id desc");
			return cursor;
		}
}
