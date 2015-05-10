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
		//���ô��ڲ��ɸı��С
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addKeyListener(this);
		//�����ı���
		sendArea=new JTextField();
		sendArea.addKeyListener(this);		//���ı������һ�����̼�����
		//������ť
		jbt_Send=new JButton("����");
		jbt_Exit=new JButton("�˳�");
		jbt_SingleChat=new JButton("˽��");
		//����һ����ʾ�����û����б���DefaultListModel��ΪModel��
		//���ģ�Ϳ���ǿ�Ƶ���ӣ�ɾ���б��е�Ԫ��
		onlineUserList=new JList<>(userModel);
		onlineUserList.setVisibleRowCount(8);
		onlineUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//�������û��б���ӵ�һ���л������������
		jsp_UserList=new JScrollPane(onlineUserList);
		jsp_UserList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp_UserList.setPreferredSize(new Dimension(100,300));
		jsp_UserList.setBorder(BorderFactory.createTitledBorder("�����û�"));

		//�����ı��򡢻�����壬����������
		chatArea=new JTextArea(8,8);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		jsp_Chat=new JScrollPane(chatArea);
		jsp_Chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp_Chat.setPreferredSize(new Dimension(260,300));
		jsp_Chat.setBorder(BorderFactory.createTitledBorder("������Ϣ"));

		//����һ������������������Ϣ���û��б�
		JPanel jpNorth=new JPanel();
		jpNorth.add(jsp_Chat,BorderLayout.CENTER);
		jpNorth.add(jsp_UserList,BorderLayout.EAST);

		//����һ�����������Ӱ�ť
		JPanel jpButton=new JPanel();
		jpButton.add(jbt_Send);
		jpButton.add(jbt_Exit);
		jpButton.add(jbt_SingleChat);

		//����һ�������������ı���
		JPanel jpSendArea=new JPanel();
		jpSendArea.setLayout(new BorderLayout(5,0));
		jpSendArea.add(sendArea);
		//�߽�ʽ����
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
		//���Ͱ�ť���ü����� ������ջ��У���ȡ���    
		jbt_Send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatMsg();
			}});

		//�˳���ť
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
		//����û��Ƿ�ѡ����һ���û�˽��
		if(onlineUserList.getMaxSelectionIndex()==-1){
			JOptionPane.showMessageDialog(this, "��˭˽�ģ�");
			return;
		}
		//��ñ�ѡ�е��û�����
		String singleChatName=(String) onlineUserList.getSelectedValue();
		if(client.getSingleChatList().containsKey(singleChatName)){
			JOptionPane.showMessageDialog(this, "˽�Ĵ����Ѵ�");
			return;
		}
		Message msg_SCW=new Message();
		msg_SCW.setUserName(userName);
		msg_SCW.setOtherName(singleChatName);
		msg_SCW.setSingleChatMessage("");
		client.creatSingleChatWindow(msg_SCW);
		
		System.out.println("����˽�Ĵ���");
	}
	public void showChatMsg(Message msg_chat){
		chatArea.append(msg_chat.getChatMessage()+"\r\n");
		chatArea.setCaretPosition(chatArea.getText().length());
	}
	public void sendChatMsg(){
		String chat_msg=sendArea.getText();
		if(chat_msg.equals("")){
			JOptionPane.showMessageDialog(this, "�����뷢�͵���Ϣ��");
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
		chatArea.append("�ң�"+chat_msg+"\r\n");		//ͨ�����ַ�ʽ�����Լ����͵���Ϣ��ʾ���Լ�����������
	}
	public void setClient(Client client){
		this.client=client;
	}
	public void showUpdateUserMsg(Message msg_updateUser){
		/**
		 * ���ڸı�˼·��
		 * 3���������û��б������е��û���ɾ��
		 * 4��һ���Խ�Message�����userList������û�����ӵ��б�����
		 */
		
		String[] userList_temp=new String[msg_updateUser.userList.size()-1];
		for(int i=0;i<userList_temp.length;i++){
			if(!userName.equals(msg_updateUser.userList.get(i))){
				userList_temp[i]=msg_updateUser.userList.get(i);
			}
		}
		//�������ԭ���б�
		userModel.removeAllElements();
		
		for(int i=0;i<userList_temp.length;i++){
			userModel.addElement(userList_temp[i]);
			System.out.println("��������û�:"+userList_temp[i]);
		}
	}
	public void addUserToList(Message msg_addUser){
		userModel.addElement(msg_addUser.getOnlineUser());
		chatArea.append(msg_addUser.getOnlineUser()+"///������\r\n");
	}
	public void deleUserFromList(Message msg_deleUser){
		String deleUser_str=msg_deleUser.getOutOfLineUserName();
		userModel.removeElement(deleUser_str);
		chatArea.append(deleUser_str+"///������\r\n");
	}
	public void closeChatWindow(){
		int flag = JOptionPane.showConfirmDialog(this, "�Ƿ��˳������ң�", "��ʾ", 
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
