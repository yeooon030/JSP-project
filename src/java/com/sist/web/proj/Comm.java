package com.sist.web.proj;

import java.io.Serializable;

public class Comm implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long commNum;
	private long boaNum;
	private String memId;
	private String memName;
	private String commDate;
	private String commText;
	
	public long getCommNum() {
		return commNum;
	}
	public void setCommNum(long commNum) {
		this.commNum = commNum;
	}
	public long getBoaNum() {
		return boaNum;
	}
	public void setBoaNum(long boaNum) {
		this.boaNum = boaNum;
	}
	public String getMemId() {
		return memId;
	}
	public void setMemId(String memId) {
		this.memId = memId;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getCommDate() {
		return commDate;
	}
	public void setCommDate(String commDate) {
		this.commDate = commDate;
	}
	public String getCommText() {
		return commText;
	}
	public void setCommText(String commText) {
		this.commText = commText;
	}
}
