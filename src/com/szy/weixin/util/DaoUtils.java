package com.szy.weixin.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class DaoUtils {
	private static Connection conn;

	/**��ȡ���ݿ�����*/
	static{
		try { 
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/weixin2";
			String user = "root"; 
			String password = "root"; 
		    conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**��ȡStatement*/
	public  static Statement getStmt(){
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**��ȡPreparedStatement*/
	public static PreparedStatement getPstmt(String sql){
		PreparedStatement  pstmt = null; 
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			return null;
		}
		return pstmt;
	}
	
	/**�ر�����*/
	public static void closeConn(Connection conn){
		try {
			if(conn != null){
			conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}

