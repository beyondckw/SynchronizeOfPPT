package com.newppt.android.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class ImageBytes {
	String ip;
	int  port = 8889;
	String typeString = null;

	public ImageBytes(String ip,String typeString) {
		this.ip = ip;
		this.typeString = typeString;
		
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
