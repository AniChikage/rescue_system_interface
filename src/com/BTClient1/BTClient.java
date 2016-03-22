package com.BTClient1;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.security.auth.PrivateCredentialPermission;

import com.BTClient1.DeviceListActivity;
import com.test.BTClient.R;
import com.chen.provider.QuesOperate;

import android.R.bool;
import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
//import android.view.Menu;            //如使用菜单加入此三包
//import android.view.MenuInflater;
//import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class BTClient extends Activity {
	
	private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
	
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
	
	private InputStream is;    //输入流，用来接收蓝牙数据
	//private TextView text0;    //提示栏解句柄
    private EditText edit0;    //发送数据输入句柄
    private EditText recordName;
    private TextView dis;       //接收数据显示句柄
    private TextView rssi;       //显示信号强度
    private ProgressBar barRssi;  //进度条显示信号强度
    private ImageView img;
    private ImageView bthimg;
    private ImageView omniimg;
    private ImageView dirimg;
    private ScrollView sv;      //翻页句柄
    private String smsg = "";    //显示用数据缓存
    private String fmsg = "";    //保存用数据缓存
    private String smsg_copy = "";//缓存smsg
    private String[] smsg_s;
    private String userID = "";
    private String userID2 ="";
    private String strRssi = "50";
    private String[] dBTBNAME;
    private String dbname="";
    private ArrayList<Byte>  pComData = new ArrayList();
    private SQLiteDatabase db;
    private SQLiteDatabase recordDB;
    private RoundProgressBar rpRssi;
    private int intRssi,intRssi1,intRssi2;
    private double dleRssi;
    private TextView userName;
    private String SrcAddress = "";
    private String DestAddress = "";
    private String tempID="";
    private TextView antenna;
    private TableLayout tableLayout;  
    private Button backdet;
    private Button choosedb;
    private Button btnsave;
    private Button btnload;
    private int countRow;
    private int listTime;
    static private String Sfilter="NoMatch";
    private BTFlag btf;
    private String btnIDString;
    private Button[] btnID;
    private Button btnID1;
    private Button btnID2;
    private Button btnID3;
    private Button btnID4;
    private Button btnID5;
    private Button btnID6;
    private TextView[] rssiID;
    private TextView rssiID1;
    private TextView rssiID2;
    private TextView rssiID3;
    private TextView rssiID4;
    private TextView rssiID5;
    private TextView shoID;
    private TextView shoUserName;
    private TextView shoHeight;
    private TextView shoWeight;
    private TextView shoGender;
    private TextView shoBirth;
    private TextView shoBloodType;
    private TextView shoAddress;
    private TextView shoPhone;
  //  private TextView choseddb;
    private TextView bthStatus;
    private TextView omniStatus;
    private TextView dirStatus;
    private int idi;
    private int Con = 0;
    ListView listView;	//声明一个ListView对象
	private List<Info> OmnilistInfo = new ArrayList<Info>();  //声明一个list，动态存储要显示的信息
	
    private Handler handle = new Handler();
    
 //   private Integer intRssi = 0;
  //  private Unsigned int uRssi = 0;
    public String filename=""; //用来保存存储的文件名
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;
    boolean selfCheck = false;
    boolean omniCheck = false;
    boolean dirCheck = false;
	
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
	
    private Runnable runref = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 1000 * 1);// 间隔1秒
        }
        void update() {
            //刷新msg的内容
        	btf.setCountFlag(0);
        	for(int i=0;i<5;i++)
        	{
        		btnID[i].setText("");
        		rssiID[i].setText("");
        	}
        	
        	
        	SelfCheck();
        	
        	Time t = new Time();
            t.setToNow();
            int lastTime = t.hour*3600+t.minute*60+t.second;
            
            for(int i=0;i<OmnilistInfo.size();i++)
            {
            	Info getRefInfo = (Info)OmnilistInfo.get(i);
            	getRefInfo.setRSSI(String.valueOf(strRssi));
            	if((lastTime-getRefInfo.getTime())>10)
            	{
            	//	OmnilistInfo.remove(i);
            	//	getRefInfo.setRSSI(getRefInfo.getRSSI()+"(buzai)");
            		getRefInfo.setIsRange("超时");
            		
            	}
//            	else {
//            	//	getRefInfo.setRSSI(getRefInfo.getRSSI());
//				}
            }
            listView.setAdapter(new ListViewAdapter(OmnilistInfo));
           

        }
    }; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   //设置画面为主画面 main.xml
        imporDatabase();
        rssi = (TextView)findViewById(R.id.strrssi);
        img = (ImageView)findViewById(R.id.header);
        userName = (TextView)findViewById(R.id.userName);
        rpRssi = (RoundProgressBar)findViewById(R.id.roundProgressBar2);
  //      antenna = (TextView)findViewById(R.id.antenna);
        tableLayout = (TableLayout) this.findViewById(R.id.tableLayout); 
