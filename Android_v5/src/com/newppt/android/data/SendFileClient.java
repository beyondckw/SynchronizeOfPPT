package com.newppt.android.data;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

import com.newppt.android.entity.DMT;
import com.newppt.android.entity.MyPath;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class SendFileClient extends Thread {
//	private Socket s;
	String ip;
	int port = 8889;
	Handler handler;
	String typeString;
	final int timeOut = 5000;
	
	public SendFileClient(String ip, Handler handler, String type) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.handler = handler;
		this.typeString = type;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		loadFile();
	}

	/**
	 * 涓嬭浇鏂囦欢鍑芥暟
	 */
	private void loadFile() {
		try {
//			Socket s = new Socket(ip, port);
			
			Socket s = new Socket();
//			 System.out.println("------1-4-0");
			s.connect(new InetSocketAddress(ip, port), timeOut);
//			 System.out.println("------1-3-0");
			
			
			byte[] buf = new byte[100];
			DataOutputStream os = null;
			os = new DataOutputStream(s.getOutputStream());
			os.writeUTF(typeString);
			System.out.println("-----kk----11" );
			
			s.setSoTimeout(timeOut);
			
			InputStream is = s.getInputStream();
			// 接收传输来的文件名
			int len = is.read(buf);
			String fileName = new String(buf, 0, len,"GBK");
			
			System.out.println(fileName);
			String savePath = MyPath.savePptFilePath + fileName;

			// 接收传输来的文件
			FileOutputStream fos = new FileOutputStream(savePath);
			System.out.println("-----gg-----11");
			int data;
			byte by[] = new byte[1024];
			System.out.println("----uu-----22");
			
			if (typeString.equals("PPT")) {
				Message msg = new Message();
				msg.what = 0x124;
				msg.obj = "开始下载";
				handler.sendMessage(msg);
			}

			while (-1 != (data = is.read(by))) {
				// System.out.println("----1");
				fos.write(by, 0, data);
				
			}
			System.out.println("---------33");
			fos.close();
			is.close();
			s.close();
			if (typeString.equals("PPT")) {
				Message msg = new Message();
				msg.what = 0x125;
				msg.obj = "下载结束";
				handler.sendMessage(msg);
				System.out.println("---------44");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Message msg = new Message();
//			System.out.println("-----00000");
			msg.what = 0x126;
			msg.obj = "下载失败";
			handler.sendMessage(msg);
			
			e.printStackTrace();
		} 
	}

	public byte[] getByte() throws Exception {
		
		System.out.println("---10010");
		Socket s = new Socket(ip, port);
		System.out.println("---10011");
		DataOutputStream os = null;
		os = new DataOutputStream(s.getOutputStream());
		os.writeUTF(typeString);

		InputStream in = s.getInputStream();
		System.out.println("---------1sss" + "\n");

//		byte[] buf = new byte[100];
//		int len = in.read(buf);
//		String fileName = new String(buf, 0, len);
//		System.out.println(fileName);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] byffer = new byte[1024];
		int length = -1;
		while ((length = in.read(byffer)) != -1) {
			byteArrayOutputStream.write(byffer, 0, length);

		}
		byteArrayOutputStream.close();
		in.close();
		return byteArrayOutputStream.toByteArray();
	}

}
