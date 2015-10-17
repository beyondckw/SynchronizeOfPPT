package com.newppt.android.entity;


import java.io.Serializable;

public class DMT implements Serializable{

	private int currentPage;
	private  int page;
	private  String filename;
	
	public String getFilename()
	{
		return filename;
	}
	
	public void setFilename(String name)
	{
		this.filename=name;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}
	
	public int getPages()
	{
		return page;
	}
	
	public void setCurrentPage(int currentPage)
	{
		this.currentPage=currentPage;
	}
	
	public void setPage(int page) 
	{
		this.page=page;
	}
}
