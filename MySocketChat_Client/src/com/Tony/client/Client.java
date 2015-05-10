package com.Tony.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.Tony.server.util.Message;
import com.Tony.server.util.Translate;

public class Client extends Thread {

	private ChatFrame chatFrame;
	private LoginFrame loginFrame;
	

	private OutputStream out;
	private InputStream in;
	private Socket socket;
	private HashMap<String,SingleChatFrame> singleChatList;
	
	private String userName;
	private boolean firstMsg=true;


	public Client(){
		singleChatList= new HashMap<String, SingleChatFrame>();
	}
	public void creatChatWindow(String userName){
		this.userName=userName;
		//loginFrame.dispose();
		chatFrame=new ChatFrame(this,this.userName);
		try {
			out=socket.getOutputStream();
			in=socket.getInputStream();
		} catch (IOException e) {
			System.out.println("获取out，in失败");
			e.printStackTrace();
		}
		System.out.println("creatChatFrame成功");
		start();
	}
	public void creatSingleChatWindow(Message msg_singleChat){
		String otherName=msg_singleChat.getOtherName();
		SingleChatFrame scf=new SingleChatFrame(otherName,this);
		scf.showSingleChatMsg(msg_singleChat);
		singleChatList.put(otherName, scf);
		//singleChatList.get(otherName).showSingleChatMsg(msg_singleChat);
	}
	public void closeChatWindow(){

	}
	public void closeSingleChatWindow(){

	}
	public void run(){
		while(true){
			byte[] buffer = new byte[1024];
			try {
				in.read(buffer);
				Message clientReceive=(Message) Translate.ByteToObject(buffer);
				//解析接收到的数据
				parseMsg(clientReceive);
			} catch (IOException e) {
				System.out.println(userName+"：解析消息失败");
				break;
			}
		}
	}
	public void parseMsg(Message msgToParse){
		if(msgToParse.isSystemMessage()){
			if(msgToParse.isOnlineMsg()){
				if(firstMsg){
					System.out.println("updateUserMsg");
					chatFrame.showUpdateUserMsg(msgToParse);
					firstMsg=false;
				}else{
					chatFrame.addUserToList(msgToParse);
				}
				return;
			}
			if(msgToParse.isOutOfLineMsg()){
				chatFrame.deleUserFromList(msgToParse);
				return;
			}
		}
		if(msgToParse.isSingleChat()){
			if(singleChatList.isEmpty()){
				creatSingleChatWindow(msgToParse);
			}else{
				if(singleChatList.containsKey(msgToParse.getOtherName())){
					singleChatList.get(msgToParse.getOtherName())
					.showSingleChatMsg(msgToParse);
				}else{
					creatSingleChatWindow(msgToParse);
				}
			}
			return;
		}
		chatFrame.showChatMsg(msgToParse);
	}
	public void sendMsg(Message msg_toServer){
		try {
			out.write(Translate.ObjectToByte(msg_toServer));
		} catch (IOException e) {
			System.out.println(userName+"：发送消息失败！");
			JOptionPane.showMessageDialog(chatFrame, "与服务器断开连接\r\n聊天窗口即将关闭！");
			System.exit(0);
			e.printStackTrace();
		}
	}
	public void setLoginFrame(LoginFrame loginFrame) {
		this.loginFrame = loginFrame;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getUserName() {
		return userName;
	}
	
	public ChatFrame getChatFrame() {
		return chatFrame;
	}
	public HashMap<String, SingleChatFrame> getSingleChatList() {
		return singleChatList;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client=new Client();
		LoginFrame loginFrame=new LoginFrame(client);
		client.setLoginFrame(loginFrame);
	}
}
