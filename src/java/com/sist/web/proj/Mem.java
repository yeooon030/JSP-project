package com.sist.web.proj;

import java.io.Serializable;

public class Mem implements Serializable{

	private static final long serialVersionUID = 1L;

	private String memId;
	private String memPwd;
	private String memName;
	private String memEmail;
	private String memSta;
	private String regDate;
	
	private long startRow;			
	private long endRow;
	
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

	public String getMemId() {
		return memId;
	}
	
	public void setMemId(String memId) {
		this.memId = memId;
	}
	
	public String getMemPwd() {
		return memPwd;
	}
	
	public void setMemPwd(String memPwd) {
		this.memPwd = memPwd;
	}
	
	public String getMemName() {
		return memName;
	}
	
	public void setMemName(String memName) {
		this.memName = memName;
	}
	
	public String getMemEmail() {
		return memEmail;
	}
	
	public void setMemEmail(String memEmail) {
		this.memEmail = memEmail;
	}
	
	public String getMemSta() {
		return memSta;
	}
	
	public void setMemSta(String memSta) {
		this.memSta = memSta;
	}
	
	public String getRegDate() {
		return regDate;
	}
	
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
	
	
}
