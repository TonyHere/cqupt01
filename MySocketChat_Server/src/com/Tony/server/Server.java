package com.Tony.server;

public class Server {
	private ServerFrame serverFrame;
	private ServerThread serverThread;
	
	Server(){

	}

	public ServerFrame getServerFrame() {
		return serverFrame;
	}

	public void setServerFrame(ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
	}

	public ServerThread getServerThread() {
		return serverThread;
	}
	public void startServer(){
		serverThread=new ServerThread(serverFrame);
		//serverThread.setServerFrame(serverFrame);
		serverThread.start();
		
	}
	public void stopServer(){
		serverFrame.showChatMessage("·şÎñÆ÷Í£Ö¹");
		serverThread.stopServerThread();
	}
	public static void main(String[] args){
		Server server=new Server ();
		ServerFrame serverFrame=new ServerFrame(server);
		server.setServerFrame(serverFrame);
	}
	
}
