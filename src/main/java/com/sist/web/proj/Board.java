package com.sist.web.proj;

import java.io.Serializable;

public class Board implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long boaNum;
	private String memId;
	private String memPwd;
	private String memName;
	private String boaTitle;
	private String boaContent;
	private int boaCnt;
	private String regDate;
	private int boaLike;
	
	private long startRow;			
	private long endRow;
	
	
	public Board()
	{
		boaNum = 0;
		memId = "";
		memPwd = "";
		memName = "";
		boaTitle = "";
		boaContent = "";
		boaCnt = 0;
		regDate = "";
		boaLike = 0;
		
		startRow = 0;
		endRow = 0;
	}
	
	public long getStartRow() {
		return startRow;
	}

	public void setStartRow(long startRow) {
		this.startRow = startRow;
	}

	public long getEndRow() {
		return endRow;
	}

	public void setEndRow(long endRow) {
		this.endRow = endRow;
	}

	public long getBoaNum()
	{
		return boaNum;
	}

	public void setBoaNum(long boaNum)
	{
		this.boaNum = boaNum;
	}
	
	public String getMemId() 
	{
		return memId;
	}
	
	public void setMemId(String memId) 
	{
		this.memId = memId;
	}
	
	public String getMemPwd() 
	{
		return memPwd;
	}
	
	public void setMemPwd(String memPwd) 
	{
		this.memPwd = memPwd;
	}
	
	public String getMemName() 
	{
		return memName;
	}
	
	public void setMemName(String memName) 
	{
		this.memName = memName;
	}
	
	public String getBoaTitle() 
	{
		return boaTitle;
	}
	
	public void setBoaTitle(String boaTitle) 
	{
		this.boaTitle = boaTitle;
	}
	
	public String getBoaContent() 
	{
		return boaContent;
	}
	
	public void setBoaContent(String boaContent) 
	{
		this.boaContent = boaContent;
	}
	
	public int getBoaCnt() 
	{
		return boaCnt;
	}
	
	public void setBoaCnt(int boaCnt) 
	{
		this.boaCnt = boaCnt;
	}
	
	public String getRegDate() 
	{
		return regDate;
	}
	
	public void setRegDate(String regDate) 
	{
		this.regDate = regDate;
	}
	
	public int getBoaLike() 
	{
		return boaLike;
	}
	
	public void setBoaLike(int boaLike) 
	{
		this.boaLike = boaLike;
	}
			
}
