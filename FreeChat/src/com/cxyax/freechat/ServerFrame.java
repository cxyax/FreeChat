package com.cxyax.freechat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 *   FreeChat| by 程序员阿鑫 www.cxyax.com  2020年11月26日19:15:42
 *   仅供学习交流，如作它用所承受的法律责任一概与作者无关
 *   
 * @author ah xin
 * 当前：服务器界面
 */
public class ServerFrame extends JFrame{
	
	JLabel txtServerIP,txtServerPort,txturl;  //服务器ip，服务器端口
	JTextField showServerState,showServerIP,showServerPort;  //显示服务器状态，显示服务器ip，显示服务器端口
	JButton startServer,stopServer;  //启动服务器，关闭服务器
	private int port;
	private ChatServer cs;
	
	public ServerFrame(int port) {
		this.port = port;
	}
	
	public ServerFrame() {
		this.setTitle("聊天室服务器端 V1.0");
		this.setSize(400,200);
		this.setLayout(null);
		this.setResizable(false);
		this.setLocationRelativeTo(getOwner());
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
		
		this.inio();
		this.setVisible(true);
	}
	public void inio() {
		JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.setSize(getSize());
		
		//显示服务器当前状态
		showServerState = new JTextField();
		showServerState.setBounds(10, 10, 35, 25);
		showServerState.setEditable(false); //设置文本框不可编辑
		showServerState.setText("关闭");
		showServerState.setForeground(Color.red); //设置字体颜色
		jp.add(showServerState);
		
		//开服按钮
		startServer = new JButton("开服");
		startServer.setBounds(50, 10, 60, 25);
		startServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if("关闭".equals(showServerState.getText())) {
					showServerState.setText("启动");
					showServerState.setForeground(Color.green); //设置字体颜色
					cs = new ChatServer(ServerFrame.this);
					cs.start();
					showServerPort.setText(cs.getPort()+"");
				}else {
					JOptionPane.showMessageDialog(null, "服务器已开启，请勿重新开启！");
				}
			}
		});
		jp.add(startServer);
		
		
		//关服按钮
		stopServer = new JButton("关服");
		stopServer.setBounds(120, 10, 60, 25);
		stopServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if("启动".equals(showServerState.getText())) {
					try {
						if(cs.server != null) {
							showServerState.setText("关闭");
							showServerState.setForeground(Color.red); //设置字体颜色
							cs.server.close();
							System.err.println("服务器已关闭...");
							showServerPort.setText("");
							
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(null, "服务器已关闭，请勿重复关闭。");
				}
				
				
			}
		});
		jp.add(stopServer);
		//显示服务器IP
		txtServerIP = new JLabel("服务器IP：");
		txtServerIP.setBounds(10, 60, 70, 25);
		jp.add(txtServerIP);

		showServerIP = new JTextField();
		showServerIP.setBounds(110, 60, 200, 25);
		showServerIP.setText(ChatServer.getServerIP());
		showServerIP.setEditable(false);
		jp.add(showServerIP);
		
		//显示服务器端口
		txtServerPort = new JLabel("服务器端口：");
		txtServerPort.setBounds(10, 100, 100, 25);
		jp.add(txtServerPort);
		
		showServerPort = new JTextField();
		showServerPort.setBounds(110, 100, 100, 25);
		showServerPort.setText("");
		showServerPort.setEditable(false);
		jp.add(showServerPort);
		
		txturl = new JLabel("欢迎使用程序员阿鑫开发java简易聊天软件  www.cxyax.com");
		txturl.setBounds(20, 145, 400, 25);
		jp.add(txturl);
		
		this.add(jp);
	}
	public static void main(String[] args) {
		new ServerFrame();
	}

}
