package com.cxyax.freechat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *   FreeChat| by 程序员阿鑫 www.cxyax.com  2020年11月26日19:15:42
 *   仅供学习交流，如作它用所承受的法律责任一概与作者无关
 *   
 * @author ah xin
 * 当前：服务器端
 */
public class ChatServer extends Thread{
	public int port;
	private static List<Socket> clients = new ArrayList<Socket>();
	public ServerSocket server;
	public ServerFrame serverFrame;
	
	public ChatServer(ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(20197);
			setPort(port = server.getLocalPort());
			System.out.println("服务器端已启动，正在等待客户端链接...");
			while(true){
				Socket socket = server.accept();
				clients.add(socket);
				System.out.println(socket+"已有客户端连接服务器");
				
				new Thread(new ServerReceiveImpl(socket,clients)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	//获取服务器IP地址
	public static String getServerIP() {
		String getip = null;
		try {
			InetAddress inetaddress = InetAddress.getLocalHost();
			String ip = inetaddress+"";
			int index = ip.indexOf("/");
			getip = ip.substring(index+1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getip;
	}

	//修改服务器端口号
	public void setPort(int port) {
		this.port = port;
		serverFrame.showServerPort.setText(String.valueOf(port));
	}
	//获取服务器端口号
	public int getPort() {
		return port;
	}
		
}