//        bthimg = (ImageView)findViewById(R.id.bthStatusimg);
//        omniimg = (ImageView)findViewById(R.id.omniStatusimg);
//        dirimg = (ImageView)findViewById(R.id.dirStatusimg);
        bthStatus = (TextView)findViewById(R.id.bthStatus);
        omniStatus = (TextView)findViewById(R.id.omniStatus);
        dirStatus = (TextView)findViewById(R.id.dirStatus);
        
        recordName = (EditText)findViewById(R.id.recordName);
        
        btnsave = (Button)findViewById(R.id.btnsave);
        btnload = (Button)findViewById(R.id.btnload);
        btnID = new Button[6];
        rssiID = new TextView[6];
        
        btnID1 = (Button)findViewById(R.id.btnID1);
        btnID2 = (Button)findViewById(R.id.btnID2);
        btnID3 = (Button)findViewById(R.id.btnID3);
        btnID4 = (Button)findViewById(R.id.btnID4);
        btnID5 = (Button)findViewById(R.id.btnID5);
        choosedb = (Button)findViewById(R.id.choosedb);
        rssiID1 = (TextView)findViewById(R.id.RSSIID1);
        rssiID2 = (TextView)findViewById(R.id.RSSIID2);
        rssiID3 = (TextView)findViewById(R.id.RSSIID3);
        rssiID4 = (TextView)findViewById(R.id.RSSIID4);
        rssiID5 = (TextView)findViewById(R.id.RSSIID5);
   //     shoID = (TextView)findViewById(R.id.userName);
        shoUserName = (TextView)findViewById(R.id.shoUserName);
        shoHeight = (TextView)findViewById(R.id.shoHeight);
        shoWeight = (TextView)findViewById(R.id.shoWeight);
        shoGender = (TextView)findViewById(R.id.shoGender);
        shoBirth  =(TextView)findViewById(R.id.shoBirth);
        shoBloodType = (TextView)findViewById(R.id.shoBloodType);
        shoAddress = (TextView)findViewById(R.id.shoAddress);
        shoPhone = (TextView)findViewById(R.id.shoPhone);
  //      choseddb = (TextView)findViewById(R.id.choseddb);
  //      btnID6 = (Button)findViewById(R.id.btnID6);
        btnID[0] = btnID1;
        btnID[1] = btnID2;
        btnID[2] = btnID3;
        btnID[3] = btnID4;
        btnID[4] = btnID5;
        rssiID[0] = rssiID1;
        rssiID[1] = rssiID2;
        rssiID[2] = rssiID3;
        rssiID[3] = rssiID4;
        rssiID[4] = rssiID5;
        dBTBNAME = new String[2];
        dBTBNAME[0] = "tb_user";
        dBTBNAME[1] = "test2";
        dbname = dBTBNAME[0];
        
  //      btnID6.setOnClickListener(new ListRef());
        btnID1.setOnClickListener(new ID1());
        btnID2.setOnClickListener(new ID2());
        btnID3.setOnClickListener(new ID3());
        btnID4.setOnClickListener(new ID4());
        btnID5.setOnClickListener(new ID5());
        
        userName.setOnClickListener(new PopWindow());
        shoUserName.setOnClickListener(new PopWindow());
        choosedb.setOnClickListener(new Cdb());
        btnsave.setOnClickListener(new SaveRecord());
        btnload.setOnClickListener(new LoadRecord());
     //   recordDB = SQLiteDatabase.openOrCreateDatabase("/data/data/com.test.BTClient/record/record.dbs", null);
        //
        listView=(ListView)this.findViewById(R.id.omnilistView);	//将listView与布局对象关联 
      
    //    setInfo();
    //    listView.setAdapter(new ListViewAdapter(OmnilistInfo));
        
        //处理Item的点击事件
        listView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        		Info getObject = OmnilistInfo.get(position);	//通过position获取所点击的对象
        		int infoId = getObject.getId();	//获取信息id
        		String infoTitle = getObject.getUserID();	//获取信息标题
        		String infoDetails = getObject.getRSSI();	//获取信息详情
        		
        	//	dirimg.setImageResource(R.drawable.blue);
        		dirStatus.setTextColor(Color.rgb(178, 223, 219));
        		btf.setShowFlag(infoTitle);
        		userName.setText(infoTitle);
        		shoUserName.setText(query(infoTitle));
        		shoHeight.setText("身高:"+queryHeight(infoTitle)+"cm");
        		shoWeight.setText("体重:"+queryWeight(infoTitle)+"kg");
        		shoGender.setText("性别:"+queryGender(infoTitle));
        		shoBirth.setText("出生年月:"+queryBirth(infoTitle));
        		shoBloodType.setText("血型:"+queryBloodType(infoTitle));
        		shoAddress.setText("家庭住址:"+queryAddress(infoTitle));
        		shoPhone.setText("联系电话:"+queryPhone(infoTitle));
        		//Toast显示测试
        		//Toast.makeText(BTClient.this, "信息ID:"+infoId,Toast.LENGTH_SHORT).show();
        	}
        });
        
      //  backdet = (Button)findViewById(R.id.backdet);
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.test.BTClient/database/test.dbs", null);
        countRow=0;
        btf = (BTFlag)getApplication();  
        btf.setShowFlag("NoMatch");
        btf.setCountFlag(0);
        
       //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (_bluetooth == null){
        	Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        handle.postDelayed(runref, 1000 * 60);
     //   detHide();ssss
        // 设置设备可以被搜索  
       new Thread(){
    	   public void run(){
    		   if(_bluetooth.isEnabled()==false){
        		_bluetooth.enable();
    		   }
    	   }   	   
       }.start();      
    }
    public void setInfo(){
	OmnilistInfo.clear();
	int i=0;
	while(i<10){
		Info information = new Info();
		information.setId(1000+i);
		information.setUserID("标题"+i);
		information.setRSSI("详细信息"+i);
		
		OmnilistInfo.add(information);	//将新的info对象加入到信息列表中
		i++;
	}
    }
    
    //载入记录
    class LoadRecord implements OnClickListener{
    	
    	@Override
    	public void onClick(View arg0){
    		Intent intent = new Intent();
    		intent.setClass(BTClient.this, Record.class);
    		startActivity(intent);
    	}
    }
    
    //保存记录
    class SaveRecord implements OnClickListener{
    	@Override
		public void onClick(View arg0) {

    	//	String recordString="";
    		String recordNameString="";
    		
    		recordNameString = recordName.getText().toString();
    		
            
            
            final EditText inputServer = new EditText(BTClient.this);
            inputServer.setFocusable(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(BTClient.this);
            builder.setTitle("保存文件名")
                    .setView(inputServer)
                    .setNegativeButton("取消", null);
            builder.setPositiveButton("保存",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String inputName = inputServer.getText().toString();
                            String recordString="";
                            for(int i=0;i<OmnilistInfo.size();i++)
                            {
                            	Info getRecInfo = (Info)OmnilistInfo.get(i);
                            	recordString+=getRecInfo.getUserID()+"  "+
                            	              query(getRecInfo.getUserID())+"  "+ConvertTime(getRecInfo.getTime())+"\n";
                            }
                            try{
                				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
                					
                					inputName =inputName+".txt";   //在文件名末尾加上.txt										
                					File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
                					File BuildDir = new File(sdCardDir, "/RescueData");   //打开data目录，如不存在则生成
                					if(BuildDir.exists()==false)BuildDir.mkdirs();
                					File saveFile =new File(BuildDir, inputName);  //新建文件句柄，如已存在仍新建文档
                					FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
                					stream.write(recordString.getBytes());
                					stream.close();
                					Toast.makeText(BTClient.this, "存储成功！", Toast.LENGTH_SHORT).show();
                				}else{
                					Toast.makeText(BTClient.this, "没有存储卡！", Toast.LENGTH_LONG).show();
                				}
                			
                			}catch(IOException e){
                				return;
                			}
                        }
                    });
            builder.show();
            
