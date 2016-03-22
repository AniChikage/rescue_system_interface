package com.BTClient1;

public class Info {
	private int id;	//信息ID
	private String userID;   //用户ID
	private String RSSI;	//RSSI
	private String isRange; //是否在搜索范围
	private int infoTime;

	//信息ID处理函数
    public void setId(int id) {  
        this.id = id;
    }
    public int getId() {  
        return id;
    }
	//标题
    public void setUserID(String userID) {  
        this.userID = userID;
    }
	public String getUserID() {
        return userID;  
    }
	
	//信号强度
    public void setRSSI(String rss) {  
        this.RSSI = rss;
    }
	public String getRSSI() {  
        return RSSI;  
    }
	
	//是否在搜索范围
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
