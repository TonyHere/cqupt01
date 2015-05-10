package com.Tony.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import com.Tony.server.util.Message;
import com.Tony.server.util.User;

public class ServerFrame extends JFrame{
	private Server server;
	private ServerThread serverThread;

	private JButton jb_start=null;
	private JButton jb_stop=null;
	private JButton jb_exit=null;
	private JPanel jp_button=null;
	private JPanel jp_user=null;
	private JTextArea jt_chatArea=null;
	private JScrollPane jsp_chatArea=null;
	private JScrollPane jsp_onlineList=null;
	private JList<String> jl_onlineUser=null;
	private DefaultListModel<String> onlineUserModel=new DefaultListModel<>();


	public ServerFrame(Server server){
		this();
		this.server=server;
		this.serverThread=server.getServerThread();
	}

	public ServerFrame(){
		setTitle("服务器");
		setLocation(300, 300);
		setSize(400, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		init();
	}

	public void init(){
		//现在写与按钮相关的东西
		jb_start=new JButton("启动服务器");
		jb_stop=new JButton("停止服务器");
		jb_stop.setEnabled(false);
		jb_exit=new JButton("退出服务器");
		//用一个面板来装按钮
		jp_button=new JPanel();
		jp_button.add(jb_start);
		jp_button.add(jb_stop);
		jp_button.add(jb_exit);
		//设置到南边
		this.add(jp_button,BorderLayout.SOUTH);

		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//设置聊天信息区域
		jt_chatArea=new JTextArea(8,8);
		jt_chatArea.setEditable(false);
		jt_chatArea.setLineWrap(true);

		//将显示聊天信息区域添加到滑动面板
		jsp_chatArea = new JScrollPane();
		jsp_chatArea.setPreferredSize(new Dimension(260,290));
		//jsp_chatArea.setBounds(5, 5, 200, 290);
		jsp_chatArea.setWheelScrollingEnabled(true);
		jsp_chatArea.setBorder(BorderFactory.createTitledBorder("聊天消息"));
		getContentPane().add(jsp_chatArea);
		jsp_chatArea.setViewportView(jt_chatArea);

		//下面是显示在线用户的列表
		jl_onlineUser=new JList<>(onlineUserModel);
		jl_onlineUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jl_onlineUser.setVisibleRowCount(8);

		//将在线用户列表添加到一个有滑动条的面板中
		jsp_onlineList= new JScrollPane(jl_onlineUser);
		jsp_onlineList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp_onlineList.setPreferredSize(new Dimension(100,290));
		jsp_onlineList.setBorder(BorderFactory.createTitledBorder("在线用户"));

		//设置到中间
		jp_user=new JPanel();
		jp_user.add(jsp_chatArea,BorderLayout.CENTER);
		jp_user.add(jsp_onlineList,BorderLayout.EAST);
		this.add(jp_user,BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				jb_stop.doClick();
			}
		});
		jb_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("jb_start被点击了");
				jb_start.setEnabled(false);
				jb_stop.setEnabled(true);
				server.startServer();
			}
		});
		jb_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("jb_stop被点击了");
				click_Stop();

			}
		});
		jb_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jb_stop.isEnabled()){
					System.out.println("jb_exit被点击了");
					jb_stop.doClick();
				}else{
					System.exit(0);
				}
			}
		});

	}

	public void click_Stop(){
		int flag = JOptionPane.showConfirmDialog(this, "是否要停止服务器？", "警告", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(flag == JOptionPane.OK_OPTION){
			jb_start.setEnabled(true);
			jb_stop.setEnabled(false);
			//onlineUserModel.removeAllElements();
			server.stopServer();
		}
	}


	public ServerThread getServerThread() {
		return serverThread;
	}
	public void setServerThread(ServerThread serverThread) {
		this.serverThread = serverThread;
	}


	public void addOnlineUser(User user){
		System.out.println("serverFrame.addOnlineUser");
		onlineUserModel.addElement(user.getUserName());
		String onLineMsg=user.getUserName()+"：/////上线了";
		showChatMessage(onLineMsg);
	}

	public void deleOnlineUser(User user){
		for(int i=0;i<onlineUserModel.size();i++){
			if(user.getUserName().equals(onlineUserModel.getElementAt(i))){
				onlineUserModel.remove(i);
				String outOfLineMsg=user.getUserName()+"：/////下线了";
				showChatMessage(outOfLineMsg);
				return;
			}
		}
	}
	public void showChatMessage(String msg_str){
		jt_chatArea.append(msg_str+"\r\n");
		jt_chatArea.setCaretPosition(jt_chatArea.getText().length());
	}

	public void showChatMessage(Message msg){
		if(msg.isSystemMessage()){
			//			if(chatMsg_Msg.isOnlineMsg()){
			//				jt_chatArea.append(chatMsg_Msg.getOnlineUser()+"：####上线了\r\n");
			//				jt_chatArea.setCaretPosition(jt_chatArea.getText().length());
			//			}
			//			if(chatMsg_Msg.isOutOfLineMsg()){
			//				jt_chatArea.append(chatMsg_Msg.getOutOfLineUserName()+"：###下线了\r\n");
			//				jt_chatArea.setCaretPosition(jt_chatArea.getText().length());
			//			}
			return;
		}
		if(msg.isSingleChat()){
			jt_chatArea.append(msg.getUserName()+">>>私聊："
					+msg.getOtherName()+"\r\n"
					+msg.getSingleChatMessage()+"\r\n");
			jt_chatArea.setCaretPosition(jt_chatArea.getText().length());
			return;
		}
		jt_chatArea.append(msg.getChatMessage()+"\r\n");
		jt_chatArea.setCaretPosition(jt_chatArea.getText().length());
	}
	public static void main(String[] args){
		new ServerFrame();
	}
}
