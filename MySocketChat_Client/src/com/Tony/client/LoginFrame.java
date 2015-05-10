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
import com.Tony.server.util.Translate;;



public class LoginFrame extends JFrame implements KeyListener{
	private Client client;

	private String ipAddr,port;
	private Socket socket=null;
	public String userName;

	private JButton jbt_resign=new JButton("注册");
	private JButton jbt_exit=new JButton("退出");
	private JButton jbt_login=new JButton("登录");

	private JTextField jf_userName=new JTextField();
	public JTextField jf_ipAddr=new JTextField();
	public JTextField jf_port=new JTextField();
	private JPasswordField jp_passwords=new JPasswordField();


	public LoginFrame(){
		init();
	}
	public LoginFrame(Client client){
		this();
		this.client=client;
	}
	public void init(){
		//创建窗体对象，设置属性和布局
		this.setTitle("登录聊天室");
		this.setSize(250, 150);
		this.setLocation(350,315);
		jf_ipAddr.setText("localhost");
		jf_port.setText("8008");
		//设置监听器
		jf_userName.addKeyListener(this);
		jp_passwords.addKeyListener(this);
		jf_ipAddr.addKeyListener(this);
		jf_port.addKeyListener(this);
		jbt_login.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					login();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}});
		jbt_exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}});
		jbt_resign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resign();
			}
		});
		//创建文本面板
		//用户名面板
		JPanel userName_l=new JPanel();
		userName_l.setLayout(new BorderLayout(8,0));
		userName_l.add(new JLabel("用户名："),BorderLayout.WEST);
		userName_l.add(jf_userName, BorderLayout.CENTER);
		//密码面板
		JPanel passwords_l=new JPanel();
		passwords_l.setLayout(new BorderLayout(8,0));
		passwords_l.add(new JLabel(" 密码："),BorderLayout.WEST);
		passwords_l.add(jp_passwords,BorderLayout.CENTER);
		//用户名和密码面板
		JPanel user_info=new JPanel();
		user_info.setLayout(new BorderLayout(16,0));
		user_info.add(userName_l,BorderLayout.NORTH);
		user_info.add(passwords_l,BorderLayout.CENTER);

		//IP面板
		JPanel ipAddr_l=new JPanel();
		ipAddr_l.setLayout(new BorderLayout(8,0));
		ipAddr_l.add(new JLabel("IP地址："), BorderLayout.WEST);
		ipAddr_l.add(jf_ipAddr, BorderLayout.CENTER);
		//this.add(ipAddr_l,BorderLayout.NORTH);
		//端口面板
		JPanel port_l=new JPanel();
		port_l.setLayout(new BorderLayout(8,0));
		port_l.add(new JLabel("端口号："), BorderLayout.WEST);
		port_l.add(jf_port, BorderLayout.CENTER);
		//this.add(port_l,BorderLayout.CENTER);
		//创建文本框面板
		JPanel allText =new JPanel();
		allText.setLayout(new BorderLayout(32,0));
		allText.add(user_info,BorderLayout.NORTH);
		allText.add(ipAddr_l,BorderLayout.CENTER);
		allText.add(port_l,BorderLayout.SOUTH);
		//按钮面板
		JPanel button_l=new JPanel();
		button_l.setLayout(new BorderLayout(8,0));
		button_l.add(jbt_login, BorderLayout.WEST);
		button_l.add(jbt_resign,BorderLayout.CENTER);
		button_l.add(jbt_exit, BorderLayout.EAST);
		this.add(allText, BorderLayout.CENTER);
		this.add(button_l,BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//将窗口设置成用户不能设置窗口大小
		//this.setResizable(false);
		this.setVisible(true);

	}
	public void login() throws IOException{
		if(jf_userName.getText().equals("")){
			JOptionPane.showMessageDialog(this, "用户名不能为空！");
			return;
		}
		if(jp_passwords.getText().equals("")){
			JOptionPane.showMessageDialog(this, "密码不能为空！");
			return;
		}

		if(jf_ipAddr.getText().equals("")){
			JOptionPane.showMessageDialog(this, "ip地址不能为空！");
			return;
		}
		if(jf_port.getText().equals("")){
			JOptionPane.showMessageDialog(this, "端口号不能为空！");
			return;
		}
		userName=jf_userName.getText().trim();
		ipAddr=jf_ipAddr.getText().trim();
		port=jf_port.getText().trim();
		Message userMessage=new Message(userName,jp_passwords.getText().trim());
		if(getFeedback(ipAddr,port,userMessage)){
			System.out.println("准备creatChatWindow");
			this.dispose();
			client.setSocket(socket);
			client.creatChatWindow(userName);
		}
	}
	public boolean getFeedback(String ip,String port,Message userMessage) throws IOException {
		OutputStream out=null;
		InputStream in=null;
		//尝试连接，将用户的信息提交给服务器,,端口将字符串解析为数字
		try {
			socket = new Socket(ip, Integer.parseInt(port));
			out = socket.getOutputStream();
			in= socket.getInputStream();
			out.write(Translate.ObjectToByte(userMessage));
		} catch (NumberFormatException e1) {
			// TODO 自动生成的 catch 块
			JOptionPane.showMessageDialog(this, "连接的服务器端口号port为整数,取值范围为：1024<port<65535");
			//e1.printStackTrace();
		} catch (UnknownHostException e1) {
			// TODO 自动生成的 catch 块
			JOptionPane.showMessageDialog(this, "服务器地址有误");
			System.exit(0);
			//e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			JOptionPane.showMessageDialog(this, "连接服务器失败，请稍后再试");
			return false;
			//e1.printStackTrace();
		}
		byte[] buffer = new byte[1024];
		try {

			in.read(buffer);
			Message sysMessage=(Message) Translate.ByteToObject(buffer);
			//将比特数组转换成字符串
			//String receive = new String(buffer, 0, index);
			switch(sysMessage.getSysMessage()){
			case "login":
				JOptionPane.showMessageDialog(this, "登录成功");
				//this.socket=socket;
				return true;
			case "unlogin":
				JOptionPane.showMessageDialog(this, "登录失败");
				socket.close();
				return false;
			case "isUnResign":
				JOptionPane.showMessageDialog(this, "用户未注册");
				socket.close();
				return false;
			case "wrongPasswords":
				JOptionPane.showMessageDialog(this, "密码错误");
				socket.close();
				return false;
			case "haveLogin":
				JOptionPane.showMessageDialog(this, "用户已登录");
				socket.close();
				return false;
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "登录失败");
			socket.close();
			return false;
		}
		JOptionPane.showMessageDialog(this, "登录失败");
		socket.close();
		return false;
	}


	public void resign(){
		RegisterFrame resign =new RegisterFrame(client);
		resign.jf_ipAddr.setText(this.jf_ipAddr.getText());
		resign.jf_port.setText(this.jf_port.getText());
		resign.setVisible(true);
		this.dispose();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER)
			jbt_login.doClick();
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	public static void main(String[] args){
		new LoginFrame();
	}
}
