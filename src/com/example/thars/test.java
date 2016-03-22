package com.example.thars;

import com.chen.modl.User;
import com.chen.sqlite.dao.UserDAO;
import com.test.BTClient.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class test extends Activity{
	
	private Button btn_anime = null;
	
	protected void onCreate(Bundle savadInstanceState)
	{
		super.onCreate(savadInstanceState);
		setContentView(R.layout.test);
		
		btn_anime = (Button)findViewById(R.id.btn_anime);
		btn_anime.setOnClickListener(new anime_OnClickListener());
	}
	
	class anime_OnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
	        intent.setClass(test.this, Anime.class);
	        startActivity(intent);
			
		}
	}

}