//            try{
//				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
//					
//					recordNameString =recordNameString+".txt";   //在文件名末尾加上.txt										
//					File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
//					File BuildDir = new File(sdCardDir, "/RescueData");   //打开data目录，如不存在则生成
//					if(BuildDir.exists()==false)BuildDir.mkdirs();
//					File saveFile =new File(BuildDir, recordNameString);  //新建文件句柄，如已存在仍新建文档
//					FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
//					stream.write(recordString.getBytes());
//					stream.close();
//					Toast.makeText(BTClient.this, "存储成功！", Toast.LENGTH_SHORT).show();
//				}else{
//					Toast.makeText(BTClient.this, "没有存储卡！", Toast.LENGTH_LONG).show();
//				}
//			
//			}catch(IOException e){
//				return;
//			}
            
//            if(!recordName.equals("")){
//            	recordDB.execSQL("create table "+recordName+" (ID varchar(30), RSSI varchar(20))");
//            }
//            
//            for(int i=0;i<OmnilistInfo.size();i++)
//            {
//            	Info getRefInfo = (Info)OmnilistInfo.get(i);
//            	recordDB.execSQL("insert into "+recordName+" values('"+getRefInfo.getUserID()+"','"+getRefInfo.getRSSI()+"')");
//            }
            
    	}
    }
    
    //
    public String ConvertTime(int timeStr)
    {
    	String timeString="";
    	int year;
    	int month;
    	int date;
    	int hour;
    	int minute;
    	int second;
    	
    	Time t=new Time(); 
    	t.setToNow(); 
    	year = t.year;  
    	month = t.month;  
    	date = t.monthDay; 
    	hour = timeStr/3600;
    	minute = timeStr%3600/60;
    	second = timeStr%3600%60;
    	
    	timeString = year+"/"+month+"/"+date+"  "+hour+":"+minute+":"+second;
    	
    	return timeString;
    }
    
    //
    public class ListViewAdapter extends BaseAdapter {  
        View[] itemViews;
  
        public ListViewAdapter(List<Info> OmnilistInfo) {
			// TODO Auto-generated constructor stub
            itemViews = new View[OmnilistInfo.size()];            
            for(int i=0;i<OmnilistInfo.size();i++){
            	Info getInfo=(Info)OmnilistInfo.get(i);	//获取第i个对象
            	//调用makeItemView，实例化一个Item
            	itemViews[i]=makeItemView(
            			getInfo.getUserID(), getInfo.getRSSI(), getInfo.getIsRange()
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
        private View makeItemView(String strTitle, String strText, String strrange) {  
            LayoutInflater inflater = (LayoutInflater) BTClient.this  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
  
            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.item, null);
            
            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) itemView.findViewById(R.id.OmniUserId);  
            title.setText(strTitle);	//填入相应的值
            TextView text = (TextView) itemView.findViewById(R.id.OmniRssi);  
            text.setText(strText);  
            TextView isrange = (TextView) itemView.findViewById(R.id.OmniIsRange);  
            isrange.setText(strrange);  
            
            return itemView;  
        }
  
        
		public View getView(int position, View convertView, ViewGroup parent) {  
            if (convertView == null)  
                return itemViews[position];  
            return convertView;
        }
    }	
    //弹窗
    class PopWindow implements OnClickListener{
    	
    	@Override
		public void onClick(View arg0) {
    		new AlertDialog.Builder(BTClient.this) 
    		.setTitle(userName.getText().toString()+"的详细信息")
    		.setItems(new String[]{"姓名:"+shoUserName.getText().toString(),
    				shoGender.getText().toString(),
    				shoBirth.getText().toString(),
    				shoHeight.getText().toString(),
    				shoWeight.getText().toString(),
    				shoBloodType.getText().toString(),
    				shoAddress.getText().toString(),
    				shoPhone.getText().toString()},
    				null)
    		.setPositiveButton("返回", null)
   // 		.setNegativeButton("否", null)
    		.show();
    	}
    }
    //选择数据库
    	class Cdb implements OnClickListener{
    //	String[] city = new String[]{"北京市","东京市"};
    		String[] city = new String[]{"云南省禄丰县"};
    	@Override
		public void onClick(View arg0) {
    		new AlertDialog.Builder(BTClient.this) 
    		.setTitle("请选择")
    	//	.setIcon(android.R.drawable.ic_dialog_info)                
    		.setSingleChoiceItems(city, 0, 
    		  new DialogInterface.OnClickListener() {
    		                            
    		     public void onClick(DialogInterface dialog, int which) {
    		 //   	 rssi.setText(String.valueOf(which));
    		    	 Toast.makeText(BTClient.this, "您选择了:"+city[which],Toast.LENGTH_SHORT).show();
    		         dbname = dBTBNAME[which];
    		//         choseddb.setText("数据库:"+city[which]);
    		         choosedb.setText(city[which]);
    		    	 dialog.dismiss();
    		     }
    		  }
    		)
    		.setNegativeButton("取消", null)
    		.show();//
    	}
    }
    
    //导入数据库
    public void imporDatabase() { 
        //存放数据库的目录 
        String DB_PATH="/data/data/com.test.BTClient/database/";
        String DB_NAME = "test.dbs";
        String DB_PATHRC="/data/data/com.test.BTClient/record/";
        
        File ffFile = new File(DB_PATH + DB_NAME);
        ffFile.delete();
        
        File dbFile = new File(DB_PATHRC);
        dbFile.delete();
        
        if ((new File(DB_PATHRC)).exists() == false) { 
        	File f = new File(DB_PATHRC); 
            // 如 database 目录不存在，新建该目录  
            if (!f.exists()) { 
                f.mkdir(); 
            }
        }
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
    
    //刷新列表按钮事件
    class ListRef implements OnClickListener{
    	@Override
		public void onClick(View arg0) {
    		btf.setCountFlag(0);
    	}
    }
    
    class ID1 implements OnClickListener{
    	@Override
		public void onClick(View arg0) {
    		btf.setShowFlag(btnID1.getText().toString());
    	//	dirimg.setImageResource(R.drawable.blue);
    	}
    }
    class ID2 implements OnClickListener{
    	@Override
		public void onClick(View arg0) {
    		btf.setShowFlag(btnID2.getText().toString());
    	//	dirimg.setImageResource(R.drawable.blue);
    	}
    }
    class ID3 implements OnClickListener{
    	@Override
		public void onClick(View arg0) {
    		btf.setShowFlag(btnID3.getText().toString());
    	//	dirimg.setImageResource(R.drawable.blue);
    	}
    }
    class ID4 implements OnClickListener{
    	@Override
		public void onClick(View arg0) {
    		btf.setShowFlag(btnID4.getText().toString());
    	//	dirimg.setImageResource(R.drawable.blue);
    	}
    }
    class ID5 implements OnClickListener{
    	@Override
		public void onClick(View arg0) {
    		btf.setShowFlag(btnID5.getText().toString());
    	//	dirimg.setImageResource(R.drawable.blue);
    	}
    }
    

    //发送按键响应
    public void onSendButtonClicked(View v){
    	int i=0;
    	int n=0;
    	try{
    		OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流
    		byte[] bos = edit0.getText().toString().getBytes();
    		for(i=0;i<bos.length;i++){
    			if(bos[i]==0x0a)n++;
    		}
    		byte[] bos_new = new byte[bos.length+n];
    		n=0;
    		for(i=0;i<bos.length;i++){ //手机中换行为0a,将其改为0d 0a后再发送
    			if(bos[i]==0x0a){
    				bos_new[n]=0x0d;
    				n++;
    				bos_new[n]=0x0a;
    			}else{
    				bos_new[n]=bos[i];
    			}
    			n++;
    		}
    		
    		os.write(bos_new);	
    	}catch(IOException e){  		
    	}  	
    }
    
    //接收活动结果，响应startActivityForResult()
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
    		// 响应返回结果
            if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                // MAC地址，由DeviceListActivity设置返回
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // 得到蓝牙设备句柄      
                _device = _bluetooth.getRemoteDevice(address);
 
                // 用服务号得到socket
                try{
                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                }catch(IOException e){
                	Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                }
                //连接socket
            	Button btn = (Button) findViewById(R.id.Button03);
                try{
                	_socket.connect();
                	Toast.makeText(this, "连接"+_device.getName()+"成功！", Toast.LENGTH_SHORT).show();
                	btn.setText("断开");
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                		_socket.close();
                		_socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                	}
                	
                	return;
                }
                
                //打开接收线程
                try{
            		is = _socket.getInputStream();   //得到蓝牙数据输入流
            		}catch(IOException e){
            			Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
            			return;
            		}
            		if(bThread==false){
            			SelfCheck();
            			ReadThread.start();
            			bThread=true;
            		}else{
            			bRun = true;
            		}
            }
    		break;
    	default:break;
    	}
    }
    
    //查询自检
    void SelfCheck()
    {
    	int i=0;
    	int n=0;
    	String sendbuf;
    	try{
    		OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流
    		sendbuf = "fafbfffdfe";
    		byte[] bos = new byte[5];
    		bos[0]=(byte) 0xfa;
    		bos[1]=(byte) 0xfb;
    		bos[2]=(byte) 0xff;
    		bos[3]=(byte) 0xfd;
    		bos[4]=(byte) 0xfe;
    		
    		os.write(bos);	
    	}catch(IOException e){  		
    	}  	
    }
    
    //接收数据线程
    Thread ReadThread=new Thread(){
    	
    	public void run(){
    		int num = 0;
    	//	byte[] buffer = new byte[10];
    		byte[] buffer_new = new byte[10];
    		int i = 0;
    		int n = 0;
    		byte s = 0;
    		bRun = true;
    		//接收线程
    		while(true){
    			try{
    				byte[]  tempbuffer = new byte[50] ;
    	 			int t = 0; 
    	 			int temp1 = 0 ;
    	 			int temp2 = 0;
    	 			
    	 			while((temp1 = is.read()) != -1){
    	 				if(toByteArray(temp1) == 0x0a){
    	 					if(toByteArray(temp2 = is.read()) == 0x0d){
    	 						break;
    	 					}else{
    	 						tempbuffer[t] = toByteArray(temp1);
    	 						temp1=0;
    	 						t++;
    	 						tempbuffer[t] = toByteArray(temp2);
    	 						temp2=0;
    	 						t++;
    	 					}
    	 				}else{
    	 					tempbuffer[t] = toByteArray(temp1);
    	 					temp1= 0;
    	 					t++;
    	 				}
    	 			}
    	 			
    	 			temp1=0;
    	 			temp2=0;
    	 			
    	 			byte[] buffer = new byte[t];
    	 			for(int j = 0 ; j < t ; j++){
    	 				buffer[j]=tempbuffer[j];
    	 			}
    	 			
    	 			int length = buffer.length;
    	 			if(length > 0)
    	 			{
     					for (byte p : buffer)
     					{
     						pComData.add(p);
     					}
     					
     					//smsg+=bytesToHexString(pComData.get(2));
     					//smsg+=pComData.get(2)&0x0ff;
     					
     					while (pComData.size() >= 1)
     					{
     						while (!String.valueOf(pComData.get(0)&0x0ff).equals("250") && !String.valueOf(pComData.get(1)&0x0ff).equals("251"))
     						{
       							pComData.remove(0);
     						}
     					//	if (pComData.size() < 10) return;
     						int le=pComData.size();
     						int lenth = 2;
     						while (lenth < pComData.size())
     						{
     							if (!String.valueOf(pComData.get(lenth-2)&0x0ff).equals("253") || !String.valueOf(pComData.get(lenth-1)&0x0ff).equals("254"))
     								lenth++;
     							else
     								break;
     						}
     						if (String.valueOf(pComData.get(lenth-2)&0x0ff).equals("253") && String.valueOf(pComData.get(lenth-1)&0x0ff).equals("254"))
     						{
     							byte[] data = new byte[lenth];
     							for (int i1 = 0; i1 < lenth; i1++)
     							{
     								data[i1] = (Byte)pComData.get(0);
     								pComData.remove(0);
     							}
     							
     							if (String.valueOf(data[0]&0x0ff).equals("250")&& String.valueOf(data[1]&0x0ff).equals("251") && String.valueOf(data[lenth-2]&0x0ff).equals("253") && String.valueOf(data[lenth-1]&0x0ff).equals("254"))
     							{
     								if(String.valueOf(data[2]&0x0ff).equals("255"))
     								{
     								    Con = 1;
     									Log.v("!!!","!!!");
     							//		bthimg.setImageResource(R.drawable.blue);
     									bthStatus.setTextColor(Color.rgb(178, 223, 219));
     									omniCheck = true;
     								}
     								else
     								{
     									SrcAddress = String.valueOf(data[2]&0x0ff);
     									DestAddress = String.valueOf(data[3]);
     									userID = String.valueOf(data[5]);
     									userID2 = String.valueOf(data[6]);
     									strRssi = String.valueOf(data[7]&0x0ff);
     								}
     										
     							}
     							else
     							{	                           		
     								userID = "32";
     								strRssi = "50";
     							}
     						}
     						else
     							break;
     					}
    	 			}
    				//发送显示消息，进行显示刷新
    					handler.sendMessage(handler.obtainMessage());       	    		
    	    		}catch(Exception e){
    	    			e.printStackTrace();
    	    		}
    		}
    	}
    };
    
    //消息处理队列
    Handler handler= new Handler(){
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
            if(omniCheck){
            	btnIDShow();
           // 	bthimg.setImageResource(R.drawable.red);
           // 	bthimg.setImageResource(R.drawable.blue);
			//	omniimg.setImageResource(R.drawable.blue);
				omniStatus.setTextColor(Color.rgb(178, 223, 219));
    			if(btf.getShowFlag().equals(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2))))
            	{
            		userName.setText(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2)));
            		tempID = String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2));
            		shoUserName.setText(query(tempID));
            		shoHeight.setText("身高:"+queryHeight(tempID)+"cm");
            		shoWeight.setText("体重:"+queryWeight(tempID)+"kg");
            		shoGender.setText("性别:"+queryGender(tempID));
            		shoBirth.setText("出生年月:"+queryBirth(tempID));
            		shoBloodType.setText("血型:"+queryBloodType(tempID));
            		shoAddress.setText("家庭住址:"+queryAddress(tempID));
            		shoPhone.setText("联系电话:"+queryPhone(tempID));
            	    //百分比
            		//	rpRssi.setProgress((int)(Integer.valueOf(strRssi)*100/(double)256));
            		if(Integer.valueOf(SrcAddress)==2)
            		{
            			rpRssi.setProgress(Integer.valueOf(strRssi));
            		}
            		
            		rssi.setText("信号强度："+String.valueOf(strRssi));
            	}
            }

    //			smsg+=query(userID);
     //   		if(query(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2))).equals("41766"))
      //  		{
      //  			img.setImageResource(R.drawable.test1);
     //   		}
     //   		else
     //   		{
     //   			img.setImageResource(R.drawable.logo);
     //   		}
        		///img.setImageResource(R.drawable.logo);
        	//	userName.setText("ID："+query(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2))));

        	//	if(Sfilter.equals(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2))))
    			
        	//	addRow();
        	//	sv.scrollTo(0,dis.getMeasuredHeight()); //跳至数据最后一页
    	
    		
    	}
    };
    //
    private void btnIDShow()
    {
    	btnIDString = String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2));
    	if(btf.getCountFlag()<5)
    	{
    		if(!isExist(btnID,btf.getCountFlag(),btnIDString))
    		{
        	btnID[btf.getCountFlag()].setText(btnIDString);
        	rssiID[btf.getCountFlag()].setText(String.valueOf(strRssi));
        	btf.setCountFlag(btf.getCountFlag()+1);
    		}
    	}
        if(btf.getCountFlag()>100)
        	btf.setCountFlag(10);
        
        Time t = new Time();
        t.setToNow();
        int perTime = t.hour*3600+t.minute*60+t.second;
        
        //
        Info information = new Info();
		information.setId(btf.getCountFlag());
		information.setUserID(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2)));
		information.setRSSI(String.valueOf(strRssi));
		information.setIsRange("");
		information.setTime(perTime);
		
		UpdateInfoTime(information,OmnilistInfo);
		
		if(!isExistInList(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2)),OmnilistInfo))
		OmnilistInfo.add(information);	//将新的info对象加入到信息列表中
		
		int position;
		//rssi.setText("信号强度："+String.valueOf("fg"));
		listView.setAdapter(new ListViewAdapter(OmnilistInfo));

    }
    
    //
    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }
    
    //更新列表时间
    private void UpdateInfoTime(Info informaInfo, List<Info> omnilistInfo2)
    {
    	int i;
    	for(i=0;i<omnilistInfo2.size();i++)
    	{
    		if(omnilistInfo2.get(i).getUserID().equals(informaInfo.getUserID()))
    		{
    			omnilistInfo2.get(i).setTime(informaInfo.getTime());
    			omnilistInfo2.get(i).setIsRange("");
    		}
    	}
    }
    
    
    //
    private boolean isExistInList(String str, List<Info> omnilistInfo2)
    {
    	boolean isExist = false;
    	int i;
    	for(i=0;i<omnilistInfo2.size();i++)
    	{
    		if(omnilistInfo2.get(i).getUserID().equals(str))
    			isExist = true;
    	}
    	return isExist;
    }
    //去掉重复
    private boolean isExist(Button[] btnID,int btnIDlenth,String str)
    {
    	boolean isExist = false;
    	int i;
    	for(i=0;i<btnIDlenth;i++)
    	{
    		if(btnID[i].getText().toString().equals(str))
    			isExist = true;
    	}
    	
    	return isExist;
    }
    
    //
    private boolean StringEqual(String str1, String str2)
    {
    	return str1.equals(str2);
    }
    //show
    private void detShow()
    {
    	img.setVisibility(0);
    	rssi.setVisibility(0);
    	userName.setVisibility(0);
    	rpRssi.setVisibility(0);
    //	antenna.setVisibility(0); 	
    	backdet.setVisibility(0);
    	tableLayout.setVisibility(4);
    }
    //hide
    private void detHide()
    {
    	img.setVisibility(4);
    	rssi.setVisibility(4);
    	userName.setVisibility(4);
    	rpRssi.setVisibility(4);
   // 	antenna.setVisibility(4); 	
    	backdet.setVisibility(4);
    	tableLayout.setVisibility(0);
    }
    
    //添加行
    private void addRow()  
    {  
        TableRow tableRow = new TableRow(this);  
        Button button = new Button(this);  
        button.setText(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2)));  
        button.setOnClickListener(new View.OnClickListener()  
            {     
               @Override  
               public void onClick(View view)  
               {  
            	   Sfilter = String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2));
            	   btf.setShowFlag(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2)));
               }  
           });  
   //     tableRow.addView(textView);  
        countRow++;
        if(countRow>100)countRow=8;
        if(countRow<7){
        	tableRow.addView(button);  
        	tableLayout.addView(tableRow); 
        }
        else
        {
        	tableRow.addView(button);  
        	tableLayout.removeViewAt(0); 
        	tableLayout.addView(tableRow); 
        }
        
         
        
