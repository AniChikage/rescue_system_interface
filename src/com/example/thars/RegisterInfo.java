package com.example.thars;

import com.chen.modl.User;
import com.chen.sqlite.dao.UserDAO;
import com.test.BTClient.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterInfo extends Activity{
	
	  private EditText regis_username;
	  private EditText regis_pwd;
	  private Button btn_register;
	  SQLiteDatabase db;
	 
	  @Override
	  protected void onDestroy() {
	    // TODO Auto-generated method stub
	    super.onDestroy();
	    db.close();
	  }

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.register);
	    regis_username = (EditText) findViewById(R.id.regi_username);
	    regis_pwd = (EditText) findViewById(R.id.regi_pwd);
	    btn_register = (Button) findViewById(R.id.regi_handle_btn);
	    btn_register.setOnClickListener(new regis_OnClickListener());
	  }

	  class regis_OnClickListener implements OnClickListener{
		  @Override
		  public void onClick(View v) {
		        // TODO Auto-generated method stub
		        String name = regis_username.getText().toString();
		        String password = regis_pwd.getText().toString();
		        if (!(name.equals("") && password.equals(""))) {
		          if (addUser(name, password)) {
		                // 跳转到登录界面
		                Intent in = new Intent();
		                in.setClass(RegisterInfo.this,LoginActivity.class);
		                startActivity(in);
		                // 销毁当前activity
		                RegisterInfo.this.onDestroy();
		              }
		          else {
			            new AlertDialog.Builder(RegisterInfo.this)
			                           .setTitle("注册失败")
			                           .setMessage("注册失败")
			                           .setPositiveButton("确定", null);
		          } 
		        } 
		        else {
		          new AlertDialog.Builder(RegisterInfo.this)
		                         .setTitle("帐号密码不能为空")
		                         .setMessage("帐号密码不能为空")
		                         .setPositiveButton("确定", null);
		        }
		      }
	  }
	  // 添加用户
 	  public Boolean addUser(String name, String password) {
	    String str = "insert into tb_user values(?,?) ";
	    LoginActivity main = new LoginActivity();
	    db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()
	        + "/test.dbs", null);
	    LoginActivity.db = db;
	    try {
	      db.execSQL(str, new String[] { name, password });
	      return true;
	    } catch (Exception e) {
	      main.createDb();
	      return false;
	    }
	  }
}
