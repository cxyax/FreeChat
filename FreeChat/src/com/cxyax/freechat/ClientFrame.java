package com.cxyax.freechat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *   FreeChat| by 程序员阿鑫 www.cxyax.com  2020年11月26日19:15:42
 *   仅供学习交流，如作它用所承受的法律责任一概与作者无关
 *   
 * @author ah xin
 * 当前：客户端界面
 */
public class ClientFrame extends JFrame {
	ClientFrame frame;
	JTable table;
	DefaultTableModel tableModel;  //声明DefaultTableModel类
	Object[] hades = {"聊天室"};
	Object[][] tableData = {{"系统消息：欢迎进入聊天室~"},{"温馨提示：请先输入IP和端口连接服务器~"}};
	
	JLabel jspname,jspContent,jspip,jspport; //昵称，内容 ,IP，端口 (文本框)
	JTextField txtname,txtContent,txtip,txtport,txtState;
	JButton connection,send;
	
	public Socket socket;
	
	public ClientFrame() {
		init();
		initListenners();
	}
	
	
	public void init(){
		tableModel = new DefaultTableModel();
		tableModel.setDataVector(tableData, hades);
		table = new JTable(tableModel);
		table.setEnabled(false); //设置表格不可编辑
		
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane,BorderLayout.CENTER);
		
		JPanel hader = new JPanel();
		//IP文本框
		jspip = new JLabel("IP");
		hader.add(jspip);
		
		txtip = new JTextField(10);
		txtip.setText("127.0.0.1");
		hader.add(txtip);
		//端口文本框
		jspport = new JLabel("端口");
		hader.add(jspport);
		
		txtport = new JTextField(5);
		txtport.setText("20197");
		hader.add(txtport);
		
		//连接按钮
		connection = new JButton("连接");
		hader.add(connection);
		
		//状态显示
		txtState = new JTextField(4);
		txtState.setEditable(false);
		txtState.setText("未连接");
		txtState.setForeground(Color.red);
		hader.add(txtState);
		
		this.add(hader,BorderLayout.NORTH);
		
		//底部部分的组件
		JPanel down = new JPanel();
		
		jspname = new JLabel("昵称");
		down.add(jspname);
		
		txtname = new JTextField(5);
		down.add(txtname);
		
		jspContent = new JLabel("内容");
		down.add(jspContent);
		
		txtContent = new JTextField(15);
		down.add(txtContent);
		
		send = new JButton("发送");
		down.add(send);
		
		this.add(down,BorderLayout.SOUTH);
		
		this.setTitle("聊天室 V1.0");
		this.setSize(400,500);
		this.setLocationRelativeTo(getOwner());
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//组件事件
	public void initListenners(){
		//连接按钮
		connection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ip = txtip.getText();
				String strport = txtport.getText();
				//判断输入的IP端口是否为空
				if("未连接".equals(txtState.getText())){
					if(ip.length() != 0 && strport.length() != 0) {
//						//判断输入端口是否为纯数字
						boolean flag = false;
						for(int i = 0; i < strport.length(); i++) {
							char a = strport.charAt(i);
//							System.out.println((int)a);
							//ASCII码 String强转int造成
							if(a >= 48 && a <= 57) {
								flag = true;
							}
						}
						if(flag) {
							//将字符串转换为int类型
							int port = Integer.parseInt(strport);
							//监测输入端口是否为系统占用端口
							if(port >= 0 && port <= 1024) {
								JOptionPane.showMessageDialog(null, "您输入的端口已被系统占用，请换一个","系统消息：",JOptionPane.ERROR_MESSAGE);
							}else {
								ChatClient client=new ChatClient(ip, port,ClientFrame.this);
								client.startClient();
								socket=client.socket;
								//判断socket是否为空   为空就代表没有连接
								if(socket != null) {
									txtState.setText("已连接");
									connection.setText("关闭");
									String data = "已成功连接，跟大家一起聊天吧！";
									Object[] obj = {data};
									ClientFrame.this.tableModel.addRow(obj);
									loadRecord();
								}else {
									JOptionPane.showMessageDialog(null, "连接失败，请检查服务器端是否开启以及IP端口是否正确。","系统消息",JOptionPane.CANCEL_OPTION);
								}
							}
						}else {
							JOptionPane.showMessageDialog(null, "请输入正确的端口");
						}
					}else {
						JOptionPane.showMessageDialog(null, "IP和端口不能为空");
					}
				}else{
					int state = JOptionPane.showConfirmDialog(null, "确定断开连接？","系统消息", JOptionPane.YES_NO_OPTION);
					//返回值为0和1,0代表是，1代表否
					if(state == 0) {
						if(socket != null) {
							try {
								socket.close();
								//关闭成功修改按钮文字为连接
								connection.setText("连接");
								//关闭成功修改连接状态文字为未连接
								txtState.setText("未连接");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		//发送按钮
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String state = txtState.getText();
				if("已连接".equals(state)) {
					String name = txtname.getText();
					if(name.length() == 0) {
						name = ChatServer.getServerIP();
					}
					String content = txtContent.getText();
					String data = name+":"+content;
					try {
						OutputStream out = socket.getOutputStream();
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
						writer.write(data);
						writer.newLine();
						writer.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}else {
					JOptionPane.showMessageDialog(null, "未连接服务器，无法发送","系统消息",JOptionPane.CANCEL_OPTION);
				}
			}
		});
	}
	public void loadRecord(){
		try {
			FileReader fr = new FileReader("D://storageMessage.txt");
			BufferedReader reader = new BufferedReader(fr);
			String data = null;
			while((data = reader.readLine()) != null){
				Object[] obj = {data};
				tableModel.addRow(obj);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		int n = JOptionPane.showConfirmDialog(null, "软件会在D盘创建一个名为storageMessage.txt文档来存储聊天记录,\n您是否继续进入软件？","系统消息",JOptionPane.YES_NO_OPTION);
		if(n == 0) {
			new ClientFrame();
		}
	}
	
}
