package com.BTClient1;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import com.BTClient1.DeviceListActivity;
import com.test.BTClient.R;
import com.chen.provider.QuesOperate;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
//import android.view.Menu;            //��ʹ�ò˵����������
//import android.view.MenuInflater;
//import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class IDList extends Activity {
	
	private final static int REQUEST_CONNECT_DEVICE = 1;    //�궨���ѯ�豸���
	
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP����UUID��
	
	private InputStream is;    //������������������������
	//private TextView text0;    //��ʾ������
    private EditText edit0;    //��������������
    private TextView dis;       //����������ʾ���
    private TextView rssi;       //��ʾ�ź�ǿ��
    private ProgressBar barRssi;  //��������ʾ�ź�ǿ��
    private ImageView img;
    private ScrollView sv;      //��ҳ���
    private String smsg = "";    //��ʾ�����ݻ���
    private String fmsg = "";    //���������ݻ���
    private String smsg_copy = "";//����smsg
    private String[] smsg_s;
    private String userID = "";
    private String userID2 ="";
    private String strRssi = "50";
    private ArrayList<Byte>  pComData = new ArrayList();
    private SQLiteDatabase db;
    private RoundProgressBar rpRssi;
    private int intRssi,intRssi1,intRssi2;
    private double dleRssi;
    private TextView userName;
    private String SrcAddress = "";
    private String DestAddress = "";
    private TextView antenna;
 //   private Integer intRssi = 0;
  //  private Unsigned int uRssi = 0;
    public String filename=""; //��������洢���ļ���
    BluetoothDevice _device = null;     //�����豸
    BluetoothSocket _socket = null;      //����ͨ��socket
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;
	
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //��ȡ�����������������������豸
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   //���û���Ϊ������ main.xml
        rssi = (TextView)findViewById(R.id.strrssi);
        img = (ImageView)findViewById(R.id.header);
        userName = (TextView)findViewById(R.id.userName);
        rpRssi = (RoundProgressBar)findViewById(R.id.roundProgressBar2);
        sv = (ScrollView)findViewById(R.id.ScrollView01);  //�õ���ҳ���
        antenna = (TextView)findViewById(R.id.antenna);
        dis = (TextView) findViewById(R.id.in);      //�õ�������ʾ���
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()
		        + "/test.dbs", null);
       //����򿪱��������豸���ɹ�����ʾ��Ϣ����������
        if (_bluetooth == null){
        	Toast.makeText(this, "�޷����ֻ���������ȷ���ֻ��Ƿ����������ܣ�", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // �����豸���Ա�����  
       new Thread(){
    	   public void run(){
    		   if(_bluetooth.isEnabled()==false){
        		_bluetooth.enable();
    		   }
    	   }   	   
       }.start();      
    }

    //���Ͱ�����Ӧ
    public void onSendButtonClicked(View v){
    	int i=0;
    	int n=0;
    	try{
    		OutputStream os = _socket.getOutputStream();   //�������������
    		byte[] bos = edit0.getText().toString().getBytes();
    		for(i=0;i<bos.length;i++){
    			if(bos[i]==0x0a)n++;
    		}
    		byte[] bos_new = new byte[bos.length+n];
    		n=0;
    		for(i=0;i<bos.length;i++){ //�ֻ��л���Ϊ0a,�����Ϊ0d 0a���ٷ���
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
    
    //���ջ�������ӦstartActivityForResult()
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     //���ӽ������DeviceListActivity���÷���
    		// ��Ӧ���ؽ��
            if (resultCode == Activity.RESULT_OK) {   //���ӳɹ�����DeviceListActivity���÷���
                // MAC��ַ����DeviceListActivity���÷���
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // �õ������豸���      
                _device = _bluetooth.getRemoteDevice(address);
 
                // �÷���ŵõ�socket
                try{
                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                }catch(IOException e){
                	Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                }
                //����socket
            	Button btn = (Button) findViewById(R.id.Button03);
                try{
                	_socket.connect();
                	Toast.makeText(this, "����"+_device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
                	btn.setText("�Ͽ�");
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                		_socket.close();
                		_socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                	}
                	
                	return;
                }
                
                //�򿪽����߳�
                try{
            		is = _socket.getInputStream();   //�õ���������������
            		}catch(IOException e){
            			Toast.makeText(this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
            			return;
            		}
            		if(bThread==false){
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
    
    //���������߳�
    Thread ReadThread=new Thread(){
    	
    	public void run(){
    		int num = 0;
    	//	byte[] buffer = new byte[10];
    		byte[] buffer_new = new byte[10];
    		int i = 0;
    		int n = 0;
    		byte s = 0;
    		bRun = true;
    		//�����߳�
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
     								//smsg+=String.valueOf(data[3]);
     								SrcAddress = String.valueOf(data[2]);
     								DestAddress = String.valueOf(data[3]);
     								userID = String.valueOf(data[5]);
     								userID2 = String.valueOf(data[6]);
     								strRssi = String.valueOf(data[7]&0x0ff);
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
    				//������ʾ��Ϣ��������ʾˢ��
    					handler.sendMessage(handler.obtainMessage());       	    		
    	    		}catch(Exception e){
    	    			e.printStackTrace();
    	    		}
    		}
    	}
    };
    
    //��Ϣ�������
    Handler handler= new Handler(){
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		smsg+=query(userID);
    		if(query(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2))).equals("41766"))
    		{
    			img.setImageResource(R.drawable.test1);
    		}
    		else
    		{
    			img.setImageResource(R.drawable.logo);
    		}
    		///img.setImageResource(R.drawable.logo);
    	//	userName.setText("ID��"+query(String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2))));
    		userName.setText("ID��"+String.valueOf(Integer.valueOf(userID)*256+Integer.valueOf(userID2)));
    		rpRssi.setProgress((int)(Integer.valueOf(strRssi)*100/(double)256));
    		rssi.setText("�ź�ǿ�ȣ�"+String.valueOf(strRssi));
    		antenna.setText("���ߣ�"+SrcAddress);
    		sv.scrollTo(0,dis.getMeasuredHeight()); //�����������һҳ
    	}
    };
    
    //�رճ�����ô�����
    public void onDestroy(){
    	super.onDestroy();
    	if(_socket!=null)  //�ر�����socket
    	try{
    		_socket.close();
    	}catch(IOException e){}
    //	_bluetooth.disable();  //�ر���������
    }
    
    //�˵�������
  /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {//�����˵�
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) { //�˵���Ӧ����
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
    
    //���Ӱ�����Ӧ����
    public void onConnectButtonClicked(View v){ 
    	if(_bluetooth.isEnabled()==false){  //����������񲻿�������ʾ
    		Toast.makeText(this, " ��������...", Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	
        //��δ�����豸���DeviceListActivity�����豸����
    	Button btn = (Button) findViewById(R.id.Button03);
    	if(_socket==null){
    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //��ת��������
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //���÷��غ궨��
    	}
    	else{
    		 //�ر�����socket
    	    try{
    	    	
    	    	is.close();
    	    	_socket.close();
    	    	_socket = null;
    	    	bRun = false;
    	    	btn.setText("����");
    	    }catch(IOException e){}   
    	}
    	return;
    }
    
    //���水����Ӧ����
    public void onSaveButtonClicked(View v){
    	Save();
    }
    
    //���������Ӧ����
    public void onClearButtonClicked(View v){
    	smsg="";
    	fmsg="";
    	dis.setText(smsg);
    	return;
    }
    
    //�˳�������Ӧ����
    public void onQuitButtonClicked(View v){
    	finish();
    }
    
    //���湦��ʵ��
	private void Save() {
		//��ʾ�Ի��������ļ���
		LayoutInflater factory = LayoutInflater.from(IDList.this);  //ͼ��ģ�����������
		final View DialogView =  factory.inflate(R.layout.sname, null);  //��sname.xmlģ��������ͼģ��
		new AlertDialog.Builder(IDList.this)
								.setTitle("�ļ���")
								.setView(DialogView)   //������ͼģ��
								.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() //ȷ��������Ӧ����
								{
									public void onClick(DialogInterface dialog, int whichButton){
										EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //�õ��ļ����������
										filename = text1.getText().toString();  //�õ��ļ���
										
										try{
											if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���SD����׼����
												
												filename =filename+".txt";   //���ļ���ĩβ����.txt										
												File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
												File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
												if(BuildDir.exists()==false)BuildDir.mkdirs();
												File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
												FileOutputStream stream = new FileOutputStream(saveFile);  //���ļ�������
												stream.write(fmsg.getBytes());
												stream.close();
												Toast.makeText(IDList.this, "�洢�ɹ���", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(IDList.this, "û�д洢����", Toast.LENGTH_LONG).show();
											}
										
										}catch(IOException e){
											return;
										}
										
										
										
									}
								})
								.setNegativeButton("ȡ��",   //ȡ��������Ӧ����,ֱ���˳��Ի������κδ��� 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) { 
									}
								}).show();  //��ʾ�Ի���
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
			String str="select * from tb_user where password=?";
	          Cursor cursor = db.rawQuery(str, new String []{userID});
			 cursor.moveToFirst();
			 name = cursor.getString(0);
		    } catch (Exception e) {
		    //  main.createDb();
		    }
		return name;
	}
}