package com.Tony.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Tony.server.util.Message;
import com.Tony.server.util.Translate;



public class RegisterFrame extends JFrame implements KeyListener {
	private Client client;

	
	//private JButton jbtResign=new JButton("ע��");
	private JButton jbt_cancel=new JButton("ȡ��");
	private JButton jbt_register=new JButton("ע��");

	private JTextField jf_userName=new JTextField();
	public JTextField jf_ipAddr=new JTextField();
	public JTextField jf_port=new JTextField();
	private JPasswordField jp_passwords=new JPasswordField();

	private String ipAddr,port;
	private Socket socket=null;

	public RegisterFrame(){
		init();
	}
	public RegisterFrame(Client client){
		this();
		this.client=client;
	}
	public void init(){
		//������������������ԺͲ���
		this.setTitle("ע���˺�");
		this.setSize(250, 150);
		this.setLocation(400,300);
		//���ü�����
//		jf_userName.addKeyListener(this);
//		jp_passwords.addKeyListener(this);
//		jf_ipAddr.addKeyListener(this);
//		jf_ipAddr.addKeyListener(this);
		jbt_register.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					resign();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}});
		jbt_cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}});
		//�����ı����
		//�û������
		JPanel userName_l=new JPanel();
		userName_l.setLayout(new BorderLayout(8,0));
		userName_l.add(new JLabel("�û�����"),BorderLayout.WEST);
		userName_l.add(jf_userName, BorderLayout.CENTER);
		//�������
		JPanel passwords_l=new JPanel();
		passwords_l.setLayout(new BorderLayout(8,0));
		passwords_l.add(new JLabel(" ���룺"),BorderLayout.WEST);
		passwords_l.add(jp_passwords,BorderLayout.CENTER);
		//�û������������
		JPanel user_info=new JPanel();
		user_info.setLayout(new BorderLayout(16,0));
		user_info.add(userName_l,BorderLayout.NORTH);
		user_info.add(passwords_l,BorderLayout.CENTER);

		//IP���
		JPanel ipAddr_l=new JPanel();
		ipAddr_l.setLayout(new BorderLayout(8,0));
		ipAddr_l.add(new JLabel("IP��ַ��"), BorderLayout.WEST);
		ipAddr_l.add(jf_ipAddr, BorderLayout.CENTER);
		//this.add(ipAddr_l,BorderLayout.NORTH);
		//�˿����
		JPanel port_l=new JPanel();
		port_l.setLayout(new BorderLayout(8,0));
		port_l.add(new JLabel("�˿ںţ�"), BorderLayout.WEST);
		port_l.add(jf_port, BorderLayout.CENTER);
		//this.add(port_l,BorderLayout.CENTER);
		//�����ı������
		JPanel allText =new JPanel();
		allText.setLayout(new BorderLayout(32,0));
		allText.add(user_info,BorderLayout.NORTH);
		allText.add(ipAddr_l,BorderLayout.CENTER);
		allText.add(port_l,BorderLayout.SOUTH);
		//��ť���
		JPanel button_l=new JPanel();
		button_l.setLayout(new BorderLayout(8,0));
		button_l.add(jbt_register, BorderLayout.WEST);
		//button_l.add(jbtResign,BorderLayout.CENTER);
		button_l.add(jbt_cancel, BorderLayout.EAST);
		this.add(allText, BorderLayout.CENTER);
		this.add(button_l,BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//���������ó��û��������ô��ڴ�С
		//this.setResizable(false);
		this.setVisible(true);
	}
	public void cancel(){
		LoginFrame login= new LoginFrame(client);
		login.jf_ipAddr.setText(this.jf_ipAddr.getText());
		login.jf_port.setText(this.jf_port.getText());
		login.setVisible(true);
		this.dispose();
	}
	public void resign() throws IOException{
		if(jf_userName.getText().equals("")){
			JOptionPane.showMessageDialog(this, "�û�������Ϊ�գ�");
			return;
		}
		if(jp_passwords.getText().equals("")){
			JOptionPane.showMessageDialog(this, "���벻��Ϊ�գ�");
			return;
		}
		if(jf_ipAddr.getText().equals("")){
			JOptionPane.showMessageDialog(this, "ip��ַ����Ϊ�գ�");
			return;
		}
		if(jf_port.getText().equals("")){
			JOptionPane.showMessageDialog(this, "�˿ںŲ���Ϊ�գ�");
			return;
		}
		ipAddr=jf_ipAddr.getText().trim();
		port=jf_port.getText().trim();
		String userName=jf_userName.getText().trim();
		String passwords=jp_passwords.getText().trim();
		//����һ��ע��Ķ���
		Message userMessage=new Message(userName,passwords,true);
		//��ע���־����Ϊtrue
		if(getFeedback(ipAddr,port,userMessage)){
			client.setSocket(socket);
			client.creatChatWindow(userName);
		}
		//���ô������
	}

	//�������ӷ���������ע����Ϣ�ύ�������������������Է���������Ϣ
	public boolean getFeedback(String ip,String port,Message userMessage) throws IOException{
		OutputStream out=null;
		InputStream in=null;
		//�������ӣ�����������
		socket = new Socket(ip, Integer.parseInt(port));
		out = socket.getOutputStream();
		in= socket.getInputStream();

		out.write(Translate.ObjectToByte(userMessage));
		byte[] buffer = new byte[1024];
		int index=0;
		try {
			index = in.read(buffer);
			Message sysMessage=(Message) Translate.ByteToObject(buffer);
			//����������ת�����ַ���
			//String receive = new String(buffer, 0, index);
			switch(sysMessage.getSysMessage()){
			case "register":
				JOptionPane.showMessageDialog(this, "ע��ɹ�");
				this.dispose();
				return true;
			case "unlogin":
				JOptionPane.showMessageDialog(this, "��¼ʧ��");
				socket.close();
				return false;
			case "isUnResign":
				//��Ҫ�޸�Ϊ�û���ע��
				JOptionPane.showMessageDialog(this, "�û�δ��ע��");
				socket.close();
				return false;
			case "haveLogin":
				JOptionPane.showMessageDialog(this, "�û��ѵ�¼");
				socket.close();
				return false;
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "01ע��ʧ��");
			socket.close();
			return false;
		}
		JOptionPane.showMessageDialog(this, "02ע��ʧ��");
		socket.close();
		return false;
	}
	//public void resign(){
	//	login();
	//}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
			jbt_register.doClick();

	}
	public void keyReleased(KeyEvent e) {}
	public static void main(String[] args){
		new RegisterFrame();
	}
}
