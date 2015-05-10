package com.Tony.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.Tony.server.util.Message;
import com.Tony.server.util.Translate;
import com.Tony.server.util.User;

public class ServerThread extends Thread{
	private ServerFrame serverFrame;
	
	private BroadCast broadCast;
	private HashMap<String,ClientThread> onlineUser;


	
	private ServerSocket serverscoket;
	private Socket socket;
	public ArrayList<ClientThread> clientList;
	private String userName;
	private User user;
	private boolean stop_flag=false;
	


	//����Server���д�����serverFrame������������
	//newһ��broadcast���Լ���ServerFrame������
	public ServerThread(ServerFrame serverFrame){
		this.serverFrame=serverFrame;
		serverFrame.showChatMessage("����������");
		broadCast=new BroadCast(serverFrame,this);
		onlineUser=new HashMap<String, ClientThread>();
		clientList=new ArrayList<ClientThread>();
		
	}


	public void setServerFrame(ServerFrame serverFrame){
		this.serverFrame=serverFrame;
	}

	@Override
	public void run() {
		System.out.println("serverThread ��ʼrun");
		try {
			//Socket socket=null;
			//�����󶨵��ض��˿ڵķ������׽���
			serverscoket = new ServerSocket(8008);
			while(!stop_flag){
				System.out.println("�������ȴ��ͻ�������...");
				if(serverscoket.isClosed()){
					stop_flag=true;
				}else{
					try{
						socket = serverscoket.accept();
						String ip = socket.getLocalAddress().getHostAddress();
						int port = socket.getPort();
						System.out.println("�����ϵĿͻ���ip��" + ip + ",�˿ںţ�" + port);
					}catch(IOException e){
						socket=null;
						stop_flag=true;
					}
				}
				if(socket!=null){
					//�������ӣ�����û���Ϣ
					if(checkUserInfo(socket,getUserInfo(socket))){
						loginSuccess(user);
					}else{
						System.out.println("���û��ѶϿ����ӣ�");
						socket.close();
						continue;
					}
					System.out.println("���ڹ�������"+onlineUser.size()+"���ͻ���");
				}
			}
			System.out.println("serverThread׼��ֹͣ");
		}catch(IOException e){
			System.out.println("�û�����ʧ�ܣ�");
		}

	}

	public User getUserInfo(Socket socket){
		InputStream in = null;
		byte[] buffer = new byte[1024];
		try{
			in=socket.getInputStream();
			//in.read(buffer);
			int index = in.read(buffer);
			//����õı�������ǿ��ת���ɶ���
			Message getUserInfo=(Message) Translate.ByteToObject(buffer);
			System.out.println(getUserInfo.getUserName()+"="+getUserInfo.getPasswords());
			//����˻�������
			//����һ��user���󣬷�����
			User user_temp=new User(getUserInfo.getUserName(),getUserInfo.getPasswords());
			if(getUserInfo.isResign()){		//�ж��Ƿ�����ע���û�
				user_temp.setResign(true);
				System.out.println("�յ��û���" + user_temp.getUserName()+"��ע������");
			}else{
				System.out.println("�յ��û���" + user_temp.getUserName()+"�ĵ�¼����");
			}
			return user_temp;
		}catch(IOException e){
			System.out.println("�û����ӳ����쳣��");
			return null;
		}
	}

	public boolean checkUserInfo(Socket socket,User user_check) throws IOException{
		OutputStream out =null;
		out=socket.getOutputStream();
		BufferedReader br = null;
		BufferedWriter bw = null;
		user=user_check;
		if(user_check.isResign()){
			try {
				bw = new BufferedWriter(new FileWriter("UserInfo.txt", true));
				bw.write(user_check.getUserName()+"="+user_check.getPasswords());
				bw.newLine();
				bw.flush();
				System.out.println("ע��ɹ�");
				Message systemMessage=new Message();
				systemMessage.setSystemMessage(true);
				systemMessage.setSysMessage("register");
				out.write(Translate.ObjectToByte(systemMessage));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("checkUserInfo����");
			} finally {
				try {
					bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return true;
		}else{
			
			System.out.println("�����û���Ϣ");
			br=new BufferedReader(new FileReader("UserInfo.txt"));
			String user_line=null;
			Message systemMessage=new Message();
			systemMessage.setSystemMessage(true);
			
			try {
				System.out.println("����û��Ƿ��ѵ�¼");
				if(onlineUser.containsKey(user_check.getUserName())){
					System.out.println("�û��ѵ�¼��");
					br.close();
					systemMessage.setSysMessage("haveLogin");
					out.write(Translate.ObjectToByte(systemMessage));
					return false;
				}
				while ((user_line = br.readLine()) != null) {

					//���û���������ֿ�
					String userName_line=user_line.substring(0,user_line.indexOf("="));
					String userPasswords_line=user_line.substring(user_line.indexOf("=")+1).trim();

					if (userName_line.equals(user_check.getUserName())) {
						if(userPasswords_line.equals(user_check.getPasswords())){
							System.out.println("��¼�ɹ���");
							br.close();
							systemMessage.setSysMessage("login");
							out.write(Translate.ObjectToByte(systemMessage));
							return true;
						}else{
							System.out.println("�������");
							br.close();
							systemMessage.setSysMessage("wrongPasswords");
							out.write(Translate.ObjectToByte(systemMessage));
							return false;
						}
					}
				}
				//���û����UserInfo.txt�ļ���û��ƥ�䵽�û�������߿ͻ��˸��û���û��ע��
				System.out.println("�û������ڣ�");
				br.close();
				systemMessage.setSysMessage("isUnResign");
				out.write(Translate.ObjectToByte(systemMessage));
				return false;
			} catch (IOException e) {
				System.out.println("BBB checkUserInfo����");
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}
	public void loginSuccess(User user_login){
		ClientThread clientThread =new ClientThread(this,user,broadCast);
		clientThread.start();
		onlineUser.put(user.getUserName(), clientThread);
		clientList.add(clientThread);
		serverFrame.addOnlineUser(user);
		refreshOnlineUser(user_login);
		System.out.println(user.getUserName());
		System.out.println("onlienUser.size:"+onlineUser.size());
		System.out.println("clientList.size:"+clientList.size());
		
	}
	
	public void stopServerThread(){
		try {
			serverscoket.close();
			this.onlineUser.clear();
			for(int i=0;i<clientList.size();i++){
				this.clientList.get(i).getIn().close();
			}
			this.clientList.clear();
			System.out.println("����ȫ�������");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ServerThread��Serversocket�ر�ʧ��");
		}
		stop_flag=true;
	}
	public void deleClientThread(String client_dele){
		onlineUser.remove(client_dele);
		for(int i=0;i<clientList.size();i++){
			if(clientList.get(i).getUserName().equals(client_dele)){
				System.out.println("ɾ����"+clientList.get(i).getUserName());
				clientList.remove(i);
				break;
			}
		}
		broadCast.sendDeleUserMsg(client_dele);
		serverFrame.deleOnlineUser(new User(client_dele));
	}
	public void refreshOnlineUser(User user_refresh){
		
		broadCast.sendAddUserMsg(user_refresh.getUserName());
	}
	public Socket getSocket() {
		return socket;
	}

}
