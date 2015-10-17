package com.newppt.android.data;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.newppt.android.entity.MyPath;
import com.newppt.android.ui.AndroidPPT;

public class AndroidServer {

	//以后用得上
	//	private static ArrayList<Socket> socketList = new ArrayList<Socket>();

	private AndroidPPT sp = null;
	ServerSocket loadServer = null;
	ServerSocket server = null;

	public AndroidServer() throws Exception {
		
		sp = new AndroidPPT("ServerPPT");
		System.out.println("文件监听");
		//create a new file
		MyPath myPath = new MyPath();
		String rootPath = myPath.returnRootPath();
		File file = new File(rootPath);
		if (!file.exists() && !file.isDirectory()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 同步线程
		new Thread() {
			public void run() {
				try {
					connect();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		// 发送文件线程
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SendPPT();
			}
		}.start();
	}

	private void SendPPT() {
		
		try{
			loadServer = new ServerSocket(8889);
			
			while (true) {
				Socket loadSocket = loadServer.accept();
				
				
				SendFileThread sendFileThread = new SendFileThread(loadSocket,sp);
				sendFileThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 同步连接
	 * 
	 * @throws Exception
	 */
	private void connect() throws Exception {
		server = new ServerSocket(8888);
		System.out.println("同步监听");
		while (true) {
			Socket socket = server.accept();
//			System.out.println("连接成功");

//			socketList.add(socket);
			SerToClient serToClient = new SerToClient(socket, sp);
			serToClient.start();

		}
	}
}
