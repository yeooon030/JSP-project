package com.sist.web.test;

import java.io.Serializable;

public class TestPaging implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long totalCount;	//총 게시물 수
	private long totalPage;		//총 페이지 수
	private long startRow;		//게시물 시작 rownum(oracle)
	private long endRow;		//게시물 끝 rownum(oracle)
	private long listCount;		//한 페이지에 게시물 수
	private long pageCount;		//페이징 범위 수
	private long curPage;		//현재 페이지
	
	private long startPage;		//시작 페이지 번호
	private long endPage;		//종료 페이지 번호
	
	private long totalBlock;	//총 블럭 수
	private long curBlock;		//현재 블럭
	
	private long prevBlockPage;	//이전 블럭 페이지
	private long nextBlockPage;	//다음 블럭 페이지
	
	private long startNum;		//게시글 시작 번호(desc)
	
	
	public TestPaging(long totalCount, long listCount, long pageCount, long curPage)
	{
		this.totalCount = totalCount;
		this.listCount = listCount;
		this.pageCount = pageCount;
		this.curPage = curPage;
		
		if(totalCount > 0)
		{
			pagingProc();
		}
	}
	
	
	
	public long getTotalCount() {
		return totalCount;
	}



	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}



	public long getTotalPage() {
		return totalPage;
	}



	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
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



	public long getListCount() {
		return listCount;
	}



	public void setListCount(long listCount) {
		this.listCount = listCount;
	}



	public long getPageCount() {
		return pageCount;
	}



	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}



	public long getCurPage() {
		return curPage;
	}



	public void setCurPage(long curPage) {
		this.curPage = curPage;
	}



	public long getStartPage() {
		return startPage;
	}



	public void setStartPage(long startPage) {
		this.startPage = startPage;
	}



	public long getEndPage() {
		return endPage;
	}



	public void setEndPage(long endPage) {
		this.endPage = endPage;
	}



	public long getTotalBlock() {
		return totalBlock;
	}



	public void setTotalBlock(long totalBlock) {
		this.totalBlock = totalBlock;
	}



	public long getCurBlock() {
		return curBlock;
	}



	public void setCurBlock(long curBlock) {
		this.curBlock = curBlock;
	}



	public long getPrevBlockPage() {
		return prevBlockPage;
	}



	public void setPrevBlockPage(long prevBlockPage) {
		this.prevBlockPage = prevBlockPage;
	}



	public long getNextBlockPage() {
		return nextBlockPage;
	}



	public void setNextBlockPage(long nextBlockPage) {
		this.nextBlockPage = nextBlockPage;
	}



	public long getStartNum() {
		return startNum;
	}



	public void setStartNum(long startNum) {
		this.startNum = startNum;
	}



	//페이징 계산 프로세스
	private void pagingProc()
	{
		//총 페이지 수 구하기
		totalPage = (long)Math.ceil((double)totalCount / listCount);
		
		System.out.println("============================");
		System.out.println("totalCount : " + totalCount + ", listCount : " + listCount);
		System.out.println("totalPage : " + totalPage);
		System.out.println("============================");
		
		//총 블럭 수
		totalBlock = (long)Math.ceil((double)totalPage / pageCount);
		//현재 블럭 구하기
		curBlock = (long)Math.ceil((double)curPage / pageCount);
		
		//시작 페이지
		startPage = ((curBlock - 1) * pageCount) + 1;
		//끝 페이지
		endPage = (startPage + pageCount) - 1;
		
		//마지막 페이지 보정
		//총 페이지보다 끝 페이지가 크다면 총 페이지를 마지막 페이지로 변환
		if(endPage > totalPage)
		{
			endPage = totalPage;
		}
		
		//시작 rownum(oracle)
		startRow = ((curPage - 1) * listCount) + 1;
		//끝 rownum(oracle)
		endRow = (startRow + listCount) - 1;
		
		//게시물 시작번호
		startNum = totalCount - ((curPage - 1) * listCount);
		
		//이전 블럭 페이지 번호
		if(curBlock > 1)
		{
			prevBlockPage = startPage - 1;
		}
		
		//다음 블럭 페이지
		if(curBlock < totalBlock)
		{
			nextBlockPage = endPage + 1;
		}
	} 

	
}