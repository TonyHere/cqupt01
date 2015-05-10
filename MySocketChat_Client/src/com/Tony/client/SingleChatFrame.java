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
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.Tony.server.util.Message;

public class SingleChatFrame extends JFrame implements KeyListener{
	private Client client;
	private ChatFrame chatFrame;
	
	
	private JButton jbt_Send=null;
	private JButton jbt_Exit=null;
	private JTextField sendArea=null;
	private JTextArea chatArea=null;
	private JScrollPane jsp_Chat=null;
	
	private String singleChatName;
	public SingleChatFrame(){
		init();
	}
	public SingleChatFrame(String singleChatName,Client client){
		//this();
		this.client=client;
		this.chatFrame=this.client.getChatFrame();
		
		this.singleChatName=singleChatName;
		System.out.println("creatsinglechatwindow succeed");
		setTitle("私聊"+singleChatName);
		init();
	}
	public void init(){
		//setTitle("test");
		setSize(300, 400);
		setLocation(chatFrame.getX()-10,chatFrame.getY()-10);
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

		//创建文本域、滑动面板，及设置属性
		chatArea=new JTextArea(8,8);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		jsp_Chat=new JScrollPane(chatArea);
		jsp_Chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp_Chat.setPreferredSize(new Dimension(260,300));
		jsp_Chat.setBorder(BorderFactory.createTitledBorder("私聊消息"));

		//创建一个面板由于添加聊天信息和用户列表
		JPanel jpNorth=new JPanel();
		jpNorth.add(jsp_Chat,BorderLayout.CENTER);

		//创建一个面板用于添加按钮
		JPanel jpButton=new JPanel();
		jpButton.add(jbt_Send);
		jpButton.add(jbt_Exit);

		//创建一个面板用于添加文本框
		JPanel jpSendArea=new JPanel();
		jpSendArea.setLayout(new BorderLayout(5,0));
		jpSendArea.add(sendArea);
		//边界式布局
		setLayout(new BorderLayout());
		//add(jspChat,BorderLayout.NORTH);
		add(jpNorth,BorderLayout.NORTH);
		add(jpSendArea,BorderLayout.CENTER);
		add(jpButton,BorderLayout.SOUTH);
		setVisible(true);
		//add(new JScrollPane(onlineUserList),BorderLayout.EAST);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jbt_Exit.doClick();
			}
		});
		//发送按钮设置监听器 ，，清空换行，获取光标    
		jbt_Send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				sendSingleChatMsg();
			}});

		//退出按钮
		jbt_Exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				closeSingleChatWindow();
			}});
		setVisible(true);
	}
	public void showSingleChatMsg(Message msg_singleChat){
		if(!msg_singleChat.getSingleChatMessage().equals("")){
			chatArea.append(msg_singleChat.getSingleChatMessage()+"\r\n");
			chatArea.setCaretPosition(chatArea.getText().length());
		}
		
	}
	public void sendSingleChatMsg(){
		String singleChat_msg=sendArea.getText();
		if(singleChat_msg.equals("")){
			JOptionPane.showMessageDialog(this, "请输入发送的消息！");
			return;
		}
		sendArea.setText("");
		sendArea.requestFocus();
		Message msg_single_toSend=new Message();
		msg_single_toSend.setSingleChatMessage(singleChat_msg);
		//msg_single_toSend.setSystemMessage(true);
		msg_single_toSend.setSingleChat(true);
		//接收方用户名
		msg_single_toSend.setUserName(singleChatName);
		msg_single_toSend.setOtherName(client.getUserName());
		
		client.sendMsg(msg_single_toSend);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		chatArea.append(sdf.format(new Date())+"\r\n");
		chatArea.append("我："+singleChat_msg+"\r\n");		//通过这种方式，将自己发送的消息显示到自己的聊天区域
	}
	
	public void closeSingleChatWindow(){
		int flag = JOptionPane.showConfirmDialog(this, "是否退出私聊窗口？", "提示", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(flag == JOptionPane.OK_OPTION){
			client.getSingleChatList().remove(singleChatName);
			this.dispose();
		}
	}
	
	
	
	public static void main(String[] args){
		new SingleChatFrame();
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
