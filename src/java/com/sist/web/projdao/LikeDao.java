package com.sist.web.projdao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sist.web.db.DBManager;

public class LikeDao {

	private static Logger logger = LogManager.getLogger(LikeDao.class);

	public int likeBoa(long boaNum, String memId)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO LIKE_TBL ");
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
			logger.error("[LikeDao] likeBoa SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return -1;
	}
	
}
