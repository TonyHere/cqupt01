package com.Tony.BookManageSystem;

public class Manager {
	private String userName;
	private String password;
	
	public Manager(String userName,String password){
		this.userName=userName;
		this.password=password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
}
