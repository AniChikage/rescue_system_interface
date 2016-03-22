package com.BTClient1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.BTClient1.BTClient;
import com.example.thars.LoginActivity;
import com.example.thars.RegisterInfo;
import com.test.BTClient.R;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActivityMain extends Activity {

	private Button lanya = null;
	private Button add = null;
	public static SQLiteDatabase database; 
    private String DB_PATH = "/data/data/com.test.BTClient/files/"; 
    String DB_NAME = "test.dbs"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tharsmain);
		
		lanya = (Button)findViewById(R.id.main_lanya);
		add   = (Button)findViewById(R.id.main_tianjia);
		
		lanya.setOnClickListener(new lanya());
		add.setOnClickListener(new addd());

		        // 检查 SQLite 数据库文件是否存在  
		        if ((new File(DB_PATH + DB_NAME)).exists() == false) { 
		            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在  
		            File f = new File(DB_PATH); 
		            // 如 database 目录不存在，新建该目录  
		            if (!f.exists()) { 
		                f.mkdir(); 
		            } 

		            try { 
		                // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流  
		                InputStream is = getBaseContext().getAssets().open(DB_NAME); 
		                // 输出流  
		                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME); 
		 
		 
		                // 文件写入  
		                byte[] buffer = new byte[1024]; 
		                int length; 
		                while ((length = is.read(buffer)) > 0) { 
		                    os.write(buffer, 0, length); 
		                } 

		                // 关闭文件流  
		                os.flush(); 
		                os.close(); 
		                is.close(); 
		            } catch (Exception e) { 
		                e.printStackTrace(); 
		            } 
		        }
		
	}
	
	class lanya implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
	        intent.setClass(ActivityMain.this, BTClient.class);
	        startActivity(intent);
		}
	}
	class addd implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
	        intent.setClass(ActivityMain.this, LoginActivity.class);
	        startActivity(intent);
		}
	}

}
