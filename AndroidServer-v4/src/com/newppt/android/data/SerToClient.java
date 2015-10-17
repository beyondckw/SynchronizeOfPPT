package com.newppt.android.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.newppt.android.entity.DMT;
import com.newppt.android.ui.AndroidPPT;

public class SerToClient extends Thread {
	Socket s;
	AndroidPPT sp;

	public SerToClient(Socket s, AndroidPPT sp) {
		this.s = s;
		this.sp = sp;
	}

	public void run() {
		while (true) {
			
			if(s.isClosed()) {
				break;
			}
			
			try {
				try {			//每隔0.1秒就发一次ppt文件
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (sp._notify)
					sendMessage(s);				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				try {
					s.close();
					// socketList.remove(s);
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 设置DMT
	 * 
	 * @return
	 */
	DMT setDMT() {
		DMT dmt = new DMT();
		dmt.setCurrentPage(sp._currentPage);
		dmt.setPage(sp._pages);
		dmt.setFilename(sp._fileName);
		return dmt;
	}

	/**
	 * send message
	 * 
	 * @param s
	 * @throws IOException
	 */
	private  void sendMessage(Socket s)  {
		
		try {
			s.setSoTimeout(5000);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			
			System.out.println("读取超时");
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
        
		DMT dmt = setDMT();
		System.out.println("---currentpage------" + dmt.getCurrentPage());
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(
					s.getOutputStream());
		
		objectOutputStream.writeObject(dmt);

		
		ObjectInputStream objectInputStream = new ObjectInputStream(
				s.getInputStream());
//		objectInputStream.wait(5*1000);
		System.out.println("---------2222" + "\n");
		try {
			DMT messageDmt = (DMT) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataInputStream is = null;
		is = new DataInputStream(s.getInputStream());
		String date = is.readUTF();
		System.out.println(date);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e1.printStackTrace();
		}

	}
}