package com.chen.modl;

public class User {
	public int uid;
	public String uname;
	public String upassword;

	public User() {

	}

	public User(int uid, String uname, String upassword) {
		this.uid = uid;
		this.uname = uname;
		this.upassword = upassword;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUpassword() {
		return upassword;
	}

	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}
  public String toString (){
	return "uid"+uid+"uname"+uname+"upassword"+upassword;
	  
  }
}
