package com.cxyax.freechat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
/**
 * 服务器端启动的接收线程
 * @author mhys
 *
 */
public class ServerReceiveImpl implements Runnable{
	private Socket socket;
	private List<Socket> clients;
	public ServerReceiveImpl(Socket socket,List<Socket> clients) {
		this.socket = socket;
		this.clients = clients;
	}
	@Override
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String data = null;
			while((data = reader.readLine()) != null){
				System.out.println(data);
				for(Socket client : clients){
					OutputStream out = client.getOutputStream();
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
					writer.write(data);
					writer.newLine();
					writer.flush();
				}
			}
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println(socket+"客户端已经断开连接");
			try {
				if(socket != null){
					socket.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//移除服务器端维护的socket列表中
			clients.remove(socket);
		}
	}

}
