package com.newppt.android.data;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.newppt.android.entity.MyPath;
import com.newppt.android.ui.AndroidPPT;

class SendFileThread extends Thread {
	Socket s = null;
	AndroidPPT sp ;

	public SendFileThread(Socket s ,AndroidPPT sp ) {
		this.s = s;
		this.sp = sp;
	}

	@Override
	public void run() {
		senFileToClient(s);
	}
	
		/**
		 * send File to Client
		 * @param s
		 */
		private void senFileToClient(Socket s) {

			DataInputStream is = null;
			OutputStream os = null;
			String fileName = null;
			String path = null;
			FileInputStream fins = null;
			try {
				
				is = new DataInputStream(s.getInputStream());
				String typeString = is.readUTF();
				if (typeString.equals("PPT")) {
					fileName = sp._fileName;
					path = sp._Path;
				}
				else {
					MyPath mypath = new MyPath();
					path = mypath.returnJpgPath();
					fileName = mypath.returnFileName();
					
					os = s.getOutputStream();
					sendImageBytes(os);
					return ;
					
				}

				os = s.getOutputStream();
				System.out.println("将文件名:" + fileName + "传输过去");
				// 先将文件名传输过去
				os.write(fileName.getBytes());
				System.out.println("开始传输文件");
				// 将文件传输过去
				// 获取文件
//				fins = new FileInputStream(path);
//				int data;
//				byte by[] = new byte[1024];
//
//				// 通过fins读取文件，并通过os将文件传输
//				while (-1 != (data = fins.read(by))) {
//
//					os.write(by, 0, data);
//					// os.write(data);
//				}
				
				sendFileBytes(os);
				System.out.println("文件传输结束");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (fins != null)
						fins.close();
					if (os != null)
						os.close();
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void sendImageBytes(OutputStream os) throws Exception{
			
			os.write(sp.getImageBytes());
		}
		
		private void sendFileBytes (OutputStream os) throws Exception {
			os.write(sp.getFileBytes());
		}
}
