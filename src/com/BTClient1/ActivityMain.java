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

		        // ��� SQLite ���ݿ��ļ��Ƿ����  
		        if ((new File(DB_PATH + DB_NAME)).exists() == false) { 
		            // �� SQLite ���ݿ��ļ������ڣ��ټ��һ�� database Ŀ¼�Ƿ����  
		            File f = new File(DB_PATH); 
		            // �� database Ŀ¼�����ڣ��½���Ŀ¼  
		            if (!f.exists()) { 
		                f.mkdir(); 
		            } 

		            try { 
		                // �õ� assets Ŀ¼������ʵ��׼���õ� SQLite ���ݿ���Ϊ������  
		                InputStream is = getBaseContext().getAssets().open(DB_NAME); 
		                // �����  
		                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME); 
		 
		 
		                // �ļ�д��  
		                byte[] buffer = new byte[1024]; 
		                int length; 
		                while ((length = is.read(buffer)) > 0) { 
		                    os.write(buffer, 0, length); 
		                } 

		                // �ر��ļ���  
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
