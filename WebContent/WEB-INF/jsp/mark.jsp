<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"> 
<title>成绩</title>
</head>
<body>

<a href="${pageContext.request.contextPath }/query.jsp">返回登陆页面</a>
<div>
<%
String str = (String)request.getAttribute("courseResult");
out.println(str);
%>
</div>

</body>
</html>