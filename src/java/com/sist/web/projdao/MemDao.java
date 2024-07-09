package com.sist.web.projdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sist.web.db.DBManager;
import com.sist.web.proj.Mem;

public class MemDao{

	private static Logger logger = LogManager.getLogger(MemDao.class);
	
	//사용자 조회
	public Mem selectMem(String memId)
	{
		Mem mem = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT MEM_ID, ");
		sql.append("       NVL(MEM_PWD, '') MEM_PWD, ");
		sql.append("       NVL(MEM_NAME, '') MEM_NAME, ");
		sql.append("       NVL(MEM_EMAIL, '') MEM_EMAIL, ");
		sql.append("       NVL(MEM_STA, 'N') MEM_STA, ");
		sql.append("       NVL(TO_CHAR(REG_DATE, 'YYYY.MM.DD HH24:MI:SS'), '') REG_DATE ");
		sql.append("  FROM MEM_TBL ");
		sql.append(" WHERE MEM_ID = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, memId);
			rs = pstmt.executeQuery();
			
			
			if(rs.next())
			{
				mem = new Mem();
				
				mem.setMemId(rs.getString("MEM_ID"));
				mem.setMemPwd(rs.getString("MEM_PWD"));
				mem.setMemName(rs.getString("MEM_NAME"));
				mem.setMemEmail(rs.getString("MEM_EMAIL"));
				mem.setMemSta(rs.getString("MEM_STA"));
				mem.setRegDate(rs.getString("REG_DATE"));
			}
			
		}
		catch(Exception e)
		{
			logger.error("[MemDao] SelectMem SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return mem;
	}
	
	//중복 아이디 체크
	public int checkMemId(String memId)
	{
		int count = 0;
		
		Connection conn = null;;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(MEM_ID) CNT ");
		sql.append("  FROM MEM_TBL ");
		sql.append(" WHERE MEM_ID = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, memId);
			
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				count = rs.getInt("CNT");
			}
		}
		catch(Exception e)
		{
			logger.error("[MemDao] checkMemId SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return count;
	}
	
	//회원가입
	public int insertMem(Mem mem)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO MEM_TBL ");
		sql.append("(MEM_ID, MEM_PWD, MEM_NAME, MEM_EMAIL, MEM_STA, REG_DATE) ");
		sql.append("VALUES (?, ?, ?, ?, ?, SYSDATE) ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			int idx = 0;
			
			pstmt.setString(++idx, mem.getMemId());
			pstmt.setString(++idx, mem.getMemPwd());
			pstmt.setString(++idx, mem.getMemName());
			pstmt.setString(++idx, mem.getMemEmail());
			pstmt.setString(++idx, mem.getMemSta());
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[MemDao] insertMem SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//회원정보 수정
	public int updateMem(Mem mem)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE MEM_TBL ");
		sql.append("   SET MEM_PWD = ?, ");
		sql.append("       MEM_NAME = ?, ");
		sql.append("       MEM_EMAIL = ? ");
		sql.append(" WHERE MEM_ID = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			int idx = 0;
			
			pstmt.setString(++idx, mem.getMemPwd());
			pstmt.setString(++idx, mem.getMemName());
			pstmt.setString(++idx, mem.getMemEmail());
			pstmt.setString(++idx, mem.getMemId());
			
			count = pstmt.executeUpdate();
//			System.out.println(sql);
		}
		catch(Exception e)
		{
			logger.debug("[MemDao] updateMem SQLExeption", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//회원탈퇴(정보유지, 회원상태 : 'X')
	public int deleteMem(String memId)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE MEM_TBL ");
		sql.append("   SET MEM_STA = 'X' ");
		sql.append(" WHERE MEM_ID = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1,memId);

			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[MemDao] deleteMem SQLExceptoin", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		return count;
	}
}
