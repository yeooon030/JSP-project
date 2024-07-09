package com.sist.web.projdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sist.web.db.DBManager;
import com.sist.web.proj.Comment;

public class CommentDao {
	
	private static Logger logger = LogManager.getLogger(CommentDao.class);

	//댓글 리스트 가져오기
	public List<Comment> boardList(long boaNum)
	{
		List<Comment> list = new ArrayList<Comment>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COMM_NUM, BOA_NUM, MEM_ID, MEM_NAME, COMM_DATE, COMM_TEXT ");
		sql.append("  FROM (SELECT ROWNUM AS RNUM, COMM_NUM, BOA_NUM, MEM_ID, MEM_NAME, COMM_DATE, COMM_TEXT ");
		sql.append("          FROM (SELECT A.COMM_NUM, ");
		sql.append("                       B.BOA_NUM, ");
		sql.append("                       A.MEM_ID, ");
		sql.append("                       NVL(A.MEM_NAME, '') MEM_NAME, ");
		sql.append("                       NVL(TO_CHAR(A.COMM_DATE, 'YYYY.MM.DD HH24:MI:SS'), '') COMM_DATE,  ");
		sql.append("                       NVL(A.COMM_TEXT,'') COMM_TEXT ");
		sql.append("                  FROM COMM_TBL A, BOA_TBL B ");
		sql.append("                 WHERE A.BOA_NUM = B.BOA_NUM ");
		sql.append("                   AND B.BOA_NUM = ? ");
		sql.append("                 ORDER BY A.COMM_NUM)) ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(1, boaNum);
			
			logger.debug("=====================================");
			logger.debug("sql : " + sql.toString());
			logger.debug("=====================================");
			
			rs = pstmt.executeQuery();
				
			while(rs.next())
			{
				Comment comm = new Comment();
				comm.setCommNum(rs.getLong("COMM_NUM"));
				comm.setBoaNum(rs.getLong("BOA_NUM"));
				comm.setMemId(rs.getString("MEM_ID"));
				comm.setMemName(rs.getString("MEM_NAME"));
				comm.setCommDate(rs.getString("COMM_DATE"));
				comm.setCommText(rs.getString("COMM_TEXT"));
				
				list.add(comm);
			}
		}
		catch(Exception e)
		{
			logger.error("[CommDao] boardList SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		
		return list;
	}
	
	//시퀀스 조회
	private long newCommSeq(Connection conn)
	{
		long commSeq = 0;

		PreparedStatement pstmt= null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT SEQ_COMM_SEQ.NEXTVAL FROM DUAL ");
		
		try
		{
			pstmt = conn.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				commSeq = rs.getLong(1);
			}
		}
		catch(Exception e)
		{
			logger.error("[CommDao] newCommSeq SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt);
		}
		return commSeq;
	}
	
	//댓글 등록
	public int writeComment(Comment comm)
	{
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO COMM_TBL ");
		sql.append("(COMM_NUM, BOA_NUM, MEM_ID, MEM_NAME, COMM_DATE, COMM_TEXT)  ");
		sql.append("VALUES (?, ?, ?, ?, SYSDATE, ?) ");
		
		try
		{
			conn = DBManager.getConnection();
			
			int idx = 0;
			long commSeq = newCommSeq(conn);
			//comm.set(commSeq);
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(++idx, commSeq);
			pstmt.setLong(++idx, comm.getBoaNum());
			pstmt.setString(++idx, comm.getMemId());
			pstmt.setString(++idx, comm.getMemName());
			pstmt.setString(++idx, comm.getCommText());
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[CommDao] writeComment SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//댓글 수
	public long countComm(long boaNum)
	{
		long count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(COMM_NUM) CNT ");
		sql.append("  FROM COMM_TBL ");
		sql.append(" WHERE BOA_NUM = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(1, boaNum);
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[CommDao] countComm SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//댓글 삭제
	public int deleteComment(long commNum) {
	     int count = 0;
	     Connection conn = null;
	     PreparedStatement pstmt = null;
	     StringBuilder sql = new StringBuilder();
	     sql.append("DELETE FROM COMM_TBL ");
	     sql.append(" WHERE COMM_NUM = ? ");
	     try 
	     {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql.toString());
	        pstmt.setLong(1, commNum);
	        count = pstmt.executeUpdate();
	     } 
	     catch (Exception e) 
	     {
	        logger.error("[CommentDao] deleteComment SQLException", e);
	     } 
	     finally 
	     {
	        DBManager.close(pstmt, conn);
	     }
	
	     return count;
	}

	//댓글 전체 삭제
	 public int deleteAllComment(long boaNum) {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM COMM_TBL ");
		sql.append(" WHERE BOA_NUM = ? ");
		
		try 
		{
		    conn = DBManager.getConnection();
		    pstmt = conn.prepareStatement(sql.toString());
		    pstmt.setLong(1, boaNum);
		    count = pstmt.executeUpdate();
		} 
		catch (Exception e) 
		{
			logger.error("[CommentDao] deleteAllComment SQLException", e);
		} 
		finally 
		{
		    DBManager.close(pstmt, conn);
		}

	     return count;
	 }
}