package com.Tony.BookManageSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class BookList extends ArrayList<Book> {
	public BookList(){
		File bookListFile =new File("bookListFile.txt");
		if(bookListFile.exists()){
			try {
				BufferedReader br =new BufferedReader(new FileReader(bookListFile));
				String bookInfo;
				while((bookInfo=br.readLine())!=null){
					int point_1=bookInfo.indexOf("=");
					int ID_temp=Integer.parseInt(bookInfo.substring(0,point_1));
					int point_2=bookInfo.indexOf("=", point_1)+point_1;
					String bookName_temp=bookInfo.substring(point_1+1,point_2);
					point_1=bookInfo.indexOf("=", point_2)+point_2;
					String author_temp=bookInfo.substring(point_2+1,point_1);
					
					int amount_temp=Integer.parseInt(bookInfo.substring(point_1+1).trim());
					this.add(new Book(ID_temp,bookName_temp,author_temp,amount_temp));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Read BookFile Failed !");
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				System.out.println("Read BookInfo Failed !");
			}
			
		}else{
			try {
				bookListFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Creat New File Failed !");
			}
			
		}
	}
	public void showAllBook(){
		System.out.println("ID\tBookName\tAuthor\t\tAmount");
		System.out.println("-----------------------------------------");
		for(int i=0;i<this.size();i++){
			Book book=this.get(i);
			System.out.println(book.getID()+"\t"+book.getName()+"\t\t"
					+book.getAuthor()+"\t\t"+book.getAmount());
		}
		System.out.println("-----------------------------------------");
	}
	public Book seekBook(int ID){
		for(int i=0;i<this.size();i++){
			if(this.get(i).getID()==ID){
				return this.get(i);
			}
		}
		return null;
	}
	public void updateBook(int ID,String newBookName){
		Book book_update;
		if((book_update=seekBook(ID))!=null){
			book_update.setName(newBookName);
			System.out.println("Update BookName Succeed !");
		}else{
			System.out.println("Update Erro ! Make sure you have input correct bookname!");
		}
	}
	public void deleBook(int ID){
		Book book_dele;
		if((book_dele=seekBook(ID))!=null){
			this.remove(book_dele);
			System.out.println("Delete Book Succeed !");
		}else{
			System.out.println("Delete Erro ! Make sure you have input correct bookname!");
		}
	}
	public void addBook(int ID,String bookName,String author,int amount){
		this.add(new Book(ID,bookName,author,amount));
		System.out.println("Add Succeed !");
	}
	public void saveBookToFile(){
		FileWriter saveBook;
		try {
			saveBook = new FileWriter("bookListFile.txt");
			for(int i=0;i<this.size();i++){
				saveBook.write(this.get(i).getID()+"="+this.get(i).getName()+"="
					+this.get(i).getAuthor()+"="+this.get(i).getAmount()+"\r\n");
			}
			saveBook.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Save BookFile Failed !");
		}
	}

}
