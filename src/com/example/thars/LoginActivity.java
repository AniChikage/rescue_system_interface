package com.example.thars;


import com.chen.modl.User;
import com.chen.sqlite.dao.UserDAO;
import com.test.BTClient.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {
    
	private Button btn_register = null;
	private Button btn_login = null;
	private Button btn_anime = null;
    private EditText login_username = null;
    private EditText login_password = null;
    public static SQLiteDatabase db;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login_username = (EditText)findViewById(R.id.username_edit);
		login_password = (EditText)findViewById(R.id.password_edit);
		btn_login = (Button)findViewById(R.id.logino_button);
		btn_register = (Button)findViewById(R.id.register_button);
		btn_anime = (Button)findViewById(R.id.anime_button);
		db = SQLiteDatabase.openOrCreateDatabase(LoginActivity.this.getFilesDir().toString()
		        + "/test.dbs", null);
		
		//log on event
		btn_login.setOnClickListener(new logon_OnClickListener());
		//register event
		btn_register.setOnClickListener(new regi_OnClickListener());
		
		btn_anime.setOnClickListener(new anime_OnClickListener());
		}
	
	    //登陆函数
		class logon_OnClickListener implements OnClickListener{	
			@Override
			public void onClick(View arg0) {
				
				if (login_username.equals("") || login_password.equals("")) {
			        // 弹出消息框
			        new AlertDialog.Builder(LoginActivity.this).setTitle("错误")
			            .setMessage("帐号或密码不能空").setPositiveButton("确定", null)
			            .show();
			      } else {
			        isUserinfo(login_username.getText().toString(), login_password.getText().toString());
			      }
				
			}
		}
		
		//注册函数
		class regi_OnClickListener implements OnClickListener{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.setClass(LoginActivity.this, RegisterInfo.class);
		        startActivity(intent);
				
			}
			
		}
		
		class anime_OnClickListener implements OnClickListener{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.setClass(LoginActivity.this, Anime.class);
		        startActivity(intent);
				
			}
			
		}
		
		//判断用户名和密码函数
		public Boolean isUserinfo(String name, String pwd) {
		      try{
		          String str="select * from tb_user where name=? and password=?";
		          Cursor cursor = db.rawQuery(str, new String []{name,pwd});
		          if(cursor.getCount()<=0){
		          new AlertDialog.Builder(LoginActivity.this)
		                         .setTitle("错误")
		                         .setMessage("帐号或密码错误！")
		                         .setPositiveButton("确定", null)
		                         .show();
		          return false;
		          }
		          else{
		            Intent intent = new Intent();
			        intent.setClass(LoginActivity.this, test.class);
			        startActivity(intent);
		          return true;
		        }
		        
		      }catch(SQLiteException e){
		        createDb();
		      }
		      return false;
		    }
		
		public void createDb() {
		    db.execSQL("create table tb_user( name varchar(30) primary key,password varchar(30))");
		  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
