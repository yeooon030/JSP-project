package com.sist.web.projdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sist.common.util.StringUtil;
import com.sist.web.db.DBManager;
import com.sist.web.proj.Board;
import com.sist.web.proj.Member;

public class BoardDao {

	private static Logger logger = LogManager.getLogger(BoardDao.class);
	
	//게시물 리스트 조회
	public List<Board> boardList(Board search)
	{
		List<Board> list = new ArrayList<Board>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
				
		sql.append("SELECT BOA_NUM, MEM_ID, MEM_PWD, MEM_NAME, BOA_TITLE, ");
		sql.append("       BOA_CONTENT, BOA_CNT, REG_DATE, BOA_LIKE ");
		sql.append(" FROM (SELECT ROWNUM AS RNUM, BOA_NUM, MEM_ID, MEM_PWD, MEM_NAME, BOA_TITLE, ");
		sql.append("              BOA_CONTENT, BOA_CNT, REG_DATE, BOA_LIKE ");
		sql.append("         FROM (SELECT A.BOA_NUM, ");
		sql.append("                      A.MEM_ID, ");
		sql.append("                      NVL(A.MEM_PWD, '') MEM_PWD, ");
		sql.append("                      NVL(B.MEM_NAME, '') MEM_NAME, ");
		sql.append("                      NVL(A.BOA_TITLE, '') BOA_TITLE, ");
		sql.append("                      NVL(A.BOA_CONTENT, '') BOA_CONTENT, ");
		sql.append("                      NVL(A.BOA_CNT, 0) BOA_CNT, ");
		sql.append("                      NVL(TO_CHAR(A.REG_DATE, 'YYYY.MM.DD HH24:MI:SS'), '') REG_DATE, ");
		sql.append("                      (SELECT COUNT(C.BOA_NUM) FROM LIKE_TBL C WHERE A.BOA_NUM = C.BOA_NUM) BOA_LIKE ");
		sql.append("                 FROM BOA_TBL A, MEM_TBL B ");
		sql.append("                WHERE  A.MEM_ID = B.MEM_ID ");
		
		if(search != null)
		{
			if(!StringUtil.isEmpty(search.getMemName()))
			{
				sql.append("	               AND B.MEM_NAME LIKE '%' || ? || '%' ");
			}
			
			if(!StringUtil.isEmpty(search.getBoaTitle()))
			{
				sql.append("	               AND A.BOA_TITLE LIKE UPPER('%' || ? || '%') ");
			}
			
			if(!StringUtil.isEmpty(search.getBoaContent()))
			{
				sql.append("                   AND DBMS_LOB.INSTR(UPPER(A.BOA_CONTENT), ?) > 0 ");
			}
		}
				
		sql.append("				ORDER BY A.BOA_NUM DESC)) ");
		
		if(search != null)
		{
			sql.append("  WHERE RNUM >= ? ");
			sql.append("    AND RNUM <= ? ");
		}
		
		try
		{
			int idx = 0;
			
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			if(search != null)
			{
				if(!StringUtil.isEmpty(search.getMemName()))
				{
					pstmt.setString(++idx, search.getMemName());
				}
				if(!StringUtil.isEmpty(search.getBoaTitle()))
				{
					pstmt.setString(++idx, search.getBoaTitle());
				}
				if(!StringUtil.isEmpty(search.getBoaContent()))
				{
					pstmt.setString(++idx, search.getBoaContent());
				}
				pstmt.setLong(++idx, search.getStartRow());
				pstmt.setLong(++idx, search.getEndRow());
			}	
			
			logger.debug("=====================================");
			logger.debug("sql : " + sql.toString());
			logger.debug("=====================================");
			
			rs = pstmt.executeQuery();
				
			while(rs.next())
			{
				Board boa = new Board();
				
				
				boa.setBoaNum(rs.getLong("BOA_NUM"));
				boa.setMemId(rs.getString("MEM_ID"));
				boa.setMemPwd(rs.getString("MEM_PWD"));
				boa.setMemName(rs.getString("MEM_NAME"));
				boa.setBoaTitle(rs.getString("BOA_TITLE"));
				boa.setBoaContent(rs.getString("BOA_CONTENT"));
				boa.setBoaCnt(rs.getInt("BOA_CNT"));
				boa.setRegDate(rs.getString("REG_DATE"));
				boa.setBoaLike(rs.getInt("BOA_LIKE"));
					
				list.add(boa);
			}
			
		}
		catch(Exception e)
		{
			logger.error("[BoaDao] selectMem SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return list;
	}
	
	//총 게시물 조회
	public long totalBoaCount(Board search)
	{
		long totalCount = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT COUNT(A.BOA_NUM) TOTAL_CNT ");
		sql.append("  FROM BOA_TBL A, MEM_TBL B ");
		sql.append(" WHERE A.MEM_ID = B.MEM_ID ");
		
		if(search != null)
		{
			if(!StringUtil.isEmpty(search.getMemName()))
			{
				sql.append("   AND B.MEM_NAME LIKE '%' || ? || '%' ");
			}
			if(!StringUtil.isEmpty(search.getBoaTitle()))
			{
				sql.append("   AND A.BOA_TITLE LIKE UPPER('%' || ? || '%') ");
			}
			if(!StringUtil.isEmpty(search.getBoaContent()))
			{
				sql.append("   AND DBMS_LOB.INSTR(UPPER(A.BOA_CONTENT), ?) > 0 ");
			}
		}
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			int idx = 0;
			
			if(search != null)
			{
				if(!StringUtil.isEmpty(search.getMemName()))
				{
					pstmt.setString(++idx, search.getMemName());
				}
				if(!StringUtil.isEmpty(search.getBoaTitle()))
				{
					pstmt.setString(++idx, search.getBoaTitle());
				}
				if(!StringUtil.isEmpty(search.getBoaContent()))
				{
					pstmt.setString(++idx, search.getBoaContent());
				}
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				totalCount = rs.getLong("TOTAL_CNT");
			}
			
		}
		catch(Exception e)
		{
			logger.error("[BoaDao] totalBoaCount SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return totalCount;
	}
	
	//게시글 작성
	public int uploadBoa(Board boa)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO BOA_TBL ");
		sql.append("(BOA_NUM, MEM_ID, MEM_PWD, MEM_NAME, BOA_TITLE, BOA_CONTENT, BOA_CNT, REG_DATE ) ");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, 0, SYSDATE) ");
		
		logger.debug("=====================================");
		logger.debug("sql : " + sql.toString());
		logger.debug("=====================================");
		
		try
		{
			int idx = 0;
			long boaSeq = 0;
			
			conn = DBManager.getConnection();
			
			boaSeq = newBoaSeq(conn);
			boa.setBoaNum(boaSeq);
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(++idx, boa.getBoaNum());
			pstmt.setString(++idx, boa.getMemId());
			pstmt.setString(++idx, boa.getMemPwd());
			pstmt.setString(++idx, boa.getMemName());
			pstmt.setString(++idx, boa.getBoaTitle());
			pstmt.setString(++idx, boa.getBoaContent());

			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[BoaDao] uploadBoa SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//시퀀스 조회
	private long newBoaSeq(Connection conn)
	{
		long boaSeq = 0;

		PreparedStatement pstmt= null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT SEQ_BOA_SEQ.NEXTVAL FROM DUAL ");
		
		try
		{
			pstmt = conn.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				boaSeq = rs.getLong(1);
			}
		}
		catch(Exception e)
		{
			logger.error("[BoaDao] newBoaSeq SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt);
		}
		return boaSeq;
	}
	
	//게시글 작성
	public int writeBoard(Board boa)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO BOA_TBL ");
		sql.append("(BOA_NUM, MEM_ID, MEM_PWD, MEM_NAME, BOA_TITLE, BOA_CONTENT, BOA_CNT, REG_DATE ) ");
		sql.append("VALUES (?, ?, ?, ?, ?, ?, 0, SYSDATE ) ");
		
		try
		{
			conn = DBManager.getConnection();
			
			int idx = 0;
			long boaSeq = newBoaSeq(conn);
			boa.setBoaNum(boaSeq);
			
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(++idx, boaSeq);
			pstmt.setString(++idx, boa.getMemId());
			pstmt.setString(++idx, boa.getMemPwd());
			pstmt.setString(++idx, boa.getMemName());
			pstmt.setString(++idx, boa.getBoaTitle());
			pstmt.setString(++idx, boa.getBoaContent());
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[BoaDao] writeBoard SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//상세게시물 조회
	public Board viewBoa(long boaNum)
	{
		Board boa = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT A.BOA_NUM, ");
		sql.append("       A.MEM_ID, ");
		sql.append("       NVL(A.MEM_PWD, '') MEM_PWD, ");
		sql.append("       NVL(B.MEM_NAME, '') MEM_NAME, ");
		sql.append("       NVL(A.BOA_TITLE, '') BOA_TITLE, ");
		sql.append("       NVL(A.BOA_CONTENT, '') BOA_CONTENT, ");
		sql.append("       NVL(A.BOA_CNT, '') BOA_CNT, ");
		sql.append("       NVL(TO_CHAR(A.REG_DATE,'YYYY.MM.DD HH24:MI:SS'), '') REG_DATE, ");
		sql.append("       (SELECT COUNT(C.BOA_NUM) FROM LIKE_TBL C WHERE A.BOA_NUM = C.BOA_NUM) BOA_LIKE ");
		sql.append("  FROM BOA_TBL A, MEM_TBL B ");
		sql.append(" WHERE A.MEM_ID = B.MEM_ID ");
		sql.append("   AND BOA_NUM = ? ");
		
		try
		{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setLong(1, boaNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				boa = new Board();
				
				boa.setBoaNum(rs.getLong("BOA_NUM"));
				boa.setMemId(rs.getString("MEM_ID"));
				boa.setMemPwd(rs.getString("MEM_PWD"));
				boa.setMemName(rs.getString("MEM_NAME"));
				boa.setBoaTitle(rs.getString("BOA_TITLE"));
				boa.setBoaContent(rs.getString("BOA_CONTENT"));
				boa.setBoaCnt(rs.getInt("BOA_CNT"));
				boa.setRegDate(rs.getString("REG_DATE"));
				boa.setBoaLike(rs.getInt("BOA_LIKE"));
			}
			
			logger.debug(sql.toString());
			
		}
		catch(Exception e)
		{
			logger.error("[BoaDao] viewBoa SQLException", e);
		}
		finally
		{
			DBManager.close(rs, pstmt, conn);
		}
		
		return boa;
	}
	
	//조회수 증가
	public int readCntBoa(long boaNum)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE BOA_TBL ");
		sql.append("   SET BOA_CNT = BOA_CNT + 1 ");
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
			logger.error("[BoaDao] readCntBoa SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//게시물 삭제
	public int deleteBoa(long boaNum)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("DELETE FROM BOA_TBL ");
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
			logger.error("[BoaDao] deleteBoa SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		
		return count;
	}
	
	//게시물 수정
	public int updateBoa(Board boa)
	{
		int count = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE BOA_TBL ");
		sql.append("   SET BOA_TITLE = ?, ");
		sql.append("       BOA_CONTENT = ? ");
		sql.append(" WHERE BOA_NUM = ? ");
		
		try
		{
			int idx = 0;
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(++idx, boa.getBoaTitle());
			pstmt.setString(++idx, boa.getBoaContent());
			pstmt.setLong(++idx, boa.getBoaNum());
			
			count = pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			logger.error("[BoaDao] updateBoa SQLException", e);
		}
		finally
		{
			DBManager.close(pstmt, conn);
		}
		
		return count;
	}
	
	//회원관리 리스트 조회
		public List<Member> MemList(Member search)
		{
			List<Member> list = new ArrayList<Member>();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sql = new StringBuilder();
					
			sql.append("SELECT MEM_ID, MEM_NAME, MEM_EMAIL, MEM_STA, REG_DATE ");
			sql.append("  FROM (SELECT ROWNUM AS RNUM, MEM_ID, MEM_NAME, MEM_EMAIL, MEM_STA, REG_DATE ");
			sql.append("          FROM (SELECT MEM_ID, ");
			sql.append("                      NVL(MEM_NAME, '') MEM_NAME, ");
			sql.append("                      NVL(MEM_EMAIL, '') MEM_EMAIL, ");
            sql.append("		   			 (CASE ");
			sql.append("          			  WHEN MEM_STA LIKE 'Y' THEN '사용가능' ");
			sql.append("			          WHEN MEM_STA LIKE 'N' THEN '사용정지' ");
			sql.append("			          WHEN MEM_STA LIKE 'X' THEN '탈퇴' ");
			sql.append("			          ELSE '' ");
			sql.append(" 		              END ) MEM_STA, ");
			//sql.append("                      NVL(MEM_STA, '') MEM_STA, ");
			sql.append("                      NVL(TO_CHAR(REG_DATE, 'YYYY.MM.DD HH24:MI:SS'), '') REG_DATE ");
			sql.append("                  FROM MEM_TBL ");
			sql.append("				 WHERE MEM_ID NOT LIKE '%admin%' ");
			
			if(search != null)
			{
				if(!StringUtil.isEmpty(search.getMemId()))
				{
					sql.append("	               AND MEM_ID LIKE '%' || ? || '%' ");
				}
				
				if(!StringUtil.isEmpty(search.getMemName()))
				{
					sql.append("	               AND MEM_NAME LIKE '%' || ? || '%'  ");
				}
				
				if(!StringUtil.isEmpty(search.getMemSta()))
				{
					sql.append("                   AND MEM_STA LIKE '%' || ? || '%' ");
				}
			}
					
			sql.append("				ORDER BY REG_DATE)) ");
			
			if(search != null)
			{
				sql.append("  WHERE RNUM >= ? ");
				sql.append("    AND RNUM <= ? ");
			}
			
			try
			{
				int idx = 0;
				
				conn = DBManager.getConnection();
				pstmt = conn.prepareStatement(sql.toString());
				
				if(search != null)
				{
					if(!StringUtil.isEmpty(search.getMemId()))
					{
						pstmt.setString(++idx, search.getMemId());
					}
					if(!StringUtil.isEmpty(search.getMemName()))
					{
						pstmt.setString(++idx, search.getMemName());
					}
					if(!StringUtil.isEmpty(search.getMemSta()))
					{
						pstmt.setString(++idx, search.getMemSta());
					}
					pstmt.setLong(++idx, search.getStartRow());
					pstmt.setLong(++idx, search.getEndRow());
				}	
				
				logger.debug("=====================================");
				logger.debug("sql : " + sql.toString());
				logger.debug("=====================================");
				
				rs = pstmt.executeQuery();
					
				while(rs.next())
				{
					Member mem = new Member();
					
					mem.setMemId(rs.getString("MEM_ID"));
					mem.setMemName(rs.getString("MEM_NAME"));
					mem.setMemEmail(rs.getString("MEM_EMAIL"));
					mem.setMemSta(rs.getString("MEM_STA"));
					mem.setRegDate(rs.getString("REG_DATE"));
					
					list.add(mem);
				}
				
			}
			catch(Exception e)
			{
				logger.error("[BoaDao] MemList SQLException", e);
			}
			finally
			{
				DBManager.close(rs, pstmt, conn);
			}
			
			return list;
		}
		
		//총 회원수 조회
		public long totalMemCount(Member search)
		{
			long totalCount = 0;
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sql = new StringBuilder();
			
			sql.append("SELECT COUNT(MEM_ID) CNT ");
			sql.append("  FROM MEM_TBL ");
			sql.append(" WHERE MEM_ID NOT LIKE '%admin%' ");
			
			if(search != null)
			{
				if(!StringUtil.isEmpty(search.getMemId()))
				{
					sql.append("     AND MEM_ID LIKE '%' || ? || '%' ");
				}
				if(!StringUtil.isEmpty(search.getMemName()))
				{
					sql.append("     AND MEM_NAME LIKE '%' || ? || '%' ");
				}
				if(!StringUtil.isEmpty(search.getMemSta()))
				{
					sql.append("    AND MEM_STA LIKE '%' || ? || '%' ");
				}
			}
			
			logger.debug("=====================================");
			logger.debug("sql : " + sql.toString());
			logger.debug("=====================================");
			
			try
			{
				conn = DBManager.getConnection();
				pstmt = conn.prepareStatement(sql.toString());
				
				int idx = 0;
				
				if(search != null)
				{
					if(!StringUtil.isEmpty(search.getMemId()))
					{
						pstmt.setString(++idx, search.getMemId());
					}
					if(!StringUtil.isEmpty(search.getMemName()))
					{
						pstmt.setString(++idx, search.getMemName());
					}
					if(!StringUtil.isEmpty(search.getMemSta()))
					{
						pstmt.setString(++idx, search.getMemSta());
					}
				}
				
				rs = pstmt.executeQuery();
				
				if(rs.next())
				{
					totalCount = rs.getLong("CNT");
				}
				
				
			}
			catch(Exception e)
			{
				logger.error("[BoaDao] totalMemCount SQLException", e);
			}
			finally
			{
				DBManager.close(rs, pstmt, conn);
			}
			
			return totalCount;
		}
}
