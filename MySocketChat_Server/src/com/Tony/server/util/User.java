package com.Tony.server.util;

/**
 * 用于在服务器端对用户的存储
 * @author tony
 *
 */
public class User {
	private String userName=null;
	private String passwords=null;
	private boolean isResign=false;
	
	public User(String userName, String passwords) {
		this.userName = userName;
		this.passwords = passwords;
	}
	public User(String userName){
		this.userName = userName;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public String getPasswords() {
		return passwords;
	}


	public boolean isResign() {
		return isResign;
	}


	public void setResign(boolean isResign) {
		this.isResign = isResign;
	}
	
	
}
