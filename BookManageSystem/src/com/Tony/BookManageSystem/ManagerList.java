package com.Tony.BookManageSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ManagerList extends ArrayList<Manager> {
	//private Manager manager;

	public ManagerList(){
		File managerFile=new File("ManagerFile.txt");
		if(managerFile.exists()){
			String managerInfo;
			try {
				System.out.println("read managerInfo");
				BufferedReader br = new BufferedReader(new FileReader(managerFile));
				while((managerInfo=br.readLine())!=null){
					String userName_temp=managerInfo.substring(0,managerInfo.indexOf("="));
					String password_temp=managerInfo.substring(managerInfo.indexOf("=")+1);
					System.out.println("managerName:"+userName_temp
							+"  password:"+password_temp);
					this.add(new Manager(userName_temp,password_temp));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Read ManagerFile Erro !");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Get ManagerInfo Erro !");
			}

		}else{
			try {
				managerFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Creat ManagerFile Failed !");
			}
		}
	}
	public boolean login (String userName,String password){
		if(selectUser(new Manager(userName,password))!=null){
			return true;
		}
		return false;
	}
	public Manager selectUser(Manager manager){
		for(int i=0;i<this.size();i++){
			if(manager.getUserName().equals(this.get(i).getUserName())){
				if(manager.getPassword().equals(this.get(i).getPassword()))
					return manager;
			}
		}
		return null;
	}
	//  暂时不用------------------------------------------------------
	public void register(String managerName,String password){	
		Manager manager_temp=new Manager(managerName,password);
		System.out.println("manager register:"+managerName+"---"+password);
		this.add(manager_temp);
	}
	public void deleUser(Manager manager){
		Manager manager_temp = selectUser(manager);
		if(manager_temp.equals(null)){
			System.out.println("Delete User Failed!--Please input correct userName or password");
		}else{
			if(this.remove(manager_temp)){
				System.out.println("Delete User Succeed");
			}
		}
	}
	public void saveManagerToFile(){
		//		FileWriter saveManager;
		//		try {
		//			saveManager = new FileWriter("ManagerFile.txt");
		//			for(int i=0;i<this.size();i++){
		//				saveManager.write(this.get(i).getUserName()+"="+this.get(i).getPassword());
		//			}
		//			saveManager.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//			System.out.println("Save ManagerFile Failed !");
		//		}
		//	}
		try {
			System.out.println("save managerInfo");
			FileWriter fw =new FileWriter("ManagerFile.txt");
			for(int i =0;i<this.size();i++){
				fw.write(this.get(i).getUserName()+"="
						+this.get(i).getPassword()+"\r\n");
				System.out.println(this.get(i).getUserName());
			}
			fw.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
