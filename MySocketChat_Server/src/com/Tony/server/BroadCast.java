package com.Tony.server;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.Tony.server.util.Message;
import com.Tony.server.util.Translate;

public class BroadCast {
	private ServerFrame serverFrame;
	private ServerThread serverTread;
	private OutputStream out;

	public BroadCast(ServerFrame serverframe,ServerThread serverThread){
		this.serverFrame=serverframe;
		this.serverTread=serverThread;
	}

	public void sendMsgToClient(Message msg_toClient){
		if(msg_toClient.isSystemMessage()){
			if(msg_toClient.isOnlineMsg()){
				sendAddUserMsg(msg_toClient.getOnlineUser());
				return;
			}
			if(msg_toClient.isOutOfLineMsg()){
				sendDeleUserMsg(msg_toClient.getOutOfLineUserName());
				return;
			}
		}
		if(msg_toClient.isSingleChat()){
			sendSingleChatMsg(msg_toClient);
			return;
		}
		if(!(msg_toClient.getChatMessage().equals("")||
				msg_toClient==null)){
			sendMsgToAll(msg_toClient);
		}
	}
	public void sendAddUserMsg(String str_addUser){

		Message msg_refresh=new Message();
		msg_refresh.setSystemMessage(true);
		msg_refresh.setOnlineMsg(true);
		msg_refresh.setOnlineUser(str_addUser);
		//获取用户列表
		for(int a=0;a<serverTread.clientList.size();a++){
			msg_refresh.userList.add(serverTread.
					clientList.get(a).getUserName());
			System.out.println("cast。add"+
					serverTread.clientList.get(a).getUserName());
		}
		//给每个人发消息
		for(int i=0;i<serverTread.clientList.size();i++){
			sendMsg(msg_refresh,serverTread.clientList.get(i));
		}
		serverFrame.showChatMessage(msg_refresh);
	}
	public void sendDeleUserMsg(String str_deleUser){
		Message msg_deleUser=new Message();
		msg_deleUser.setSystemMessage(true);
		msg_deleUser.setOutOfLineMsg(true);
		msg_deleUser.setOutOfLineUserName(str_deleUser);
		for(int i=0;i<serverTread.clientList.size();i++){
			//			if(!serverTread.clientList.get(i).getUserName().equals(str_deleUser)){
			//			}
			sendMsg(msg_deleUser, serverTread.clientList.get(i));
			System.out.println("发送下线消息给："
					+serverTread.clientList.get(i).getUserName());

		}
	}
	public void sendSingleChatMsg(Message msg_singleChat){
		//设置日期格式， new Date()为获取当前系统时间
		String chatTemp=msg_singleChat.getSingleChatMessage();
		System.out.println(chatTemp+"singlechatMsg");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置好发送的消息的格式
		msg_singleChat.setSingleChatMessage((sdf.format(new Date())+
				"\r\n"+msg_singleChat.getOtherName()+":"+chatTemp));
		for(int i=0;i<serverTread.clientList.size();i++){
			if(serverTread.clientList.get(i).getUserName().equals
					(msg_singleChat.getUserName())){
				sendMsg(msg_singleChat,serverTread.clientList.get(i));
			}
		}
		serverFrame.showChatMessage(msg_singleChat);
	}
	public void sendMsgToAll(Message msg_toAll){
		//设置日期格式， new Date()为获取当前系统时间
		String chatTemp=msg_toAll.getChatMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置好发送的消息的格式
		System.out.println(msg_toAll.getUserName());
		msg_toAll.setChatMessage((sdf.format(new Date())+
				"\r\n"+msg_toAll.getUserName()+":"+chatTemp));
		for(int i=0;i<serverTread.clientList.size();i++){
			//用ArrayList来获取用户列表
			if(!serverTread.clientList.get(i).getUserName().equals
					(msg_toAll.getUserName())){
				sendMsg(msg_toAll,serverTread.clientList.get(i));
				System.out.println("发送给："
						+serverTread.clientList.get(i).getUserName());
			}
		}
		serverFrame.showChatMessage(msg_toAll);
	}

	public void sendMsg(Message msg,ClientThread clientThread){
		out=clientThread.getOut();
		try {
			out.write(Translate.ObjectToByte(msg));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("broadcast发送消息失败");
		}
	}
}