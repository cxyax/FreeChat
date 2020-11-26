package com.cxyax.freechat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 *   FreeChat| by 程序员阿鑫 www.cxyax.com  2020年11月26日19:15:42
 *   仅供学习交流，如作它用所承受的法律责任一概与作者无关
 *   
 * @author ah xin
 * 当前：客户端启动的接收线程
 */
public class ClientReceiveImpl implements Runnable{
	private Socket socket;
	public ClientFrame frame;
	public ClientReceiveImpl(Socket socket,ClientFrame frame) {
		this.socket = socket;
		this.frame = frame;
	}
	public void storageMessage(){
		
		
	}
	@Override
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String data = null;
			//创建消息记录文件
			FileWriter fw = new FileWriter(new File("D://storageMessage.txt"),true);
			BufferedWriter writer = new BufferedWriter(fw);
			while((data = reader.readLine()) != null){
				System.out.println(data);
				Object[] rowData = {data};
				writer.write(data);
				writer.newLine();
				writer.flush();
				frame.tableModel.addRow(rowData);
			}
		} catch (IOException e) {
			System.err.println(socket+"已经断开连接");
			try {
				if(socket != null){
					socket.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//移除服务器端维护的socket列表中
//			clients.remove(socket);
			//e.printStackTrace();
		}
	}

}