//        tableLayout.removeViewAt(0);   
    }  
    
    //关闭程序掉用处理部分
    public void onDestroy(){
    	handle.removeCallbacks(runref); //停止刷新
    	super.onDestroy();
    	if(_socket!=null)  //关闭连接socket
    	try{
    		_socket.close();
    	}catch(IOException e){}
    //	_bluetooth.disable();  //关闭蓝牙服务
    }
    
    //菜单处理部分
  /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {//建立菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) { //菜单响应函数
        switch (item.getItemId()) {
        case R.id.scan:
        	if(_bluetooth.isEnabled()==false){
        		Toast.makeText(this, "Open BT......", Toast.LENGTH_LONG).show();
        		return true;
        	}
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.quit:
            finish();
            return true;
        case R.id.clear:
        	smsg="";
        	ls.setText(smsg);
        	return true;
        case R.id.save:
        	Save();
        	return true;
        }
        return false;
    }*/
    
    //连接按键响应函数
    public void onConnectButtonClicked(View v){ 
    	if(_bluetooth.isEnabled()==false){  //如果蓝牙服务不可用则提示
    		Toast.makeText(this, " 打开蓝牙中...", Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	
        //如未连接设备则打开DeviceListActivity进行设备搜索
    	Button btn = (Button) findViewById(R.id.Button03);
    	if(_socket==null){
    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
    	}
    	else{
    		 //关闭连接socket
    	    try{
    	    	
    	    	is.close();
    	    	_socket.close();
    	    	_socket = null;
    	    	bRun = false;
    	    	btn.setText("连接");
    	   // 	bthimg.setImageResource(R.drawable.red);
    	   // 	omniimg.setImageResource(R.drawable.red);
    	   // 	dirimg.setImageResource(R.drawable.red);
    	    	bthStatus.setTextColor(Color.rgb(209, 209, 209));
    	    	omniStatus.setTextColor(Color.rgb(209, 209, 209));
    	    	dirStatus.setTextColor(Color.rgb(209, 209, 209));
    	    }catch(IOException e){}   
    	}
    	return;
    }
    
    //保存按键响应函数
    public void onSaveButtonClicked(View v){
    	Save();
    }
    
    //清除按键响应函数
    public void onClearButtonClicked(View v){
    	smsg="";
    	fmsg="";
    	dis.setText(smsg);
    	return;
    }
    
    //退出按键响应函数
    public void onQuitButtonClicked(View v){
    	finish();
    }
    
    //保存功能实现
	private void Save() {
		//显示对话框输入文件名
		LayoutInflater factory = LayoutInflater.from(BTClient.this);  //图层模板生成器句柄
		final View DialogView =  factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
		new AlertDialog.Builder(BTClient.this)
								.setTitle("文件名")
								.setView(DialogView)   //设置视图模板
								.setPositiveButton("确定",
								new DialogInterface.OnClickListener() //确定按键响应函数
								{
									public void onClick(DialogInterface dialog, int whichButton){
										EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //得到文件名输入框句柄
										filename = text1.getText().toString();  //得到文件名
										
										try{
											if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
												
												filename =filename+".txt";   //在文件名末尾加上.txt										
												File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
												File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
												if(BuildDir.exists()==false)BuildDir.mkdirs();
												File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
												FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
												stream.write(fmsg.getBytes());
												stream.close();
												Toast.makeText(BTClient.this, "存储成功！", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(BTClient.this, "没有存储卡！", Toast.LENGTH_LONG).show();
											}
										
										}catch(IOException e){
											return;
										}
										
										
										
									}
								})
								.setNegativeButton("取消",   //取消按键响应函数,直接退出对话框不做任何处理 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) { 
									}
								}).show();  //显示对话框
	} 
	public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }
	public static byte toByteArray(int number) 
	{ 
	int temp = number; 
	byte[] b=new byte[4]; 
	for (int i = b.length - 1; i > -1; i--) 
	{ 
	b[i] = new Integer(temp & 0xff).byteValue(); 
	temp = temp >> 8; 
	} 
	return b[3]; 
	} 
	
	public String query(String userID)
	{
		String name="";
		try {
			String str="select * from "+dbname+" where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 name = cursor.getString(0);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return name;
	}
	public String queryHeight(String userID)
	{
		String height="";
		try {
			String str="select * from "+dbname+" where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 height = cursor.getString(2);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return height;
	}
	public String queryWeight(String userID)
	{
		String weight="";
		try {
			String str="select * from "+dbname+" where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 weight = cursor.getString(3);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return weight;
	}
	public String queryGender(String userID)
	{
		String gender="";
		try {
			String str="select * from "+dbname+" where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 gender = cursor.getString(4);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return gender;
	}
	public String queryBirth(String userID)
	{
		String birth="";
		try {
			String str="select * from "+dbname+" where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 birth = cursor.getString(5);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return birth;
	}
	public String queryBloodType(String userID)
	{
		String bloodtype="";
		try {
			String str="select * from "+dbname+" where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 bloodtype = cursor.getString(6);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return bloodtype;
	}
	public String queryAddress(String userID)
	{
		String address="";
		try {
			String str="select * from "+dbname+" where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 address = cursor.getString(7);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return address;
	}
	public String queryPhone(String userID)
	{
		String phone="";
		try {
			String str="select * from "+dbname+" where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 phone = cursor.getString(8);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return phone;
	}
	
}