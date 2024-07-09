package com.sist.web.projdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sist.web.db.DBManager;

public class LikeDao {

	private static Logger logger = LogManager.getLogger(LikeDao.class);
	
	//좋아요 추가
	public int likeDao(long boaNum, String memId)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO LIKE_TBL ");
		sql.append("(BOA_NUM, MEM_ID) ");
		sql.append("VALUES(?, ?) ");
			
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(1, boaNum);
			pstmt.setString(2, memId);
			
			return pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[LikeDao] likeDao SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return -1;
	}
	
	//좋아요 여부
	public int likeExistDao(long boaNum, String memId) {
	      Connection conn = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      StringBuilder sql = new StringBuilder();
	      sql.append("SELECT COUNT(*) CNT ");
	      sql.append("  FROM LIKE_TBL ");
	      sql.append(" WHERE BOA_NUM = ? ");
	      sql.append("   AND  MEM_ID = ? ");

	      try {
	         conn = DBManager.getConnection();
	         pstmt = conn.prepareStatement(sql.toString());
	         int result = 0;
	         pstmt.setLong(1, boaNum);
	         pstmt.setString(2, memId);
	         rs = pstmt.executeQuery();
	         if (rs.next()) {
	            result = rs.getInt("CNT");
	         }

	         int var10 = result;
	         return var10;
	      } catch (Exception e) {
	         logger.error("[LikeDao] likeExistDao SQLException", e);
	      } finally {
	         DBManager.close(pstmt, conn);
	      }

	      return -1;
	   }
	
}
