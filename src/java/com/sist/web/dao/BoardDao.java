package com.sist.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sist.common.util.StringUtil;
import com.sist.web.db.DBManager;
import com.sist.web.model.Board;

public class BoardDao
{
	private static Logger logger = LogManager.getLogger(BoardDao.class);
	
	//게시물 리스트 조회
	public List<Board> boardList(Board search)
	{
		List<Board> list = new ArrayList<Board>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
				
		
		sql.append("SELECT BBS_SEQ, ");
		sql.append("       USER_ID, ");
		sql.append("       USER_NAME, ");
		sql.append("       USER_EMAIL, ");
		sql.append("       BBS_PWD, ");
		sql.append("       BBS_TITLE, ");
		sql.append("       BBS_CONTENT, ");
		sql.append("       BBS_READ_CNT, ");
		sql.append("       REG_DATE ");
		sql.append("  FROM (SELECT ROWNUM AS RNUM, ");
		sql.append("               BBS_SEQ, ");
		sql.append("               USER_ID, ");
		sql.append("               USER_NAME, ");
		sql.append("	           USER_EMAIL, ");
		sql.append("	           BBS_PWD, ");
		sql.append("	           BBS_TITLE, ");
		sql.append("	           BBS_CONTENT, ");
		sql.append("	           BBS_READ_CNT, ");
		sql.append("	           REG_DATE ");
		sql.append("	      FROM (SELECT A.BBS_SEQ, ");
		sql.append("			           A.USER_ID, ");
		sql.append("			           NVL(B.USER_NAME, '') USER_NAME, ");
		sql.append("			           NVL(B.USER_EMAIL, '') USER_EMAIL, ");
		sql.append("			           NVL(A.BBS_PWD, '') BBS_PWD, ");
		sql.append("			           NVL(A.BBS_TITLE, '') BBS_TITLE, ");
		sql.append("			           NVL(A.BBS_CONTENT, '') BBS_CONTENT, ");
		sql.append("			           NVL(A.BBS_READ_CNT, 0) BBS_READ_CNT, ");
		sql.append("			           NVL(TO_CHAR(A.REG_DATE, 'YYYY.MM.DD HH24:MI:SS'), '') REG_DATE ");
		sql.append("	              FROM TBL_BOARD A, TBL_USER B ");
		sql.append("	             WHERE A.USER_ID = B.USER_ID ");
		
		if(search != null)
		{
			if(!StringUtil.isEmpty(search.getBbsName()))
			{
				sql.append("	               AND B.USER_NAME LIKE '%' || ? || '%' ");
			}
			
			if(!StringUtil.isEmpty(search.getBbsTitle()))
			{
				sql.append("	               AND A.BBS_TITLE LIKE UPPER('%' || ? || '%' ");
			}
			
			if(!StringUtil.isEmpty(search.getBbsContent()))
			{
				sql.append("                   AND DBMS_LOB.INSTR(A.BBS_CONTENT, ?) > 0 ");
			}
		}
		
		
   		sql.append("		         ORDER BY A.BBS_SEQ DESC)) ");
   		
   		if(search != null)
   		{
		sql.append(" WHERE RNUM >= ? ");
		sql.append("   AND RNUM <= ? ");
   		}
   		
   		
		try
		{
			int idx = 0;
			
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			if(search != null)
			{
				if(!StringUtil.isEmpty(search.getBbsName()))
				{
					pstmt.setString(++idx, search.getBbsName());
				}
				
				if(!StringUtil.isEmpty(search.getBbsTitle()))
				{
					pstmt.setString(++idx, search.getBbsTitle());
				}
				
				if(!StringUtil.isEmpty(search.getBbsContent()))
				{
					pstmt.setString(++idx, search.getBbsContent());
				}
				
				pstmt.setLong(++idx, search.getStartRow());
				pstmt.setLong(++idx, search.getEndRow());
			}
			
			
			logger.debug("=====================================");
			logger.debug("sql : " + sql.toString());
			logger.debug("=====================================");
			
			rs = pstmt.executeQuery();
			
			while(rs.next())		//하나의 레코드를 불러올 때는 if썼지만 여기서는 while
			{
				Board board = new Board();
				
				board.setBbsSeq(rs.getLong("BBS_SEQ"));
				board.setUserId(rs.getString("USER_ID"));	
				board.setBbsName(rs.getString("USER_NAME"));	
				board.setBbsEmail(rs.getString("USER_EMAIL"));	
				board.setBbsPwd(rs.getString("BBS_PWD"));	
				board.setBbsTitle(rs.getString("BBS_TITLE"));	
				board.setBbsContent(rs.getString("BBS_CONTENT"));	
				board.setBbsReadCnt(rs.getInt("BBS_READ_CNT"));
				board.setRegDate(rs.getString("REG_DATE"));
					
				list.add(board);
			}
		}
		catch(Exception e)
		{
			logger.error("[BoardDao]boardList SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		
		return list;
	}
	
	
	
	//총 게시물 수 조회
	public long boardTotalCount(Board search)
	{
		long totalCount = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(A.BBS_SEQ) AS TOTAL_COUNT ");
		sql.append("  FROM TBL_BOARD A, TBL_USER B ");
		sql.append(" WHERE A.USER_ID = B.USER_ID ");
		
		if(search != null)
		{
			if(!StringUtil.isEmpty(search.getBbsName()))
			{
				sql.append("   AND B.USER_NAME LIKE '%' || ? || '%' ");
			}
			
			if(!StringUtil.isEmpty(search.getBbsTitle()))
			{
				sql.append("   AND A.BBS_TITLE LIKE '%' || ? || '%' ");
			}
			
			if(!StringUtil.isEmpty(search.getBbsContent()))
			{
				sql.append("   AND DBMS_LOB.INSTR(A.BBS_CONTENT, ?) > 0 ");
			}
		}

		
		try
		{
			int idx = 0;
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			if(search != null)
			{
				if(!StringUtil.isEmpty(search.getBbsName()))
				{
					pstmt.setString(++idx, search.getBbsName());
				}
				
				if(!StringUtil.isEmpty(search.getBbsTitle()))
				{
					pstmt.setString(++idx, search.getBbsTitle());
				}
				
				if(!StringUtil.isEmpty(search.getBbsContent()))
				{
					pstmt.setString(++idx, search.getBbsContent());
				}
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				totalCount = rs.getLong("TOTAL_COUNT");
			}
		}
		catch(Exception e)
		{
			logger.error("[BoardDao]boardTotalCount SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		return totalCount;
	}
	
	//게시물 등록
	public int boardInsert(Board board)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();

		sql.append("INSERT INTO TBL_BOARD ");
		sql.append(" (BBS_SEQ, USER_ID, BBS_NAME, BBS_EMAIL, BBS_PWD, BBS_TITLE, BBS_CONTENT, BBS_READ_CNT, REG_DATE) ");
		sql.append("VALUES ");
		sql.append(" (?, ?, ?, ?, ?, ?, ?, 0, SYSDATE) ");
		
		
		logger.debug("=====================================");
		logger.debug("sql : " + sql.toString());
		logger.debug("=====================================");
		
		try
		{
			int idx = 0;
			long bbsSeq = 0;
			
			conn = DBManager.getConnection();
			
			bbsSeq = newBbsSeq(conn);
			board.setBbsSeq(bbsSeq);
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(++idx, board.getBbsSeq());
			pstmt.setString(++idx, board.getUserId());
			pstmt.setString(++idx, board.getBbsName());
			pstmt.setString(++idx, board.getBbsEmail());
			pstmt.setString(++idx, board.getBbsPwd());
			pstmt.setString(++idx, board.getBbsTitle());
			pstmt.setString(++idx, board.getBbsContent());
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[BoardDao] boardInsert SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
				
		return count;
	}
	
	//시퀀스 조회
	private long newBbsSeq(Connection conn)	//connection 객체가 매개변수로 들어왔다는 건 매개변수 정의할 필요 ㄴ
	{
		long bbsSeq = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT SEQ_BOARD_SEQ.NEXTVAL FROM DUAL ");
		
		try
		{
			pstmt = conn.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				bbsSeq = rs.getLong(1);
			}
		}
		catch(Exception e)
		{
			logger.error("[BoardDao] newBbsSeq SQLException e", e);
		}
		finally
		{
			DBManager.close(rs, pstmt); 			//conn은 절대 닫으면 안 됨
		}
		
		return bbsSeq;
	}
	
	//상세 게시글 조회
	public Board boardSelect(long bbsSeq)
	{
		Board board = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT A.BBS_SEQ, ");
		sql.append("       A.USER_ID, ");
		sql.append("       NVL(B.USER_NAME, '') AS USER_NAME, ");
		sql.append("       NVL(B.USER_EMAIL, '') AS USER_EMAIL, ");
		sql.append("       NVL(A.BBS_PWD, '') AS BBS_PWD, ");
		sql.append("       NVL(A.BBS_TITLE, '') AS BBS_TITLE, ");
		sql.append("       NVL(A.BBS_CONTENT, '') AS BBS_CONTENT, ");
		sql.append("       NVL(A.BBS_READ_CNT, '') AS BBS_READ_CNT, ");
		sql.append("       NVL(TO_CHAR(A.REG_DATE, 'YYYY.MM.DD HH24:MI:SS'), '') AS REG_DATE ");
		sql.append("	  FROM TBL_BOARD A, TBL_USER B ");
		sql.append("	 WHERE A.BBS_SEQ = ? ");
		sql.append("   AND A.USER_ID = B.USER_ID ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt= conn.prepareStatement(sql.toString());
			
			pstmt.setLong(1, bbsSeq);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				board = new Board();
				
				board.setBbsSeq(rs.getLong("BBS_SEQ"));   
			    board.setUserId(rs.getString("USER_ID"));   
			    board.setBbsName(rs.getString("USER_NAME"));  
			    board.setBbsEmail(rs.getString("USER_EMAIL"));   
			    board.setBbsPwd(rs.getString("BBS_PWD"));   
			    board.setBbsTitle(rs.getString("BBS_TITLE"));   
			    board.setBbsContent(rs.getString("BBS_CONTENT"));   
			    board.setBbsReadCnt(rs.getInt("BBS_READ_CNT"));   
			    board.setRegDate(rs.getString("REG_DATE"));  
				
			}
		}
		catch(Exception e)
		{
			logger.error("[BoardDao] boardSelect SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return board;
	}
	
	//게시물 조회수 증가
	public int boardReadCntPlus(long bbsSeq)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE TBL_BOARD ");
		sql.append("   SET BBS_READ_CNT = BBS_READ_CNT + 1 ");
		sql.append(" WHERE BBS_SEQ = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(1, bbsSeq);
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[BoardDao] boardReadCntPlus SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//게시물 삭제
	public int boardDelete(long bbsSeq)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("DELETE FROM TBL_BOARD ");
		sql.append(" WHERE BBS_SEQ = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, bbsSeq);
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[BoardDao] boardDelete SQLExceptioin", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		return count;
	}
	
	//게시물 수정
	public int boardUpdate(Board board)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE TBL_BOARD ");
		sql.append("   SET BBS_TITLE = ?, ");
		sql.append("       BBS_CONTENT = ? ");
		sql.append(" WHERE BBS_SEQ = ? ");
		
		try
		{
			int idx = 0;
			
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(++idx, board.getBbsTitle());
			pstmt.setString(++idx, board.getBbsContent());
			pstmt.setLong(++idx, board.getBbsSeq());
			
			
			count = pstmt.executeUpdate();
			
			logger.debug(sql.toString());
		}
		catch(Exception e)
		{
			logger.error("[BoardDao] boardUpdate SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
}
