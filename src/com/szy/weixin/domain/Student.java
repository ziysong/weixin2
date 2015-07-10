package com.szy.weixin.domain;


/**学生类：用于绑定学号和微信用户名*/
public class Student {

	private String username;
	private String password;
	private String fromUserName;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	
	
	
	
}
