package com.Tony.server.util;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 用于在客户端和服务器之间的信息传递
 * 将所有的信息都封装到这个类里面，再转换成字节流进行传递
 * @author tony
 *
 */
public class Message implements Serializable{
	private String userName=null;
	private String passwords=null;
	private String otherName=null;
	private String chatMessage=null;
	private String singleChatMessage=null;
	private String onlineUser=null;
	private String outOfLineUserName=null;
	private String sysMessage=null;
	
	public ArrayList<String> userList=new ArrayList<String>();
	
	private boolean systemMessage=false;
	private boolean onlineMsg=false;
	private boolean outOfLineMsg=false;
	private boolean updateUser=false;
	private boolean isResign=false;
	private boolean addOnlineUser=false;
	private boolean deleOutOfLineUser=false;
	//私聊不属于系统消息
	private boolean isSingleChat=false;
	
	public Message(){
		
	}
	public Message(String chatMessage){
		this.chatMessage=chatMessage;
	}
	//用于检测账号是否正确
	public Message(String userName,String passwords){
		this.userName=userName;
		this.passwords=passwords;
	}
	public Message(String userName,String passwords,boolean isResign){
		this(userName,passwords);
		this.isResign=isResign;
	}
	public Message(String userName,String otherName,String singleChatMessage,boolean isSingleChat){
		this.userName=userName;
		this.otherName=otherName;
		this.singleChatMessage=singleChatMessage;
		this.isSingleChat=isSingleChat;
	}
	
	
	public String getOnlineUser() {
		return onlineUser;
	}
	public void setOnlineUser(String onlineUser) {
		this.onlineUser = onlineUser;
	}
	public String getChatMessage() {
		return chatMessage;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}
	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}
	public void setSingleChatMessage(String singleChatMessage) {
		this.singleChatMessage = singleChatMessage;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
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
	public boolean isSingleChat() {
		return isSingleChat;
	}
	public void setSingleChat(boolean isSingleChat) {
		this.isSingleChat = isSingleChat;
	}
	
	public boolean isSystemMessage() {
		return systemMessage;
	}
	public void setSystemMessage(boolean isSystemMessage) {
		this.systemMessage = isSystemMessage;
	}
	public String getSingleChatMessage() {
		return singleChatMessage;
	}
	public String getOutOfLineUserName() {
		return outOfLineUserName;
	}
	public void setOutOfLineUserName(String outOfLineUserName) {
		this.outOfLineUserName = outOfLineUserName;
	}
	public String getSysMessage() {
		return sysMessage;
	}
	public void setSysMessage(String sysMessage) {
		this.sysMessage = sysMessage;
	}
	public boolean isUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(boolean updateUser) {
		this.updateUser = updateUser;
	}
	public boolean isOutOfLineMsg() {
		return outOfLineMsg;
	}
	public void setOutOfLineMsg(boolean outOfLineMsg) {
		this.outOfLineMsg = outOfLineMsg;
	}
	public boolean isOnlineMsg() {
		return onlineMsg;
	}
	public void setOnlineMsg(boolean onlineMsg) {
		this.onlineMsg = onlineMsg;
	}
	
	
}