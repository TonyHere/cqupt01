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
		setTitle("������");
		setLocation(300, 300);
		setSize(400, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		init();
	}

	public void init(){
		//����д�밴ť��صĶ���
		jb_start=new JButton("����������");
		jb_stop=new JButton("ֹͣ������");
		jb_stop.setEnabled(false);
		jb_exit=new JButton("�˳�������");
		//��һ�������װ��ť
		jp_button=new JPanel();
		jp_button.add(jb_start);
		jp_button.add(jb_stop);
		jp_button.add(jb_exit);
		//���õ��ϱ�
		this.add(jp_button,BorderLayout.SOUTH);

		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//����������Ϣ����
		jt_chatArea=new JTextArea(8,8);
		jt_chatArea.setEditable(false);
		jt_chatArea.setLineWrap(true);

		//����ʾ������Ϣ������ӵ��������
		jsp_chatArea = new JScrollPane();
		jsp_chatArea.setPreferredSize(new Dimension(260,290));
		//jsp_chatArea.setBounds(5, 5, 200, 290);
		jsp_chatArea.setWheelScrollingEnabled(true);
		jsp_chatArea.setBorder(BorderFactory.createTitledBorder("������Ϣ"));
		getContentPane().add(jsp_chatArea);
		jsp_chatArea.setViewportView(jt_chatArea);

		//��������ʾ�����û����б�
		jl_onlineUser=new JList<>(onlineUserModel);
		jl_onlineUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jl_onlineUser.setVisibleRowCount(8);

		//�������û��б���ӵ�һ���л������������
		jsp_onlineList= new JScrollPane(jl_onlineUser);
		jsp_onlineList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp_onlineList.setPreferredSize(new Dimension(100,290));
		jsp_onlineList.setBorder(BorderFactory.createTitledBorder("�����û�"));

		//���õ��м�
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
				System.out.println("jb_start�������");
				jb_start.setEnabled(false);
				jb_stop.setEnabled(true);
				server.startServer();
			}
		});
		jb_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("jb_stop�������");
				click_Stop();

			}
		});
		jb_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jb_stop.isEnabled()){
					System.out.println("jb_exit�������");
					jb_stop.doClick();
				}else{
					System.exit(0);
				}
			}
		});

	}

	public void click_Stop(){
		int flag = JOptionPane.showConfirmDialog(this, "�Ƿ�Ҫֹͣ��������", "����", 
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
		String onLineMsg=user.getUserName()+"��/////������";
		showChatMessage(onLineMsg);
	}

	public void deleOnlineUser(User user){
		for(int i=0;i<onlineUserModel.size();i++){
			if(user.getUserName().equals(onlineUserModel.getElementAt(i))){
				onlineUserModel.remove(i);
				String outOfLineMsg=user.getUserName()+"��/////������";
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
			//				jt_chatArea.append(chatMsg_Msg.getOnlineUser()+"��####������\r\n");
			//				jt_chatArea.setCaretPosition(jt_chatArea.getText().length());
			//			}
			//			if(chatMsg_Msg.isOutOfLineMsg()){
			//				jt_chatArea.append(chatMsg_Msg.getOutOfLineUserName()+"��###������\r\n");
			//				jt_chatArea.setCaretPosition(jt_chatArea.getText().length());
			//			}
			return;
		}
		if(msg.isSingleChat()){
			jt_chatArea.append(msg.getUserName()+">>>˽�ģ�"
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
