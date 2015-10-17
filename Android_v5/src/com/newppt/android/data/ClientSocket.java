package com.newppt.android.data;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Message;

import com.newppt.android.entity.DMT;
import com.newppt.android.entity.HMessage;
import com.newppt.android.entity.MyPath;
import com.newppt.android.logical.FileInfo;


public class ClientSocket extends Thread {
	private String ip;
	private int port;
	private Socket socket;
	private Handler handler;
	public Handler reHandler;
	InputStream is;
	ObjectInputStream objectInputStream = null;
	ObjectOutputStream objectOutputStream;
	DataOutputStream os;
	String fileName = null;
	int page;
	int currentPage;
	final int timeOut = 10 * 1000;
	volatile boolean threadStopTip = false;
	
	public boolean connectSucceedTip = false;

	public ClientSocket(String ip, int port, Handler handler) {
		this.ip = ip;
		this.port = port;
		this.handler = handler;
		fileName = "";
		page = 0;
		currentPage = 0;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// socket = new Socket(ip, port);
			socket = new Socket();
			// System.out.println("------1-4-0");
			socket.connect(new InetSocketAddress(ip, port), timeOut);
			// System.out.println("------1-3-0");
			socket.setSoTimeout(timeOut);
			// System.out.println("------1-2-0");
			is = socket.getInputStream();
			// System.out.println("------1-1-0");
			connectSucceedTip = true;
			new Thread() {
				public void run() {
					try {
						while (true) {
							// sleep(1000);
							if (threadStopTip) {
								break;
							}
							readAndSendMessage(socket);
						}
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			}.start();
		} catch (SocketTimeoutException e) {
			// TODO: handle exception

			Message msg = new Message();
			if (!socket.isClosed() && socket.isConnected()) {
				System.out.println("------1-1-1-读取超时");
				msg.obj = "读取超时";
			}

			else {
				System.out.println("------1-1-1-连接超时");
				msg.obj = "连接超时,请确认IP地址是否正确";

			}
			msg.what = 0x110;
			handler.sendMessage(msg);

			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		catch (Exception e) {
			e.printStackTrace();

			Message msg = new Message();
			msg.what = 0x110;
			msg.obj = "ip地址不正确";
			handler.sendMessage(msg);

			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void setStopTip() {
		threadStopTip = true;
	}

	private void readAndSendMessage(Socket s) throws Exception {
		// System.out.println("-----currentpage-----" + currentPage);
		objectInputStream = new ObjectInputStream(new BufferedInputStream(
				s.getInputStream()));
		DMT msgDmt = (DMT) objectInputStream.readObject();

		objectOutputStream = new ObjectOutputStream(s.getOutputStream());
		objectOutputStream.writeObject(msgDmt);
		if (currentPage != msgDmt.getCurrentPage() || msgDmt.getCurrentPage() == msgDmt.getCurrentPage() && !fileName.equals(msgDmt.getFilename())) {
			currentPage = msgDmt.getCurrentPage();
			
//        System.out.println("---119"+fileName + "  "+msgDmt.getFilename());
//			
//			if (!fileName.equals(msgDmt.getFilename()) ) {
//				String rootPath = MyPath.rootPath + msgDmt.getFilename();
//				FileInfo.DeleteFolder(rootPath);
//			}
			fileName = msgDmt.getFilename();

			SendFileClient sendFileClient = new SendFileClient(ip, handler,
					"SVG");

			// ImageBytes imgebytes = new ImageBytes(ip,"SVG");

			Message msg = new Message();
			msg.what = 0x123;
			byte[] buffer = sendFileClient.getByte();
			// byte[] buffer = imgebytes.getByte();

			HMessage hMessage = new HMessage();
			hMessage.setBuffer(buffer);
			hMessage.setMsgDmt(msgDmt);
			msg.obj = hMessage;
			handler.sendMessage(msg);

			// save jpg
			String rootPath = MyPath.rootPath + msgDmt.getFilename();
			String savePath = rootPath + MyPath.pptJpg;

			FileInfo.CreateFile(rootPath);
			FileInfo.CreateFile(savePath);
			String filePath = savePath + "/ppt-" + msgDmt.getCurrentPage()
					+ ".jpg";

			new SaveJpgThread(buffer, filePath).start();

		}
		os = new DataOutputStream(socket.getOutputStream());
		os.writeUTF("ok");
	}
	
	public String getFileName() {
		return fileName;
	}
}
