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
 * ʵ��SQLite���ݿ����ӡ�ɾ�����޸ġ���ѯ����
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
		 * �������
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
		 * ������
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
		//���
		case R.id.buadd:
			try{
			
			User user = new User(Integer.valueOf(edid.getText().toString()), edname.getText().toString(), edpass.getText().toString());
			userDAO.add(user);
            Toast.makeText(Anime.this, "�ɹ���ӣ�", Toast.LENGTH_LONG)  
            .show(); 
            datashow.setText("��������Ϊ��"+"\n"+"ID:"+Integer.valueOf(edid.getText().toString())+","+"����:"+edname.getText().toString()
            		+","+"����:"+edpass.getText().toString()
            		);
             empty();
			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
			break;
		//ɾ��
        case R.id.budelete:
        	try{
        		if(!edid.getText().toString().equals("")){
        		userDAO.delete(Integer.valueOf(edid.getText().toString()));
        		 Toast.makeText(Anime.this, "���ɹ�ɾ����"+Integer.valueOf(edid.getText().toString()), Toast.LENGTH_LONG).show();
        		 empty();
        		}else {
        			 Toast.makeText(Anime.this, "����������Ҫɾ����ID", Toast.LENGTH_LONG).show();
        		}
        		}catch (Exception e) {
        		Log.i(TAG, e.getMessage());
			}
        	break;
        //�޸�
        	case R.id.buupdate:
        		try{
        			User user = userDAO.find(Integer.valueOf(edid.getText().toString()));
        			user.setUname(edname.getText().toString());
        			user.setUpassword(edpass.getText().toString());
        			userDAO.update(user);
        			Toast.makeText(Anime.this, "�޸ĳɹ�", Toast.LENGTH_LONG).show();
                    datashow.setText("�޸ĺ�����Ϊ��"+"\n"+"ID:"+Integer.valueOf(edid.getText().toString())+","+"����:"+edname.getText().toString()
                    		+","+"����:"+edpass.getText().toString()
                    		);
        			 empty();
        		}catch (Exception e) {
        			Log.i(TAG, e.getMessage());
        			Toast.makeText(Anime.this, "������", Toast.LENGTH_LONG).show();
				}
        		break;
        //��ѯ
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
        			Toast.makeText(Anime.this, "��ʾ����"+e.getMessage(), Toast.LENGTH_LONG).show();
				}
        		break;
		default:
			break;
		}
		
	}
 
}

