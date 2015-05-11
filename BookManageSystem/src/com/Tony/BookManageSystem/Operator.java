package com.Tony.BookManageSystem;

import java.util.Scanner;

public class Operator {
	
	private String userName;
	private String password;
	//private String managerName;
	
	
	private BookList bookList;
	private UserList userList;
	private ManagerList managerList;
	private int operatorRight;
	private Scanner scan = new Scanner(System.in);
	public Operator(){
		bookList=new BookList();
		userList = new UserList();
		managerList = new ManagerList();
		startSystem();
	}

	public void startSystem(){
		operatorRight=0;
		System.out.println("Welcom to Tony's Library");
		while(operatorRight==0){
			login();
		}
		switch(operatorRight){
		case 1:
			loginUser();
			break;
		case 2:
			loginManager();
			break;
		}
	}
	public void login(){
		while(operatorRight==0){
			System.out.println("1.User Login\r\n2.User Register\r\n"+
					"3.Manager Login\r\n4.Manager Register\r\n5.Exit");
			int choice=scan.nextInt();
			if(choice==5){
				exitSystem();
				System.exit(0);
			}
			
			switch(choice){
			case 1:
				if(showLoginUserMsg()){
					operatorRight=1;
				}else{
					System.out.println("User's userName or password erro !"
							+"Please input correctly");
				}
				break;
			case 2:
				registerUser();
				operatorRight=1;
				break;
			case 3:
				//this.managerName=userName;
				if(showLoginManagerMsg()){
					operatorRight=2;
				}else{
					System.out.println("Manager's userName or password erro !");
				}
			case 4:
				registerManager();
				operatorRight=2;
				break;
			default:
				System.out.println("Erro! Make sure you have the account");
			}
			
		}
	}
	public void loginUser(){
		System.out.println("Hello "+userName+"!");
		System.out.println("Do you want to do ?\r\n---------------------------");
		int option=0;
		while(option!=2){
			System.out.println("1.Show All Book\r\n2.Exit");
			option=scan.nextInt();
			while(true){
				if(option==1||option==2){
					break;
				}else{
					System.out.println("Illeagle Number!Input Agian!");
					option=scan.nextInt();
				}
			}
			switch (option) {
			case 1:
				seekBook();
				break;
			case 2:
				exitSystem();
				System.out.println("Exit System!");
				break;
			}
		}
	}
	public boolean showLoginUserMsg(){
		System.out.println("Please input the UserName");
		userName=scan.next();
		System.out.println("Please input the Password");
		password=scan.next();
		if(userList.login(userName, password)){
			return true;
		}
		return false;
		
	}
	public boolean showLoginManagerMsg(){
		System.out.println("Please input the ManagerName");
		userName=scan.next();
		System.out.println("Please input the Password");
		password=scan.next();
		if(managerList.login(userName, password)){
			return true;
		}
		return false;
	}
	public void loginManager(){
		System.out.println("Hello "+userName+"!");
		System.out.println("Do you want to do ?\r\n---------------------------");
		int option=0;
		while(true){
			System.out.println("1.Add Book\r\n2.Delete Book\r\n"
		+"3.Update Book\r\n4.Show All User\r\n5.Show All Book\r\n6.Exit");
			option=scan.nextInt();
			while(true){
				if(option==1||option==2||option==3||option==4||option==5||option==6){
					break;
				}else{
					System.out.println("Illeagle Number!Input Agian!");
					option=scan.nextInt();
				}
			}
			switch (option) {
			case 1:
				addBook();
				break;
			case 2:
				deleBook();
				break;
			case 3:
				updateBook();
				break;
			case 4:
				showUser();
				break;
			case 5:
				bookList.showAllBook();
				break;
			case 6:
				exitSystem();
				System.out.println("Exit System!");
				System.exit(0);
			}
		}
	}
	public void registerUser(){
		System.out.println("Please input the UserName");
		userName=scan.next();
		System.out.println("Please input the Password");
		password=scan.next();
		userList.register(new User(userName,password));
		System.out.println("Register Succeed !");
	}
	public void registerManager(){
		System.out.println("Please input the RegisterManagerName");
		userName=scan.next();
		System.out.println("Please input the Password");
		password=scan.next();
		managerList.register(userName,password);
		System.out.println("Register Succeed !");
	}
	//下面是管理员功能
	public void updateBook(){
		bookList.showAllBook();
		System.out.println("Which ID of book do you want to update ?");
		int ID=scan.nextInt();
		System.out.println("Inupt the new name of the book:");
		String	newBookName=scan.next();
		bookList.updateBook(ID, newBookName);
	}
	public void deleBook(){
		bookList.showAllBook();
		System.out.println("Which ID of book do you want to delete ?");
		int ID=scan.nextInt();
		bookList.deleBook(ID);
	}
	public void addBook(){
		System.out.println("Please input the ID of the book that you wnat you add:");
		int id=scan.nextInt();
		System.out.println("Please input the Nameof the book that you wnat you add:");
		String bookName=scan.next();
		System.out.println("Please input the Author of the book that you wnat you add:");
		String author=scan.next();
		System.out.println("Please input the Amount of the book that you wnat you add:");
		int amount=scan.nextInt();
		
		bookList.addBook(id,bookName,author,amount);
	}
	public void showUser(){
		System.out.println("user Name\tPassword");
		System.out.println("---------------------------");
		for(int i=0;i<userList.size();i++){
			System.out.println(userList.get(i).getUserName()
					+"\t\t"+userList.get(i).getPassword());
		}
		System.out.println("---------------------------");
	}
	public void exitSystem(){
		userList.saveUserToFile();
		bookList.saveBookToFile();
		managerList.saveManagerToFile();
	}
	
//	public void selecteBook(){
//
//	}
	//下面是用户功能
	//用户 	查找书
	public void seekBook(){
		bookList.showAllBook();
	}
	//用户	借书
	public void rentBook(){

	}
	public static void main(String[] args) {
		new Operator();
	}

}
