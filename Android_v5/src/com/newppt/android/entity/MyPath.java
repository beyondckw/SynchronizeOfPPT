package com.newppt.android.entity;

import android.os.Environment;

public class MyPath {
	
	public  static String rootPath = Environment.getExternalStorageDirectory() + "/"
			+ "AndroidPPT/";
	public static String savePptFilePath = Environment.getExternalStorageDirectory() + "/"
			+ "AndroidPPTFile/";
	public static String jpgPath = rootPath + "ppt.jpg";
	
	public static String pptJpg = "/ppt";
	public static String noteJpg = "/note";
	
	String fileName ="ppt.jpg";

	public MyPath() {
		// TODO Auto-generated constructor stub
	}
	
	public String returnRootPath() {
		return rootPath;
	}
	
	public String returnJpgPath() {
		return jpgPath;
	}
	
	public String returnFileName() {
		return fileName;
	}

}
