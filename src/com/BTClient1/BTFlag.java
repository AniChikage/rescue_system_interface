package com.BTClient1;

import android.app.Application;

public class BTFlag extends Application{

	public String show_flag;
	public int count_flag;
	
	public void setShowFlag(String SF){
		this.show_flag = SF;
	}
	
	public String getShowFlag(){
		return show_flag;
	}
	
	public void setCountFlag(int CF){
		this.count_flag = CF;
	}
	
	public int getCountFlag(){
		return count_flag;
	}
	@Override  
	public void onCreate() {   
	// TODO Auto-generated method stub   
	super.onCreate();   
	setShowFlag("NoMatch");   
	}

}
