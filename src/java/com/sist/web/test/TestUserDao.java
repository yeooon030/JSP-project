package com.sist.web.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sist.web.dao.UserDao;
import com.sist.web.db.DBManager;
import com.sist.web.model.User;

public class TestUserDao {
	private static Logger logger = LogManager.getLogger(UserDao.class);
	
	//사용자 조회
	public TestUser userSelect(String userId)
	{
		TestUser user = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT USER_ID, ");
		sql.append("       NVL(USER_PWD, '') USER_PWD, ");
		sql.append("       NVL(USER_NAME, '') USER_NAME, ");
		sql.append("       NVL(USER_EMAIL, '') USER_EMAIL, ");
		sql.append("       NVL(STATUS, 'N') STATUS, ");
		sql.append("       NVL(TO_CHAR(REG_DATE,'YYYY.MM.DD HH24:MISS:SS'),'') REG_DATE ");
		sql.append("  FROM TEST_USER ");
		sql.append(" WHERE USER_ID = ? ");
		
	
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				user = new TestUser();
				
			    user.setUserId(rs.getString("USER_ID"));
			    user.setUserPwd(rs.getString("USER_PWD"));
			    user.setUserName(rs.getString("USER_NAME"));
			    user.setUserEmail(rs.getString("USER_EMAIL"));
			    user.setStatus(rs.getString("STATUS"));
			    user.setRegDate(rs.getString("REG_DATE"));
			}
		}
		catch(Exception e)
		{
			logger.debug("[TestUserDao] userSelect SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return user;
	}
	
	//중복 아이디 체크
	public int userIdSelectCount(String userId)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(USER_ID) CNT ");
		sql.append("  FROM TEST_USER ");
		sql.append(" WHERE USER_ID = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				count = rs.getInt("CNT");
			}
		}
		catch(Exception e)
		{
			logger.error("[TestUserDao] userIdSelectCount SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return count;
	}
}
