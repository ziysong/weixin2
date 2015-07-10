package com.szy.weixin.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.szy.weixin.domain.Student;
import com.szy.weixin.mark.MarkQueryUtils;
import com.szy.weixin.util.DaoUtils;
import com.szy.weixin.util.MessageUtils;

@WebServlet(urlPatterns={"/markQueryServlet"})
public class MarkQueryServlet extends HttpServlet{
	private static String fromUserName = null;
	//每次点击查成绩时就会把用户的微信名封装到fromUserName
	public static void setFromUserName(String str){
		MarkQueryServlet.fromUserName = str;
	}
	
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PreparedStatement  pstmt = null; 
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		//先判断该微信名是否绑定在在数据库,存在就取出用户名和密码直接查询,否则跳转到登陆页面
		if(request.getParameter("username") == null){
			ResultSet rsQ =  queryByFromUserName(fromUserName);
			try {
				while(rsQ.next()){
					if(fromUserName.equals(rsQ.getString("fromUserName"))){
						String username = rsQ.getString("username");
						String password = rsQ.getString("password");
						Map<String, String> mapResult = new HashMap<>();
						mapResult = MarkQueryUtils.markQuery(username, password);
						String courseResult = MarkQueryServlet.getCourseResult(mapResult);
						request.setAttribute("courseResult", courseResult);
						request.getRequestDispatcher("/WEB-INF/jsp/mark.jsp").forward(request, response);
						return;
					}
				}
				response.sendRedirect(request.getContextPath()+"/query.jsp");
				return;
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
	
		//获得表单输入的信息
		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		String login = request.getParameter("login");
		Map<String, String> mapResult = new HashMap<>();
		mapResult = MarkQueryUtils.markQuery(username, password);
		
		if(mapResult.containsKey("error")){
			request.setAttribute("mapResult", mapResult.get("error"));
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
		}else{
			String courseResult = getCourseResult(mapResult);
			
			//绑定微信和学号：login==null说明是登陆并绑定
			if(login == null){ //每次查询数据库,有记录就更新否则插入
				try {
					ResultSet rs = queryByUserName(username);
					if(rs.next()){
						rs.close();
						String sqlUpdate = "update student_info set username=?,password=?,fromUserName=? where username=?";
						pstmt = DaoUtils.getPstmt(sqlUpdate);
						pstmt.setString(1, username);
						pstmt.setString(2, password);
						pstmt.setString(3, fromUserName);
						pstmt.setString(4, username);  
						pstmt.executeUpdate();    
						pstmt.close();  
					}else{
						String sqlAdd = "insert into student_info(username,password,fromUserName) values(?,?,?)";
						pstmt = DaoUtils.getPstmt(sqlAdd);
						pstmt.setString(1, username);
						pstmt.setString(2, password);
						pstmt.setString(3, fromUserName);
						pstmt.executeUpdate();
						pstmt.close();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			} else {//不绑定微信号,但保留学号密码
				ResultSet rs = queryByUserName(username); 
				try {
					if(rs.next()){//如果数据库中有记录:密码变了就更新数据库否则不变
						String usernameQ = rs.getString("username");
						String passwordQ = rs.getString("password");
						rs.close();
						if(!(usernameQ==username && passwordQ==password)){
							String sqlUpdate = "update student_info set username=?,password=? where username=?";
							pstmt = DaoUtils.getPstmt(sqlUpdate);
							pstmt.setString(1, username);
							pstmt.setString(2, password);
							pstmt.setString(3, username);
							pstmt.executeUpdate();
							pstmt.close();
						}
					}else{//如果数据库中没记录:就插入学号密码
						String sqlAdd = "insert into student_info(username,password) values(?,?)";
						pstmt = DaoUtils.getPstmt(sqlAdd);
						pstmt.setString(1, username);
						pstmt.setString(2, password);
						pstmt.executeUpdate();
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			request.setAttribute("courseResult", courseResult);
			request.getRequestDispatcher("/WEB-INF/jsp/mark.jsp").forward(request, response);
			return;
		}
		
	}
	
	
	//查询数据库中记录:根据用户名
	public static ResultSet queryByUserName(String username){
		PreparedStatement pstmt = null;
		String sqlQuery = "select * from student_info where username=?";
		pstmt = DaoUtils.getPstmt(sqlQuery);
		try {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return rs;  
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	//查询数据库中记录:根据用户名
	public static ResultSet queryByFromUserName(String fromUserName){
		PreparedStatement pstmt = null;
		String sqlQuery = "select * from student_info where fromUserName=?";
		pstmt = DaoUtils.getPstmt(sqlQuery);
		try {
			pstmt.setString(1, fromUserName);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	//组装成绩表格
	public static String getCourseResult(Map<String, String> mapResult){
		int courseNum = Integer.parseInt(mapResult.get("courseNum"));
		String courseRes = mapResult.get("courseRes");
		
		String[] courseAll = courseRes.split(";");
		StringBuffer courseResult = new StringBuffer();
		courseResult.append("<table>");
		courseResult.append("<tr>"
				+ "<td>课程名</td>"
				+ "<td>学分</td>"
				+ "<td>课程属性</td>"
				+ "<td>成绩</td>"
				+ "</tr>");  
		for(int i=0; i<courseNum; i++){  
			String[] courseSin = courseAll[i].split(" ");
			courseResult.append("<tr>");
			for(int j=2; j<courseSin.length; j++){
				courseResult.append("<td>"+courseSin[j]+"</td>");
			}
			courseResult.append("</tr>");
		}
		courseResult.append("</table>");
		return courseResult.toString();      
	}
	
	
}