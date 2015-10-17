package com.newppt.android.data;

import java.io.FileOutputStream;
import java.io.IOException;

public class SaveJpgThread extends Thread {

	private byte[] buffer;
	String filePath;

	public SaveJpgThread(byte[] buffer, String filePath) {
		// TODO Auto-generated constructor stub
		this.buffer = buffer;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			FileOutputStream out = new FileOutputStream(filePath);

			out.write(buffer);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
