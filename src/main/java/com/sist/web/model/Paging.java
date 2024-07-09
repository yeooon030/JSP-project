package com.sist.web.model;

import java.io.Serializable;

public class Paging implements Serializable {

   private static final long serialVersionUID = 1L;
   
   private long totalCount;      //총 게시물 수
   private long totalPage;         //총 페이지 수
   private long startRow;         //게시물 시작 Row (Oracle rownum)
   private long endRow;         //게시물 끝 Row (Oracle rownum)
   private long listCount;         //한 페이지의 게시물 수
   private long pageCount;         //페이징 범위 수
   private long curPage;         //현재 페이지
   
   private long startPage;         //시작 페이지 번호
   private long endPage;         //끝 페이지 번호
   
   private long totalBlock;      //총 블럭 수
   private long curBlock;         //현재 블럭
   
   private long prevBlockPage;      //이전 블럭 페이지
   private long nextBlockPage;      //다음 블럭 페이지
   
   private long startNum;         //시작 번호 (게시물 번호 적용 desc) => 삭제해도 순서 완벽
   
   public Paging(long totalCount, long listCount, long pageCount, long curPage) {
      this.totalCount = totalCount;
      this.listCount = listCount;
      this.pageCount = pageCount;
      this.curPage = curPage;
      
      if (totalCount > 0) {
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
   private void pagingProc() {
      //총 페이지 수
      totalPage = (long)Math.ceil((double)totalCount / listCount);      //총 페이지 = 총 건수 / 한 페이지 건수
//      System.out.println("================");
//      System.out.println("totalCount : " + totalCount);
//      System.out.println("listCount : " + listCount);
//      System.out.println("totalPage : " + totalPage);
      
      
      //총 블럭 수
      totalBlock = (long)Math.ceil((double)totalPage / pageCount);      //총 블럭 = 총 페이지 / 페이징 범위
//      System.out.println("pageCount : " + pageCount);
//      System.out.println("totalBlock : " + totalBlock);
      
      //현재 블럭
      curBlock = (long)Math.ceil((double)curPage / pageCount);         //현재 블럭 = 현재 페이지 / 페이징 범위
//      System.out.println("curpage : " + curPage);
//      System.out.println("curBlock : " + curBlock);
      
      //시작페이지
      startPage = ((curBlock - 1) * pageCount) + 1;                  //시작페이지 = ((현재 블럭 - 1) * 페이징 범위) + 1
//      System.out.println("startPage : " + startPage);
      
      //끝페이지
      endPage = (startPage + pageCount) - 1;                        //끝페이지 = (시작페이지 + 페이징 범위) - 1
//      System.out.println("endPage : " + endPage);
      
      //마지막 페이지 보정
      //총 페이지 보다 끝 페이지가 크다면 총 페이지를 마지막 페이지로 변환
      if (endPage > totalPage) {                                 //끝 페이지 > 총 페이지
         endPage = totalPage;                                 //끝 페이지 = 총 페이지
      }
      
      //시작 rownum
      startRow = ((curPage - 1) * listCount) + 1;                  //시작Row = ((현재 페이지 - 1) * 한 페이지 게시물 수) + 1
//      System.out.println("startRow : " + startRow);
      
      //끝 rownum
      endRow = (startRow + listCount) - 1;                     //끝Row = (시작Row + 한 페이지 게시물 수) - 1
//      System.out.println("endRow : " + endRow);
      
      //게시물 시작 번호
      startNum = totalCount - ((curPage -1) * listCount);      //시작 번호 = 총 게시물 수 - ((현재 페이지 - 1) * 한 페이지 게시물 수)
//      System.out.println("startNum : " + startNum);
      
      //이전 블럭 페이지 번호
      if (curBlock > 1) {                              //현재 블럭 > 1
         prevBlockPage = startPage - 1;                  //이전 블럭 페이지 = 시작 페이지 번호 - 1
         System.out.println("prevBlockPage : " + prevBlockPage);
      }
      
      //다음 블럭 페이지 번호
      if (curBlock < totalBlock) {                     //현재 블럭 < 총 블럭 수
         nextBlockPage = endPage + 1;                  //다음 블락 페이지 = 끝 페이지 + 1
//         System.out.println("nextBlockPage : " + nextBlockPage);
      }
      
//      System.out.println("================");
   }
}