package com.sist.web.proj;

import java.io.Serializable;

public class Like implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long boaNum;
    private String memId;
    
    public Like()
    {
    	boaNum = (long)0;
    	memId = "";
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
    
}
