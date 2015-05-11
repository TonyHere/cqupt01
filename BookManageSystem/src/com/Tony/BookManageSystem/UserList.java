package com.Tony.BookManageSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserList extends ArrayList<User> {
	//private User user;
	
	public UserList(){
		File userFile=new File("UserFile.txt");
		if(userFile.exists()){
			String userInfo;
			BufferedReader br;
			try {
				System.out.println("read userInfo");
				br = new BufferedReader(new FileReader(userFile));
				while((userInfo=br.readLine())!=null){
					String userName_temp=userInfo.substring(0,userInfo.indexOf("="));
					String password_temp=userInfo.substring(userInfo.indexOf("=")+1);
					System.out.println("userName:"+userName_temp
							+"  password:"+password_temp);
					this.add(new User(userName_temp,password_temp));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Read UserFile Erro !");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Get UserInfo Erro !");
			}
			
		}else{
			try {
				userFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Creat UserFile Failed !");
			}
		}
	}
	public boolean login (String userName,String password){
		User user_temp=new User(userName,password);
		if(checkUser(user_temp)!=null){
			return true;
		}
		return false;
	}
	
	public void register(User user){
		this.add(user);
	}
	public void deleUser(User user){
		User user_temp = checkUser(user);
		if(user_temp.equals(null)){
			System.out.println("Delete User Failed!--Please input correct userName or password");
		}else{
			if(this.remove(user_temp)){
				System.out.println("Delete User Succeed");
			}
		}
	}
	
	public User checkUser(User user){
		for(int i=0;i<this.size();i++){
			if(user.getUserName().equals(this.get(i).getUserName())){
				if(user.getPassword().equals(this.get(i).getPassword()))
					return user;
			}
		}
		return null;
	}
//	public User inputUserMsg(){
//		
//	}
	public void saveUserToFile(){
		try {
			FileWriter fw =new FileWriter("UserFile.txt");
			for(int i =0;i<this.size();i++){
				fw.write(this.get(i).getUserName()
						+"="+this.get(i).getPassword()+"\r\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
