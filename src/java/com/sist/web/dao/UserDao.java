package com.sist.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sist.web.db.DBManager;
import com.sist.web.model.User;

public class UserDao 
{
	private static Logger logger = LogManager.getLogger(UserDao.class);
	
	//사용자 조회
	public User userSelect(String userId)
	{
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT USER_ID, "); 
		sql.append("       NVL(USER_PWD, '') USER_PWD, ");
		sql.append("       NVL(USER_NAME, '') USER_NAME, ");
		sql.append("       NVL(USER_EMAIL, '') USER_EMAIL, ");
		sql.append("       NVL(STATUS, 'N') STATUS, "); 
		sql.append("       NVL(TO_CHAR(REG_DATE, 'YYYY.MM.DD HH24:MISS:SS'), '') REG_DATE ");
		sql.append("  FROM TBL_USER ");
		sql.append(" WHERE USER_ID = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, userId);	//인덱스 1부터 시작
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				user = new User();
				
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
			logger.error("[UserDao] userSelect SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}

		
		return user;
	}
	
	//아이디 존재 확인
	public int userIdSelectCount(String userId)
	{
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		
		sql.append("SELECT COUNT(USER_ID) CNT ");
		sql.append("  FROM TBL_USER ");
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
			logger.error("[UserDao] userIdSelectCount SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return count;
	}
	
	//사용자 등록
	public int userInsert(User user)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO TBL_USER ");
		sql.append(" (USER_ID, USER_PWD, USER_NAME, USER_EMAIL, STATUS, REG_DATE) ");
		sql.append(" VALUES (?, ?, ?, ?, ?, SYSDATE) ");
		//사용하다가 컬럼이 추가될 수도 있으므로 insert문에서 컬럼명 다 적기
		
		try
		{
			int idx = 0;
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(++idx, user.getUserId());
			pstmt.setString(++idx, user.getUserPwd());
			pstmt.setString(++idx, user.getUserName());
			pstmt.setString(++idx, user.getUserEmail());
			pstmt.setString(++idx, user.getStatus());
			//인덱스 값을 숫자로 적으면 열 추가 시 숫자를 일일이 변경하기 번거로우므로 인덱스 변수 이용해서 전치증가 연산자 사용
			
			count = pstmt.executeUpdate();
			
		}
		catch(Exception e)
		{
			logger.error("[UserDao userInsert SQLException]", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//사용자 정보 수정
	public int userUpdate(User user)
	{
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		
		sql.append("UPDATE TBL_USER ");
		sql.append("   SET USER_PWD = ?, ");
		sql.append("       USER_NAME = ?, ");
		sql.append("       USER_EMAIL = ? ");
		sql.append(" WHERE USER_ID = ? ");
		
		try
		{
			int idx = 0;
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(++idx, user.getUserPwd());
			pstmt.setString(++idx, user.getUserName());
			pstmt.setString(++idx, user.getUserEmail());
			pstmt.setString(++idx, user.getUserId());
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[UserDao]userUpdate SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
			
		return count;
	}
	
}







