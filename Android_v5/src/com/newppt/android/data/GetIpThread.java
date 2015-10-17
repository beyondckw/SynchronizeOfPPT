package com.newppt.android.data;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.os.Handler;
import android.os.Message;

public class GetIpThread extends Thread {
	Handler dhanHandler;
	String ipString;

	public GetIpThread(Handler handler) {
		this.dhanHandler = handler;
	}

	@Override
	public void run() {
		System.out.println("-------1----->");
		getIP();
		Message msg = new Message();
		msg.what = 0x123;
		msg.obj = ipString;
		dhanHandler.sendMessage(msg);
	}

	private void getIP() {
		try {
			System.out.println("------21------>");
			DatagramSocket dSocket = new DatagramSocket(8989);
			byte[] by = new byte[1024];
			DatagramPacket packet = new DatagramPacket(by, by.length);
			System.out.println("------2------>");
			try {
				dSocket.receive(packet);
				ipString = new String(packet.getData(), 0, packet.getLength());
				dSocket.close();
				// ip.setText(ipString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
