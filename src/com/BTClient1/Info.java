package com.BTClient1;

public class Info {
	private int id;	//��ϢID
	private String userID;   //�û�ID
	private String RSSI;	//RSSI
	private String isRange; //�Ƿ���������Χ
	private int infoTime;

	//��ϢID������
    public void setId(int id) {  
        this.id = id;
    }
    public int getId() {  
        return id;
    }
	//����
    public void setUserID(String userID) {  
        this.userID = userID;
    }
	public String getUserID() {
        return userID;  
    }
	
	//�ź�ǿ��
    public void setRSSI(String rss) {  
        this.RSSI = rss;
    }
	public String getRSSI() {  
        return RSSI;  
    }
	
	//�Ƿ���������Χ
    public void setIsRange(String isrange) {  
        this.isRange = isrange;
    }
	public String getIsRange() {  
        return isRange;  
    }
	
	public int getTime()
	{
		return infoTime;
	}

	public void setTime(int tim)
	{
		this.infoTime = tim;
	}
}
