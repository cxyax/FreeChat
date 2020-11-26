package com.cxyax.freechat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *   FreeChat| by 程序员阿鑫 www.cxyax.com  2020年11月26日19:15:42
 *   仅供学习交流，如作它用所承受的法律责任一概与作者无关
 *   
 * @author ah xin
 * 当前：客户端
 */
public class ChatClient {
	ClientFrame frame;
	public String ip;
	public int port;
	Socket socket;
	public ChatClient(String ip,int port,ClientFrame frame) {
		this.ip = ip;
		this.port = port;
		this.frame=frame;
	}

	public  void startClient() {
		try {
			 socket = new Socket(ip, port);
			System.out.println(socket+"客户端连接服务器成功！");
			//新启动线程
			new Thread(new ClientReceiveImpl(socket,frame)).start();;
			
			
//			Scanner sc = new Scanner(System.in);
//			OutputStream out = socket.getOutputStream();
//			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
//			String data = null;
//			while(true){
//				data = sc.nextLine();
//				writer.write(data);
//				writer.newLine();
//				writer.flush();
//			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
