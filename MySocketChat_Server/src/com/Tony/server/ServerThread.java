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
	


	//将在Server类中创建的serverFrame传过来，并且
	//new一个broadcast将自己和ServerFrame传给它
	public ServerThread(ServerFrame serverFrame){
		this.serverFrame=serverFrame;
		serverFrame.showChatMessage("服务器启动");
		broadCast=new BroadCast(serverFrame,this);
		onlineUser=new HashMap<String, ClientThread>();
		clientList=new ArrayList<ClientThread>();
		
	}


	public void setServerFrame(ServerFrame serverFrame){
		this.serverFrame=serverFrame;
	}

	@Override
	public void run() {
		System.out.println("serverThread 开始run");
		try {
			//Socket socket=null;
			//创建绑定到特定端口的服务器套接字
			serverscoket = new ServerSocket(8008);
			while(!stop_flag){
				System.out.println("服务器等待客户端连接...");
				if(serverscoket.isClosed()){
					stop_flag=true;
				}else{
					try{
						socket = serverscoket.accept();
						String ip = socket.getLocalAddress().getHostAddress();
						int port = socket.getPort();
						System.out.println("连接上的客户端ip：" + ip + ",端口号：" + port);
					}catch(IOException e){
						socket=null;
						stop_flag=true;
					}
				}
				if(socket!=null){
					//尝试连接，检查用户信息
					if(checkUserInfo(socket,getUserInfo(socket))){
						loginSuccess(user);
					}else{
						System.out.println("该用户已断开连接！");
						socket.close();
						continue;
					}
					System.out.println("现在共连接了"+onlineUser.size()+"个客户端");
				}
			}
			System.out.println("serverThread准备停止");
		}catch(IOException e){
			System.out.println("用户连接失败！");
		}

	}

	public User getUserInfo(Socket socket){
		InputStream in = null;
		byte[] buffer = new byte[1024];
		try{
			in=socket.getInputStream();
			//in.read(buffer);
			int index = in.read(buffer);
			//将获得的比特数组强制转换成对象
			Message getUserInfo=(Message) Translate.ByteToObject(buffer);
			System.out.println(getUserInfo.getUserName()+"="+getUserInfo.getPasswords());
			//获得账户和密码
			//建立一个user对象，方便检查
			User user_temp=new User(getUserInfo.getUserName(),getUserInfo.getPasswords());
			if(getUserInfo.isResign()){		//判断是否是新注册用户
				user_temp.setResign(true);
				System.out.println("收到用户：" + user_temp.getUserName()+"的注册请求");
			}else{
				System.out.println("收到用户：" + user_temp.getUserName()+"的登录请求");
			}
			return user_temp;
		}catch(IOException e){
			System.out.println("用户连接出现异常！");
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
				System.out.println("注册成功");
				Message systemMessage=new Message();
				systemMessage.setSystemMessage(true);
				systemMessage.setSysMessage("register");
				out.write(Translate.ObjectToByte(systemMessage));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("checkUserInfo出错");
			} finally {
				try {
					bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return true;
		}else{
			
			System.out.println("查找用户信息");
			br=new BufferedReader(new FileReader("UserInfo.txt"));
			String user_line=null;
			Message systemMessage=new Message();
			systemMessage.setSystemMessage(true);
			
			try {
				System.out.println("检测用户是否已登录");
				if(onlineUser.containsKey(user_check.getUserName())){
					System.out.println("用户已登录！");
					br.close();
					systemMessage.setSysMessage("haveLogin");
					out.write(Translate.ObjectToByte(systemMessage));
					return false;
				}
				while ((user_line = br.readLine()) != null) {

					//将用户名和密码分开
					String userName_line=user_line.substring(0,user_line.indexOf("="));
					String userPasswords_line=user_line.substring(user_line.indexOf("=")+1).trim();

					if (userName_line.equals(user_check.getUserName())) {
						if(userPasswords_line.equals(user_check.getPasswords())){
							System.out.println("登录成功！");
							br.close();
							systemMessage.setSysMessage("login");
							out.write(Translate.ObjectToByte(systemMessage));
							return true;
						}else{
							System.out.println("密码错误！");
							br.close();
							systemMessage.setSysMessage("wrongPasswords");
							out.write(Translate.ObjectToByte(systemMessage));
							return false;
						}
					}
				}
				//如过没有在UserInfo.txt文件中没有匹配到用户名则告诉客户端该用户名没有注册
				System.out.println("用户不存在！");
				br.close();
				systemMessage.setSysMessage("isUnResign");
				out.write(Translate.ObjectToByte(systemMessage));
				return false;
			} catch (IOException e) {
				System.out.println("BBB checkUserInfo出错");
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
			System.out.println("集合全部被清空");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ServerThread，Serversocket关闭失败");
		}
		stop_flag=true;
	}
	public void deleClientThread(String client_dele){
		onlineUser.remove(client_dele);
		for(int i=0;i<clientList.size();i++){
			if(clientList.get(i).getUserName().equals(client_dele)){
				System.out.println("删除："+clientList.get(i).getUserName());
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
