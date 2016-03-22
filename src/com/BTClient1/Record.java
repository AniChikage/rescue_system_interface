package com.BTClient1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.test.BTClient.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Record extends Activity {

    ListView recordlistView;
    ArrayList name;
    List<Data> mFileList;
    List<Data> recordListInfo = new ArrayList<Data>();
    String dataPath;
    private TextView txtdataString;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);  
        importRecord();  
        recordlistView = (ListView)findViewById(R.id.recordlistView);
        recordlistView.setAdapter(new DataListViewAdapter(recordListInfo));
        txtdataString = (TextView)findViewById(R.id.txtdata);
        txtdataString.setMovementMethod(ScrollingMovementMethod.getInstance());
        recordlistView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        		Data getObject = recordListInfo.get(position);	//通过position获取所点击的对象
        		String infoId = Environment.getExternalStorageDirectory().toString()+ "/RescueData/"+getObject.getDataPath();	//获取信息id
        		//Toast显示测试
        		//Toast.makeText(Record.this, "信息ID:"+infoId,Toast.LENGTH_SHORT).show();
        	    txtdataString.setText(readSDcard(infoId));
        	}
        });
	}  
	
	private String readSDcard(String path) {
		   StringBuffer str = new StringBuffer();
		   try {
		    // 判断是否存在SD
		    if (Environment.getExternalStorageState().equals(
		      Environment.MEDIA_MOUNTED)) {
		     File file = new File(path);                                                                
		     // 判断是否存在该文件
		     if (file.exists()) {
		      // 打开文件输入流
		      FileInputStream fileR = new FileInputStream(file);
		      BufferedReader reads = new BufferedReader(
		        new InputStreamReader(fileR));
		      String st = null;
		      while ((st =reads.readLine())!=null ) {
		       str.append(st);
		       str.append("\r\n");
		      }
		      fileR.close();
		     } else {
		     
		     }
		    }else{
		   //  showMessage("SD卡不存在！！");
		    }
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
		   return str.toString();
		  }
	
	public void importRecord()
	{
		dataPath = Environment.getExternalStorageDirectory().toString()+ "/RescueData";
		File f = new File(dataPath);
        File[] files = f.listFiles();
        recordListInfo.clear();
        for (int i = 0; i < files.length; i++) { 
        	  File file = files[i]; 
        	  Data data = new Data();
        	  data.setDataPath(file.getPath().replaceAll(dataPath+"/", ""));
        	  recordListInfo.add(data);
        	 } 
	}
	
	public List<Data> getFile(File file){
		File[] fileArray =file.listFiles();
		for (File f : fileArray) {
		 if(f.isFile()){
			 
		 }
		 else{
			 getFile(f);
		}
		}
		return mFileList;
	}

	public class DataListViewAdapter extends BaseAdapter {  
        View[] itemViews;
  
        public DataListViewAdapter(List<Data> DataInfo) {
			// TODO Auto-generated constructor stub
            itemViews = new View[DataInfo.size()];            
            for(int i=0;i<DataInfo.size();i++){
            	Data getInfo=(Data)DataInfo.get(i);	//获取第i个对象
            	//调用makeItemView，实例化一个Item
            	itemViews[i]=makeItemView(
            			getInfo.getDataPath()
            			);
            }
		}

		public int getCount() {
            return itemViews.length;  
        }
  
        public View getItem(int position) {  
            return itemViews[position];  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }
        
        //绘制Item的函数
        private View makeItemView(String DataPath) {  
            LayoutInflater inflater = (LayoutInflater) Record.this  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
  
            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.itemdata, null);
            
            // 通过findViewById()方法实例R.layout.item内各组件
            TextView path = (TextView) itemView.findViewById(R.id.txtname);  
            path.setText(DataPath);	//填入相应的值
            
            return itemView;  
        }
        
		public View getView(int position, View convertView, ViewGroup parent) {  
            if (convertView == null)  
                return itemViews[position];  
            return convertView;
        }
	}
}
