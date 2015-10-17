package com.newppt.android.entity;

public class MyPath {
	
	String rootPath = "D:\\AndroidPPT" ;
	String jpgPath = rootPath + "\\ppt.jpg";
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
