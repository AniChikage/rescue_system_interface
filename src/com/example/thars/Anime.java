package com.example.thars;

import com.chen.modl.User;
import com.chen.sqlite.dao.UserDAO;
import com.test.BTClient.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 实现SQLite数据库增加、删除、修改、查询操作
 * 
 */
public class Anime extends Activity implements OnClickListener{
 private static final String TAG = "err";
private EditText edname,edpass,edid;
 private ListView listview;
 private TextView datashow;
 private Button buadd,buupdate,budelete,bushow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anime);
		/*
		 * 引入组件
		 */
		edname = (EditText)findViewById(R.id.edname);
		edpass = (EditText)findViewById(R.id.edpass);
		edid = (EditText)findViewById(R.id.edid);
		
		listview = (ListView)findViewById(R.id.listview);
		
		datashow = (TextView)findViewById(R.id.datashow);
		
		buadd = (Button)findViewById(R.id.buadd);
		buupdate = (Button)findViewById(R.id.buupdate);
		budelete = (Button)findViewById(R.id.budelete);
		bushow = (Button)findViewById(R.id.bushow);
		
		/*
		 * 监听器
		 */
		buadd.setOnClickListener(this);
		buupdate.setOnClickListener(this);
		budelete.setOnClickListener(this);
		bushow.setOnClickListener(this);
	}
   public void empty(){
       edid.setText("");
       edname.setText("");
       edpass.setText("");
   }
	@Override
	public void onClick(View v) {
		UserDAO userDAO = new UserDAO(this);
		switch (v.getId()) {
		//添加
		case R.id.buadd:
			try{
			
			User user = new User(Integer.valueOf(edid.getText().toString()), edname.getText().toString(), edpass.getText().toString());
			userDAO.add(user);
            Toast.makeText(Anime.this, "成功添加！", Toast.LENGTH_LONG)  
            .show(); 
            datashow.setText("新添数据为："+"\n"+"ID:"+Integer.valueOf(edid.getText().toString())+","+"姓名:"+edname.getText().toString()
            		+","+"密码:"+edpass.getText().toString()
            		);
             empty();
			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
			break;
		//删除
        case R.id.budelete:
        	try{
        		if(!edid.getText().toString().equals("")){
        		userDAO.delete(Integer.valueOf(edid.getText().toString()));
        		 Toast.makeText(Anime.this, "您成功删除了"+Integer.valueOf(edid.getText().toString()), Toast.LENGTH_LONG).show();
        		 empty();
        		}else {
        			 Toast.makeText(Anime.this, "请输入你想要删除的ID", Toast.LENGTH_LONG).show();
        		}
        		}catch (Exception e) {
        		Log.i(TAG, e.getMessage());
			}
        	break;
        //修改
        	case R.id.buupdate:
        		try{
        			User user = userDAO.find(Integer.valueOf(edid.getText().toString()));
        			user.setUname(edname.getText().toString());
        			user.setUpassword(edpass.getText().toString());
        			userDAO.update(user);
        			Toast.makeText(Anime.this, "修改成功", Toast.LENGTH_LONG).show();
                    datashow.setText("修改后数据为："+"\n"+"ID:"+Integer.valueOf(edid.getText().toString())+","+"姓名:"+edname.getText().toString()
                    		+","+"密码:"+edpass.getText().toString()
                    		);
        			 empty();
        		}catch (Exception e) {
        			Log.i(TAG, e.getMessage());
        			Toast.makeText(Anime.this, "出错了", Toast.LENGTH_LONG).show();
				}
        		break;
        //查询
        	case R.id.bushow:
        		try{
                   Cursor cursor = userDAO.select();
                   SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listview, cursor, 
                		  new String[]{"_id","uname","upassword"},
                		  new int[]{R.id.tvuid,R.id.tvuname,R.id.tvupass});
                 listview.setAdapter(adapter);
        			datashow.setText("");
        		}catch (Exception e) {
        			Log.i(TAG, e.getMessage());
        			Toast.makeText(Anime.this, "显示不了"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
        		break;
		default:
			break;
		}
		
	}
 
}

