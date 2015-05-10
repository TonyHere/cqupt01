package com.Tony.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.Tony.server.util.Message;
import com.Tony.server.util.Translate;
import com.Tony.server.util.User;

public class ClientThread extends Thread{

	private BroadCast broadCast;
	private ServerThread serverThread;
	private InputStream in;
	private OutputStream out;
	private String userName;



	public ClientThread(ServerThread serverThread,User user,BroadCast broadCast){
		userName=user.getUserName();
		this.serverThread=serverThread;
		try {
			in=serverThread.getSocket().getInputStream();
			out=serverThread.getSocket().getOutputStream();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("ClientThread��ȡ���������ʧ��");
		}
		this.broadCast=broadCast;

	}
	//��ȡ�ͻ��˷�������Ϣ����ͨ��broadcastת���������û�
	@Override
	public void run() {
		while(true){
			byte[] buffer=new byte[1024];
			try {

				in.read(buffer);
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println(this.userName+"clientThread��ȡ�쳣");
				serverThread.deleClientThread(this.userName);
				try {
					in.close();
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("clientThread,out,in�ر�ʧ��");
				}
				break;
			}

			Message client_msg=(Message) Translate.ByteToObject(buffer);
			broadCast.sendMsgToClient(client_msg);
			
		}
	}
	
	public InputStream getIn() {
		return in;
	}
	public OutputStream getOut() {
		return out;
	}
	public String getUserName() {
		return userName;
	}
	

}
