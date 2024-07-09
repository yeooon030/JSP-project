/**
 * <pre>
 * 프로젝트명 : BasicBoard
 * 패키지명   : com.icia.web.db
 * 파일명     : DBManager.java
 * 작성일     : 2021. 1. 5.
 * 작성자     : daekk
 * </pre>
 */
package com.sist.web.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DBManager
{
	private static Logger logger = LogManager.getLogger(DBManager.class);
	
	private DBManager() {}

	public static Connection getConnection()
	{
		Connection conn = null;
		
		try
		{
			// Context 객체 생성
			Context context = new InitialContext();
            // context 객체의 lookup 메서드를 이용해 "java:comp/env" 에 해당하는 객체를 얻는다.
			// JNDI 스팩으로 정의되어 있음 java:comp/env(루트임) 밑에서 읽어옴.
            Context envContext = (Context)context.lookup("java:comp/env");
            // envContext 객체의 lookup 메서드를 이용해 "jdbc/icia"에 해당하는 객체(DataSource)를 얻는다.
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/sist");
            // getConnection 메서드를 이용해서 커넥션 풀로 부터 커넥션 객체를 얻는다.
            conn = dataSource.getConnection();

			// 아래 처럼 간략하게 설정 가능
			/*
			Context context = new InitialContext();
			DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/icia");
			conn = dataSource.getConnection();
			*/
		}
		catch (NamingException e)
		{
			logger.error("[DBManager] getConnection NamingException", e);
		}
		catch (SQLException e)
		{
			logger.error("[DBManager] getConnection SQLException", e);
		}
	    		
		return conn;
	}
	
	public static void close(ResultSet rs)
	{
		close(rs, null, null);
	}
	
	public static void close(PreparedStatement pstmt)
	{
		close(null, pstmt, null);
	}
	
	public static void close(Connection conn)
	{
		close(null, null, conn);
	}

	public static void close(ResultSet rs, PreparedStatement pstmt)
	{
		close(rs, pstmt, null);
	}
	
	public static void close(PreparedStatement pstmt, Connection conn)
	{
		close(null, pstmt, conn);
	}
	
	public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn)
	{
		if(rs != null)
		{
			try
			{
				rs.close();
			}
			catch (SQLException e)
			{
				logger.error("[DBManager] close ResultSet SQLException", e);
			}
		}
		
		if(pstmt != null)
		{
			try
			{
				pstmt.close();
			}
			catch (SQLException e)
			{
				logger.error("[DBManager] close PreparedStatement SQLException", e);
			}
		}
		
		if(conn != null)
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				logger.error("[DBManager] close Connection SQLException", e);
			}
		}
	}
	
	public static void setAutoCommit(Connection conn, boolean flag)
	{
		if(conn != null)
		{
			try
			{
				if(conn.getAutoCommit() != flag)
				{
					conn.setAutoCommit(flag);
				}
			}
			catch (SQLException e)
			{
				logger.error("[DBManager] setAutoCommit SQLException", e);
			}
		}
	}

	public static void rollback(Connection conn)
	{
		if(conn != null)
		{
			try
			{
				conn.rollback();
			}
			catch (SQLException e)
			{
				logger.error("[DBManager] rollback SQLException", e);
			}
		}
	}
}
