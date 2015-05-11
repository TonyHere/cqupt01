package com.Tony.BookManageSystem;

public class Book {
	private String name;
	private String author;
	private int id;
	private int amount;
	public Book(int id,String name,String author){
		this.id=id;
		this.name=name;
		this.author=author;
	}
	public Book(int id,String name,String author,int amount){
		this(id,name,author);
		this.amount=amount;
	}
	public int getID() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getAuthor() {
		return author;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
