package com.Tony.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.Tony.server.util.Message;

public class ChatFrame extends JFrame implements KeyListener{
	
	private Client client;
	private String userName;
	
	private JButton jbt_Send=null;
	private JButton jbt_Exit=null;
	private JButton jbt_SingleChat=null;
	private JTextField sendArea=null;
	private JTextArea chatArea=null;
	private JScrollPane jsp_Chat=null;
	private JScrollPane jsp_UserList=null;
	private JList onlineUserList=null;
	private DefaultListModel<String> userModel=new DefaultListModel<>();


	public ChatFrame(Client client,String title){
		this.client=client;
		userName=title;
		System.out.println("creatChatFrame succeed");
		init();
	}
	public void init(){
		setTitle(userName);
		setSize(400, 407);
		setLocation(100,100);
		//设置窗口不可改变大小
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addKeyListener(this);
		//创建文本框
		sendArea=new JTextField();
		sendArea.addKeyListener(this);		//在文本域添加一个键盘监听器
		//创建按钮
		jbt_Send=new JButton("发送");
		jbt_Exit=new JButton("退出");
		jbt_SingleChat=new JButton("私聊");
		//创建一个显示在线用户的列表，用DefaultListModel作为Model，
		//这个模型可以强制的添加，删除列表中的元素
		onlineUserList=new JList<>(userModel);
		onlineUserList.setVisibleRowCount(8);
		onlineUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//将在线用户列表添加到一个有滑动条的面板中
		jsp_UserList=new JScrollPane(onlineUserList);
		jsp_UserList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp_UserList.setPreferredSize(new Dimension(100,300));
		jsp_UserList.setBorder(BorderFactory.createTitledBorder("在线用户"));

		//创建文本域、滑动面板，及设置属性
		chatArea=new JTextArea(8,8);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		jsp_Chat=new JScrollPane(chatArea);
		jsp_Chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp_Chat.setPreferredSize(new Dimension(260,300));
		jsp_Chat.setBorder(BorderFactory.createTitledBorder("聊天消息"));

		//创建一个面板由于添加聊天信息和用户列表
		JPanel jpNorth=new JPanel();
		jpNorth.add(jsp_Chat,BorderLayout.CENTER);
		jpNorth.add(jsp_UserList,BorderLayout.EAST);

		//创建一个面板用于添加按钮
		JPanel jpButton=new JPanel();
		jpButton.add(jbt_Send);
		jpButton.add(jbt_Exit);
		jpButton.add(jbt_SingleChat);

		//创建一个面板用于添加文本框
		JPanel jpSendArea=new JPanel();
		jpSendArea.setLayout(new BorderLayout(5,0));
		jpSendArea.add(sendArea);
		//边界式布局
		setLayout(new BorderLayout());
		add(jpNorth,BorderLayout.NORTH);
		add(jpSendArea,BorderLayout.CENTER);
		add(jpButton,BorderLayout.SOUTH);
		setVisible(true);
		//add(new JScrollPane(onlineUserList),BorderLayout.EAST);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jbt_Exit.doClick();
				System.out.println("closing");
			}
		});
		//发送按钮设置监听器 ，，清空换行，获取光标    
		jbt_Send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatMsg();
			}});

		//退出按钮
		jbt_Exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				closeChatWindow();
			}});
		jbt_SingleChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				singleChat();
			}
		});
	}
	
	public void singleChat(){
		//检测用户是否选择了一个用户私聊
		if(onlineUserList.getMaxSelectionIndex()==-1){
			JOptionPane.showMessageDialog(this, "和谁私聊？");
			return;
		}
		//获得被选中的用户名字
		String singleChatName=(String) onlineUserList.getSelectedValue();
		if(client.getSingleChatList().containsKey(singleChatName)){
			JOptionPane.showMessageDialog(this, "私聊窗口已打开");
			return;
		}
		Message msg_SCW=new Message();
		msg_SCW.setUserName(userName);
		msg_SCW.setOtherName(singleChatName);
		msg_SCW.setSingleChatMessage("");
		client.creatSingleChatWindow(msg_SCW);
		
		System.out.println("创建私聊窗口");
	}
	public void showChatMsg(Message msg_chat){
		chatArea.append(msg_chat.getChatMessage()+"\r\n");
		chatArea.setCaretPosition(chatArea.getText().length());
	}
	public void sendChatMsg(){
		String chat_msg=sendArea.getText();
		if(chat_msg.equals("")){
			JOptionPane.showMessageDialog(this, "请输入发送的消息！");
			return;
		}
		sendArea.setText("");
		sendArea.requestFocus();
		Message msg_toSend=new Message(chat_msg);
		msg_toSend.setUserName(userName);
		System.out.println(userName);
		
		client.sendMsg(msg_toSend);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		chatArea.append(sdf.format(new Date())+"\r\n");
		chatArea.append("我："+chat_msg+"\r\n");		//通过这种方式，将自己发送的消息显示到自己的聊天区域
	}
	public void setClient(Client client){
		this.client=client;
	}
	public void showUpdateUserMsg(Message msg_updateUser){
		/**
		 * 现在改变思路：
		 * 3、将在线用户列表里所有的用户都删除
		 * 4、一次性将Message里面的userList里面的用户都添加到列表里面
		 */
		
		String[] userList_temp=new String[msg_updateUser.userList.size()-1];
		for(int i=0;i<userList_temp.length;i++){
			if(!userName.equals(msg_updateUser.userList.get(i))){
				userList_temp[i]=msg_updateUser.userList.get(i);
			}
		}
		//首先清空原有列表
		userModel.removeAllElements();
		
		for(int i=0;i<userList_temp.length;i++){
			userModel.addElement(userList_temp[i]);
			System.out.println("添加在线用户:"+userList_temp[i]);
		}
	}
	public void addUserToList(Message msg_addUser){
		userModel.addElement(msg_addUser.getOnlineUser());
		chatArea.append(msg_addUser.getOnlineUser()+"///上线了\r\n");
	}
	public void deleUserFromList(Message msg_deleUser){
		String deleUser_str=msg_deleUser.getOutOfLineUserName();
		userModel.removeElement(deleUser_str);
		chatArea.append(deleUser_str+"///下线了\r\n");
	}
	public void closeChatWindow(){
		int flag = JOptionPane.showConfirmDialog(this, "是否退出聊天室？", "提示", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(flag == JOptionPane.OK_OPTION){
			System.exit(0);
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			jbt_Send.doClick();
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
